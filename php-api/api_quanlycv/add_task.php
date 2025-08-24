<?php
require 'connectDB.php'; // Đảm bảo file này chứa kết nối PDO

// Nhận dữ liệu JSON từ Android
$data = json_decode(file_get_contents("php://input"), true);

// Ghi log dữ liệu nhận được để debug
file_put_contents("debug_log.txt", json_encode($data, JSON_PRETTY_PRINT) . "\n", FILE_APPEND);

// Kiểm tra dữ liệu đầu vào
if (!$data || !isset($data['user_id'], $data['task_title'], $data['due_date'])) {
    echo json_encode(["message" => "Thiếu dữ liệu đầu vào hoặc dữ liệu không hợp lệ!"]);
    exit();
}

// Gán biến từ dữ liệu đầu vào
$user_id = $data['user_id'];
$task_title = $data['task_title'];
$task_description = isset($data['task_description']) ? $data['task_description'] : '';
$due_date = $data['due_date'];
$created_date = !empty($data['created_date']) ? $data['created_date'] : date("Y-m-d");
$status = isset($data['status']) ? $data['status'] : 'Đang chờ';

try {
    if (!$conn) {
        throw new Exception("Lỗi kết nối CSDL!");
    }

    // Bắt đầu transaction
    $conn->beginTransaction();

    // Chèn dữ liệu vào bảng tasks
    $query = "INSERT INTO tasks (user_id, task_title, task_description, created_date, due_date, status) 
              VALUES (:user_id, :task_title, :task_description, :created_date, :due_date, :status)";
    
    $stmt = $conn->prepare($query);
    $stmt->bindParam(':user_id', $user_id, PDO::PARAM_INT);
    $stmt->bindParam(':task_title', $task_title, PDO::PARAM_STR);
    $stmt->bindParam(':task_description', $task_description, PDO::PARAM_STR);
    $stmt->bindParam(':created_date', $created_date, PDO::PARAM_STR);
    $stmt->bindParam(':due_date', $due_date, PDO::PARAM_STR);
    $stmt->bindParam(':status', $status, PDO::PARAM_STR);

    if (!$stmt->execute()) {
        throw new Exception("Lỗi SQL khi chèn công việc: " . implode(", ", $stmt->errorInfo()));
    }

    // Lấy ID công việc vừa chèn
    $task_id = $conn->lastInsertId();

    // Tạo thời gian thông báo
    $notify_time = date("Y-m-d H:i:s");
    $reminder_time = date("Y-m-d H:i:s", strtotime($due_date . " -1 day"));
    $message = "Bạn có công việc mới: " . $task_title . " - Hạn chót: " . $due_date;

    // Chèn thông báo vào bảng notifications
    $query = "INSERT INTO notifications (task_id, notify_time, reminder_time, message) 
              VALUES (:task_id, :notify_time, :reminder_time, :message)";
    
    $stmt = $conn->prepare($query);
    $stmt->bindParam(':task_id', $task_id, PDO::PARAM_INT);
    $stmt->bindParam(':notify_time', $notify_time, PDO::PARAM_STR);
    $stmt->bindParam(':reminder_time', $reminder_time, PDO::PARAM_STR);
    $stmt->bindParam(':message', $message, PDO::PARAM_STR);

    if (!$stmt->execute()) {
        throw new Exception("Lỗi SQL khi chèn thông báo: " . implode(", ", $stmt->errorInfo()));
    }

    // Commit transaction nếu không có lỗi
    $conn->commit();
    echo json_encode(["task_id" => $task_id, "message" => "Thêm công việc & thông báo thành công!"]);
} catch (PDOException $e) {
    $conn->rollBack(); // Hoàn tác nếu có lỗi SQL
    file_put_contents("error_log.txt", "Lỗi PDO: " . $e->getMessage() . "\n", FILE_APPEND);
    echo json_encode(["message" => "Lỗi cơ sở dữ liệu: " . $e->getMessage()]);
} catch (Exception $e) {
    $conn->rollBack(); // Hoàn tác nếu có lỗi khác
    file_put_contents("error_log.txt", "Lỗi hệ thống: " . $e->getMessage() . "\n", FILE_APPEND);
    echo json_encode(["message" => "Lỗi: " . $e->getMessage()]);
}
?>
