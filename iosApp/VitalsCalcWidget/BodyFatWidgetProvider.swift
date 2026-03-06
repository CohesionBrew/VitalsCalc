import WidgetKit
import SwiftUI

struct BodyFatWidgetProvider: TimelineProvider {

    func placeholder(in context: Context) -> BodyFatEntry {
        BodyFatEntry(date: .now, bodyFatPercent: 18.5, category: "FITNESS")
    }

    func getSnapshot(in context: Context, completion: @escaping (BodyFatEntry) -> Void) {
        completion(loadCurrentEntry())
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<BodyFatEntry>) -> Void) {
        let currentEntry = loadCurrentEntry()
        let nextUpdate = Calendar.current.date(byAdding: .hour, value: 1, to: .now)!
        let timeline = Timeline(entries: [currentEntry], policy: .after(nextUpdate))
        completion(timeline)
    }

    private func loadCurrentEntry() -> BodyFatEntry {
        guard let defaults = UserDefaults(suiteName: WidgetConstants.appGroupId),
              defaults.object(forKey: "widget_body_fat_value") != nil else {
            return BodyFatEntry(date: .now, bodyFatPercent: nil, category: nil)
        }

        let percent = defaults.double(forKey: "widget_body_fat_value")
        let category = defaults.string(forKey: "widget_body_fat_category")

        return BodyFatEntry(date: .now, bodyFatPercent: percent, category: category)
    }
}
