<?php
// Kết nối đến database
require_once 'connectDB.php';

// Thiết lập header cho API
header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET');

try {
    // Lấy user_id từ URL (vd: api.php?user_id=5)
    if (!isset($_GET['user_id'])) {
        echo json_encode(["error" => "Thiếu tham số user_id"]);
        exit;
    }

    $user_id = intval($_GET['user_id']);

    // Truy vấn lấy công việc theo user_id
    $stmt = $conn->prepare("SELECT * FROM tasks WHERE user_id = :user_id");
    $stmt->bindParam(':user_id', $user_id, PDO::PARAM_INT);
    $stmt->execute();

    $tasks = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($tasks, JSON_UNESCAPED_UNICODE);

} catch(PDOException $e) {
    echo json_encode(["error" => $e->getMessage()]);
}
?>
