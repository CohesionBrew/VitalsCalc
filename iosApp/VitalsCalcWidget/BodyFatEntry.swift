import WidgetKit
import SwiftUI

struct BodyFatEntry: TimelineEntry {
    let date: Date
    let bodyFatPercent: Double?
    let category: String?

    var percentFormatted: String {
        guard let pct = bodyFatPercent else { return "--%" }
        return String(format: "%.1f%%", pct)
    }

    var categoryName: String {
        guard let cat = category?.uppercased() else { return "No data" }
        switch cat {
        case "ESSENTIAL": return "Essential"
        case "ATHLETES": return "Athletes"
        case "FITNESS": return "Fitness"
        case "AVERAGE": return "Average"
        case "OBESE": return "Obese"
        default: return cat.replacingOccurrences(of: "_", with: " ").capitalized
        }
    }

    var categoryColor: Color {
        guard let cat = category?.uppercased() else { return .gray }
        switch cat {
        case "ESSENTIAL": return Color(red: 0.12, green: 0.53, blue: 0.90)
        case "ATHLETES": return Color(red: 0.30, green: 0.69, blue: 0.31)
        case "FITNESS": return Color(red: 0.40, green: 0.73, blue: 0.42)
        case "AVERAGE": return Color(red: 0.99, green: 0.85, blue: 0.21)
        case "OBESE": return Color(red: 0.83, green: 0.18, blue: 0.18)
        default: return .gray
        }
    }

    var hasData: Bool {
        bodyFatPercent != nil && category != nil
    }
}
