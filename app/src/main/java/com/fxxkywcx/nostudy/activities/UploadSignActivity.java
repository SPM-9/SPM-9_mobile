package com.fxxkywcx.nostudy.activities;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.Variable;
import com.fxxkywcx.nostudy.entity.AnnouncementEntity;
import com.fxxkywcx.nostudy.entity.UserSignEntity;
import com.fxxkywcx.nostudy.entity.UserSignsEntity;
import com.fxxkywcx.nostudy.network.GetAnnouncementInfos;
import com.fxxkywcx.nostudy.network.UploadSign;
import com.fxxkywcx.nostudy.utils.InternetToasts;
import com.fxxkywcx.nostudy.utils.UploadStudyTaskToast;
import com.fxxkywcx.nostudy.utils.ViewUtils;

import java.util.Date;

public class UploadSignActivity extends AppCompatActivity {

    TimePicker timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_sign);

        timePicker = findViewById(R.id.upload_sign_clock);
    }

    public void upload(View view) {
        Date current = new Date(System.currentTimeMillis());
        Date end = new Date();
        end.setMinutes(timePicker.getMinute());
        end.setHours(timePicker.getHour());
        if (current.after(end)) {
            UploadStudyTaskToast.StartLaterThanEnd(UploadSignActivity.this);
            return;
        }

        UserSignsEntity userSigns = new UserSignsEntity();
        userSigns.setStartTime(current);
        userSigns.setEndTime(end);
        userSigns.setTeacherId(Variable.currentTeacher.getTeacherId());

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if (status == GetAnnouncementInfos.NETWORK_FAILURE) {
                    InternetToasts.NoInternetToast(UploadSignActivity.this);
                } else {
                    Toast.makeText(UploadSignActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return true;
            }
        });
        UploadSign.getInstance().uploadSign(handler, userSigns);
    }
}