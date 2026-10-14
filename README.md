# BPO Intelligence Hub — v12.3.4 (Daily Market Signals)

Ứng dụng tra cứu và quản lý dữ liệu BPO cho một người dùng trên thiết bị cá nhân. Bản này gộp các thay đổi trước đó vào một source thống nhất; số phiên bản sản phẩm hiển thị là **12.3.4**.

## Chức năng đã gộp

- **Tổng quan:** dashboard dự án, nhân sự, khách hàng và cơ hội tiềm năng.
- **Tra cứu 3 chiều:** tìm theo vị trí công việc, khu vực hoặc ngành/lĩnh vực; hiển thị các yếu tố liên quan và các dự án đang chạy. Có gợi ý theo miền Bắc, miền Trung, miền Nam và các ngành BPO phổ biến. Danh mục gợi ý không phải xác nhận dự án thực tế.
- **Dự án & nhân sự:** thêm/sửa/xóa, xem chi tiết; lọc theo khu vực, quận/huyện, phường/xã, trạng thái và ngành.
- **Khách hàng:** xem chi tiết và lọc theo khu vực, nhóm, trạng thái.
- **Lương & phí dịch vụ:** benchmark theo vị trí/khu vực/ngành; lọc khu vực, trạng thái nguồn và ngành. Số liệu mẫu phải được xác minh trước khi sử dụng.
- **Cơ hội tiềm năng:** nhấn tên để xem chi tiết; sửa thông tin từ cửa sổ chi tiết.
- **Giao diện:** màu chủ đạo tùy chỉnh, sáng/tối/theo hệ thống, cỡ chữ; màu nhấn được áp dụng nhất quán cho điều hướng, nút, bộ lọc và thành phần tra cứu.
- **Excel & dữ liệu:** chỉ còn một ô chọn file; hỗ trợ template XLSX tích hợp, nhập XLSX khi thư viện SheetJS tải được, nhập CSV offline, xuất XLSX khi có thư viện hoặc CSV dự phòng. Có xem trước số dòng trước khi nhập.
- **Tải tệp Android:** dùng hộp thoại lưu tệp hệ thống cho template, bản xuất và bản sao lưu JSON. Chọn thư mục/filename trong hộp thoại Android để tệp được lưu thật. Hủy hộp thoại thì không tạo tệp.
- **Sao lưu & phục hồi:** xuất JSON và nhập lại dữ liệu từ bản sao lưu.
- **Web/PWA + Android WebView:** cùng dùng một bộ source; Android assets được đồng bộ từ `web/`.
- **GitHub Actions:** workflow build APK debug và workflow build iOS bằng Capacitor trên macOS runner.

## Phiên bản Android

- Product version (`versionName`): **12.3.4**.
- Android build number (`versionCode`): **49**. Đây là mã nội bộ để Android cho phép cập nhật APK; người dùng thấy phiên bản sản phẩm 12.3.4.

## Build APK bằng GitHub Actions

1. Tạo hoặc mở repository GitHub.
2. Upload **nội dung thư mục `bpo-hub/`** vào root repository, không upload thêm một lớp thư mục ngoài. Phải thấy `.github/`, `android/`, `web/`, `README.md`.
3. Mở **Actions → Build Android APK → Run workflow**.
4. Khi workflow hoàn tất thành công, tải artifact `BPO-Intelligence-Hub-debug-apk`.
5. Cài APK và kiểm tra trên điện thoại.

## Build iOS bằng GitHub Actions (Capacitor)

Workflow `.github/workflows/build-ios-app.yml` dùng macOS runner, đóng gói `web/` vào Capacitor và build hai đầu ra: ứng dụng cho iOS Simulator và bản build thiết bị iOS chưa ký (unsigned).

1. Upload **nội dung thư mục `bpo-hub/`** lên root repository, cùng cấp `.github/`, `web/`, `android/`, `package.json` và `capacitor.config.json`.
2. Mở **Actions → Build BPO Intelligence Hub iOS → Run workflow**. Workflow cũng tự chạy khi có thay đổi trong `web/` hoặc cấu hình Capacitor trên nhánh `main`/`master`.
3. Tải artifact `BPO-Intelligence-Hub-iOS-build`.
4. Bản `.app` Simulator dùng để chạy trong iOS Simulator trên máy Mac. File ZIP device là bản build **chưa ký**, dùng cho bước ký/phát hành tiếp theo.

**Giới hạn tài khoản Apple:** workflow này không tạo IPA đã ký và không thể cài trực tiếp bản unsigned lên iPhone. Để phân phối lên iPhone, TestFlight hoặc App Store, cần cấu hình chứng chỉ/provisioning profile và tài khoản Apple Developer phù hợp. Chưa cần đưa chứng chỉ bí mật vào repository để thử build unsigned.

## Lưu ý về dữ liệu và kiểm thử

- Dữ liệu được lưu cục bộ trên thiết bị/trình duyệt hiện tại; xóa dữ liệu ứng dụng hoặc đổi thiết bị có thể làm mất dữ liệu. Xuất JSON backup định kỳ.
- App không tự thu thập dữ liệu từ các website; phần nghiên cứu tạo truy vấn tới nguồn công khai để người dùng tự xác minh. Không nhập dữ liệu cá nhân nhạy cảm hoặc thông tin riêng tư.
- Dự án demo được đánh dấu giả lập; không được coi là khách hàng hay dự án thật.
- Source đã được kiểm tra cú pháp JavaScript và cấu trúc ZIP. APK cần được build bằng workflow và kiểm thử trên thiết bị thực tế; việc kiểm tra source không thay thế kiểm thử thiết bị.


## Detail views audit (product version 12.0.0)
- Customer records: click company name to open detail modal.
- Project records: click project/customer name to open detail modal.
- Job/benchmark records: click job position to open detail modal.
- Potential leads: click company/target name to open detail modal.
- Detail modal includes an edit action wired to the relevant record form.

## Bản cập nhật UX v12.0.0
- Vuốt từ cạnh trái sang phải để quay lại; nút Back và phím Back Android đóng modal/menu trước, quay về màn hình trước hoặc trang Tổng quan. Tại trang Tổng quan, nhấn Back hai lần trong 2 giây để thoát.
- Menu di động có lớp nền để đóng menu khi chạm ra ngoài; bỏ xử lý click điều hướng bị lặp.
- Cỡ chữ mặc định tăng lên 15px, tăng cỡ chữ bảng và biểu mẫu.
- Trường lương/phí hỗ trợ nhập số thập phân bằng dấu phẩy hoặc dấu chấm; thêm đơn vị tính lương theo giờ/tháng, phí theo người/giờ, người/tháng, phần trăm lương hoặc phí cố định/lần, cùng thuế/VAT (%).
- Phiên bản sản phẩm giữ `12.0.0`; Android `versionCode` tăng lên `18` để cài cập nhật đè lên bản build trước.


## Import dữ liệu từ bảng có sẵn
- Trang Excel & dữ liệu tự nhận diện các tiêu đề tiếng Việt tương đương: `Khu vực`, `Lĩnh vực của KH`, `Tên Dự án (tên KH)`, `Vị trí nhân sự`, `Phí Dịch vụ – năm gần nhất`.
- Có thể nhập workbook `.xlsx` hoặc xuất sheet thành `.csv` UTF-8. Dữ liệu tự động tạo/cập nhật hồ sơ dự án, khách hàng tiềm năng và benchmark vị trí/phí.
- Mức phí được phân tích từ chuỗi như `480,000 VNĐ/nhân sự/ngày (năm 2026)`; đơn vị ngày/giờ/tháng được giữ lại. Bản ghi nhập từ dữ liệu tùy biến được đánh dấu `Cần xác minh`; kiểm tra trước khi sử dụng.
- Giữ nguyên `versionName 12.0.0`; Android `versionCode 19` để cho phép cập nhật APK mới.


## Danh mục khu vực và ngành nghề (v12.0.0)
- Biểu mẫu dự án, khách hàng, benchmark và cơ hội dùng danh sách chọn có sẵn cho Khu vực và Ngành/lĩnh vực.
- Chọn “＋ Khác (nhập mới)” để nhập giá trị mới. Giá trị tùy chỉnh được lưu vào bản ghi và tự xuất hiện trong danh sách lựa chọn của các biểu mẫu sau.
- Các giá trị hiện có trong dữ liệu cũng được thêm vào danh sách, giúp sửa bản ghi cũ không làm mất lựa chọn.
- Phiên bản sản phẩm vẫn là 12.0.0; versionCode Android tăng lên 20 để cho phép cài cập nhật APK.


## Giao diện v12 – Visual Refresh
- Làm mới giao diện theo phong cách SaaS hiện đại, tím-indigo thống nhất toàn app.
- Cải thiện tương phản, khoảng cách, bo góc, trạng thái hover/focus, bảng, form và modal.
- Tối ưu dashboard cho màn hình điện thoại và chế độ tối.
- Phiên bản sản phẩm vẫn là 12.0.0; Android versionCode 22 để cài cập nhật.


## App icon update — v12.2.0
- Thiết kế icon launcher tối giản: chữ B cách điệu kết hợp biểu đồ tăng trưởng, tông tím–xanh trên nền sáng.
- Cập nhật icon Android cho mọi mật độ màn hình và adaptive icon; đồng bộ icon PWA với giao diện mới.
- Product version: `12.2.0`; Android `versionCode`: `43`.
- Không thay đổi cấu trúc dữ liệu hoặc chức năng nghiệp vụ trong bản cập nhật icon này.


## Update 12.2.0 — linked search and instant filters
- Three-way search now reads projects, clients, pricing/position benchmarks and leads from the local database, including imported rows. Results are clickable to open the matching detail record.
- Project and client filters apply immediately when a select changes; the redundant Apply buttons have been removed. Clear-filter buttons remain.
- Android WebView assets are synced from the web source. Android internal build code: 24; product version remains 12.2.0.

## Sao lưu JSON mã hóa / giải mã (v12.2.0)
- Trang **Excel & dữ liệu** có nút **Xuất sao lưu mã hóa (.bpoenc)** và **Chọn file JSON / mã hóa để khôi phục**.
- File `.bpoenc` dùng AES-256-GCM; khóa được tạo từ mật khẩu qua PBKDF2-HMAC-SHA256 với salt ngẫu nhiên và 210.000 vòng lặp. Mỗi file có IV ngẫu nhiên riêng.
- Trên Android, mã hóa/giải mã dùng API mật mã của Java; trên trình duyệt web, dùng Web Crypto API khi chạy trong ngữ cảnh được hỗ trợ.
- File JSON sao lưu thường vẫn được hỗ trợ. Khi chọn file mã hóa, app hỏi mật khẩu trước khi giải mã và xác thực cấu trúc dữ liệu trước khi khôi phục.
- Mật khẩu không được lưu trong app. Nếu quên mật khẩu, không thể khôi phục nội dung file mã hóa. Hãy giữ mật khẩu riêng và lưu an toàn.
- Product version giữ `12.2.0`; Android `versionCode` tăng lên `25` để cài cập nhật APK.


## Theme refresh — v12.2.0

- Cài đặt giao diện được chuyển thành bộ chọn theme dạng thẻ, áp dụng ngay khi chạm.
- Bổ sung hai theme mới: **Tươi trẻ, năng động** (xanh lá/teal, điểm nhấn vàng) và **Lãng mạn, thơ mộng** (hồng/hoa cà).
- Giữ các theme **Mặc định / Hiện đại**, **Tối (Dark)** và **Theo hệ thống**. Theme Theo hệ thống tự theo dõi thay đổi chế độ sáng/tối của thiết bị.
- Đồng bộ palette cho nền, sidebar, thẻ KPI, nút, bộ lọc, bảng và biểu mẫu; tùy chọn màu nhấn riêng vẫn hoạt động. Có thể khôi phục màu nhấn mặc định theo theme.
- Product version vẫn là `12.2.0`; Android `versionCode` là `26`.


### v12.2.0 — Three-way search data integrity fix
- Fresh installs start with empty business records; no demo projects, clients, leads, or sample benchmarks are inserted automatically.
- On upgrade, only known built-in sample IDs (`P-DEMO-*`, `C-DEMO-*`, `R-PUBLIC-*`, `L-DEMO-*`) are removed; other locally stored records are preserved.
- Three-way search suggestion lists are generated only from saved projects, pricing benchmarks, clients, and leads.
- When no matching records exist, the UI explicitly asks the user to add/import data.
- Product version remains 12.2.0; Android versionCode 27.


### v12.2.0 — startup and navigation hotfix
- Fixed a startup crash on fresh installs: `load()` was called before `EMPTY_DB` had initialized, causing a temporal-dead-zone `ReferenceError`. Because the app script stopped during initialization, delegated navigation and the mobile menu never received their event handlers.
- Moved the empty database definition before initialization and replaced `structuredClone` in the fallback path with a plain empty-object copy for older Android WebView compatibility.
- Removed the unused hard-coded demo seed dataset from the application bundle. Fresh installs begin with empty business data; known sample records are filtered from existing local storage.
- Synced web JavaScript into Android assets and updated asset cache tokens. Product version remains `12.2.0`; Android `versionCode` is `28`.


### v12.2.0 — dashboard compactness and search UX fix

- Three-way search now shows a short prompt until at least one of position, region, or industry is entered; it no longer renders every saved record by default.
- Matching result tables are limited to 8 rows initially, with a bounded internal scroll area and an explicit expand/collapse control. The three dimension chip lists are also height-limited on mobile.
- “Cập nhật gần đây” now has its own record-type filter (all, projects, clients, benchmarks/fees, opportunities), shows 3 records by default, and can expand to 20.
- Product version remains `12.2.0`; Android `versionCode` is `29`.


## Quality pass (product version 12.2.0)
- Android build code: 31 (product version remains 12.2.0).
- Uses system font fallbacks to avoid blocking startup on remote Google Fonts.
- Filtered lists update in place, preserve current filter selections and show a live result count.
- Navigation back button is removed from layout when unavailable; edge-swipe avoids controls, tables and modals.
- Excel template sheets contain headers only; no illustrative/sample rows are imported accidentally.
- Save failures restore the last successfully persisted local database snapshot and show an error.
- Sidebar backup action is labeled JSON backup to match the actual exported format.


### v12.2.0 — Three-way search suggestion popup fix

- Replaced native HTML `datalist` suggestions in the three-way market finder with an opaque, theme-aware custom dropdown for Android WebView compatibility.
- Supports tap-to-select, typing custom values, keyboard arrows, Enter, and Escape.
- Preserves the existing search/filter logic and keeps results sourced from saved records only.
- Product version remains 12.2.0; Android build code is 36.


## Version 12.2.0 — Settings icon refresh
- Replaced the Unicode gear with a crisp inline SVG precision-slider icon, better aligned with the app's modern theme.
- Preserved the `settingsBtn` identifier and click handler so opening Settings continues to work.
- Updated Android product version to `12.2.0` and build code to `42`.
- Web assets are mirrored to Android assets and validated before build.


## Settings icon correction — v12.2.0
- Replaced the mistaken three-slider symbol with a conventional outlined gear icon.
- Kept the existing `settingsBtn` ID, click handler, theme-based `currentColor`, and Settings navigation.
- Product version remains `12.2.0`; Android build code is `43`.


## Version 12.2.1 — status map and competitor / potential projects
- Map markers distinguish active projects (green), projects requiring verification (red), and competitor/potential records (purple), with counts on each marker where the region can be matched.
- Added `recordType` to opportunity records: `Dự án đối thủ`, `Dự án tiềm năng`, or `Cơ hội khách hàng`. Existing records remain compatible and default to `Cơ hội khách hàng` in the legacy opportunity list.
- Regional statistics now include competitor and potential project counts, a separate filterable table, and public web-search links per region. Web results are opened externally and must be manually reviewed and saved; the app does not silently scrape external websites.
- Product version `12.2.1`; Android `versionCode` `48`.


## Version 12.3.4 — Daily Market Signals

- Added `.github/workflows/update-market-signals.yml`, scheduled daily at 08:00 Vietnam time (01:00 UTC), with a manual `workflow_dispatch` option.
- `scripts/update-market-signals.py` collects public Google News RSS headlines on BPO/outsourcing, contact center/customer service, hiring, labor and salary topics. It records headline, publisher, original article URL, publication date and topic; it does not scrape article bodies or invent market facts.
- The workflow validates the generated JSON and commits `web/data/market-signals.json`. All GitHub Actions must have permission to write repository contents; the repository must allow GitHub Actions to create commits.
- The dashboard shows up to 10 current headlines, last refresh time and a manual refresh button. It attempts to fetch the remote feed when the app opens; the installed Android/iOS build receives its public raw GitHub feed URL during the build workflow. **The repository must be public for an unsigned client to fetch the raw JSON without credentials.** Never embed a personal access token in the app.
- In hosted web deployments, `data/market-signals.json` is loaded relative to the site. The Android/iOS build workflows generate `market-feed-config.js` with the repository raw URL.
- The daily feed action needs to run successfully at least once before fresh results appear. GitHub scheduled workflows may be delayed, and upstream RSS availability can vary. If all queries fail, the script preserves the previous feed and fails visibly in Actions.
- Product version `12.3.4`; Android `versionCode 52`.


## Version 12.3.4 — Market signal list
- Market signal panel now shows the first 10 items and provides a “Xem thêm” button to reveal 10 more per tap until all fetched RSS items are visible.
- Product version `12.3.4`; Android `versionCode 52`.


## Version 12.3.4 — Numbered market signal pagination
- Market signals now use numbered pages (10 items per page), with previous/next buttons and current-page highlighting.
- Shows the displayed range (for example, 1–10 / 60) and lets users select pages 1, 2, 3, 4, etc.
- Product version `12.3.4`; Android `versionCode 52`.
