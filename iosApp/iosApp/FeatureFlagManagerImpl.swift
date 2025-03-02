//
//  FeatureFlagManagerImpl.swift
//  iosApp
//
//  Created by Mirzamehdi on 01/03/2025.
//

import Foundation
import ComposeApp
import FirebaseRemoteConfig

class FeatureFlagManagerImpl: FeatureFlagManager {

    private let remoteConfig = RemoteConfig.remoteConfig()

    init() {
        let settings = RemoteConfigSettings()

        #if targetEnvironment(simulator)
        settings.minimumFetchInterval = 3600
        #endif
        let defaultValues = FeatureFlagManagerCompanion.shared.DEFAULT_VALUES
        remoteConfig.configSettings = settings
        remoteConfig.setDefaults(defaultValues)
    }

    func syncsFlagsAsync() {
        remoteConfig.fetchAndActivate { status, error in
            if error?.localizedDescription.isEmpty == false {
                print("Feature Flag Sync Failed: \(error!.localizedDescription)")
            } else {
                print("Feature Flag Sync is completed, result: \(status)")
            }
        }
    }

    func getBoolean(key: String) -> Bool {
        return remoteConfig[key].boolValue
    }

    func getString(key: String) -> String {
        return remoteConfig[key].stringValue
    }

    func getLong(key: String) -> Int64 {
        return remoteConfig[key].numberValue.int64Value
    }

    func getDouble(key: String) -> Double {
        return remoteConfig[key].numberValue.doubleValue
    }

}
