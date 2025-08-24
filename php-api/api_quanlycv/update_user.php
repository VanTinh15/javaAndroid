<?php
header('Content-Type: application/json');
require_once 'connectDB.php';

// Đọc dữ liệu JSON từ client
$data = json_decode(file_get_contents("php://input"), true);

// Kiểm tra dữ liệu
if (
    isset($data['user_id']) &&
    isset($data['fullName']) &&
    isset($data['email']) &&
    isset($data['username']) &&
    isset($data['phoneNumber'])
) {
    $user_id = $data['user_id'];
    $fullName = $data['fullName'];
    $email = $data['email'];
    $username = $data['username'];
    $phoneNumber = $data['phoneNumber'];

    try {
        $sql = "UPDATE users SET fullName = :fullName, email = :email, username = :username, phoneNumber = :phoneNumber WHERE user_id = :user_id";
        $stmt = $conn->prepare($sql);
        $stmt->bindParam(':fullName', $fullName);
        $stmt->bindParam(':email', $email);
        $stmt->bindParam(':username', $username);
        $stmt->bindParam(':phoneNumber', $phoneNumber);
        $stmt->bindParam(':user_id', $user_id);

        if ($stmt->execute()) {
            echo json_encode(["success" => true, "message" => "Cập nhật thành công"]);
        } else {
            echo json_encode(["success" => false, "message" => "Cập nhật thất bại"]);
        }
    } catch (PDOException $e) {
        echo json_encode(["success" => false, "message" => "Lỗi: " . $e->getMessage()]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Thiếu dữ liệu"]);
}
