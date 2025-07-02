package com.example.quanlycv.activities;
import com.example.quanlycv.model.Task;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.quanlycv.api.ApiClient;
import com.example.quanlycv.api.ApiService;
import com.example.quanlycv.R;
import com.example.quanlycv.model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTaskActivity extends MenuActivity {
    private EditText edtTaskTitle, edtTaskDescription, edtDueDate;
    private Button btnSaveTask;
    private ApiService apiService;
    private int userId;  // lấy user_id từ intent

    protected int getLayoutResourceId() {
        return R.layout.activity_add_task;  // phần nội dung thực tế
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        edtTaskTitle = findViewById(R.id.edt_task_title);
        edtTaskDescription = findViewById(R.id.edt_task_description);
        edtDueDate = findViewById(R.id.edt_due_date);
        //edtNotificationTime = findViewById(R.id.edt_notification_time);
        btnSaveTask = findViewById(R.id.btn_save_task);

        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            Toast.makeText(this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userId = user.getUser_id();

        apiService = ApiClient.getClient().create(ApiService.class);

        edtDueDate.setOnClickListener(v -> showDatePicker());

        // Sự kiện chọn giờ
        //edtNotificationTime.setOnClickListener(v -> showTimePicker());

        btnSaveTask.setOnClickListener(v ->addTask());

    }

    private void addTask() {
        String title = edtTaskTitle.getText().toString().trim();
        String description = edtTaskDescription.getText().toString().trim();
        String dueDate = edtDueDate.getText().toString().trim();
        //String reminderTime = edtNotificationTime.getText().toString().trim();  // Nếu có thời gian nhắc nhở


        if (title.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tiêu đề và ngày hết hạn!", Toast.LENGTH_SHORT).show();
            return;
        }

        String createdDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Task task = new Task(0,userId, title, description,createdDate, dueDate, "Đang chờ");


        Log.d("API_REQUEST", "Dữ liệu gửi lên: " + new Gson().toJson(task));



        Gson gson = new Gson();
        String jsonTask = gson.toJson(task);
        Log.d("API_REQUEST", "Dữ liệu gửi lên: " + jsonTask);

        apiService.addTask(task).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("API_RESPONSE", "Dữ liệu trả về: " + new Gson().toJson(response.body()));
                        Toast.makeText(AddTaskActivity.this, "Thêm công việc thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Không có dữ liệu";
                        Log.e("API_RESPONSE", "HTTP CODE: " + response.code());
                        Log.e("API_RESPONSE", "Lỗi từ server: " + errorBody);
                        Toast.makeText(AddTaskActivity.this, "Lỗi khi thêm công việc!", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi kết nối: " + t.getMessage());
                Toast.makeText(AddTaskActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }




    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {

            String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);


            edtDueDate.setText(formattedDate);
        }, year, month, day);

        datePickerDialog.show();
    }



}
