package com.example.quanlycv.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.quanlycv.api.ApiClient;
import com.example.quanlycv.api.ApiService;
import com.example.quanlycv.R;
import com.example.quanlycv.model.Task;
import com.example.quanlycv.model.User;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskDetailActivity extends MenuActivity {

    private EditText edtTitle, edtDescription, edtCreatedDate, edtDueDate, edtStatus;
    private ApiService apiService;
    private int taskId;
    private Button btnSave;
    private int userId;  // lấy user_id từ intent

    protected int getLayoutResourceId() {
        return R.layout.activity_task_detail;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edtTitle = findViewById(R.id.edt_title);
        edtDescription = findViewById(R.id.edt_description);
        edtCreatedDate = findViewById(R.id.edt_created_date);
        edtDueDate = findViewById(R.id.edt_due_date);
        edtStatus = findViewById(R.id.edt_status);

        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            Log.d("BTN", "Đã nhấn nút lưu");

            updateTask();
        });

        taskId = getIntent().getIntExtra("task_id", -1);

        if (taskId == -1) {
            Toast.makeText(this, "Không tìm thấy task_id!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Lấy user ID từ Intent
        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            Toast.makeText(this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userId = user.getUser_id();

        apiService = ApiClient.getClient().create(ApiService.class);

        fetchTaskDetail(taskId);

    }

    private void fetchTaskDetail(int id) {
        apiService.getTaskById(id).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Task task = response.body();
                    edtTitle.setText(task.getTask_title());
                    edtDescription.setText(task.getTask_description());
                    edtCreatedDate.setText(task.getCreated_date());
                    edtDueDate.setText(task.getDue_date());
                    edtStatus.setText(task.getStatus());
                } else {
                    Toast.makeText(TaskDetailActivity.this, "Không tìm thấy dữ liệu chi tiết!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(TaskDetailActivity.this, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
                Log.e("TaskDetail", "Error: ", t);
            }
        });
    }
    private void updateTask() {
        String newTitle = edtTitle.getText().toString().trim();
        String newDescription = edtDescription.getText().toString().trim();
        String newDueDate = edtDueDate.getText().toString().trim();
        String newStatus = edtStatus.getText().toString().trim(); // Hoặc lấy từ Spinner trạng thái nếu có

        if (newTitle.isEmpty() || newDescription.isEmpty() || newDueDate.isEmpty()) {
            Toast.makeText(TaskDetailActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("TaskDetail", "Dữ liệu chỉnh sửa: ");
        Log.d("TaskDetail", "Title: " + newTitle);
        Log.d("TaskDetail", "Description: " + newDescription);
        Log.d("TaskDetail", "Due Date: " + newDueDate);
        Log.d("TaskDetail", "Status: " + newStatus);

        apiService.updateTask(taskId, newTitle, newDescription, newDueDate, newStatus).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TaskDetailActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    fetchTaskDetail(taskId); // Lấy lại thông tin task mới
                    Log.d("TaskDetail", "Cập nhật thành công. Dữ liệu đã được lưu vào server.");
                    finish();

                } else {
                    Toast.makeText(TaskDetailActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    Log.e("TaskDetail", "Cập nhật thất bại. Lỗi server: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(TaskDetailActivity.this, "Lỗi server!", Toast.LENGTH_SHORT).show();
                Log.e("TaskDetail", "Lỗi server khi gọi API: ", t);
            }
        });
    }



}
