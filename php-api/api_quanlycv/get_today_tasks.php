
<?php
///* get_today_tasks.php - API lấy danh sách công việc hôm nay */
// Kết nối đến database
require_once 'connectDB.php';

// Thiết lập header cho API
header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET');

// Lấy user_id từ tham số truy vấn (nếu có)
$user_id = isset($_GET['user_id']) ? intval($_GET['user_id']) : null;

try {
    // Lấy ngày hiện tại
    $today = date('Y-m-d');
    
    // Truy vấn lấy các công việc có due_date là hôm nay
    $query = "SELECT * FROM tasks WHERE due_date = :today ";
    if ($user_id) {
        $query .= " AND user_id = :user_id";
    }
  
    $query .= " ORDER BY task_id DESC"; 

    $stmt = $conn->prepare($query);
    $stmt->bindParam(':today', $today, PDO::PARAM_STR);
    if ($user_id) {
        $stmt->bindParam(':user_id', $user_id, PDO::PARAM_INT);
    }
    $stmt->execute();
    
    // Fetch tất cả kết quả dưới dạng associative array
    $todayTasks = $stmt->fetchAll(PDO::FETCH_ASSOC);
    
    // Trả về kết quả dưới dạng JSON
    echo json_encode($todayTasks, JSON_UNESCAPED_UNICODE);
    
} catch(PDOException $e) {
    // Trả về lỗi nếu có
    echo json_encode(["error" => $e->getMessage()]);
}
?>