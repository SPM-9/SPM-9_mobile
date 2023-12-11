package com.fxxkywcx.nostudy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.fxxkywcx.nostudy.activities.*;
import com.fxxkywcx.nostudy.entity.StudyTaskEntity;
import com.fxxkywcx.nostudy.file_io.FileIO;
import com.fxxkywcx.nostudy.file_io.SaveReadUserInfo;
import com.fxxkywcx.nostudy.utils.IOToasts;
import com.fxxkywcx.nostudy.utils.InternetToasts;
import com.fxxkywcx.nostudy.utils.LoginRegisterViews;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.fxxkywcx.nostudy.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainActivity mainActivity;
    public MainActivity() {
        this.mainActivity = MainActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void JumpToWarning(View view) {
        if (Variable.currentUser != null) {
            if (!Variable.currentUser.isChosenCourse()) {
                InternetToasts.notChosenCourse(mainActivity);
                startActivity(new Intent(mainActivity, ChooseClassActivity.class));
            } else {
                // 学生端，跳转到预警系统
            }
        } else if (Variable.currentTeacher != null) {
            // 教师端，跳转到发起预警系统
        }
    }

    public void JumpToScore(View view) {
        if (Variable.currentUser != null) {
            if (!Variable.currentUser.isChosenCourse()) {
                InternetToasts.notChosenCourse(mainActivity);
                startActivity(new Intent(mainActivity, ChooseClassActivity.class));
            } else {
                // 学生端，跳转到成绩查询
            }
        } else if (Variable.currentTeacher != null) {
            // 教师端，不跳转
        }
    }

    public void JumpToHomework(View view) {
        if (Variable.currentUser != null) {
            if (!Variable.currentUser.isChosenCourse()) {
                InternetToasts.notChosenCourse(mainActivity);
                startActivity(new Intent(mainActivity, ChooseClassActivity.class));
            } else {
                // 学生端，跳转到作业列表
            }
        } else if (Variable.currentTeacher != null) {
            // 教师端，跳转到发布作业Activity
            Intent intent = new Intent(mainActivity, UploadStudyTaskActivity.class);
            intent.putExtra("taskType", StudyTaskEntity.HOMEWORK);
            startActivity(new Intent(mainActivity, UploadStudyTaskActivity.class));
        }
    }

    public void JumpToExam(View view) {
        if (Variable.currentUser != null) {
            if (!Variable.currentUser.isChosenCourse()) {
                InternetToasts.notChosenCourse(mainActivity);
                startActivity(new Intent(mainActivity, ChooseClassActivity.class));
            } else {
                // 学生端，跳转到考试列表
            }
        } else if (Variable.currentTeacher != null) {
            // 教师端，跳转到发布作业Activity
            Intent intent = new Intent(mainActivity, UploadStudyTaskActivity.class);
            intent.putExtra("taskType", StudyTaskEntity.EXAM);
            startActivity(intent);
        }
    }

    public void JumpToChat(View view) {
        if (Variable.currentUser != null) {
            if (!Variable.currentUser.isChosenCourse()) {
                InternetToasts.notChosenCourse(mainActivity);
                startActivity(new Intent(mainActivity, ChooseClassActivity.class));
            } else {
                // 学生端，跳转到讨论区
            }
        } else {
            // 教师端，跳转到讨论区
        }
    }

    public void JumpToResourceDownload(View view) {
        if (Variable.currentUser != null) {
            if (!Variable.currentUser.isChosenCourse()) {
                InternetToasts.notChosenCourse(mainActivity);
                startActivity(new Intent(mainActivity, ChooseClassActivity.class));
            } else {
                // 学生端，跳转到资源列表
            }
        } else if (Variable.currentTeacher != null) {
            // 教师端，跳转到发布资源Activity
            startActivity(new Intent(mainActivity, UploadResourceActivity.class));
        }
    }

    public void JumpToSign(View view) {
        if (Variable.currentUser != null) {
            if (!Variable.currentUser.isChosenCourse()) {
                InternetToasts.notChosenCourse(mainActivity);
                startActivity(new Intent(mainActivity, ChooseClassActivity.class));
            } else {
                // 学生端，跳转到签到界面
            }
        } else {
            // 教师端，跳转到发起签到
        }
    }

    public void JumpToChooseCourse(View view) {
        if (Variable.currentUser != null) {
            // 学生端，跳转到选课activity
            startActivity(new Intent(mainActivity, ChooseClassActivity.class));
        } else if (Variable.currentTeacher != null) {
            // 教师端，跳转到确认选课
            startActivity(new Intent(mainActivity, ClassPermissionActivity.class));
        }
    }

    public void modifypassword(View view) {}

    public void exit(View view) {
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;

                if (status == FileIO.IO_ERROR) {
                    IOToasts.IOFailedToast(mainActivity);
                } else {
                    Variable.currentUser = null;
                    Variable.currentTeacher = null;
                    LoginRegisterViews.Logout(mainActivity);
                    startActivity(new Intent(mainActivity, LoginActivity.class));
                    finish();
                }
                return true;
            }
        });

        SaveReadUserInfo.getInstance(mainActivity).DeleteUserLoginInfo(handler);
    }

    public void about(View view) {
        Intent intent = new Intent(this, AboutInfoActivity.class);
        startActivity(intent);
    }

    public void JumpToAnnouncement(View view) {
        if (Variable.currentUser != null) {
            // 学生端，不响应
        } else if (Variable.currentTeacher != null) {
            // 教师端，跳转到发布公告Activity
            startActivity(new Intent(mainActivity, UploadAnnouncementActivity.class));
        }
    }
}