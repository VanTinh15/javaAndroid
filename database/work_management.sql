-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th7 02, 2025 lúc 03:49 AM
-- Phiên bản máy phục vụ: 8.4.4
-- Phiên bản PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `work_management`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `attachments`
--

CREATE TABLE `attachments` (
  `attachment_id` int NOT NULL,
  `task_id` int NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `file_path` varchar(500) NOT NULL,
  `uploaded_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `attachments`
--

INSERT INTO `attachments` (`attachment_id`, `task_id`, `file_name`, `file_path`, `uploaded_at`) VALUES
(1, 1, 'baocao_q1.docx', '/uploads/baocao_q1.docx', '2025-03-23 09:49:13'),
(2, 2, 'hopdong_khachhang.pdf', '/uploads/hopdong_khachhang.pdf', '2025-03-23 09:49:13'),
(3, 3, 'test_results.xlsx', '/uploads/test_results.xlsx', '2025-03-23 09:49:13');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `notifications`
--

CREATE TABLE `notifications` (
  `notification_id` int NOT NULL,
  `task_id` int NOT NULL,
  `notify_time` datetime NOT NULL,
  `message` text NOT NULL,
  `reminder_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `notifications`
--

INSERT INTO `notifications` (`notification_id`, `task_id`, `notify_time`, `message`, `reminder_time`) VALUES
(1, 1, '2024-03-29 08:00:00', 'Nhắc nhở: Hoàn thành báo cáo dự án', NULL),
(2, 2, '2024-04-04 10:00:00', 'Nhắc nhở: Chuẩn bị họp với khách hàng', NULL),
(3, 3, '2024-03-24 14:30:00', 'Nhắc nhở: Kiểm thử phần mềm trước khi triển khai', NULL),
(4, 5, '2025-03-31 09:39:27', 'Bạn có công việc mới: iltut - Hạn chót: 2025-04-10', '2025-04-09 00:00:00'),
(5, 6, '2025-03-31 15:18:39', 'Bạn có công việc mới: okso  sk ok - Hạn chót: 2025-04-09', '2025-04-08 00:00:00'),
(6, 7, '2025-05-11 16:22:56', 'Bạn có công việc mới: co the hay khong the - Hạn chót: 2025-05-11', '2025-05-10 00:00:00'),
(17, 18, '2025-05-12 12:07:08', 'Bạn có công việc mới: tet cong vic hom nay dang tong - Hạn chót: 2025-05-12', '2025-05-11 00:00:00'),
(18, 19, '2025-05-12 12:22:23', 'Bạn có công việc mới: tieu de - Hạn chót: 2025-05-12', '2025-05-11 00:00:00'),
(19, 20, '2025-05-12 17:25:42', 'Bạn có công việc mới: nhap tie tie eitie tiei - Hạn chót: 2025-05-12', '2025-05-11 00:00:00'),
(20, 21, '2025-05-15 11:39:07', 'Bạn có công việc mới: mo ta tieu de - Hạn chót: 2025-05-15', '2025-05-14 00:00:00'),
(21, 22, '2025-05-15 16:26:59', 'Bạn có công việc mới: tieu die cong viec - Hạn chót: 2025-05-15', '2025-05-14 00:00:00'),
(22, 23, '2025-05-15 16:27:16', 'Bạn có công việc mới: tieu tieu tuei - Hạn chót: 2025-05-15', '2025-05-14 00:00:00'),
(23, 24, '2025-05-15 16:29:11', 'Bạn có công việc mới: tiueu de - Hạn chót: 2025-05-15', '2025-05-14 00:00:00'),
(24, 25, '2025-05-15 16:54:17', 'Bạn có công việc mới: admin - Hạn chót: 2025-05-15', '2025-05-14 00:00:00'),
(25, 26, '2025-05-16 12:48:02', 'Bạn có công việc mới: hoan thanh thong ke - Hạn chót: 2025-05-16', '2025-05-15 00:00:00'),
(26, 27, '2025-05-22 04:38:21', 'Bạn có công việc mới: tieu de cong viec - Hạn chót: 2025-05-22', '2025-05-21 00:00:00'),
(27, 28, '2025-05-26 03:54:25', 'Bạn có công việc mới: Thiết kế giao diện - Hạn chót: 2025-05-26', '2025-05-25 00:00:00'),
(28, 29, '2025-05-26 03:54:59', 'Bạn có công việc mới: Tối ưu hiệu suất - Hạn chót: 2025-05-26', '2025-05-25 00:00:00'),
(29, 30, '2025-05-26 03:55:25', 'Bạn có công việc mới: Kiểm thử chức năng - Hạn chót: 2025-05-26', '2025-05-25 00:00:00'),
(30, 31, '2025-05-26 03:55:50', 'Bạn có công việc mới: Triển khai lên server - Hạn chót: 2025-05-26', '2025-05-25 00:00:00'),
(31, 42, '2025-06-17 03:59:00', 'Bạn có công việc mới: hhhh - Hạn chót: 2025-06-17', '2025-06-16 00:00:00');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tasks`
--

CREATE TABLE `tasks` (
  `task_id` int NOT NULL,
  `user_id` int NOT NULL,
  `task_title` varchar(255) NOT NULL,
  `task_description` text,
  `created_date` date DEFAULT (curdate()),
  `due_date` date NOT NULL,
  `status` enum('Đang chờ','Đã hoàn thành') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'Đang chờ'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `tasks`
--

INSERT INTO `tasks` (`task_id`, `user_id`, `task_title`, `task_description`, `created_date`, `due_date`, `status`) VALUES
(1, 1, 'Báo cáo dự án', 'Viết báo cáo tiến độ dự án quý 1', '2025-03-23', '2024-03-30', 'Đang chờ'),
(2, 2, 'Họp với khách hàng', 'Chuẩn bị tài liệu và họp với khách hàng về hợp đồng', '2025-03-23', '2024-04-05', 'Đã hoàn thành'),
(3, 3, 'Kiểm thử phần mềm', 'Chạy test cho module đăng nhập và ghi nhận lỗi', '2025-03-23', '2025-03-25', 'Đã hoàn thành'),
(5, 4, 'ngon ngu ky hieu', 'xin chao tieng viet', '2025-03-31', '2025-05-11', 'Đang chờ'),
(6, 4, 'khi nao d di di', 'co the u', '2025-03-31', '2025-04-09', 'Đang chờ'),
(7, 4, 'co the hay khong the', 'Chay Demo Teas App', '2025-05-11', '2025-05-11', 'Đã hoàn thành'),
(18, 4, 'tet cong vic hom nay dang tong', 'cho ngy mo ta', '2025-05-12', '2025-05-05', 'Đang chờ'),
(19, 4, 'tieu de', 'mo ta', '2025-05-12', '2025-05-12', 'Đang chờ'),
(20, 4, 'yỉu fiuhu', 'mo toa  cong vic', '2025-05-12', '2025-05-12', 'Đang chờ'),
(21, 4, 'mo ta tieu de', 'mo ta cong ve ây aha', '2025-05-15', '2025-05-15', 'Đang chờ'),
(22, 3, 'tieu die cong viec hahah', 'mo ta cong viec huhu', '2025-05-15', '2025-05-16', 'Đang chờ'),
(23, 3, 'tieu tieu dai huhu', 'mo ta mo ta', '2025-05-15', '2025-05-15', 'Đang chờ'),
(24, 3, 'hu ha hi he', 'gga h', '2025-05-15', '2025-05-15', 'Đang chờ'),
(25, 4, 'admin', 'mo ta', '2025-05-15', '2025-05-15', 'Đang chờ'),
(26, 4, 'hoan thanh thong ke', 'chuc nang trang thong ke tinh tong so va hien thi bieu do theo ngay thang nam', '2025-05-16', '2025-05-16', 'Đang chờ'),
(27, 4, 'tieu de cong viec', 'mo ta cong viec hom nay', '2025-05-22', '2025-05-22', 'Đã hoàn thành'),
(28, 4, 'Thiết kế giao diện', 'Thiết kế UI cho màn hình chính', '2025-05-26', '2025-05-26', 'Đang chờ'),
(29, 4, 'Tối ưu hiệu suất', 'Cải thiện tốc độ load dữ liệu', '2025-05-26', '2025-05-26', 'Đang chờ'),
(30, 4, 'Kiểm thử chức năng', 'Viết test cho các module chính', '2025-05-26', '2025-05-26', 'Đang chờ'),
(31, 4, 'Triển khai lên server', 'Đưa phiên bản mới lên máy chủ staging', '2025-05-26', '2025-05-26', 'Đang chờ'),
(32, 3, 'Viết báo cáo tuần', 'Báo cáo công việc từ thứ 2 đến thứ 6', '2025-06-15', '2025-06-15', 'Đang chờ'),
(33, 3, 'Chuẩn bị slide thuyết trình', 'Chuẩn bị nội dung và thiết kế slide PowerPoint', '2025-06-15', '2025-06-15', 'Đang chờ'),
(34, 3, 'Gửi email khách hàng', 'Gửi báo giá và thông tin hợp tác', '2025-06-15', '2025-06-15', 'Đang chờ'),
(35, 3, 'Kiểm tra code backend', 'Review API và fix lỗi nếu có', '2025-06-15', '2025-06-15', 'Đang chờ'),
(36, 3, 'Họp với nhóm phát triển', 'Thảo luận tiến độ và công việc tuần tới', '2025-06-15', '2025-06-15', 'Đang chờ'),
(37, 3, 'Đọc tài liệu kỹ thuật', 'Tìm hiểu công nghệ mới để áp dụng vào dự án', '2025-06-15', '2025-06-15', 'Đang chờ'),
(38, 3, 'Cập nhật tiến độ dự án', 'Ghi chú tiến độ lên hệ thống quản lý', '2025-06-15', '2025-06-15', 'Đang chờ'),
(39, 3, 'Test chức năng đăng nhập', 'Kiểm thử tính năng login và báo lỗi nếu có', '2025-06-15', '2025-06-15', 'Đang chờ'),
(40, 3, 'Backup cơ sở dữ liệu', 'Tạo bản sao lưu định kỳ trên máy chủ', '2025-06-15', '2025-06-15', 'Đang chờ'),
(41, 3, 'Giao việc cho thành viên mới', 'Hướng dẫn và phân công công việc ban đầu', '2025-06-15', '2025-06-15', 'Đang chờ'),
(42, 3, 'hhhh', 'hhhh', '2025-06-17', '2025-06-17', 'Đang chờ');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `task_history`
--

CREATE TABLE `task_history` (
  `history_id` int NOT NULL,
  `task_id` int NOT NULL,
  `changed_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `old_status` enum('Đang chờ','Đã hoàn thành') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `new_status` enum('Đang chờ','Đã hoàn thành') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `task_history`
--

INSERT INTO `task_history` (`history_id`, `task_id`, `changed_at`, `old_status`, `new_status`) VALUES
(1, 1, '2025-03-23 09:48:47', 'Đang chờ', 'Đã hoàn thành'),
(2, 2, '2025-03-23 09:48:47', 'Đang chờ', 'Đang chờ'),
(3, 3, '2025-03-23 09:48:47', 'Đang chờ', 'Đã hoàn thành');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `user_id` int NOT NULL,
  `username` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `avatar_url` varchar(200) NOT NULL,
  `fullName` varchar(50) NOT NULL,
  `phoneNumber` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`user_id`, `username`, `email`, `password`, `created_at`, `avatar_url`, `fullName`, `phoneNumber`) VALUES
(1, 'vana', 'nguyenvana@example.com', 'hashedpassword1', '2025-03-23 09:47:44', '', 'Nguyễn Văn A', '0'),
(2, 'tranb', 'tranthib@example.com', 'hashedpassword2', '2025-03-23 09:47:44', '', 'Trần Thị B', '0'),
(3, 'vanc', 'phamvanc@example.com', '333', '2025-03-23 09:47:44', '', 'Nguyễn Văn C', '0'),
(4, 'admin', 'admin@gmail.com', '123', '2025-03-23 09:59:57', '', 'Trần Văn Quan', '0254845457');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `attachments`
--
ALTER TABLE `attachments`
  ADD PRIMARY KEY (`attachment_id`),
  ADD KEY `task_id` (`task_id`);

--
-- Chỉ mục cho bảng `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`notification_id`),
  ADD KEY `task_id` (`task_id`);

--
-- Chỉ mục cho bảng `tasks`
--
ALTER TABLE `tasks`
  ADD PRIMARY KEY (`task_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Chỉ mục cho bảng `task_history`
--
ALTER TABLE `task_history`
  ADD PRIMARY KEY (`history_id`),
  ADD KEY `task_id` (`task_id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `attachments`
--
ALTER TABLE `attachments`
  MODIFY `attachment_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `notifications`
--
ALTER TABLE `notifications`
  MODIFY `notification_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT cho bảng `tasks`
--
ALTER TABLE `tasks`
  MODIFY `task_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT cho bảng `task_history`
--
ALTER TABLE `task_history`
  MODIFY `history_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `attachments`
--
ALTER TABLE `attachments`
  ADD CONSTRAINT `attachments_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `tasks`
--
ALTER TABLE `tasks`
  ADD CONSTRAINT `tasks_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `task_history`
--
ALTER TABLE `task_history`
  ADD CONSTRAINT `task_history_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
