package com.example.quanlycv.model;

public class Task {
    public int task_id;
    public int user_id;
    public String task_title;
    public String task_description;
    public String created_date;
    public String due_date;
    public String status;

    public Task() {

    }

    public Task(int task_id, int user_id, String task_title, String task_description, String created_date, String due_date, String status) {
        this.task_id = task_id;
        this.user_id = user_id;
        this.task_title = task_title;
        this.task_description = task_description;
        this.created_date = created_date;
        this.due_date = due_date;
        this.status = status;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public String getTask_description() {
        return task_description;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }
}
