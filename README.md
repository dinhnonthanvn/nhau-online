# Nhậu Online — Android MVP 0.1.0

## Định hướng
- Android native: Kotlin + Jetpack Compose
- Avatar cartoon (emoji placeholder trong bản scaffold)
- Phòng nhậu góc nhìn top-down
- Chat chữ và biểu cảm (demo local)
- Dữ liệu bàn, bạn bè và tin nhắn hiện là dữ liệu mẫu trong bộ nhớ

## Mở dự án
1. Giải nén `NhauOnlineMVP.zip`.
2. Mở thư mục `NhauOnlineMVP` bằng Android Studio.
3. Dùng JDK 17.
4. Chờ Gradle Sync tải các dependency.
5. Chạy app trên emulator hoặc điện thoại Android (API 26 trở lên).

## Những gì đã có
- Trang chủ và danh sách bàn.
- Tạo bàn nhậu với tên và số chỗ.
- Vào phòng nhậu minh họa top-down.
- Avatar placeholder, nút biểu cảm và chat local.
- Màn hình cửa hàng, bạn bè, hồ sơ.

## Những gì chưa có
Đây là scaffold MVP giao diện, chưa phải bản phát hành trực tuyến:
- Chưa có đăng ký/đăng nhập thật.
- Chưa có backend, WebSocket hoặc đồng bộ nhiều thiết bị.
- Dữ liệu mất khi đóng ứng dụng.
- Avatar hiện dùng emoji placeholder, chưa phải bộ nhân vật đồ họa hoàn chỉnh.
- Cửa hàng chưa có sản phẩm thật, đặt hàng hoặc thanh toán.

## Kiến trúc dự kiến cho bước tiếp theo
- Android: MVVM + Repository + Retrofit
- Backend: NestJS REST API + WebSocket Gateway
- Database: PostgreSQL
- Redis: presence, room state và scale-out realtime
- Admin: web dashboard riêng

## Gói Android
Application ID: `vn.nhauonline.app`
Version: `0.1.0`
Min SDK: 26
Target SDK: 35
