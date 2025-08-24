<?php 
    require 'connectDB.php';

    // Kiểm tra xem có truyền task_id không
    if (isset($_GET['task_id'])) {
        $task_id = $_GET['task_id'];

        try {
            // Chuẩn bị câu truy vấn
            $stmt = $conn->prepare("SELECT * FROM tasks WHERE task_id = :task_id");
            $stmt->bindParam(':task_id', $task_id, PDO::PARAM_INT);
            $stmt->execute();

            // Lấy dữ liệu
            $task = $stmt->fetch(PDO::FETCH_ASSOC);

            if ($task) {
                echo json_encode($task); // Trả về JSON
            } else {
                echo json_encode(["message" => "Không tìm thấy task"]);
            }

        } catch (PDOException $e) {
            echo json_encode(["error" => "Lỗi truy vấn: " . $e->getMessage()]);
        }
    } else {
        echo json_encode(["error" => "Thiếu task_id"]);
    }
?>