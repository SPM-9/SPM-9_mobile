package com.fxxkywcx.nostudy.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.Variable;
import com.fxxkywcx.nostudy.entity.CommitEntity;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import com.fxxkywcx.nostudy.entity.StudyTaskEntity;
import com.fxxkywcx.nostudy.file_io.FileIO;
import com.fxxkywcx.nostudy.file_io.StoreStudyTaskFile;
import com.fxxkywcx.nostudy.network.DownloadStudyTaskFile;
import com.fxxkywcx.nostudy.network.GetAnnouncementInfos;
import com.fxxkywcx.nostudy.network.GetCommit;
import com.fxxkywcx.nostudy.network.GetStudyTaskInfos;
import com.fxxkywcx.nostudy.utils.FileUtils;
import com.fxxkywcx.nostudy.utils.IOToasts;
import com.fxxkywcx.nostudy.utils.InternetToasts;
import java.io.File;
import java.util.Date;

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
    RelativeLayout commitButton;
    Handler handler;
    boolean isCommitted;
    Date startDatetime;
    Date ddlDatetime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_task_info);

        Intent intent = getIntent();
        NotificationEntity notification = (NotificationEntity) intent.getSerializableExtra("notification");
        StudyTaskEntity studyTask = (StudyTaskEntity) intent.getSerializableExtra("studyTask");
        if (notification != null) {
            taskId = notification.getFori_taskId();
        } else if (studyTask != null) {
            taskId = studyTask.getTaskId();
        } else {
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
        commitButton = findViewById(R.id.studyTask_commitButton);



        Handler getCommitHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if (status == GetStudyTaskInfos.NETWORK_FAILURE) {
                    InternetToasts.NoInternetToast(studyTaskInfoActivity);
                } else {
                    if (msg.obj == null) {
                        commitWindow.setVisibility(View.GONE);
                        isCommitted = false;
                    } else {
                        commitWindow.setVisibility(View.VISIBLE);
                        CommitEntity commit = (CommitEntity) msg.obj;
                        commitTime.setText(Final.format.format(commit.getUploadTime()));
                        if (commit.getResult() == null)
                            commitScore.setText("未评分");
                        else
                            commitScore.setText(String.valueOf(commit.getResult()));
                        isCommitted = true;
                    }
                }
                return true;
            }
        });

        handler = new Handler(new Handler.Callback() {
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
                    startDatetime = studyTask.getStartTime();
                    ddlDatetime = studyTask.getEndTime();
                    Date now = new Date();
                    if (now.after(ddlDatetime) || now.before(startDatetime))
                        commitButton.setVisibility(View.GONE);

                    GetCommit.getInstance().getCommit(getCommitHandler, Variable.currentUser.getUid(), taskId);
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        GetStudyTaskInfos.getInstance().getStudyTaskInfo(handler, taskId);
    }

    public void Commit(View view) {
        if (isCommitted)
            return;
        Date now = new Date();
        if (now.after(ddlDatetime) || now.before(startDatetime)) // 未开始、过期作业不允许提交
            commitButton.setVisibility(View.GONE);

        Intent intent = new Intent(studyTaskInfoActivity, CommitActivity.class);
        intent.putExtra("taskId", taskId);
        startActivity(intent);
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
                    Log.e("dir: ", studyTaskInfoActivity.getFilesDir().getAbsolutePath());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File filePointer = (File) msg.obj;
                    Uri fileUri;
                    //Android 7.0之后，分享文件需要授予临时访问权限
                    fileUri = FileProvider.getUriForFile(studyTaskInfoActivity, "com.fxxkywcx.app.fileprovider", filePointer);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//给目标文件临时授权
                    //intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//系统会检查当前所有已创建的Task中是否有该要启动的Activity的Task;
                    // 若有，则在该Task上创建Activity；若没有则新建具有该Activity属性的Task，并在该新建的Task上创建Activity。
                    intent.setDataAndType(fileUri, getContentResolver().getType(fileUri));
                    startActivity(intent);
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

                    InternetToasts.DownloadSuccessToast(StudyTaskInfoActivity.this);
                    StoreStudyTaskFile.getInstance(studyTaskInfoActivity).storeStudyTaskFile(storeHandler, fileName, file);
                }

                return true;
            }
        });

        DownloadStudyTaskFile.getInstance().DownloadFile(downloadHandler, taskId);
    }
}