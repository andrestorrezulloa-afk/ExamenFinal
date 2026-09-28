package edu.ucb.pablostify

import androidx.compose.ui.window.ComposeUIViewController
import edu.ucb.pablostify.di.initKoinIos
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoinIos()
    return ComposeUIViewController { App() }
}
