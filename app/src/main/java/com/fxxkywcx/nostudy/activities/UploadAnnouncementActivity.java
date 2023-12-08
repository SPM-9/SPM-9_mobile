package com.fxxkywcx.nostudy.activities;

import android.os.*;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.Variable;
import com.fxxkywcx.nostudy.entity.AnnouncementEntity;
import com.fxxkywcx.nostudy.network.GetAnnouncementInfos;
import com.fxxkywcx.nostudy.network.UploadAnnouncement;
import com.fxxkywcx.nostudy.network.UploadResource;
import com.fxxkywcx.nostudy.utils.InternetToasts;
import com.fxxkywcx.nostudy.utils.UploadStudyTaskToast;

public class UploadAnnouncementActivity extends AppCompatActivity {
    private UploadAnnouncementActivity uploadAnnouncementActivity;
    public UploadAnnouncementActivity() {
        this.uploadAnnouncementActivity = UploadAnnouncementActivity.this;
    }
    private final String TAG = "UploadAnnouncementActivity";
    private EditText title;
    private EditText body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_announcement);

        title = findViewById(R.id.uploadAnnouncement_title);
        body = findViewById(R.id.uploadAnnouncement_body);
    }

    public void Upload(View view) {
        if (title.getText().toString().isEmpty()) {
            UploadStudyTaskToast.NoTitle(uploadAnnouncementActivity);
            return;
        }

        AnnouncementEntity announcement = new AnnouncementEntity();
        announcement.setTitle(title.getText().toString());
        announcement.setBody(body.getText().toString());

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if (status == GetAnnouncementInfos.NETWORK_FAILURE) {
                    InternetToasts.NoInternetToast(uploadAnnouncementActivity);
                } else {
                    InternetToasts.UploadSuccessToast(uploadAnnouncementActivity);
                    finish();
                }

                return true;
            }
        });

        UploadAnnouncement.getInstance().uploadAnnouncement(handler, announcement,
                Variable.currentTeacher.getTeacherId());
    }
}