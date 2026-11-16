import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.ZipInputStream
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room3)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    android {
        namespace = "edu.ucb.pablostify.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.room3.sqlite.wrapper)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.navigation.compose)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation("io.coil-kt.coil3:coil-compose:3.3.0")
            implementation("io.coil-kt.coil3:coil-network-ktor3:3.3.0")

            implementation(libs.androidx.room3.runtime) // <-- AGREGA ESTO
            implementation(libs.androidx.sqlite.bundled) // <-- AGREGA ESTO
            implementation(libs.firebase.database)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
    add("kspAndroid", libs.androidx.room3.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room3.compiler)
    add("kspIosArm64", libs.androidx.room3.compiler)
}
room3 {
    schemaDirectory("$projectDir/schemas")
}

abstract class DownloadLocaliseTranslations : DefaultTask() {
    @get:Internal abstract val apiKey: Property<String>
    @get:Input abstract val previewOnly: Property<Boolean>
    @get:Internal abstract val archiveFile: RegularFileProperty
    @get:Internal abstract val extractionDirectory: DirectoryProperty
    @get:Internal abstract val resourcesDirectory: DirectoryProperty
    @get:Internal abstract val backupDirectory: DirectoryProperty

    @TaskAction
    fun download() {
        val apiKey = apiKey.orNull?.takeIf { it.isNotBlank() }
            ?: throw GradleException("Configura LOCO_API_KEY en las propiedades de usuario de Gradle")
        val outputZip = archiveFile.get().asFile
        val tempExtractDir = extractionDirectory.get().asFile
        val targetDir = resourcesDirectory.get().asFile

        outputZip.parentFile.mkdirs()
        val connection = (URL("https://localise.biz/api/export/archive/xml.zip?key=$apiKey")
            .openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 30000
            readTimeout = 60000
            instanceFollowRedirects = false
        }
        try {
            val status = connection.responseCode
            if (status != HttpURLConnection.HTTP_OK) {
                throw GradleException("Loco respondió HTTP $status; no se importaron recursos")
            }
            connection.inputStream.use { input ->
                outputZip.outputStream().use { output -> input.copyTo(output) }
            }
        } catch (error: GradleException) {
            throw error
        } catch (error: Exception) {
            throw GradleException("No se pudo descargar el ZIP de Loco (${error.javaClass.simpleName})")
        } finally {
            connection.disconnect()
        }

        logger.lifecycle("Loco HTTP 200: ZIP descargado (${outputZip.length()} bytes)")

        val zipHeader = outputZip.inputStream().use { input -> ByteArray(4).also { input.read(it) } }
        if (!zipHeader.contentEquals(byteArrayOf(0x50, 0x4b, 0x03, 0x04))) {
            throw GradleException("La respuesta de Loco no es un archivo ZIP válido")
        }

        if (tempExtractDir.exists()) tempExtractDir.deleteRecursively()
        tempExtractDir.mkdirs()
        val extractRoot = tempExtractDir.canonicalFile
        var entryCount = 0
        ZipInputStream(outputZip.inputStream()).use { zip ->
            var entry = zip.nextEntry
            while (entry != null) {
                val relativePath = entry.name.replace('\\', '/')
                val destination = File(extractRoot, relativePath).canonicalFile
                if (relativePath.startsWith('/') ||
                    !destination.path.startsWith(extractRoot.path + File.separator)
                ) {
                    throw GradleException("El ZIP contiene una ruta fuera del directorio de extracción")
                }
                if (entry.isDirectory) {
                    destination.mkdirs()
                } else {
                    destination.parentFile.mkdirs()
                    destination.outputStream().use { output -> zip.copyTo(output) }
                }
                entryCount++
                zip.closeEntry()
                entry = zip.nextEntry
            }
        }
        if (entryCount == 0) throw GradleException("El ZIP de Loco está vacío")

        val localePattern = Regex("values(?:-[a-z]{2}(?:-r[A-Z]{2})?)?")
        val valueDirs = tempExtractDir.walkTopDown()
            .filter { it.isDirectory && localePattern.matches(it.name) && File(it, "strings.xml").isFile }
            .toList()
        if (valueDirs.isEmpty() || valueDirs.none { it.name == "values" } ||
            valueDirs.map { it.name }.distinct().size != valueDirs.size
        ) {
            throw GradleException("El ZIP no contiene carpetas values/strings.xml inequívocas")
        }

        val identifiersByLocale = valueDirs.associate { directory ->
            val xml = File(directory, "strings.xml")
            val factory = DocumentBuilderFactory.newInstance().apply {
                setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)
                setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
                setFeature("http://xml.org/sax/features/external-general-entities", false)
                setFeature("http://xml.org/sax/features/external-parameter-entities", false)
                isXIncludeAware = false
                isExpandEntityReferences = false
            }
            val document = try {
                factory.newDocumentBuilder().parse(xml)
            } catch (error: Exception) {
                throw GradleException("XML inválido en ${directory.name}/strings.xml (${error.javaClass.simpleName})")
            }
            if (document.documentElement.tagName != "resources") {
                throw GradleException("XML sin raíz resources en ${directory.name}")
            }
            val nodes = document.documentElement.getElementsByTagName("string")
            val names = (0 until nodes.length).map { (nodes.item(it) as Element).getAttribute("name") }
            if (names.isEmpty()) {
                throw GradleException("Loco no entregó traducciones <string> en ${directory.name}/strings.xml; no se importó nada")
            }
            if (names.distinct().size != names.size ||
                names.any { !Regex("[a-z][a-z0-9_]*").matches(it) }
            ) {
                throw GradleException("Identificadores de strings vacíos, duplicados o inválidos en ${directory.name}")
            }
            directory.name to names.toSet()
        }
        val defaultNames = identifiersByLocale.getValue("values")
        if (identifiersByLocale.values.any { it != defaultNames }) {
            throw GradleException("Los idiomas del ZIP no tienen los mismos identificadores")
        }

        logger.lifecycle("Loco HTTP 200: ZIP descargado y validado; idiomas: ${identifiersByLocale.keys.sorted()}")
        logger.lifecycle("Identificadores: ${defaultNames.sorted()}")
        if (previewOnly.get()) {
            logger.lifecycle("Vista previa: no se copiaron archivos a composeResources")
            return
        }
        if (defaultNames.any { it.equals("api_key", ignoreCase = true) || it.equals("loco_api_key", ignoreCase = true) }) {
            throw GradleException("La exportación contiene un identificador de credenciales; no se importó")
        }

        val backupDir = File(backupDirectory.get().asFile, System.currentTimeMillis().toString())
        valueDirs.forEach { directory ->
            val existing = File(targetDir, "${directory.name}/strings.xml")
            if (existing.exists()) {
                val backup = File(backupDir, "${directory.name}/strings.xml")
                backup.parentFile.mkdirs()
                existing.copyTo(backup)
            }
        }
        valueDirs.forEach { directory ->
            val destination = File(targetDir, "${directory.name}/strings.xml")
            destination.parentFile.mkdirs()
            File(directory, "strings.xml").copyTo(destination, overwrite = true)
            logger.lifecycle("Copiado idioma: ${directory.name}")
        }
    }
}

tasks.register<DownloadLocaliseTranslations>("downloadLocaliseTranslations") {
    group = "localization"
    description = "Descarga las traducciones desde Loco y las ubica en composeResources"
    apiKey.set(providers.gradleProperty("LOCO_API_KEY"))
    previewOnly.set(providers.gradleProperty("locoPreviewOnly").map { it == "true" }.orElse(false))
    archiveFile.set(layout.buildDirectory.file("translations.zip"))
    extractionDirectory.set(layout.buildDirectory.dir("temp_translations"))
    resourcesDirectory.set(layout.projectDirectory.dir("src/commonMain/composeResources"))
    backupDirectory.set(File(gradle.gradleUserHomeDir, "loco-backups"))
}
