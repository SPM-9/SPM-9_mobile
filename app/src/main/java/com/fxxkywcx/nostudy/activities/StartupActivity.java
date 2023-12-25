package com.fxxkywcx.nostudy.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.MainActivity;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.Variable;
import com.fxxkywcx.nostudy.entity.TeacherEntity;
import com.fxxkywcx.nostudy.entity.UserEntity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class StartupActivity extends AppCompatActivity {
    final int[] ad = {
//            R.drawable.ad1,
            R.drawable.ad2
    };

    final String[] adJumpURL = {
//            "https://ys.mihoyo.com/",
            "https://space.bilibili.com/315966694"
    };

    int sel_AD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);


        if (!Final.needADAndLogin) {
            UserEntity user = new UserEntity();
            user.setUid(7);
            user.setUserName("DebugUser");
            user.setPassword("debug");
            user.setNickName("测试用户");
            user.setEmail("fxxkywcx@debug.com");
            user.setMoney(0);
            user.setChosenCourse(true);
            Variable.currentUser = user;

            TeacherEntity teacher = new TeacherEntity();
            teacher.setTeacherId(1);
            teacher.setUserName("DebugTeacher");
            teacher.setPassword("debug");
            teacher.setNickName("测试老师");
            teacher.setEmail("fxxkywcx@debug.com");
            teacher.setMoney(0);
            Variable.currentTeacher = teacher;

            startActivity(new Intent(StartupActivity.this, MainActivity.class));
            finish();
            return;
        }

        Random rand = new Random();
        sel_AD = rand.nextInt(ad.length);

        new Timer().schedule(openAD, 1000);
        new Timer().schedule(startMainActivity, 4000);
    }

    private final TimerTask openAD = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setContentView(R.layout.activity_startup_ad);
                    ((ImageView) findViewById(R.id.startupAD_ad)).setImageResource(ad[sel_AD]);
                }
            });
        }
    };

    private final TimerTask startMainActivity = new TimerTask() {
        @Override
        public void run() {
            if (Variable.currentUser == null)
                startActivity(new Intent(StartupActivity.this, LoginActivity.class));
            else
                startActivity(new Intent(StartupActivity.this, MainActivity.class));
            finish();
        }
    };

    public void Jump(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(adJumpURL[sel_AD])));
    }
}