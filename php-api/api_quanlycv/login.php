<?php
require "connectDB.php";
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

$response = null; // Chỉ trả về JSON user hoặc null

if ($_SERVER['REQUEST_METHOD'] === 'GET' && isset($_GET['username'], $_GET['password'])) {
    try {
        $stmt = $conn->prepare("SELECT * FROM users WHERE username = ? AND password = ?");
        $stmt->execute([$_GET['username'], $_GET['password']]);
        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($user) {
            $response = $user; // Trả về thông tin user
        }
    } catch (PDOException $e) {
        $response = null; // Trả về null nếu lỗi
    }
}

// Trả về JSON user hoặc null
echo json_encode($response, JSON_UNESCAPED_UNICODE);
?>
