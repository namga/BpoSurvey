# BPO Intelligence Hub v12.1.0 — Build and Header Fix

- Product version remains 12.1.0.
- Android versionCode is 33 so this APK can update build 32.
- Mobile header now keeps the hamburger and page title on the first row. The Back button is a compact iOS-style pill on the same row as global search and Add Data; it no longer consumes title-row width.
- Search is compact, the Back label stays inside its pill, and the header padding/row gap are reduced to prevent an oversized header.
- GitHub Actions runs the source validation script via bash, avoiding executable-bit issues.

Build from GitHub Actions: Actions → Build BPO Intelligence Hub APK → Run workflow.

## Search overlay and Back navigation update (12.1.0 / Android build 40)
- Added a full-screen search view inspired by the supplied reference: simple arrow Back, pill search field, and scrollable suggestions.
- Suggestions are generated only from saved project positions, regions, industries, customer records, benchmarks, and opportunities. Empty datasets are clearly identified; no fake records are inserted.
- Search results can open the corresponding saved record detail.
- Added a compact `← Back` control on non-overview pages; the Overview page keeps the header uncluttered.
- Android assets are synchronized from `web/` and source validation checks build code 40.

## v12.2.0 build 45 — Header control height consistency
- Unified menu, Back, search, and Settings control heights across desktop and mobile breakpoints.
- Unified the dedicated search overlay Back and search field heights.
- Kept product version at 12.2.0; incremented Android versionCode to 45.
