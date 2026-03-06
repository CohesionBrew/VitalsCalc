import WidgetKit
import SwiftUI

struct HeartRateEntry: TimelineEntry {
    let date: Date
    let maxHr: Double?
    let zone: String?

    var maxFormatted: String {
        guard let hr = maxHr else { return "--" }
        return "\(Int(hr))"
    }

    var hasData: Bool {
        maxHr != nil && maxHr! > 0
    }

    var heartColor: Color {
        Color(red: 0.90, green: 0.22, blue: 0.21)
    }
}
