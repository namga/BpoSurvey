#!/usr/bin/env bash
set -euo pipefail
node --check web/app.js
cmp web/index.html android/app/src/main/assets/index.html
cmp web/app.js android/app/src/main/assets/app.js
cmp web/styles.css android/app/src/main/assets/styles.css
cmp web/icon.svg android/app/src/main/assets/icon.svg
cmp web/manifest.webmanifest android/app/src/main/assets/manifest.webmanifest
cmp web/market-feed-config.js android/app/src/main/assets/market-feed-config.js
cmp web/assets/bpo-header-bg.webp android/app/src/main/assets/assets/bpo-header-bg.webp
cmp web/assets/BPO-Intelligence-Hub-Excel-Template.xlsx android/app/src/main/assets/assets/BPO-Intelligence-Hub-Excel-Template.xlsx
grep -q "versionName '12.3.8'" android/app/build.gradle
grep -q 'versionCode 57' android/app/build.gradle
grep -q 'data-view="statistics"' web/index.html
grep -q 'function statisticsPage' web/app.js
grep -q 'Bản đồ phân bố theo trạng thái' web/app.js
grep -q 'map-point-verify circle{fill:#d94848}' web/styles.css
grep -q 'map-point-active circle{fill:#159b72}' web/styles.css
grep -q 'Dự án đối thủ' web/app.js
grep -q 'Dự án tiềm năng' web/app.js
test -s web/market-feed-config.js
test -s web/data/market-signals.json
test -s web/data/recruitment-signals.json
python3 -m json.tool web/data/market-signals.json >/dev/null
python3 -m json.tool web/data/recruitment-signals.json >/dev/null
cmp web/data/market-signals.json android/app/src/main/assets/data/market-signals.json
cmp web/data/recruitment-signals.json android/app/src/main/assets/data/recruitment-signals.json
! grep -q 'Tín hiệu thị trường từ nguồn công khai' web/app.js
grep -q 'Tín hiệu thị trường mới nhất' web/app.js
grep -q 'data-market-page' web/app.js
grep -q 'market-signal-pagination' web/styles.css
grep -q 'update-market-signals.yml' README.md
grep -q 'BPO_RECRUITMENT_FEED_URL' web/app.js
printf 'Source validation passed. Product 12.3.7 / Android build 56.\n'
