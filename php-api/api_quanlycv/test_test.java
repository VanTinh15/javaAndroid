<?xml version="1.0" encoding="utf-8"?>

<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/root_tongquan"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/background_color"
    tools:context=".activities.TongquanActivity">

    <!-- Thống kê tổng quan -->
    <androidx.cardview.widget.CardView
        android:id="@+id/id_tongquan"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        app:cardCornerRadius="8dp"
        app:cardElevation="4dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="16dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Tổng quan công việc"
                android:textColor="@color/text_color_primary"
                android:textSize="18sp"
                android:textStyle="bold" />

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="16dp"
                android:orientation="horizontal">

                <!-- Tổng công việc -->
                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:gravity="center"
                    android:orientation="vertical">

                    <TextView
                        android:id="@+id/tv_dangcho_tasks"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="0"
                        android:textColor="@color/colorPrimary"
                        android:textSize="24sp"
                        android:textStyle="bold" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Đang chờ"
                        android:textColor="@color/text_color_secondary"
                        android:textSize="14sp" />
                </LinearLayout>

                <!-- Công việc hôm nay -->
                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:gravity="center"
                    android:orientation="vertical">

                    <TextView
                        android:id="@+id/tv_homnay_tasks"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="0"
                        android:textColor="#E91E63"
                        android:textSize="24sp"
                        android:textStyle="bold" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Hôm nay"
                        android:textColor="@color/text_color_secondary"
                        android:textSize="14sp" />
                </LinearLayout>

                <!-- Công việc hoàn thành -->
                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:gravity="center"
                    android:orientation="vertical">

                    <TextView
                        android:id="@+id/tv_completed_tasks"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="0"
                        android:textColor="#4CAF50"
                        android:textSize="24sp"
                        android:textStyle="bold" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Hoàn thành"
                        android:textColor="@color/text_color_secondary"
                        android:textSize="14sp" />
                </LinearLayout>
            </LinearLayout>
        </LinearLayout>
    </androidx.cardview.widget.CardView>

    <!-- Công việc hôm nay -->
    <androidx.cardview.widget.CardView
        android:id="@+id/card_today_tasks"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_margin="16dp"
        android:layout_marginTop="24dp"
        app:cardCornerRadius="8dp"
        app:cardElevation="4dp"
        app:layout_constraintTop_toBottomOf="@id/id_tongquan"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toBottomOf="parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical"
            android:padding="16dp"
            >

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Công việc hôm nay"
                android:textColor="@color/text_color_primary"
                android:textSize="18sp"
                android:textStyle="bold" />

            <!-- Danh sách công việc hôm nay -->

            <!-- Hiển thị khi không có công việc -->
            <androidx.recyclerview.widget.RecyclerView
                android:id="@+id/recycler_today_tasks"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_marginTop="8dp"
                android:clipToPadding="false" />

            <TextView
                android:id="@+id/tv_no_today_tasks"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="32dp"
                android:gravity="center"
                android:text="Không có công việc nào hôm nay"
                android:textColor="@color/text_color_secondary"
                android:textSize="16sp"
                android:visibility="gone" />

        </LinearLayout>
    </androidx.cardview.widget.CardView>


    <!-- Nút thêm công việc -->
    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fab_add_task"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        android:src="@drawable/ic_add_task"
        android:contentDescription="Thêm công việc"
        app:backgroundTint="@color/buttonBackgroundColor"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

    <FrameLayout
        android:id="@+id/chat_container"
        android:layout_width="60dp"
        android:layout_height="60dp"
        android:background="@android:color/transparent"
        app:layout_constraintBottom_toTopOf="@id/fab_add_task"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginBottom="16dp"
        android:layout_marginEnd="16dp">

        <ImageButton
            android:id="@+id/btn_open_chat"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@drawable/rounded_button"
            android:src="@drawable/ic_chat"
            app:tint="@android:color/black"
            android:elevation="10dp"
            android:clickable="true"
            android:focusable="true" />
    </FrameLayout>


</androidx.constraintlayout.widget.ConstraintLayout>
