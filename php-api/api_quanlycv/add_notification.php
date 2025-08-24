<?php

// Kết nối database
require 'connectDB.php';

try {
    // Nhận dữ liệu JSON
    $data = json_decode(file_get_contents("php://input"));

    if (!isset($data->user_id, $data->task_title, $data->task_description, $data->due_date)) {
        throw new Exception("Thiếu dữ liệu đầu vào!");
    }

    $user_id = $data->user_id;
    $task_title = $data->task_title;
    $task_description = $data->task_description;
    $due_date = $data->due_date;
    $status = "Đang chờ"; // Trạng thái mặc định

    // Bắt đầu giao dịch
    $conn->beginTransaction();

    // 🔹 Thêm công việc vào database
    $query = "INSERT INTO tasks (user_id, task_title, task_description, due_date, status) 
              VALUES (:user_id, :task_title, :task_description, :due_date, :status)";
    $stmt = $conn->prepare($query);

    $stmt->bindParam(':user_id', $user_id, PDO::PARAM_INT);
    $stmt->bindParam(':task_title', $task_title, PDO::PARAM_STR);
    $stmt->bindParam(':task_description', $task_description, PDO::PARAM_STR);
    $stmt->bindParam(':due_date', $due_date, PDO::PARAM_STR);
    $stmt->bindParam(':status', $status, PDO::PARAM_STR);

    if (!$stmt->execute()) {
        throw new Exception("Lỗi SQL (tasks): " . implode(", ", $stmt->errorInfo()));
    }

    // 🔹 Lấy ID của công việc vừa tạo
    $task_id = $conn->lastInsertId();
    if (!$task_id) {
        throw new Exception("Lỗi: Không lấy được task_id!");
    }

    // 🔹 Tạo thông tin thông báo
    $notify_time = date("Y-m-d H:i:s"); // Thời gian tạo thông báo
    $reminder_time = date("Y-m-d H:i:s", strtotime($due_date . " -1 day"));
    $message = "Công việc mới: " . $task_title . " - Hạn chót: " . $due_date;

    // 🔹 Thêm thông báo vào bảng notifications
    $query = "INSERT INTO notifications (task_id, notify_time, reminder_time, message) 
              VALUES (:task_id, :notify_time, :reminder_time, :message)";
    $stmt = $conn->prepare($query);

    $stmt->bindParam(':task_id', $task_id, PDO::PARAM_INT);
    $stmt->bindParam(':notify_time', $notify_time, PDO::PARAM_STR);
    $stmt->bindParam(':reminder_time', $reminder_time, PDO::PARAM_STR);
    $stmt->bindParam(':message', $message, PDO::PARAM_STR);

    if (!$stmt->execute()) {
        throw new Exception("Lỗi SQL (notifications): " . implode(", ", $stmt->errorInfo()));
    }

    // 🔹 Commit giao dịch nếu mọi thứ thành công
    $conn->commit();

    echo json_encode(["message" => "Công việc & thông báo đã được thêm!", "task_id" => $task_id]);

} catch (Exception $e) {
    // Hoàn tác nếu có lỗi
    

    // Ghi log lỗi vào file
    error_log("LỖI: " . $e->getMessage(), 3, "error_log.txt");

    // Trả về thông tin lỗi
    echo json_encode(["message" => "Lỗi: " . $e->getMessage()]);
}

?>
