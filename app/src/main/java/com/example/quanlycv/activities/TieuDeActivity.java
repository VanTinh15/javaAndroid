package com.example.quanlycv.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlycv.R;
import com.example.quanlycv.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class TieuDeActivity extends AppCompatActivity {

    private CircleImageView imgUserAvatar;
    private TextView tvUserName, tvUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_tieude);

        User user = (User) getIntent().getSerializableExtra("user");

        tvUserName.setText(user.getUsername());
        tvUserEmail.setText(user.getEmail());
        // Ánh xạ view từ layout
        imgUserAvatar = findViewById(R.id.img_user_avatar);
        tvUserName = findViewById(R.id.tv_user_name);
        tvUserEmail = findViewById(R.id.tv_user_email);


    }
}
