package com.example.quanlycv.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycv.R;
import com.example.quanlycv.adapter.TaskAdapter;
import com.example.quanlycv.api.ApiClient;
import com.example.quanlycv.api.ApiService;
import com.example.quanlycv.api.GeminiApiService;
import com.example.quanlycv.model.GeminiRequest;
import com.example.quanlycv.model.GeminiResponse;
import com.example.quanlycv.model.Task;
import com.example.quanlycv.model.TaskStats;
import com.example.quanlycv.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.gson.Gson;

import androidx.core.text.HtmlCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

import java.util.function.Consumer;

import com.example.quanlycv.BuildConfig;


public class TongquanActivity extends MenuActivity {

    private RecyclerView recyclerTodayTasks;
    private TextView tvDangchoTasks, tvHomNayTasks, tvCompletedTasks, tvNoTodayTasks;
    private FloatingActionButton fabAddTask;
    private TaskAdapter taskAdapter;
    private ApiService apiService;
    private int userId;

    private FrameLayout chatContainer;
    private View chatBox;
    private boolean isChatOpen = false;

    private GeminiApiService geminiService;
    private static final String GEMINI_API_KEY = "AIzaSyDZT1L871J8qEXeDW_EHgk9prbuU-kvQXg";
    private static final String GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/";

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_tongquan;
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


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GEMINI_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        geminiService = retrofit.create(GeminiApiService.class);


        initViews();


        fetchTaskStats();
        fetchTodayTasks();

    }

    private void initViews() {
        recyclerTodayTasks = findViewById(R.id.recycler_today_tasks);
        tvDangchoTasks       = findViewById(R.id.tv_dangcho_tasks);
        tvHomNayTasks       = findViewById(R.id.tv_homnay_tasks);
        tvCompletedTasks   = findViewById(R.id.tv_completed_tasks);
        tvNoTodayTasks     = findViewById(R.id.tv_no_today_tasks);
        fabAddTask         = findViewById(R.id.fab_add_task);

        chatContainer = findViewById(R.id.chat_container);
        ImageButton openChatBtn = findViewById(R.id.btn_open_chat);
        openChatBtn.setOnClickListener(v -> {
            Log.d("ChatToggle", "btn_open_chat was clicked");
            chuyendoiChatBox();
        });

        recyclerTodayTasks.setLayoutManager(new LinearLayoutManager(this));

        taskAdapter = new TaskAdapter();

        recyclerTodayTasks.setAdapter(taskAdapter);

        User user = (User) getIntent().getSerializableExtra("user");


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


    @Override
    protected void onResume() {

        super.onResume();
        fetchTaskStats();
        fetchTodayTasks();
    }

    /**
     * Lấy thống kê công việc từ API
     */
    private void fetchTaskStats() {
        Log.d("thongke", "Đang gọi API thống kê với userId: " + userId);

        apiService.getTaskStats(userId).enqueue(new Callback<TaskStats>() {
            @Override
            public void onResponse(Call<TaskStats> call, Response<TaskStats> response) {
                Log.d("thongke", "URL gọi: " + call.request().url());
                if (response.isSuccessful() && response.body() != null) {
                    TaskStats stats = response.body();
                    Log.d("thongke", "JSON nhận: " + new Gson().toJson(stats));


                    runOnUiThread(() -> {
                        tvDangchoTasks.setText(String.valueOf(stats.getTotalTasks()));
                        tvHomNayTasks.setText(String.valueOf(stats.getTodayTasks()));
                        tvCompletedTasks.setText(String.valueOf(stats.getCompletedTasks()));
                    });
                } else {

                    Log.e("thongke", "Lỗi khi nhận response từ server: " + response.code());
                    Toast.makeText(TongquanActivity.this, "Lỗi khi nhận dữ liệu thống kê", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TaskStats> call, Throwable t) {
                Log.e("thongke", "Lỗi kết nối tới API", t);
                Toast.makeText(TongquanActivity.this, "Không thể kết nối server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Lấy danh sách công việc hôm nay
     */
    private void fetchTodayTasks() {

        apiService.getTodayTasks(userId).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Danhsachcongviec", "JSON nhận: " + new Gson().toJson(response.body()));  // cần import Gson
                    List<Task> todayTasks = response.body();

                    // Cập nhật UI với danh sách công việc hôm nay
                    runOnUiThread(() -> {
                        taskAdapter.setTasks(todayTasks);

                        // Kiểm tra hiển thị thông báo khi không có công việc
                        if (todayTasks.isEmpty()) {
                            tvNoTodayTasks.setVisibility(View.VISIBLE);
                            recyclerTodayTasks.setVisibility(View.GONE);
                        } else {
                            tvNoTodayTasks.setVisibility(View.GONE);
                            recyclerTodayTasks.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    Toast.makeText(TongquanActivity.this, "Lỗi khi nhận danh sách công việc", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {

                Log.e("TongquanActivity", "Lỗi kết nối server: " + t.getMessage());
                t.printStackTrace();  // In chi tiết stack trace
                Toast.makeText(TongquanActivity.this, "Không thể kết nối server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chuyendoiChatBox() {
        Log.d("Chat", "toggleChatBox() called, isChatOpen=" + isChatOpen);
        try {
            if (!isChatOpen) {

                chatContainer.removeAllViews();
                chatBox = getLayoutInflater().inflate(R.layout.activity_chat, null);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT);
                chatBox.setLayoutParams(params);

                chatContainer.addView(chatBox);
                chatContainer.setVisibility(View.VISIBLE);

                setupChatBot();

                Toast.makeText(this, "Mở hộp chat", Toast.LENGTH_SHORT).show();
            } else {
                chatContainer.removeAllViews();
                chatContainer.setVisibility(View.GONE);
                Toast.makeText(this, "Đóng hộp chat", Toast.LENGTH_SHORT).show();
            }
            isChatOpen = !isChatOpen;
        } catch (Exception e) {
            Log.e("Chat", "Lỗi trong toggleChatBox(): " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi khi mở/đóng chat: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    //Gửi câu hỏi đến AI (giả sử dùng OpenAI API)
    private void setupChatBot() {
        EditText chatInput   = chatBox.findViewById(R.id.chat_input);
        TextView chatHistory = chatBox.findViewById(R.id.chat_history);
        Button sendBtn       = chatBox.findViewById(R.id.send_btn);

        sendBtn.setOnClickListener(v -> {
            String msg = chatInput.getText().toString().trim();
            if (msg.isEmpty()) return;
            chatHistory.append("\nBạn: " + msg + "\n");
            chatInput.setText("");
            sendMessageToGemini(msg, reply -> runOnUiThread(() -> {
                Log.d("GeminiReply", reply);
                String formattedReply = reply.replace("*", "•");
                chatHistory.append("\nAI: " + formattedReply + "\n");
            }));

        });
    }

    //: Gửi request đến OpenAI (giả lập)
    private void sendMessageToGemini(String message, Consumer<String> callback) {

        apiService.getALLTasks(userId).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> todayTasks = response.body();


                    StringBuilder taskList = new StringBuilder("Tôi có các công việc hôm nay:\n");

                    for (int i = 0; i < todayTasks.size(); i++) {
                        Task task = todayTasks.get(i);
                        taskList.append(i + 1)
                                .append(". Tiêu đề: ").append(task.getTask_title()).append("\n")
                                .append("   Mô tả: ").append(task.getTask_description()).append("\n")
                                .append("   Ngày tạo: ").append(task.getCreated_date()).append("\n")
                                .append("   Deadline: ").append(task.getDue_date()).append("\n")
                                .append("   Trạng thái: ").append(task.getStatus()).append("\n\n");
                    }



                    String fullPrompt = taskList + "\n\nNgười dùng hỏi: " + message;


                    sendToGemini(fullPrompt, callback);

                } else {
                    callback.accept("Không thể lấy danh sách công việc hôm nay để tư vấn.");
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                callback.accept("Lỗi khi kết nối đến server: " + t.getMessage());
            }
        });
    }

    private void sendToGemini(String fullPrompt, Consumer<String> callback) {
        GeminiRequest.Part part = new GeminiRequest.Part(fullPrompt);
        GeminiRequest.Content content = new GeminiRequest.Content(List.of(part));
        GeminiRequest request = new GeminiRequest(List.of(content));

        geminiService.generateContent(GEMINI_API_KEY, request).enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GeminiResponse.Candidate> candidates = response.body().candidates;
                    if (candidates != null && !candidates.isEmpty()) {
                        String reply = candidates.get(0).content.parts.get(0).text;
                        callback.accept(reply.trim());
                    } else {
                        callback.accept("Không có phản hồi từ AI.");
                    }
                } else {
                    callback.accept("Lỗi Gemini API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                callback.accept("Lỗi khi gọi Gemini: " + t.getMessage());
            }
        });
    }



}