# Pablostify — Programación Móvil (Kotlin Multiplatform)

Proyecto completo preparado siguiendo el material de clase y los repositorios de referencia proporcionados.

## Entorno

- Android Studio (el curso usa Android Studio Quail 3 / 2026.1.3).
- Kotlin Multiplatform (KMP).
- Kotlin 2.4.10.
- Compose Multiplatform 1.11.1.
- Android compile/target SDK 36, min SDK 24.
- El proyecto usa el Gradle Wrapper incluido; no hace falta instalar Gradle manualmente.
- Dominio/package de la aplicación: `edu.ucb.pablostify`.

## Alcance implementado

La primera instrucción de clase pedía trabajar cinco pantallas/features con capa de presentación y dominio. Las cinco features principales de Pablostify son:

1. Login.
2. Register.
3. MovieList.
4. MovieDetail.
5. Profile.

Cada feature utiliza la separación enseñada: `presentation` (Screen, State, Events, Effects, ViewModel), `domain` (VO/valueobject, model, repository, usecase) y, cuando corresponde, `data` (implementación de repository).

Además se incluye `userinformation` como pantalla auxiliar del ejercicio Ktor: consulta un usuario público de GitHub por alias con Ktor y muestra alias, email, empresa y URL del avatar. No sustituye a las cinco pantallas principales.

## Koin KMP v2

- `di/DataModule.kt`
- `di/DomainModule.kt`
- `di/PresentationModule.kt`
- `di/SharedModules.kt`
- `di/KoinInit.kt`
- Android: `shared/src/androidMain/.../di/KoinModules.android.kt`
- iOS: `shared/src/iosMain/.../di/KoinModules.ios.kt`
- Android `MainApplication` inicializa Koin y está asociado en `AndroidManifest.xml`.
- Los ViewModels se registran con `viewModelOf` y se obtienen desde Compose con `koinViewModel()`.

## MVI / ViewModel

Los ViewModels heredan de `androidx.lifecycle.ViewModel`, mantienen State con `MutableStateFlow`, Effects con `MutableSharedFlow`, reciben Events y ejecutan UseCases. Esto conserva el patrón State / Event / Effect visto en clase.

## AppNavigation KMP

- `navigation/NavRoute.kt` usa rutas tipadas `@Serializable`.
- `navigation/AppNavHost.kt` usa `rememberNavController`, `NavHost` y `composable<T>()`.
- `App.kt` únicamente aplica `MaterialTheme` y llama `AppNavHost()`.
- La navegación se dispara desde Effects de los ViewModels.

## Ktor KMP

Dependencias incluidas:

- `ktor-client-core`
- `ktor-client-okhttp` (Android)
- `ktor-client-darwin` (iOS)
- `ktor-client-content-negotiation`
- `ktor-serialization-kotlinx-json`

El ejercicio se implementa con:

- DTO (`UserInfoDto`)
- Mapper (`toDomain()`)
- RemoteDataSource (`GithubRemoteDataSource`)
- Service (`GitHubApiService`)
- Repository implementation (`GithubRepositoryImpl`)
- Domain Repository + UseCase
- MVI ViewModel + Screen
- Registro en Koin `DataModule`, `DomainModule` y `PresentationModule`

Nota: los engines de Ktor están ubicados en sus source sets correctos para que el proyecto sea realmente multiplataforma: OkHttp en `androidMain`, Darwin en `iosMain` y core/ContentNegotiation/JSON en `commonMain`.

## Datos de demo de Pablostify

El estado actual del proyecto de clase todavía usa repositorios locales/mock para las cinco features principales, tal como el avance de Pablostify. Se conservan:

- Usuario demo: Ana García / `anagarcia@gmail.com`.
- Películas: The Matrix, Spider-Man e Interstellar.
- Detalles y reparto locales para esas películas.
- Registro, reseña y logout simulados como operaciones exitosas.
- La consulta GitHub sí usa red real mediante Ktor.

No se inventó un backend de Pablostify porque ninguno fue proporcionado en el material.

## Ejecutar

### Windows

```bat
gradlew.bat :androidApp:assembleDebug
```

O abre la raíz del proyecto en Android Studio, deja finalizar Gradle Sync, selecciona `androidApp` y ejecuta un emulador/dispositivo Android.

### macOS/Linux

```bash
./gradlew :androidApp:assembleDebug
```

La primera sincronización necesita Internet para descargar dependencias de Maven/Gradle. La pantalla `Consulta GitHub (Ktor)` también necesita Internet en tiempo de ejecución.

## Referencias seguidas

- Proyecto Pablostify: `https://github.com/jonathanluizaga25/Pablostify` (incluida su rama `avances-clase`).
- Ejemplo del Magister: `https://github.com/calyr/ucbp1_project`, especialmente `feature/koin-viewmodel` y `feature/userinformation`.
- Ejemplo adicional de navegación: `https://github.com/GCD418/mobile-1/tree/feature/nav-controller`.
- Material de clase suministrado: MVI, Signin, Koin KMP/ViewModel, KOIN KMP v2, AppNavigation KMP y Ktor KMP, además del material previo del curso.

## Qué no se agregó deliberadamente

No se agregó Hilt, Retrofit, SQLDelight ni un backend externo inventado. Sentry, flavors, fuentes, permisos adicionales, análisis estático y otros temas aparecen en material previo del curso, pero no son requisito funcional de este último hito Koin + ViewModel + Navigation + Ktor, y agregarlos sin configuración/credenciales del curso podría romper una entrega que debe abrir y compilar directamente.
