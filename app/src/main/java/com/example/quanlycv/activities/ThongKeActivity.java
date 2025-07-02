package com.example.quanlycv.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;




import androidx.activity.EdgeToEdge;

import com.example.quanlycv.R;
import com.example.quanlycv.api.ApiClient;
import com.example.quanlycv.api.ApiService;
import com.example.quanlycv.model.TaskStats;
import com.example.quanlycv.model.User;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThongKeActivity extends MenuActivity {

    private RadioGroup radioGrouplocthoigian;
    private RadioButton radioTuan, radioThang, radioNam;

    private TextView tvTongSo, tvHoanThanh, tvTyLe;
    private BarChart barChart;

    private ApiService apiService;
    private int userId;

    protected int getLayoutResourceId() {
        return R.layout.activity_thong_ke;  // phần nội dung thực tế
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        //ánh xạ các view
        radioGrouplocthoigian = findViewById(R.id.radiogroup_time_filter);
        radioTuan = findViewById(R.id.radio_week);
        radioThang = findViewById(R.id.radio_month);
        radioNam = findViewById(R.id.radio_year);

        tvTongSo = findViewById(R.id.tv_stats_total);
        tvHoanThanh = findViewById(R.id.tv_stats_completed);
        tvTyLe = findViewById(R.id.tv_stats_rate);

        //ánh xạ biểu đồ
        barChart = findViewById(R.id.barChart);

        // lấy user_id từ intent
        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            Toast.makeText(this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userId = user.getUser_id();

        apiService = ApiClient.getClient().create(ApiService.class);

        //xử lý khi chọn bộ lọc thời gian thay đổi
        radioGrouplocthoigian.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_week) {
                taiThongKeCho("week");
            } else if (checkedId == R.id.radio_month) {
                taiThongKeCho("month");
            } else if (checkedId == R.id.radio_year) {
                taiThongKeCho("year");
            }
        });

        taiThongKeCho("month");

    }
    private void taiThongKeCho(String khoangThoiGian) {
        Call<TaskStats> call = apiService.getThongke(userId, khoangThoiGian);
        call.enqueue(new Callback<TaskStats>() {
            @Override
            public void onResponse(Call<TaskStats> call, Response<TaskStats> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TaskStats thongKe = response.body();

                    // Cập nhật UI
                    tvTongSo.setText(String.valueOf(thongKe.getTotalTasks()));
                    tvHoanThanh.setText(String.valueOf(thongKe.getCompletedTasks()));

                    // Giả sử tỷ lệ hoàn thành bạn tính ở API rồi trả về dưới dạng String (vd "50%")
                    // Nếu chưa có, bạn có thể tự tính: completedTasks * 100 / totalTasks
                    if (thongKe.getTotalTasks() > 0) {
                        int tyLe = (int) ((float) thongKe.getCompletedTasks() / thongKe.getTotalTasks() * 100);
                        tvTyLe.setText(tyLe + "%");
                    } else {
                        tvTyLe.setText("0%");
                    }
                    // Cập nhật biểu đồ
                    capNhatBieuDoCot(thongKe.getCompletedTasks(), thongKe.getTotalTasks());

                } else {
                    Toast.makeText(ThongKeActivity.this, "Lỗi tải dữ liệu thống kê", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TaskStats> call, Throwable t) {
                Toast.makeText(ThongKeActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void capNhatBieuDoCot(int daHoanThanh, int tongCongViec) {
        int chuaHoanThanh = tongCongViec - daHoanThanh;

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, daHoanThanh));
        entries.add(new BarEntry(1, chuaHoanThanh));

        BarDataSet dataSet = new BarDataSet(entries, "Công việc");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.6f);

        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        // Cấu hình trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0f) {
                    return "Hoàn thành";
                } else if (value == 1f) {
                    return "Chưa hoàn thành";
                } else {
                    return "";
                }
            }
        });
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12f);

        // Trục Y
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(0f);

        barChart.getAxisRight().setEnabled(false); // tắt trục phải

        barChart.invalidate(); // vẽ lại
    }



}