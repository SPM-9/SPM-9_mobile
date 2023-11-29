package com.fxxkywcx.nostudy.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import com.fxxkywcx.nostudy.entity.StudyTaskEntity;
import com.fxxkywcx.nostudy.file_io.FileIO;
import com.fxxkywcx.nostudy.file_io.StoreStudyTaskFile;
import com.fxxkywcx.nostudy.network.DownloadStudyTaskFile;
import com.fxxkywcx.nostudy.network.GetAnnouncementInfos;
import com.fxxkywcx.nostudy.network.GetStudyTaskInfos;
import com.fxxkywcx.nostudy.utils.FileUtils;
import com.fxxkywcx.nostudy.utils.IOToasts;
import com.fxxkywcx.nostudy.utils.InternetToasts;

public class StudyTaskInfoActivity extends AppCompatActivity {
    private final StudyTaskInfoActivity studyTaskInfoActivity;
    public StudyTaskInfoActivity() {
        this.studyTaskInfoActivity = StudyTaskInfoActivity.this;
    }
    private int taskId;
    String fileName = null;
    byte[] file = null;

    TextView title;
    TextView body;
    RelativeLayout fileWindow;
    TextView fileNameTextView;
    TextView fileSize;
    TextView taskType;
    TextView startTime;
    TextView ddl;
    RelativeLayout commitWindow;
    TextView commitTime;
    TextView commitScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_task_info);

        Intent intent = getIntent();
        NotificationEntity notification = (NotificationEntity) intent.getSerializableExtra("notification");
        if (notification == null) {
            finish();
            return;
        }


        title = findViewById(R.id.studyTask_title);
        body = findViewById(R.id.studyTask_body);
        fileWindow = findViewById(R.id.studyTask_file);
        fileNameTextView = findViewById(R.id.studyTask_fileName);
        fileSize = findViewById(R.id.studyTask_fileSize);
        taskType = findViewById(R.id.studyTask_taskType);
        startTime = findViewById(R.id.studyTask_startTime);
        ddl = findViewById(R.id.studyTask_DDL);
        commitWindow = findViewById(R.id.studyTask_commit);
        commitTime = findViewById(R.id.studyTask_commitTime);
        commitScore = findViewById(R.id.studyTask_commitScore);


        taskId = notification.getFori_taskId();
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if (status == GetStudyTaskInfos.NETWORK_FAILURE) {
                    InternetToasts.NoInternetToast(studyTaskInfoActivity);
                } else {
                    StudyTaskEntity studyTask = (StudyTaskEntity) msg.obj;
                    if (studyTask == null)
                        return true;

                    title.setText(studyTask.getTitle());
                    body.setText(studyTask.getBody());
                    if (studyTask.getFileName() == null) {
                        fileWindow.setVisibility(View.GONE);
                    } else {
                        fileNameTextView.setText(studyTask.getFileName());
                        fileSize.setText(FileUtils.sizeToString(studyTask.getFileSize(), 2));
                    }
                    if (studyTask.getTaskType() == StudyTaskEntity.HOMEWORK)
                        taskType.setText("作业模式");
                    else 
                        taskType.setText("考试模式");
                    startTime.setText(Final.format.format(studyTask.getStartTime()));
                    ddl.setText(Final.format.format(studyTask.getEndTime()));

                    // TODO: 2023/11/30 发起okhttp请求，获取提交记录，如果有则修改文字，无则隐藏该视图
                    commitWindow.setVisibility(View.GONE);

                }
                return true;
            }
        });

        GetStudyTaskInfos.getInstance().getStudyTaskInfo(handler, taskId);
    }

    public void Commit(View view) {
    }

    public void GetCommitInfo(View view) {
    }

    public void GetFile(View view) {
        // /storage/emulated/0/Android/data/com.fxxkywcx.nostudy/files/Download
        Handler storeHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if (status == FileIO.IO_ERROR) {
                    IOToasts.IOFailedToast(studyTaskInfoActivity);
                } else {
                    IOToasts.IOSuccessToast(studyTaskInfoActivity);
                }
                return true;
            }
        });
        Handler downloadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if (status == GetAnnouncementInfos.NETWORK_FAILURE) {
                    InternetToasts.NoInternetToast(studyTaskInfoActivity);
                } else {
                    StudyTaskEntity task = (StudyTaskEntity) msg.obj;
                    if (task == null)
                        return true;
                    fileName = task.getFileName();
                    file = task.getFile();

                    // TODO: 2023/11/29 跳转打开方式

                    IOToasts.IOSuccessToast(StudyTaskInfoActivity.this);
                    StoreStudyTaskFile.getInstance(studyTaskInfoActivity).storeStudyTaskFile(storeHandler, fileName, file);
                }

                return true;
            }
        });

        DownloadStudyTaskFile.getInstance().DownloadFile(downloadHandler, taskId);
    }
}