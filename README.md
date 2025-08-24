# Java Android + PHP API Project

## Cấu trúc
- `app/`: Code Android (Java, Android Studio)
- `php-api/`: Code PHP API chạy bằng XAMPP
- `database/`: File database/work_managerment.sql để import vào MySQL

## Cách chạy
### 1. Setup PHP API
- Cài XAMPP
- Copy thư mục `php-api/` vào `htdocs/`
- Import `database/work_managerment.sql` vào phpMyAdmin
- Chỉnh file `connectDB.php` cho đúng user/password MySQL

### 2. Chạy Android App
- Mở Android Studio, import thư mục project
- Trong code Android, sửa API URL:
  - Nếu chạy Emulator: dùng `http://10.0.2.2/ten_api/`
  - Nếu chạy trên điện thoại thật: dùng IP máy tính, ví dụ `http://192.168.1.10/ten_api/`
