<?php
    header('Content-Type: application/json');
    require_once 'connectDB.php';

    // 1. Lấy dữ liệu từ $_POST
    $user_id          = isset($_POST['user_id'])           ? intval($_POST['user_id'])    : null;
    $current_password = isset($_POST['current_password'])  ? $_POST['current_password']   : null;
    $new_password     = isset($_POST['new_password'])      ? $_POST['new_password']       : null;

    // 2. Kiểm tra đầu vào
    if (!$user_id || empty($current_password) || empty($new_password)) {
        echo json_encode([
            'success' => false,
            'message' => 'Thiếu dữ liệu'
        ]);
        exit;
    }

    try {
        // 3. Lấy mật khẩu hiện tại từ DB (dạng plain text)
        $stmt = $conn->prepare("
            SELECT `password`
            FROM `users`
            WHERE `user_id` = :user_id
            LIMIT 1
        ");
        $stmt->bindParam(':user_id', $user_id, PDO::PARAM_INT);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        if (!$row) {
            echo json_encode([
                'success' => false,
                'message' => 'User không tồn tại'
            ]);
            exit;
        }

        // 4. So sánh mật khẩu dạng plain-text
        if ($current_password !== $row['password']) {
            echo json_encode([
                'success' => false,
                'message' => 'Mật khẩu hiện tại không đúng'
            ]);
            exit;
        }

        // 5. Cập nhật mật khẩu mới (vẫn dạng plain text — chưa mã hoá)
        $upd = $conn->prepare("
            UPDATE `users`
            SET `password` = :new_password
            WHERE `user_id` = :user_id
        ");
        $upd->bindParam(':new_password', $new_password);
        $upd->bindParam(':user_id', $user_id, PDO::PARAM_INT);

        if ($upd->execute()) {
            echo json_encode([
                'success' => true,
                'message' => 'Đổi mật khẩu thành công'
            ]);
        } else {
            echo json_encode([
                'success' => false,
                'message' => 'Không thể cập nhật mật khẩu'
            ]);
        }

    } catch (PDOException $e) {
        echo json_encode([
            'success' => false,
            'message' => 'Lỗi hệ thống: ' . $e->getMessage()
        ]);
    }
?>
