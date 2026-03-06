# Screen Porting - Improvement Suggestions

These are improvements noted during the port from old VitalsCalc to VitalsCalcNew.
They are NOT blocking and should be done in a separate pass.

## Future Improvements (Post-Port)

### 1. Landscape Support
- Old app had portrait/landscape layouts via StandardHealthScaffold
- New app uses ScreenWithToolbar (portrait only)
- Consider adding landscape support later for tablets/iPad

### 2. Analytics Integration
- Old app logged screen_viewed events via AnalyticsHelper
- Not yet wired up in new app (KAppMaker analytics not configured)

### 3. Profile Data Validation
- Old BMI screen checked if DOB/height were missing and redirected to profile
- Consider adding similar guard in new app after profile system is fully integrated

### 4. Auto-Calculate on Input Change
- Old BMI auto-calculated on weight field blur (focus lost)
- New app uses explicit "Calculate" button
- Consider: auto-calc could be UX improvement but button is more explicit

### 5. Widget Updates
- Old app updated home screen widgets after calculations
- WidgetUpdater is wired but not fully implemented in new app

### 6. Pro/Premium Feature Gating
- Old app had Pro-gated features: charts in history, CSV export, unlimited history
- New app has subscription infrastructure but not integrated with screens

### 7. CupertinoTheme ColorScheme
- Per cmp-native-ui skill: should configure CupertinoTheme colorScheme explicitly
- Currently only Material colorScheme is configured in AdaptiveTheme

### 8. Platform-Adaptive Lists
- Old history screen had iOS grouped inset lists vs Android card lists
- Could enhance with AdaptiveListSection/AdaptiveListItem pattern

### 9. Accessibility Enhancements
- Old app had comprehensive semantics: contentDescriptions, liveRegions, headings
- Port carried over basics but could enhance further

### 10. BMI Dynamic Graphics Chart
- Old app had interactive arrow-based BMI category visualization (Canvas-drawn)
- Ported version uses BmiIndicatorBar which is simpler
- Consider porting the full BmiDynamicGraphicsChart for visual parity

### 11. Charts in History
- Old app showed line/bar charts (via Vico library) for BMI/Weight/BMR trends
- Not yet ported - requires Vico dependency and Pro gating
