package com.example.quanlycv.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;


import com.example.quanlycv.R;
import com.example.quanlycv.api.ApiClient;
import com.example.quanlycv.api.ApiResponse;
import com.example.quanlycv.api.ApiService;
import com.example.quanlycv.model.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThayPasswordActivity extends MenuActivity {

    private EditText mkHientai, mkMoi, mkNhaplai;
    private Button btnChange, btnCancel;
    private ApiService apiService;
    private int userId;

    protected int getLayoutResourceId() {
        return R.layout.activity_thay_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        mkHientai = findViewById(R.id.edt_current_password);
        mkMoi = findViewById(R.id.edt_new_password);
        mkNhaplai = findViewById(R.id.edt_confirm_new_password);
        btnChange = findViewById(R.id.btn_change_password);
        btnCancel = findViewById(R.id.btn_cancel);

        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userId = user.getUser_id();

        btnChange.setOnClickListener(v -> attemptChangePassword());
        btnCancel.setOnClickListener(v -> finish());

    }
    private void attemptChangePassword() {
        String mkhientai = mkHientai.getText().toString();
        String mkmoi = mkMoi.getText().toString();
        String mknhaplai = mkNhaplai.getText().toString();

        if (TextUtils.isEmpty(mkhientai) || TextUtils.isEmpty(mkmoi) || TextUtils.isEmpty(mknhaplai)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ các trường", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mkmoi.equals(mknhaplai)) {
            Toast.makeText(this, "Mật khẩu mới và xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        // 3. Kiểm tra điều kiện mật khẩu (>=8 ký tự, có chữ hoa, chữ thường, số)
//        if (mkmoi.length() < 8 ||
//                !mkmoi.matches(".*[A-Z].*") ||
//                !mkmoi.matches(".*[a-z].*") ||
//                !mkmoi.matches(".*\\d.*")) {
//            Toast.makeText(this, "Mật khẩu mới không đủ mạnh", Toast.LENGTH_SHORT).show();
//            return;
//        }
        // 4. Gọi API đổi mật khẩu
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.changePassword(userId, mkhientai, mkmoi)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            ApiResponse body = resp.body();
                            if (body.isSuccess()) {
                                Toast.makeText(ThayPasswordActivity.this,
                                        "Đổi mật khẩu thành công",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                // Lỗi do server trả về, log chi tiết:
                                Log.e("ThayPassword", "Server error: " + body.getMessage());
                                Toast.makeText(ThayPasswordActivity.this,
                                        body.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // Lỗi HTTP (4xx,5xx), đọc errorBody
                            String errBody = "";
                            try {
                                if (resp.errorBody() != null) {
                                    errBody = resp.errorBody().string();
                                }
                            } catch (IOException e) {
                                Log.e("ThayPassword", "Error reading errorBody", e);
                            }
                            Log.e("ThayPassword", "HTTP " + resp.code() + ": " + errBody);
                            Toast.makeText(ThayPasswordActivity.this,
                                    "Lỗi server, mã: " + resp.code(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        // Lỗi kết nối hoặc exception, log stacktrace
                        Log.e("ThayPassword", "Network or conversion error", t);
                        Toast.makeText(ThayPasswordActivity.this,
                                "Không thể kết nối: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

    }
}