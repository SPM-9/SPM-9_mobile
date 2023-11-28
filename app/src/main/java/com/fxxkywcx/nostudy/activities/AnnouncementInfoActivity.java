package com.fxxkywcx.nostudy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.AnnouncementEntity;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import com.fxxkywcx.nostudy.network.GetAnnouncementInfos;
import com.fxxkywcx.nostudy.utils.InternetUtils;

public class AnnouncementInfoActivity extends AppCompatActivity {
    TextView title;
    TextView date;
    TextView body;
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
                if (status == GetAnnouncementInfos.NETWORK_FAILURE) {
                    InternetUtils.NoInternetToast(announcementInfoActivity);
                } else {
                    AnnouncementEntity announcement = (AnnouncementEntity) msg.obj;
                    if (announcement == null)
                        return true;
                    title.setText(announcement.getTitle());
                    body.setText(announcement.getBody());
                    date.setText(Final.format.format(announcement.getUploadTime()));
                }
                return true;
            }
        });

        GetAnnouncementInfos.getInstance().getAnnouncementInfo(handler, annId);
    }
}