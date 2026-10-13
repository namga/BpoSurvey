#!/usr/bin/env bash
set -euo pipefail
node --check web/app.js
cmp web/index.html android/app/src/main/assets/index.html
cmp web/app.js android/app/src/main/assets/app.js
cmp web/styles.css android/app/src/main/assets/styles.css
cmp web/icon.svg android/app/src/main/assets/icon.svg
cmp web/manifest.webmanifest android/app/src/main/assets/manifest.webmanifest
cmp web/assets/bpo-header-bg.webp android/app/src/main/assets/assets/bpo-header-bg.webp
cmp web/assets/BPO-Intelligence-Hub-Excel-Template.xlsx android/app/src/main/assets/assets/BPO-Intelligence-Hub-Excel-Template.xlsx
grep -q "versionName '12.2.0'" android/app/build.gradle
grep -q 'versionCode 45' android/app/build.gradle
printf 'Source validation passed. Product 12.2.0 / Android build 45.\n'
