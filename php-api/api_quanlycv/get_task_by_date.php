<?php
header("Content-Type: application/json; charset=UTF-8");
require_once("connectDB.php");

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    if (isset($_GET['userId']) && isset($_GET['date'])) {
        $userId = $_GET['userId'];
        
        $date = $_GET['date'];

        error_log("DEBUG - userId: $userId, date: $date");
        
        try {
            $stmt = $conn->prepare("SELECT * FROM tasks WHERE user_id = :userId AND DATE(due_date) = :dueDate");
            $stmt->bindParam(':userId', $userId, PDO::PARAM_INT);
            $stmt->bindParam(':dueDate', $date, PDO::PARAM_STR);
            $stmt->execute();

            $tasks = $stmt->fetchAll(PDO::FETCH_ASSOC);

            echo json_encode($tasks);
        } catch (PDOException $e) {
            http_response_code(500);
            echo json_encode(["error" => "Lỗi truy vấn: " . $e->getMessage()]);
        }
    } else {
        http_response_code(400);
        echo json_encode(["error" => "Thiếu userId hoặc date"]);
    }
} else {
    http_response_code(405); // Method Not Allowed
    echo json_encode(["error" => "Chỉ chấp nhận GET"]);
}
