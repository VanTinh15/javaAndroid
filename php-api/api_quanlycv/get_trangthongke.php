<?php
    require_once 'connectDB.php';

    header('Content-Type: application/json; charset=utf-8');
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Methods: GET');

    $user_id = isset($_GET['user_id']) ? intval($_GET['user_id']) : null;
    $khoangThoiGian = isset($_GET['khoang_thoi_gian']) ? $_GET['khoang_thoi_gian'] : 'month'; // Mặc định là tháng

    if (!$user_id) {
        echo json_encode(['error' => 'Thiếu user_id']);
        exit;
    }

    try {
        $stats = [];

        // Chuẩn bị khoảng thời gian lọc
        $condition = "";
        $today = date('Y-m-d');

        if ($khoangThoiGian === 'week') {
            $start = date('Y-m-d', strtotime('-6 days')); // 7 ngày gần nhất
            $condition = "AND due_date BETWEEN '$start' AND '$today'";
        } elseif ($khoangThoiGian === 'month') {
            $start = date('Y-m-01'); // ngày đầu tháng
            $condition = "AND due_date BETWEEN '$start' AND '$today'";
        } elseif ($khoangThoiGian === 'year') {
            $start = date('Y-01-01'); // đầu năm
            $condition = "AND due_date BETWEEN '$start' AND '$today'";
        }

        // Tổng công việc (trong khoảng)
        $queryTotal = "SELECT COUNT(*) as total FROM tasks WHERE user_id = :user_id $condition";
        $stmt = $conn->prepare($queryTotal);
        $stmt->bindParam(':user_id', $user_id, PDO::PARAM_INT);
        $stmt->execute();
        $stats['total_tasks'] = $stmt->fetch(PDO::FETCH_ASSOC)['total'];

        // Số công việc hôm nay
        $queryToday = "SELECT COUNT(*) as today FROM tasks WHERE user_id = :user_id AND due_date = :today";
        $stmt = $conn->prepare($queryToday);
        $stmt->bindParam(':user_id', $user_id);
        $stmt->bindParam(':today', $today);
        $stmt->execute();
        $stats['today_tasks'] = $stmt->fetch(PDO::FETCH_ASSOC)['today'];

        // Số công việc hoàn thành trong khoảng
        $queryCompleted = "SELECT COUNT(*) as completed FROM tasks WHERE user_id = :user_id AND (status = 'Đã hoàn thành' OR status = 'completed') $condition";
        $stmt = $conn->prepare($queryCompleted);
        $stmt->bindParam(':user_id', $user_id);
        $stmt->execute();
        $stats['completed_tasks'] = $stmt->fetch(PDO::FETCH_ASSOC)['completed'];

        // Tính tỷ lệ hoàn thành
        $tong = $stats['total_tasks'];
        $hoanThanh = $stats['completed_tasks'];
        $stats['ti_le'] = $tong > 0 ? round($hoanThanh * 100 / $tong) . '%' : '0%';

        echo json_encode($stats, JSON_UNESCAPED_UNICODE);
    } catch (PDOException $e) {
        echo json_encode(['error' => $e->getMessage()]);
    }
?>
