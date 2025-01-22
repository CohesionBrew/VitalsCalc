import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.measify.kappmaker.root.App
import com.measify.kappmaker.util.LocalNativeViewFactory
import com.measify.kappmaker.util.NativeViewFactory
import platform.UIKit.UIViewController

fun MainViewController(nativeViewFactory: NativeViewFactory): UIViewController =
    ComposeUIViewController {
        CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) {
            App()
        }
    }