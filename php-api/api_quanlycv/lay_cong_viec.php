<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");

require_once("connectDB.php");

// Kiểm tra tham số
if (!isset($_GET['user_id'])) {
    echo json_encode(["error" => "Thiếu tham số user_id"]);
    exit;
}

$user_id = intval($_GET['user_id']);

try {
    // Lấy công việc ĐANG CHỜ từ hôm nay trở đi
    $sqlPending = "SELECT * FROM tasks WHERE user_id = :user_id AND due_date >= CURDATE() AND status = 'Đang chờ'";
    $stmtPending = $conn->prepare($sqlPending);
    $stmtPending->bindParam(":user_id", $user_id);
    $stmtPending->execute();
    $pendingTasks = $stmtPending->fetchAll(PDO::FETCH_ASSOC);

    // Lấy công việc ĐÃ HOÀN THÀNH từ hôm nay trở đi
    $sqlCompleted = "SELECT * FROM tasks WHERE user_id = :user_id AND due_date >= CURDATE() AND status = 'Đã hoàn thành'";
    $stmtCompleted = $conn->prepare($sqlCompleted);
    $stmtCompleted->bindParam(":user_id", $user_id);
    $stmtCompleted->execute();
    $completedTasks = $stmtCompleted->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode([
        "dang_cho" => $pendingTasks,
        "da_hoan_thanh" => $completedTasks
    ]);
} catch (PDOException $e) {
    echo json_encode(["error" => "Lỗi truy vấn CSDL: " . $e->getMessage()]);
}
