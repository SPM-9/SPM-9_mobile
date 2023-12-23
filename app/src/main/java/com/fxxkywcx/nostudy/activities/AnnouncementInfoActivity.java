package com.fxxkywcx.nostudy.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.AnnouncementEntity;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import com.fxxkywcx.nostudy.network.GetAnnouncementInfos;
import com.fxxkywcx.nostudy.utils.InternetToasts;

public class AnnouncementInfoActivity extends AppCompatActivity {
    TextView title;
    TextView date;
    TextView body;
    ProgressBar progressBar;
    TextView network_err;
    LinearLayout base;
    private final AnnouncementInfoActivity announcementInfoActivity;
    public AnnouncementInfoActivity() {
        announcementInfoActivity = AnnouncementInfoActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_info);

        title = findViewById(R.id.announcement_title);
        body = findViewById(R.id.announcement_body);
        date = findViewById(R.id.announcement_dateTime);
        progressBar = findViewById(R.id.pb);
        network_err = findViewById(R.id.network_error);
        base = findViewById(R.id.announcement_base);

        Intent intent = getIntent();
        NotificationEntity notification = (NotificationEntity) intent.getSerializableExtra("notification");
        if (notification == null) {
            finish();
            return;
        }

        int annId = notification.getFori_annId();
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                progressBar.setVisibility(View.GONE);
                if (status == GetAnnouncementInfos.NETWORK_FAILURE) {
                    network_err.setText(View.VISIBLE);
                } else {
                    AnnouncementEntity announcement = (AnnouncementEntity) msg.obj;
                    if (announcement == null)
                        return true;
                    title.setText(announcement.getTitle());
                    body.setText(announcement.getBody());
                    date.setText(Final.format.format(announcement.getUploadTime()));
                    base.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        GetAnnouncementInfos.getInstance().getAnnouncementInfo(handler, annId);
    }
}