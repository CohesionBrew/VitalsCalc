import WidgetKit
import SwiftUI

struct BmiEntry: TimelineEntry {
    let date: Date
    let bmi: Double?
    let category: String?

    var bmiFormatted: String {
        guard let bmi = bmi else { return "--" }
        return String(format: "%.1f", bmi)
    }

    var categoryName: String {
        guard let cat = category?.uppercased() else { return "No data" }
        switch cat {
        case "UNDERWEIGHT": return "Underweight"
        case "NORMAL", "NORMAL_WEIGHT": return "Normal"
        case "OVERWEIGHT": return "Overweight"
        case "OBESE_I", "OBESE_CLASS_I": return "Obese I"
        case "OBESE_II", "OBESE_CLASS_II": return "Obese II"
        case "OBESE_III", "OBESE_CLASS_III": return "Obese III"
        default: return cat.replacingOccurrences(of: "_", with: " ").capitalized
        }
    }

    var categoryColor: Color {
        guard let cat = category?.uppercased() else { return .gray }
        switch cat {
        case "UNDERWEIGHT": return Color(red: 0.12, green: 0.53, blue: 0.90)
        case "NORMAL", "NORMAL_WEIGHT": return Color(red: 0.30, green: 0.69, blue: 0.31)
        case "OVERWEIGHT": return Color(red: 0.99, green: 0.85, blue: 0.21)
        case "OBESE_I", "OBESE_CLASS_I": return Color(red: 0.98, green: 0.55, blue: 0.00)
        case "OBESE_II", "OBESE_CLASS_II": return Color(red: 0.83, green: 0.18, blue: 0.18)
        case "OBESE_III", "OBESE_CLASS_III": return Color(red: 0.72, green: 0.11, blue: 0.11)
        default: return .gray
        }
    }

    var hasData: Bool {
        bmi != nil && category != nil
    }
}
