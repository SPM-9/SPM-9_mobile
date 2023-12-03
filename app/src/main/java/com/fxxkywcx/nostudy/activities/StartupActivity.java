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

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class StartupActivity extends AppCompatActivity {
    final int[] ad = {
            R.drawable.ad1,
    };

    final String[] adJumpURL = {
            "https://ys.mihoyo.com/",
    };

    int sel_AD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);



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