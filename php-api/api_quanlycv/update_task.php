<?php
include 'connectDB.php';  // Kết nối cơ sở dữ liệu với PDO

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Nhận dữ liệu từ POST request
    $task_id = $_POST['task_id'];
    $title = $_POST['task_title'];
    $desc = $_POST['task_description'];
    $due_date = $_POST['due_date'];
    $status = $_POST['status'];

    // Ghi log dữ liệu nhận được để kiểm tra
    error_log("Nhận dữ liệu từ POST: task_id = $task_id, title = $title, description = $desc, due_date = $due_date, status = $status");

    try {
        // Cập nhật task trong cơ sở dữ liệu
        $sql = "UPDATE tasks SET task_title = ?, task_description = ?, due_date = ?, status = ? WHERE task_id = ?";
        $stmt = $conn->prepare($sql);
        
        // Thực thi câu lệnh chuẩn bị với các tham số
        $stmt->execute([$title, $desc, $due_date, $status, $task_id]);

        // Kiểm tra số dòng bị ảnh hưởng (nếu > 0 thì cập nhật thành công)
        if ($stmt->rowCount() > 0) {
            // Ghi log khi cập nhật thành công
            error_log("Cập nhật task thành công với ID: $task_id");
            echo json_encode(["message" => "Cập nhật thành công"]);
        } else {
            // Nếu không có dòng nào bị ảnh hưởng, có thể task_id không tồn tại hoặc không có thay đổi
            error_log("Không có thay đổi hoặc không tìm thấy task với ID: $task_id");
            echo json_encode(["message" => "Cập nhật thất bại"]);
        }
    } catch (PDOException $e) {
        // Ghi log lỗi nếu có ngoại lệ
        error_log("Lỗi khi cập nhật task với ID: $task_id. Lỗi: " . $e->getMessage());
        echo json_encode(["message" => "Cập nhật thất bại, lỗi server"]);
    }
} else {
    // Nếu không phải POST request, ghi log lỗi
    error_log("Yêu cầu không hợp lệ, không phải POST");
    echo json_encode(["message" => "Yêu cầu không hợp lệ"]);
}
?>
