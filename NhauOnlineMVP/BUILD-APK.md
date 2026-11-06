# Build APK bằng GitHub Actions (không cần cài Android Studio)

1. Tạo một repository GitHub mới, ví dụ `nhau-online-android`.
2. Giải nén mã nguồn và tải toàn bộ nội dung thư mục `NhauOnlineMVP` lên repository (giữ nguyên cấu trúc, bao gồm `.github/workflows/android-apk.yml`).
3. Mở tab **Actions** của repository.
4. Chọn workflow **Build Nhau Online APK** rồi bấm **Run workflow** (hoặc push lên nhánh `main` để tự chạy).
5. Khi workflow chạy xong, mở lần chạy thành công và tải artifact **NhauOnline-debug-apk**.
6. Giải nén artifact để lấy `app-debug.apk`, gửi sang điện thoại Android và cài đặt. Có thể cần cho phép cài ứng dụng từ nguồn này trong phần cài đặt Android.

Đây là debug APK để thử nghiệm, chưa phải bản phát hành Play Store. Mã nguồn hiện vẫn dùng dữ liệu mẫu trong bộ nhớ; đăng nhập thật, đồng bộ nhiều thiết bị, máy chủ chat và cửa hàng chưa được triển khai.
