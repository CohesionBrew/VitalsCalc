import androidx.compose.ui.window.ComposeUIViewController
import com.measify.kappbuilder.root.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
