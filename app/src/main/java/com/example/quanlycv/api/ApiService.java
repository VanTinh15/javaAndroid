package com.example.quanlycv.api;

import android.app.Notification;

import com.example.quanlycv.model.Task;

import com.example.quanlycv.model.TaskStats;
import com.example.quanlycv.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("login.php")
    Call<User> checkLogin(@Query("username") String username, @Query("password") String password);

    @GET("get_users.php")
    Call<List<User>> getAllUsers();


    @Headers("Content-Type: application/json")
    @POST("add_task.php")
    Call<Task> addTask(@Body Task task);

    @POST("add_notification.php") // Nếu vẫn cần thêm thông báo riêng
    Call<ResponseBody> addNotification(@Body Notification notification);

    @GET( "get_task_by_id.php")
    Call<Task> getTaskById(@Query("task_id") int taskId);

    @GET("tasks/{taskId}")
    Call<Task> getTaskDetails(@Path("taskId") int taskId);


    @FormUrlEncoded
    @POST("update_task.php")
    Call<Void> updateTask(
            @Field("task_id") int id,
            @Field("task_title") String title,
            @Field("task_description") String description,
            @Field("due_date") String dueDate,
            @Field("status") String status
    );

    // API lấy công việc theo user id
    @GET("get_tasks_by_user.php")
    Call<List<Task>> getTasksByUser(@Query("user_id") int userId);


    // API lấy thống kê công việc
    @GET("get_tasks_count.php")
    Call<TaskStats> getTaskStats(@Query("user_id") int userId);

    // API lấy công việc hôm nay
    @GET("get_today_tasks.php")
    Call<List<Task>> getTodayTasks(@Query("user_id") int userId);

    @GET("get_task_by_date.php")
    Call<List<Task>> getTasksByDate(@Query("userId") int userId, @Query("date") String date);

    @GET("get_trangthongke.php")
    Call<TaskStats> getThongke(@Query("user_id") int userId, @Query("khoang_thoi_gian") String khoangThoiGian);

    @POST("update_user.php")
    Call<ApiResponse> updateUser(@Body User user);

    @GET("getUserById.php")
    Call<User> getUserById(@Query("id") int id);

    @FormUrlEncoded
    @POST("changePassword.php")
    Call<ApiResponse> changePassword(
            @Field("user_id") int userId,
            @Field("current_password") String mkhientai,
            @Field("new_password") String mkmoi
    );

    @GET("get_all_task.php")
    Call<List<Task>> getALLTasks(@Query("user_id") int userId);

}
