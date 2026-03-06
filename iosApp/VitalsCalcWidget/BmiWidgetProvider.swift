import WidgetKit
import SwiftUI

struct BmiWidgetProvider: TimelineProvider {

    func placeholder(in context: Context) -> BmiEntry {
        BmiEntry(date: .now, bmi: 22.5, category: "NORMAL")
    }

    func getSnapshot(in context: Context, completion: @escaping (BmiEntry) -> Void) {
        completion(loadCurrentEntry())
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<BmiEntry>) -> Void) {
        let currentEntry = loadCurrentEntry()
        let nextUpdate = Calendar.current.date(byAdding: .hour, value: 1, to: .now)!
        let timeline = Timeline(entries: [currentEntry], policy: .after(nextUpdate))
        completion(timeline)
    }

    private func loadCurrentEntry() -> BmiEntry {
        guard let defaults = UserDefaults(suiteName: WidgetConstants.appGroupId),
              defaults.object(forKey: "widget_bmi_value") != nil else {
            return BmiEntry(date: .now, bmi: nil, category: nil)
        }

        let bmi = defaults.double(forKey: "widget_bmi_value")
        let category = defaults.string(forKey: "widget_bmi_category")

        return BmiEntry(date: .now, bmi: bmi, category: category)
    }
}
