import SwiftUI
import WidgetKit

struct BloodPressureWidgetView: View {
    var entry: BloodPressureEntry

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
            Text("Blood Pressure")
                .font(.caption)
                .foregroundColor(.secondary)

            Text("\(entry.valueFormatted) mmHg")
                .font(.system(size: 24, weight: .bold, design: .rounded))

            Text(entry.categoryName)
                .font(.subheadline)
                .foregroundColor(entry.categoryColor)
                .fontWeight(.medium)

            if !entry.hasData {
                Text("Open app to measure")
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
                Text("Blood Pressure")
                    .font(.headline)
                    .foregroundColor(.secondary)

                Text("\(entry.valueFormatted) mmHg")
                    .font(.system(size: 36, weight: .bold, design: .rounded))

                Text(entry.categoryName)
                    .font(.subheadline)
                    .foregroundColor(entry.categoryColor)
                    .fontWeight(.medium)
            }

            Spacer()

            if entry.hasData {
                Circle()
                    .fill(entry.categoryColor)
                    .frame(width: 24, height: 24)
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .padding()
        .containerBackground(.fill.tertiary, for: .widget)
    }
}
