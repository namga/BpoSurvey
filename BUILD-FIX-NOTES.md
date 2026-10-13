# BPO Intelligence Hub v12.1.0 — Build and Header Fix

- Product version remains 12.1.0.
- Android versionCode is 33 so this APK can update build 32.
- Mobile header now keeps the hamburger and page title on the first row. The Back button is a compact iOS-style pill on the same row as global search and Add Data; it no longer consumes title-row width.
- Search is compact, the Back label stays inside its pill, and the header padding/row gap are reduced to prevent an oversized header.
- GitHub Actions runs the source validation script via bash, avoiding executable-bit issues.

Build from GitHub Actions: Actions → Build BPO Intelligence Hub APK → Run workflow.
