package com.example.quanlycv.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;


import com.bumptech.glide.Glide;
import com.example.quanlycv.R;
import com.example.quanlycv.api.ApiClient;
import com.example.quanlycv.api.ApiService;
import com.example.quanlycv.model.TaskStats;
import com.example.quanlycv.model.User;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends MenuActivity {
    private Button btnEditProfile, btnChangePassword, btnLogout;
    private ImageView imgAvatar;
    private TextView tvFullName, tvEmail, tvUsername;

    private TextView tvTongCV, tvHoanthanh, tvDangcho;
    private ApiService apiService;

    private int userId;

    private ActivityResultLauncher<Intent> editProfileLauncher;  // khai báo launcher


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_profile;  // layout đã bỏ DrawerLayout, dùng làm content bên trong activity_menu
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ánh xạ
        imgAvatar = findViewById(R.id.img_avatar);
        tvFullName = findViewById(R.id.tv_full_name);
        tvEmail = findViewById(R.id.tv_email);
        tvUsername = findViewById(R.id.tv_username);

        tvTongCV = findViewById(R.id.tv_total_tasks);
        tvHoanthanh = findViewById(R.id.tv_completed_tasks);
        tvDangcho = findViewById(R.id.tv_pending_tasks);



        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            Toast.makeText(this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userId = user.getUser_id();

        tvFullName.setText(user.getFullName());
        tvUsername.setText(user.getUsername());
        tvEmail.setText(user.getEmail());

        Glide.with(this)
                .load(user.getAvatar_url())
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(imgAvatar);

        //thống kê tổng quan
        // Khởi tạo API
        apiService = ApiClient.getClient().create(ApiService.class);
        fetchTaskStats();

        //dang ky edit profile
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        User updatedUser = (User) result.getData().getSerializableExtra("updated_user");
                        if (updatedUser != null) {
                            userId = updatedUser.getUser_id();
                            tvFullName.setText(updatedUser.getFullName());
                            tvEmail.setText(updatedUser.getEmail());
                            tvUsername.setText(updatedUser.getUsername());

                            Glide.with(this)
                                    .load(updatedUser.getAvatar_url())
                                    .placeholder(R.drawable.ic_profile)
                                    .error(R.drawable.ic_profile)
                                    .into(imgAvatar);

                            fetchTaskStats();
                        }
                    }
                }
        );


        // Xử lý sự kiện nhấn nút "Chỉnh sửa thông tin"
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chinhSuaProjectActivity = new Intent(ProfileActivity.this, ChinhSuaProjectActivity.class);
                chinhSuaProjectActivity.putExtra("user", user);
                editProfileLauncher.launch(chinhSuaProjectActivity);
            }

        });

        // Xử lý sự kiện nhấn nút "Đổi mật khẩu"
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến ChangePasswordActivity
                Intent thaymatkhauIntent = new Intent(ProfileActivity.this, ThayPasswordActivity.class);
                thaymatkhauIntent.putExtra("user", user);
                startActivity(thaymatkhauIntent);
            }
        });

        // Xử lý sự kiện nhấn nút "Đăng xuất"
        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý đăng xuất (ví dụ: chuyển về LoginActivity)
                Intent logoutIntent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(logoutIntent);
                finish();
            }
        });

    }

    private void fetchTaskStats() {
        Log.d("thongke", "Đang gọi API thống kê với userId: " + userId);

        apiService.getTaskStats(userId).enqueue(new Callback<TaskStats>() {
            @Override
            public void onResponse(Call<TaskStats> call, Response<TaskStats> response) {
                // In URL thực tế gọi (nếu dùng HttpLoggingInterceptor sẽ tự log)
                Log.d("thongke", "URL gọi: " + call.request().url());

                if (response.isSuccessful() && response.body() != null) {
                    TaskStats stats = response.body();

                    // In JSON nhận được
                    Log.d("thongke", "JSON nhận: " + new Gson().toJson(stats));

                    // Cập nhật giao diện
                    runOnUiThread(() -> {
                        tvTongCV.setText(String.valueOf(stats.getTongSo()));
                        tvHoanthanh.setText(String.valueOf(stats.getCompletedTasks()));
                        tvDangcho.setText(String.valueOf(stats.getTotalTasks()));
                    });
                } else {
                    try {
                        Log.e("thongke", "Lỗi khi nhận response từ server: " + response.code() + " - " + response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(ProfileActivity.this, "Lỗi khi nhận dữ liệu thống kê", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TaskStats> call, Throwable t) {
                Log.e("thongke", "Lỗi kết nối tới API", t);
                Toast.makeText(ProfileActivity.this, "Không thể kết nối server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}