package com.example.quanlycv.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.quanlycv.R;
import com.example.quanlycv.api.ApiClient;
import com.example.quanlycv.api.ApiResponse;
import com.example.quanlycv.api.ApiService;
import com.example.quanlycv.model.User;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChinhSuaProjectActivity extends MenuActivity {

    private EditText edtFullName, edtEmail, edtUsername, edtPhone;
    private ImageView imgProfileAvatar;
    private Button btnSaveProfile, btnCancel;

    private int userId;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_chinh_sua_project;  // layout đã bỏ DrawerLayout, dùng làm content bên trong activity_menu
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ánh xạ view
        edtFullName = findViewById(R.id.edt_fullname);
        edtEmail = findViewById(R.id.edt_email);
        edtUsername = findViewById(R.id.edt_username);
        edtPhone = findViewById(R.id.edt_phone);
        imgProfileAvatar = findViewById(R.id.img_profile_avatar);
        btnSaveProfile = findViewById(R.id.btn_save_profile);
        btnCancel = findViewById(R.id.btn_cancel);



        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            Toast.makeText(this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userId = user.getUser_id();

        if (user != null) {

            edtFullName.setText(user.getFullName());
            edtEmail.setText(user.getEmail());
            edtUsername.setText(user.getUsername());
            edtPhone.setText(user.getPhoneNumber());

            // Load avatar từ URL
            Glide.with(this)
                    .load(user.getAvatar_url())
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(imgProfileAvatar);
        }

        // TODO: Thêm xử lý nút "Lưu" và "Hủy" sau
        btnSaveProfile.setOnClickListener(v -> {
            String fullName = edtFullName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String username = edtUsername.getText().toString().trim();

            String phoneNumber = edtPhone.getText().toString().trim();

            if (!phoneNumber.matches("^\\d{9,11}$")) {
                Toast.makeText(this, "Số điện thoại phải là 9-11 chữ số", Toast.LENGTH_SHORT).show();
                return;
            }

            User updatedUser = new User();
            updatedUser.setUser_id(userId);
            updatedUser.setFullName(fullName);
            updatedUser.setEmail(email);
            updatedUser.setUsername(username);
            updatedUser.setPhoneNumber(phoneNumber);

            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            apiService.updateUser(updatedUser).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().isSuccess()) {

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("updated_user", updatedUser);
                            setResult(RESULT_OK, resultIntent);
                            finish();

                            Toast.makeText(ChinhSuaProjectActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            loadUserById(userId);
                        } else {
                            Log.e("API_ERROR", response.body().getMessage());
                            Toast.makeText(ChinhSuaProjectActivity.this, "Lỗi repo: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChinhSuaProjectActivity.this, "Lỗi khi cập nhật, mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {

                    String errorMessage = "Lỗi kết nối server: " + t.getMessage();

                    Log.e("API_ERROR", errorMessage, t); // t sẽ ghi stacktrace
                    Toast.makeText(ChinhSuaProjectActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }

            });
        });

    }

    private void loadUserById(int id) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getUserById(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();

                    edtFullName.setText(user.getFullName());
                    edtEmail.setText(user.getEmail());
                    edtUsername.setText(user.getUsername());
                    edtPhone.setText(user.getPhoneNumber());

                    Glide.with(ChinhSuaProjectActivity.this)
                            .load(user.getAvatar_url())
                            .placeholder(R.drawable.ic_profile)
                            .error(R.drawable.ic_profile)
                            .into(imgProfileAvatar);


                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updated_user", user);
                    setResult(RESULT_OK, resultIntent);
                } else {
                    Toast.makeText(ChinhSuaProjectActivity.this, "Không lấy được dữ liệu user mới", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ChinhSuaProjectActivity.this, "Lỗi khi lấy dữ liệu user: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
