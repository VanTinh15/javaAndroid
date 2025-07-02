package com.example.quanlycv.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycv.R;
import com.example.quanlycv.adapter.TaskAdapter;
import com.example.quanlycv.api.ApiClient;
import com.example.quanlycv.api.ApiService;
import com.example.quanlycv.model.Task;
import com.example.quanlycv.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LichCongViecActivity extends MenuActivity {

    private TextView tvKhongCoTask;
    private CalendarView theoNgayView;
    private FloatingActionButton fabAddTask;
    private RecyclerView recyclerHomNayTasks;
    private TaskAdapter taskAdapter;

    private ApiService apiService;
    private int userId;
    protected int getLayoutResourceId() {
        return R.layout.activity_lich_cong_viec;  // phần nội dung thực tế
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            Toast.makeText(this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userId = user.getUser_id();


        apiService = ApiClient.getClient().create(ApiService.class);

        recyclerHomNayTasks = findViewById(R.id.recycler_daily_tasks);
        theoNgayView = findViewById(R.id.lichxemtheongay);
        tvKhongCoTask = findViewById(R.id.tv_kocv_tasks);
        fabAddTask = findViewById(R.id.fab_add_task);


        recyclerHomNayTasks.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter();
        recyclerHomNayTasks.setAdapter(taskAdapter);

        fetchTodayTasks();

        theoNgayView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {

            String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);

            fetchTasksByDate(selectedDate);
        });



        taskAdapter.setOnItemClickListener(task -> {
            Intent detailIntent = new Intent(this, TaskDetailActivity.class);
            detailIntent.putExtra("task_id", task.getTask_id());
            detailIntent.putExtra("user", user);
            startActivity(detailIntent);

        });


        fabAddTask.setOnClickListener(v -> {
            Intent addintent = new Intent(this, AddTaskActivity.class);
            addintent.putExtra("user", user);
            startActivity(addintent);

        });

    }
    private void fetchTodayTasks() {

        apiService.getTodayTasks(userId).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Danhsachcongviec", "JSON nhận: " + new Gson().toJson(response.body()));  // cần import Gson
                    List<Task> todayTasks = response.body();


                    runOnUiThread(() -> {
                        taskAdapter.setTasks(todayTasks);


                        if (todayTasks.isEmpty()) {
                            tvKhongCoTask.setVisibility(View.VISIBLE);
                            recyclerHomNayTasks.setVisibility(View.GONE);
                        } else {
                            tvKhongCoTask.setVisibility(View.GONE);
                            recyclerHomNayTasks.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    Toast.makeText(LichCongViecActivity.this, "Lỗi khi nhận danh sách công việc", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {

                Log.e("TongquanActivity", "Lỗi kết nối server: " + t.getMessage());
                t.printStackTrace();
                Toast.makeText(LichCongViecActivity.this, "Không thể kết nối server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTasksByDate(String date) {
        Log.d("fetchTasksByDate", "Gọi API với userId = " + userId + ", date = " + date);

        apiService.getTasksByDate(userId, date).enqueue(new Callback<List<Task>>() {

            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<Task> tasksByDate = response.body();
                    runOnUiThread(() -> {
                        taskAdapter.setTasks(tasksByDate);

                        if (tasksByDate.isEmpty()) {
                            tvKhongCoTask.setVisibility(View.VISIBLE);
                            recyclerHomNayTasks.setVisibility(View.GONE);
                        } else {
                            tvKhongCoTask.setVisibility(View.GONE);
                            recyclerHomNayTasks.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    Log.d("loctheongay", "JSON nhận: " + new Gson().toJson(response.body()));  // cần import Gson
                    Toast.makeText(LichCongViecActivity.this, "Không lấy được công việc của ngày đã chọn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.e("LichCongViecActivity", "Lỗi khi gọi API theo ngày: " + t.getMessage());
                Toast.makeText(LichCongViecActivity.this, "Lỗi kết nối khi chọn ngày", Toast.LENGTH_SHORT).show();
            }
        });
    }

}