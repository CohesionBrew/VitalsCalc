import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.cohesionbrew.healthcalculator.root.App
import com.cohesionbrew.healthcalculator.util.LocalNativeViewFactory
import com.cohesionbrew.healthcalculator.util.NativeViewFactory
import com.cohesionbrew.healthcalculator.util.SwiftLibDependencyFactory
import com.cohesionbrew.healthcalculator.util.swiftLibDependenciesModule
import org.koin.core.KoinApplication
import platform.UIKit.UIViewController

fun MainViewController(nativeViewFactory: NativeViewFactory): UIViewController =
    ComposeUIViewController {
        CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) {
            App()
        }
    }

//This is called on application started on Swift side
fun KoinApplication.provideSwiftLibDependencyFactory(factory: SwiftLibDependencyFactory) =
    run { modules(swiftLibDependenciesModule(factory)) }