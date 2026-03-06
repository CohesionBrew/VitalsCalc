import WidgetKit
import SwiftUI

struct BloodPressureWidgetProvider: TimelineProvider {

    func placeholder(in context: Context) -> BloodPressureEntry {
        BloodPressureEntry(date: .now, value: 120, category: "NORMAL")
    }

    func getSnapshot(in context: Context, completion: @escaping (BloodPressureEntry) -> Void) {
        completion(loadCurrentEntry())
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<BloodPressureEntry>) -> Void) {
        let currentEntry = loadCurrentEntry()
        let nextUpdate = Calendar.current.date(byAdding: .hour, value: 1, to: .now)!
        let timeline = Timeline(entries: [currentEntry], policy: .after(nextUpdate))
        completion(timeline)
    }

    private func loadCurrentEntry() -> BloodPressureEntry {
        guard let defaults = UserDefaults(suiteName: WidgetConstants.appGroupId),
              defaults.object(forKey: "widget_blood_pressure_value") != nil else {
            return BloodPressureEntry(date: .now, value: nil, category: nil)
        }

        let value = defaults.double(forKey: "widget_blood_pressure_value")
        let category = defaults.string(forKey: "widget_blood_pressure_category")

        return BloodPressureEntry(date: .now, value: value, category: category)
    }
}
