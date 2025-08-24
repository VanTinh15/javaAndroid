<?php
/* get_tasks_count.php - API lấy số liệu thống kê công việc theo user_id */
// Kết nối đến database
require_once 'connectDB.php';

// Thiết lập header cho API
header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET');

// Lấy user_id từ tham số truy vấn (bắt buộc)
$user_id = isset($_GET['user_id']) ? intval($_GET['user_id']) : null;

if (!$user_id) {
    echo json_encode(['error' => 'Thiếu user_id']);
    exit;
}

try {
    $stats = [];

    // 1. Tổng số công việc
    $queryTotal = "SELECT COUNT(*) as total FROM tasks WHERE user_id = :user_id and status = 'Đang chờ'";
    $stmt = $conn->prepare($queryTotal);
    $stmt->bindParam(':user_id', $user_id, PDO::PARAM_INT);
    $stmt->execute();
    $stats['total_tasks'] = $stmt->fetch(PDO::FETCH_ASSOC)['total'];

    // 2. Công việc hôm nay (theo due_date)
    $today = date('Y-m-d');
    $queryToday = "SELECT COUNT(*) as today FROM tasks WHERE user_id = :user_id AND due_date = :today";
    $stmt = $conn->prepare($queryToday);
    $stmt->bindParam(':user_id', $user_id, PDO::PARAM_INT);
    $stmt->bindParam(':today', $today, PDO::PARAM_STR);
    $stmt->execute();
    $stats['today_tasks'] = $stmt->fetch(PDO::FETCH_ASSOC)['today'];

    // 3. Công việc đã hoàn thành
    $queryCompleted = "SELECT COUNT(*) as completed FROM tasks WHERE user_id = :user_id AND (status = 'Đã hoàn thành' OR status = 'completed')";
    $stmt = $conn->prepare($queryCompleted);
    $stmt->bindParam(':user_id', $user_id, PDO::PARAM_INT);
    $stmt->execute();
    $stats['completed_tasks'] = $stmt->fetch(PDO::FETCH_ASSOC)['completed'];

    // 4. Tổng số công việc (không phân biệt trạng thái)
    $queryAll = "SELECT COUNT(*) as tong_so FROM tasks WHERE user_id = :user_id";
    $stmt = $conn->prepare($queryAll);
    $stmt->bindParam(':user_id', $user_id, PDO::PARAM_INT);
    $stmt->execute();
    $tongSo = (int) $stmt->fetch(PDO::FETCH_ASSOC)['tong_so'];
    $stats['tong_so'] = $tongSo;
    
    // 5. Tính tỉ lệ hoàn thành
    $tiLe = $tongSo > 0 ? round(($stats['completed_tasks'] / $tongSo) * 100) : 0;
    $stats['ti_le'] = $tiLe . '%';

    //nhung thanh phan chua su dung
    // 6. Công việc quá hạn
    $queryOverdue = "SELECT COUNT(*) as overdue FROM tasks WHERE user_id = :user_id AND due_date < :today AND status != 'Đã hoàn thành'";
    $stmt = $conn->prepare($queryOverdue);
    $stmt->bindParam(':user_id', $user_id, PDO::PARAM_INT);
    $stmt->bindParam(':today', $today, PDO::PARAM_STR);
    $stmt->execute();
    $stats['overdue_tasks'] = $stmt->fetch(PDO::FETCH_ASSOC)['overdue'];

    // Trả dữ liệu JSON
    echo json_encode($stats, JSON_UNESCAPED_UNICODE);

} catch (PDOException $e) {
    echo json_encode(['error' => $e->getMessage()]);
}
?>
