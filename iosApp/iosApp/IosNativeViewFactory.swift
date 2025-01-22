//
//  IosNativeViewFactory.swift
//  iosApp
//
//  Created by Mirzamehdi on 22/01/2025.
//

import Foundation
import ComposeApp
import SwiftUI
import UIKit

class IosNativeViewFactory: NativeViewFactory{
    static var shared = IosNativeViewFactory()
    
    func createSwiftTextView(text: String) -> UIViewController {
        let swiftUIView = Text(text)
        return UIHostingController(rootView: swiftUIView)
    }
    
}
