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

## Update: dedicated project statistics (12.2.0 / Android build 47)
- Added a dedicated `Thống kê khu vực` navigation view.
- Added project totals, running-project totals, total recorded headcount, and headcount for projects whose status is exactly `Đang chạy`.
- Added a schematic Vietnam map with markers for recognized areas, using counts from locally stored project records only.
- Added a region breakdown showing project counts, running counts, and recorded headcount.
- Removed the long region distribution chart from the main overview and replaced it with a compact shortcut to statistics.
- Kept product version `12.2.0`; incremented Android `versionCode` to `47`.
- Source validation passed. A local APK build was not run because Gradle is not installed in this environment; GitHub Actions remains the APK build path.


## v12.2.1 build 48 — status map and market pipeline
- Map markers use green for active projects, red for projects requiring verification, and purple for competitor/potential records.
- Added market record type to opportunity forms and details, plus regional statistics, filterable competitor/potential rows, and public search links.
- Public web results are not auto-imported; the user verifies the source and manually saves the record to preserve source accuracy.
- Updated web asset cache keys, footer version, Android versionName and workflow version assertion to 12.2.1.


## v12.3.1 — daily market signals
- Added daily RSS collection workflow and manual run option.
- Added dashboard panel for current headlines, source links, publication timestamps and refresh status.
- Android/iOS build jobs inject the public raw JSON URL so installed apps can fetch refreshed signals without reinstalling. This requires a public GitHub repository; no secrets are bundled.
- Android build number: 49.
