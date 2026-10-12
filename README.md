# BPO Intelligence Hub v3 — Local Survey Edition

Ứng dụng tra cứu và survey thông tin dịch vụ BPO dành cho một Sales sử dụng trên một thiết bị. Ưu tiên tra cứu nhanh theo khu vực, dự án, vị trí nhân sự, mức lương, phí dịch vụ, mô hình dịch vụ, khách hàng hiện tại và doanh nghiệp tiềm năng.

## Tính năng
- Dashboard dự án, khu vực, vị trí, khách hàng và cơ hội.
- CRUD: dự án/nhân sự, khách hàng, benchmark lương và phí, cơ hội tiềm năng.
- Tìm kiếm và lọc theo khu vực/ngành/trạng thái.
- Hỗ trợ nghiên cứu thị trường: tạo truy vấn và mở Google, LinkedIn, VietnamWorks, CareerViet, TopCV, Indeed; lưu truy vấn vào ghi chú để Sales theo dõi.
- Lưu dữ liệu cục bộ trên thiết bị bằng localStorage, không cần tài khoản, máy chủ hoặc API key.
- Xuất/nhập bản sao lưu JSON.
- Web/PWA source và Android WebView project.
- GitHub Actions build APK debug.

## Quan trọng về dữ liệu
App không tự thu thập hay trích xuất nội dung từ trang web; nó tạo truy vấn tìm kiếm trên các nguồn công khai để người dùng tự kiểm tra, xác minh và nhập kết quả. Không tự động đăng nhập mạng xã hội hay thu thập dữ liệu riêng tư. Mọi mức lương/phí cần có nguồn, ngày cập nhật, khu vực, đơn vị tính và trạng thái xác minh. Database chỉ tồn tại trên thiết bị/browser hiện tại; xóa dữ liệu ứng dụng hoặc đổi thiết bị có thể làm mất dữ liệu. Hãy xuất backup định kỳ.

Mặc định database trống, không có dữ liệu khách hàng hay benchmark giả lập.

## Chạy bản web
Mở `web/index.html` trong trình duyệt để dùng thử. Với PWA/service worker đầy đủ hoặc một số trình duyệt, nên host thư mục `web/` qua HTTPS (ví dụ GitHub Pages/Netlify).

## Build APK qua GitHub Actions
1. Tạo repository GitHub mới.
2. Upload **nội dung** thư mục `bpo-hub` vào root repository, không upload thêm một lớp thư mục ngoài. Phải thấy `.github/`, `android/`, `web/`, `README.md`.
3. Mở Actions → **Build Android APK** → Run workflow.
4. Tải artifact `BPO-Intelligence-Hub-debug-apk`; file APK nằm trong artifact.

Workflow dùng Java 17, Android SDK 35 và Gradle 8.7. APK debug cần kiểm thử trên thiết bị thực tế trước khi sử dụng chính thức.

## Nâng cấp sau này
Khi cần nhiều Sales cùng sử dụng, có thể thay lớp lưu trữ local bằng PostgreSQL/Supabase và bổ sung đăng nhập, phân quyền, lịch sử chỉnh sửa và xử lý xung đột dữ liệu. Phiên bản hiện tại không có workspace nhiều người.


## v4 UI & market survey updates
- App icon: `web/icon.svg` (also bundled into Android WebView assets).
- Settings: light/dark/system theme, accent color and font size; stored locally.
- Back navigation button in the page header.
- Excel import/export uses SheetJS from a CDN; an offline connection is needed to load the parser. Download template from **Excel & dữ liệu** or use `BPO-Intelligence-Hub-Excel-Template.xlsx` supplied separately.
- Seeded examples are explicitly marked DEMO; they are fictional and not actual clients/projects. Public salary information is linked in the dashboard and benchmark records. Salary references are not BPO service prices.

### Public sources used for context
- CareerViet Salary — IT Support benchmark: https://vietnamsalary.careerviet.vn/detail/IT-Support-kw
- Adecco Vietnam Salary Guide 2026 (published March 2026): https://www.adecco.com/en-vn/insights/adecco-vietnam-salary-guide-2026
- Institute of Strategy and Policy on Labour and Social Affairs, Vietnam labour market bulletin Q2 2026: https://isos.gov.vn/Ban-Tin-thi-truong-lao-dong/thong-tin-thi-truong-lao-dong-viet-nam-quy-ii-2026-44000.html
- Reeracoen Vietnam salary guide article: https://www.reeracoen.com.vn/vi/employers/articles/vietnam-salary-guide-2025-what-employers-and-jobseekers-need-to-know

Public sources are linked for manual review; this app does not scrape pages automatically. Verify salary range, date, location and methodology before using in a proposal.


## v5: General workforce survey
- Added general workforce survey categories: production workers, warehouse/packing, cleaning, security, delivery, customer service, telesales, data entry/back office, and IT roles.
- Added public official minimum-wage benchmarks for Regions I–IV effective 2026-01-01 and links to official labor-market statistics. These legal floors are not market-average salary or BPO service fees.
- Excel template is bundled in the app under Excel & Data and can be downloaded when needed. The template has README, Projects, Clients, SalaryFees, and Leads sheets.
- General-role salary entries without confirmed numbers are intentionally left blank and marked for verification rather than inventing market wages.
