import androidx.compose.ui.window.ComposeUIViewController
import com.measify.kappmaker.root.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
