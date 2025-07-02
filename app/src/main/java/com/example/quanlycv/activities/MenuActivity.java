package com.example.quanlycv.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.bumptech.glide.Glide;


import com.example.quanlycv.R;
import com.example.quanlycv.model.User;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class MenuActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_menu);


        drawerLayout   = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar        = findViewById(R.id.thanhCongCu);


        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START)
        );


        FrameLayout contentFrame = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(getLayoutResourceId(), contentFrame, true);


        setupNavHeader();


        navigationView.setNavigationItemSelectedListener(this::onNavItemSelected);
    }


    protected abstract @LayoutRes int getLayoutResourceId();

    /** Xử lý khi chọn item trong Drawer */
    private boolean onNavItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        int userId = getIntent().getIntExtra("user_id", -1);
        User user = (User) getIntent().getSerializableExtra("user");

        // Tạo intent tương ứng với menu item
        if (id == R.id.menu_trang_chu) {
            intent = new Intent(this, TongquanActivity.class);
        } else if (id == R.id.danh_sach_cong_viec) {
            intent = new Intent(this, TaskListActivity.class);
        } else if (id == R.id.lich_cong_viec) {
            intent = new Intent(this, LichCongViecActivity.class);
        } else if (id == R.id.thong_ke) {
            intent = new Intent(this, ThongKeActivity.class);
        } else if (id == R.id.thong_tin_ca_nhan) {
            intent = new Intent(this, ProfileActivity.class);
        } else if (id == R.id.cai_dat) {
            intent = new Intent(this, CaiDatActivity.class);
        } else if (id == R.id.dang_xuat) {
            intent = new Intent(this, LoginActivity.class);
        }


        if (intent != null) {

            if (user != null) {
                intent.putExtra("user", user);
            }

            startActivity(intent);

            if (id == R.id.dang_xuat) {
                finish();
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    /** Lấy user từ Intent và bind vào header */
    private void setupNavHeader() {
        View header = navigationView.getHeaderView(0);
        CircleImageView imgAvatar = header.findViewById(R.id.img_user_avatar);
        TextView tvName  = header.findViewById(R.id.tv_user_name);
        TextView tvEmail = header.findViewById(R.id.tv_user_email);

        User user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            tvName.setText(user.getFullName());
            tvEmail.setText(user.getEmail());
            Glide.with(this)
                    .load(user.getAvatar_url())
                    .placeholder(R.drawable.ic_default_avatar)
                    .into(imgAvatar);
        }
    }


}
