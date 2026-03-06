import SwiftUI
import WidgetKit

struct BmiWidgetView: View {
    var entry: BmiEntry

    @Environment(\.widgetFamily) var family

    var body: some View {
        switch family {
        case .systemSmall:
            smallWidget
        case .systemMedium:
            mediumWidget
        default:
            smallWidget
        }
    }

    private var smallWidget: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("BMI")
                .font(.headline)
                .foregroundColor(.secondary)

            Text(entry.bmiFormatted)
                .font(.system(size: 36, weight: .bold, design: .rounded))
                .foregroundColor(.primary)

            Text(entry.categoryName)
                .font(.subheadline)
                .foregroundColor(entry.categoryColor)
                .fontWeight(.medium)

            if !entry.hasData {
                Text("Open app to calculate")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
        .padding()
        .containerBackground(.fill.tertiary, for: .widget)
    }

    private var mediumWidget: some View {
        HStack(spacing: 16) {
            VStack(alignment: .leading, spacing: 4) {
                Text("BMI")
                    .font(.headline)
                    .foregroundColor(.secondary)

                Text(entry.bmiFormatted)
                    .font(.system(size: 48, weight: .bold, design: .rounded))
                    .foregroundColor(.primary)

                Text(entry.categoryName)
                    .font(.subheadline)
                    .foregroundColor(entry.categoryColor)
                    .fontWeight(.medium)
            }

            Spacer()

            if entry.hasData {
                VStack(alignment: .trailing, spacing: 8) {
                    Circle()
                        .fill(entry.categoryColor)
                        .frame(width: 24, height: 24)
                    Text("Tap to view details")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }
            } else {
                VStack(alignment: .trailing, spacing: 4) {
                    Image(systemName: "plus.circle")
                        .font(.title)
                        .foregroundColor(.secondary)
                    Text("Open app to calculate your BMI")
                        .font(.caption)
                        .foregroundColor(.secondary)
                        .multilineTextAlignment(.trailing)
                }
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .padding()
        .containerBackground(.fill.tertiary, for: .widget)
    }
}
