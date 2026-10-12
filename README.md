# BPO Intelligence Hub — v12.1.0 (Professional App Icon)

Ứng dụng tra cứu và quản lý dữ liệu BPO cho một người dùng trên thiết bị cá nhân. Bản này gộp các thay đổi trước đó vào một source thống nhất; số phiên bản sản phẩm hiển thị là **12.1.0**.

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
- **GitHub Actions:** workflow build APK debug.

## Phiên bản Android

- Product version (`versionName`): **12.1.0**.
- Android build number (`versionCode`): **23**. Đây là mã nội bộ để Android cho phép cập nhật APK; người dùng thấy phiên bản sản phẩm 12.1.0.

## Build APK bằng GitHub Actions

1. Tạo hoặc mở repository GitHub.
2. Upload **nội dung thư mục `bpo-hub/`** vào root repository, không upload thêm một lớp thư mục ngoài. Phải thấy `.github/`, `android/`, `web/`, `README.md`.
3. Mở **Actions → Build Android APK → Run workflow**.
4. Khi workflow hoàn tất thành công, tải artifact `BPO-Intelligence-Hub-debug-apk`.
5. Cài APK và kiểm tra trên điện thoại.

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


## App icon update — v12.1.0
- Thiết kế icon launcher tối giản: chữ B cách điệu kết hợp biểu đồ tăng trưởng, tông tím–xanh trên nền sáng.
- Cập nhật icon Android cho mọi mật độ màn hình và adaptive icon; đồng bộ icon PWA với giao diện mới.
- Product version: `12.1.0`; Android `versionCode`: `23`.
- Không thay đổi cấu trúc dữ liệu hoặc chức năng nghiệp vụ trong bản cập nhật icon này.


## Update 12.1.0 — linked search and instant filters
- Three-way search now reads projects, clients, pricing/position benchmarks and leads from the local database, including imported rows. Results are clickable to open the matching detail record.
- Project and client filters apply immediately when a select changes; the redundant Apply buttons have been removed. Clear-filter buttons remain.
- Android WebView assets are synced from the web source. Android internal build code: 24; product version remains 12.1.0.
