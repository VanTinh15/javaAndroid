package com.example.quanlycv.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycv.R;
import com.example.quanlycv.adapter.TaskAdapter;
import com.example.quanlycv.api.ApiClient;
import com.example.quanlycv.api.ApiService;
import com.example.quanlycv.model.Task;
import com.example.quanlycv.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskListActivity extends MenuActivity {
    private RecyclerView recyclerPending, recyclerCompleted;
    private TaskAdapter adapterPending, adapterCompleted;
    private TextView tvNoPending, tvNoCompleted;
    private FloatingActionButton fabAddTask;
    private ApiService apiService;
    private int userId;

    private View cardPendingTasks, cardCompletedTasks;
    private TabLayout tabLayout;

    private List<Task> allTasks = new ArrayList<>(); // Lưu danh sách tất cả task (mới thêm)

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_task_list;  // phần nội dung thực tế
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Ánh xạ view

        recyclerPending = findViewById(R.id.recycler_tasks_pending);
        recyclerCompleted = findViewById(R.id.recycler_tasks_completed);
        tvNoPending = findViewById(R.id.text_no_tasks_pending);
        tvNoCompleted = findViewById(R.id.text_no_tasks_completed);
        fabAddTask = findViewById(R.id.fab_add_task);

        cardPendingTasks = findViewById(R.id.layout_tasks_pending);
        cardCompletedTasks = findViewById(R.id.layout_tasks_completed);
        tabLayout = findViewById(R.id.tab_layout);

        adapterPending = new TaskAdapter();
        adapterCompleted = new TaskAdapter();

        recyclerPending.setLayoutManager(new LinearLayoutManager(this));
        recyclerCompleted.setLayoutManager(new LinearLayoutManager(this));

        recyclerPending.setAdapter(adapterPending);
        recyclerCompleted.setAdapter(adapterCompleted);


        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            Toast.makeText(this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userId = user.getUser_id();



        // Click vào item
        adapterPending.setOnItemClickListener(task -> openTaskDetail(task.getTask_id())); //click vào item trong mục đang chờ
        adapterCompleted.setOnItemClickListener(task -> openTaskDetail(task.getTask_id()));//click vào item trong mục đã hoàn thành

        // Nút thêm công việc
        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTaskActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        apiService = ApiClient.getClient().create(ApiService.class);

        // Xử lý chuyển tab
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Chuyển tab, gọi hàm cập nhật dữ liệu theo tab
                updateCurrentTabData(); // Mới thêm
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Không cần xử lý
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Không cần xử lý
            }
        });

        showPendingTasks(); // Hiển thị mặc định tab "Đang chờ"
        fetchTasksFromServer(); // Lấy dữ liệu từ server


    }

    private void fetchTasksFromServer() {
        apiService.getTasksByUser(userId).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allTasks = response.body(); // Lưu tất cả task vào biến allTasks (Mới thêm)

                    updateCurrentTabData(); // Cập nhật dữ liệu theo tab hiện tại
                } else {
                    Log.e("API_ERROR", "Phản hồi không thành công. Mã lỗi: " + response.code());
                    Toast.makeText(TaskListActivity.this, "Lỗi lấy dữ liệu từ server!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi kết nối server", t);
                Toast.makeText(TaskListActivity.this, "Không thể kết nối đến server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm này được gọi khi người dùng chuyển tab
    private void updateCurrentTabData() {
        int selectedTab = tabLayout.getSelectedTabPosition(); // Lấy tab hiện tại
        if (selectedTab == 0) {
            showPendingTasks(); // Hiển thị công việc đang chờ
        } else {
            showCompletedTasks(); // Hiển thị công việc đã hoàn thành
        }
    }

    // Hàm này dùng để hiển thị công việc "Đang chờ"
    private void showPendingTasks() {
        List<Task> pendingTasks = new ArrayList<>();
        for (Task task : allTasks) { // Lọc task có trạng thái không phải "Hoàn thành"
            if ("Đang chờ".equalsIgnoreCase(task.status)) {
                pendingTasks.add(task);
            }
        }
        adapterPending.setTasks(pendingTasks);

        // UI
        cardPendingTasks.setVisibility(View.VISIBLE);
        cardCompletedTasks.setVisibility(View.GONE);

        if (pendingTasks.isEmpty()) {
            tvNoPending.setVisibility(View.VISIBLE);
            recyclerPending.setVisibility(View.GONE);
        } else {
            tvNoPending.setVisibility(View.GONE);
            recyclerPending.setVisibility(View.VISIBLE);
        }
    }

    // Hàm này dùng để hiển thị công việc "Đã hoàn thành"
    private void showCompletedTasks() {
        List<Task> completedTasks = new ArrayList<>();
        for (Task task : allTasks) { // Lọc task có trạng thái là "Hoàn thành"
            if ("Đã hoàn thành".equalsIgnoreCase(task.status)) {
                completedTasks.add(task);
            }
        }
        adapterCompleted.setTasks(completedTasks);

        // UI
        cardPendingTasks.setVisibility(View.GONE);
        cardCompletedTasks.setVisibility(View.VISIBLE);

        if (completedTasks.isEmpty()) {
            tvNoCompleted.setVisibility(View.VISIBLE);
            recyclerCompleted.setVisibility(View.GONE);
        } else {
            tvNoCompleted.setVisibility(View.GONE);
            recyclerCompleted.setVisibility(View.VISIBLE);
        }
    }

    // Mở chi tiết công việc
    private void openTaskDetail(int taskId) {
        Log.d("TaskListActivity", "Mở chi tiết công việc với taskId: " + taskId);

        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            Toast.makeText(this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userId = user.getUser_id();

        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("task_id", taskId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchTasksFromServer(); // Lấy lại danh sách task khi Activity quay lại
    }
}
