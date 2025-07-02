package com.example.quanlycv.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlycv.R;
import com.example.quanlycv.api.ApiClient;
import com.example.quanlycv.api.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
//    EditText etMessage;
//    Button btnSend;
//    TextView tvChat;
//    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//        tvChat = findViewById(R.id.chat_history);
//        etMessage = findViewById(R.id.chat_input);
//        btnSend = findViewById(R.id.send_btn);
//
//        apiService = ApiClient.getClient().create(ApiService.class);
//
//        btnSend.setOnClickListener(v -> {
//            String userMsg = etMessage.getText().toString();
//            if (userMsg.isEmpty()) return;
//
//            tvChat.append("\nBạn: " + userMsg);
//            etMessage.setText("");
//
//            AiMessage message = new AiMessage(userMsg);
//
//            apiService.sendMessageToAI(message).enqueue(new Callback<AiResponse>() {
//                @Override
//                public void onResponse(Call<AiResponse> call, Response<AiResponse> response) {
//                    if (response.isSuccessful()) {
//                        String reply = response.body().getChoices().get(0).getMessage().getContent();
//                        tvChat.append("\nAI: " + reply);
//                    } else {
//                        tvChat.append("\nLỗi phản hồi từ AI.");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<AiResponse> call, Throwable t) {
//                    tvChat.append("\nLỗi mạng hoặc máy chủ.");
//                }
//            });
//        });
    }
}
