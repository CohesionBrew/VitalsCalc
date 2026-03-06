import WidgetKit
import SwiftUI

struct HeartRateWidgetProvider: TimelineProvider {

    func placeholder(in context: Context) -> HeartRateEntry {
        HeartRateEntry(date: .now, maxHr: 190, zone: "Zone 2 - Fat Burn")
    }

    func getSnapshot(in context: Context, completion: @escaping (HeartRateEntry) -> Void) {
        completion(loadCurrentEntry())
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<HeartRateEntry>) -> Void) {
        let currentEntry = loadCurrentEntry()
        let nextUpdate = Calendar.current.date(byAdding: .hour, value: 1, to: .now)!
        let timeline = Timeline(entries: [currentEntry], policy: .after(nextUpdate))
        completion(timeline)
    }

    private func loadCurrentEntry() -> HeartRateEntry {
        guard let defaults = UserDefaults(suiteName: WidgetConstants.appGroupId),
              defaults.object(forKey: "widget_heart_rate_value") != nil else {
            return HeartRateEntry(date: .now, maxHr: nil, zone: nil)
        }

        let maxHr = defaults.double(forKey: "widget_heart_rate_value")
        let zone = defaults.string(forKey: "widget_heart_rate_category")

        return HeartRateEntry(date: .now, maxHr: maxHr, zone: zone)
    }
}
