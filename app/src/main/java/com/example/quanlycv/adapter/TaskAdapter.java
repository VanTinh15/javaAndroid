package com.example.quanlycv.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlycv.R;
import com.example.quanlycv.model.Task;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList = new ArrayList<>();


    private OnItemClickListener onItemClickListener; // Biến để lưu listener

    public TaskAdapter() {
    }

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    //gan su kien tu click ben ngoai
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setTasks(List<Task> tasks) {
        this.taskList = tasks;
        notifyDataSetChanged();
    }

    public void addTask(Task task) {
        taskList.add(task);
        notifyItemInserted(taskList.size() - 1); // Chỉ cập nhật item mới thêm
    }

    public void updateTask(int position, Task task) {
        taskList.set(position, task);
        notifyItemChanged(position); // Chỉ cập nhật item thay đổi
    }

    public void removeTask(int position) {
        taskList.remove(position);
        notifyItemRemoved(position); // Chỉ cập nhật item bị xóa
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.txtTitle.setText(task.getTask_title());
        holder.txtDescription.setText(task.getTask_description());
        holder.txtStatus.setText(task.getStatus());
        holder.txtDueDate.setText("Hạn chót: " + task.getDue_date());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(task);
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDescription, txtStatus, txtDueDate;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTaskTitle);
            txtDescription = itemView.findViewById(R.id.txtTaskDescription);
            txtStatus = itemView.findViewById(R.id.txtTaskStatus);
            txtDueDate = itemView.findViewById(R.id.txtTaskDueDate);
        }
    }
}
