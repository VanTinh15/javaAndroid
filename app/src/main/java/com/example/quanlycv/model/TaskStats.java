package com.example.quanlycv.model;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.SerializedName;

public class TaskStats {

    @SerializedName("total_tasks") //tổng số đang chờ
    private int totalTasks;

    @SerializedName("today_tasks") //tổng số công việc hôm nay
    private int todayTasks;

    @SerializedName("completed_tasks") // tổng số công việc hoàn thành
    private int completedTasks;


    @SerializedName("tong_so")  // tổng số công việc(tất cả)
    private int tongSo;

    @SerializedName("ti_le")
    private String tiLe;

    public int getTotalTasks() {
        return totalTasks;
    }

    public int getTodayTasks() {
        return todayTasks;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }
    public int getTongSo() {
        return tongSo;
    }
    public String getTiLe() {
        return tiLe;
    }
}
