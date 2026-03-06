import SwiftUI
import WidgetKit

@main
struct VitalsCalcWidgetBundle: WidgetBundle {
    var body: some Widget {
        BmiWidgetDef()
        BloodPressureWidgetDef()
        BodyFatWidgetDef()
        HeartRateWidgetDef()
    }
}

struct BmiWidgetDef: Widget {
    let kind: String = "BmiWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: BmiWidgetProvider()) { entry in
            BmiWidgetView(entry: entry)
        }
        .configurationDisplayName("BMI")
        .description("View your current BMI at a glance")
        .supportedFamilies([.systemSmall, .systemMedium])
    }
}

struct BloodPressureWidgetDef: Widget {
    let kind: String = "BloodPressureWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: BloodPressureWidgetProvider()) { entry in
            BloodPressureWidgetView(entry: entry)
        }
        .configurationDisplayName("Blood Pressure")
        .description("View your latest blood pressure reading")
        .supportedFamilies([.systemSmall, .systemMedium])
    }
}

struct BodyFatWidgetDef: Widget {
    let kind: String = "BodyFatWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: BodyFatWidgetProvider()) { entry in
            BodyFatWidgetView(entry: entry)
        }
        .configurationDisplayName("Body Fat")
        .description("View your current body fat percentage")
        .supportedFamilies([.systemSmall, .systemMedium])
    }
}

struct HeartRateWidgetDef: Widget {
    let kind: String = "HeartRateWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: HeartRateWidgetProvider()) { entry in
            HeartRateWidgetView(entry: entry)
        }
        .configurationDisplayName("Heart Rate")
        .description("View your heart rate zones at a glance")
        .supportedFamilies([.systemSmall, .systemMedium])
    }
}
