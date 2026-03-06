import WidgetKit
import SwiftUI

struct BloodPressureEntry: TimelineEntry {
    let date: Date
    let value: Double?
    let category: String?

    var valueFormatted: String {
        guard let v = value else { return "--" }
        return "\(Int(v))"
    }

    var categoryName: String {
        guard let cat = category?.uppercased() else { return "No data" }
        switch cat {
        case "NORMAL": return "Normal"
        case "ELEVATED": return "Elevated"
        case "HYPERTENSION_STAGE_1": return "Stage 1"
        case "HYPERTENSION_STAGE_2": return "Stage 2"
        case "HYPERTENSIVE_CRISIS": return "Crisis"
        default: return cat.replacingOccurrences(of: "_", with: " ").capitalized
        }
    }

    var categoryColor: Color {
        guard let cat = category?.uppercased() else { return .gray }
        switch cat {
        case "NORMAL": return Color(red: 0.30, green: 0.69, blue: 0.31)
        case "ELEVATED": return Color(red: 0.99, green: 0.85, blue: 0.21)
        case "HYPERTENSION_STAGE_1": return Color(red: 0.98, green: 0.55, blue: 0.00)
        case "HYPERTENSION_STAGE_2": return Color(red: 0.83, green: 0.18, blue: 0.18)
        case "HYPERTENSIVE_CRISIS": return Color(red: 0.72, green: 0.11, blue: 0.11)
        default: return .gray
        }
    }

    var hasData: Bool {
        value != nil && category != nil
    }
}
