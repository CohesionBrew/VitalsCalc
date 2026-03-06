import SwiftUI
import WidgetKit

struct HeartRateWidgetView: View {
    var entry: HeartRateEntry

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
            HStack(spacing: 4) {
                Image(systemName: "heart.fill")
                    .foregroundColor(entry.heartColor)
                    .font(.caption)
                Text("Heart Rate")
                    .font(.headline)
                    .foregroundColor(.secondary)
            }

            if entry.hasData {
                Text(entry.maxFormatted)
                    .font(.system(size: 28, weight: .bold, design: .rounded))
                    .foregroundColor(entry.heartColor)
                Text("Max bpm")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            } else {
                Text("-- bpm")
                    .font(.system(size: 28, weight: .bold, design: .rounded))
            }

            if let zone = entry.zone {
                Text(zone)
                    .font(.caption2)
                    .foregroundColor(.secondary)
                    .lineLimit(1)
            }

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
                HStack(spacing: 4) {
                    Image(systemName: "heart.fill")
                        .foregroundColor(entry.heartColor)
                    Text("Heart Rate")
                        .font(.headline)
                        .foregroundColor(.secondary)
                }

                if entry.hasData {
                    Text(entry.maxFormatted)
                        .font(.system(size: 36, weight: .bold, design: .rounded))
                        .foregroundColor(entry.heartColor)
                    Text("Max bpm")
                        .font(.caption)
                        .foregroundColor(.secondary)
                } else {
                    Text("-- bpm")
                        .font(.system(size: 36, weight: .bold, design: .rounded))
                }

                if let zone = entry.zone {
                    Text(zone)
                        .font(.caption)
                        .foregroundColor(.secondary)
                }
            }

            Spacer()

            if !entry.hasData {
                VStack(alignment: .trailing, spacing: 4) {
                    Image(systemName: "plus.circle")
                        .font(.title)
                        .foregroundColor(.secondary)
                    Text("Open app to calculate")
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
