package com.example.quanlycv.model;

public class Notification {
    private int notification_id;
    private int task_id;

    private String message;
    private String reminder_time;
    public Notification() {

    }

    public Notification(int notification_id, int task_id, String message, String reminder_time) {
        this.notification_id = notification_id;
        this.task_id = task_id;

        this.message = message;
        this.reminder_time = reminder_time;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }



    public String getReminder_time() {
        return reminder_time;
    }

    public void setReminder_time(String reminder_time) {
        this.reminder_time = reminder_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
