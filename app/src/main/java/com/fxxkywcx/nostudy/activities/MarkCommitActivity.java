package com.fxxkywcx.nostudy.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.CommitEntity;
import com.fxxkywcx.nostudy.entity.StudyTaskEntity;
import com.fxxkywcx.nostudy.file_io.FileIO;
import com.fxxkywcx.nostudy.file_io.StoreDownloadFile;
import com.fxxkywcx.nostudy.network.DownloadCommitFile;
import com.fxxkywcx.nostudy.network.DownloadStudyTaskFile;
import com.fxxkywcx.nostudy.network.GetAnnouncementInfos;
import com.fxxkywcx.nostudy.network.StudyTaskCommit;
import com.fxxkywcx.nostudy.utils.FileUtils;
import com.fxxkywcx.nostudy.utils.IOToasts;
import com.fxxkywcx.nostudy.utils.InternetToasts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MarkCommitActivity extends AppCompatActivity {
    TextView name;
    TextView date;
    TextView body;
    NumberPicker hundred;
    NumberPicker ten;
    NumberPicker one;
    TextView current;
    TextView all;
    TextView fileName;
    TextView fileSize;
    RelativeLayout fileWindow;
    private final MarkCommitActivity markCommitActivity;
    public MarkCommitActivity() {
        markCommitActivity = MarkCommitActivity.this;
    }
    List<CommitEntity> studyTaskCommit = new ArrayList<>(114);
    int index = 0;
    String fileNameStr = null;
    byte[] fileByte = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_commit);

        Intent intent = getIntent();
        StudyTaskEntity studyTask = (StudyTaskEntity) intent.getSerializableExtra("studyTask");
        if (studyTask == null) {
            finish();
            return;
        }

        name = findViewById(R.id.announcement_name);
        body = findViewById(R.id.announcement_body);
        date = findViewById(R.id.announcement_dateTime);
        current = findViewById(R.id.current);
        all = findViewById(R.id.all);
        fileName = findViewById(R.id.commit_fileName);
        fileSize = findViewById(R.id.commit_fileSize);
        fileWindow = findViewById(R.id.commit_file);
        hundred = findViewById(R.id.announcement_hundred);
        ten = findViewById(R.id.announcement_ten);
        one = findViewById(R.id.announcement_one);
        hundred.setMinValue(0);
        hundred.setMaxValue(1);
        ten.setMinValue(0);
        ten.setMaxValue(9);
        one.setMinValue(0);
        one.setMaxValue(9);
        hundred.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (oldVal == 0 && newVal == 1) {
                    ten.setValue(0);
                    one.setValue(0);
                }
            }
        });

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if (status == GetAnnouncementInfos.NETWORK_FAILURE) {
                    InternetToasts.NoInternetToast(markCommitActivity);
                    finish();
                } else {
                    studyTaskCommit = (List<CommitEntity>) msg.obj;
                    if (studyTaskCommit == null || studyTaskCommit.isEmpty()) {
                        Toast.makeText(markCommitActivity, "当前任务无可批改内容", Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                    }
                    updateContent(0);
                    all.setText(String.valueOf(studyTaskCommit.size()));
                }
                return true;
            }
        });
        StudyTaskCommit.getInstance().getStudyTaskCommit(handler, studyTask.getTaskId());
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            int status = msg.arg2;
            if (status == GetAnnouncementInfos.NETWORK_FAILURE) {
                InternetToasts.NoInternetToast(markCommitActivity);
            } else {
                Toast.makeText(markCommitActivity, "提交成功", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    });
    public void next(View view) {
        int score = hundred.getValue()*100 + ten.getValue()*10 + one.getValue();
        CommitEntity com = studyTaskCommit.get(index);
        com.setResult(score);
        StudyTaskCommit.getInstance().markCommit(handler, com.getCommitId(), score);
        if (index == studyTaskCommit.size()-1) {
            Toast.makeText(markCommitActivity, "所有作业已批改完", Toast.LENGTH_SHORT).show();
        } else {
            index ++;
            updateContent(index);
        }

    }

    public void previous(View view) {
        int score = hundred.getValue()*100 + ten.getValue()*10 + one.getValue();
        CommitEntity com = studyTaskCommit.get(index);
        com.setResult(score);
        StudyTaskCommit.getInstance().markCommit(handler, com.getCommitId(), score);
        if (index == 0) {
            Toast.makeText(markCommitActivity, "已经是第一份提交了", Toast.LENGTH_SHORT).show();
        } else {
            index --;
            updateContent(index);
        }
    }

    private void updateContent(int index) {
        CommitEntity commit = studyTaskCommit.get(index);
        name.setText(String.valueOf(commit.getFori_userId()));
        date.setText(Final.format.format(commit.getUploadTime()));
        body.setText(commit.getBody());
        current.setText(String.valueOf(index+1));
        if (commit.getFileName() != null && commit.getFileSize() != null) {
            fileWindow.setVisibility(View.VISIBLE);
            fileName.setText(commit.getFileName());
            fileSize.setText(FileUtils.sizeToString(commit.getFileSize(), 2));
        } else {
            fileWindow.setVisibility(View.GONE);
        }
        if (commit.getResult() != null) {
            int savedScore = commit.getResult();
            one.setValue(savedScore % 10);
            savedScore /= 10;
            ten.setValue(savedScore % 10);
            savedScore /= 10;
            hundred.setValue(savedScore % 10);
        }
    }

    public void download(View view) {
        CommitEntity commit = studyTaskCommit.get(index);
        Handler storeHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if (status == FileIO.IO_ERROR) {
                    IOToasts.IOFailedToast(markCommitActivity);
                } else {
                    Log.e("dir: ", markCommitActivity.getFilesDir().getAbsolutePath());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File filePointer = (File) msg.obj;
                    Uri fileUri;
                    //Android 7.0之后，分享文件需要授予临时访问权限
                    fileUri = FileProvider.getUriForFile(markCommitActivity, "com.fxxkywcx.app.fileprovider", filePointer);
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
                    InternetToasts.NoInternetToast(markCommitActivity);
                } else {
                    CommitEntity task = (CommitEntity) msg.obj;
                    if (task == null)
                        return true;
                    fileNameStr = task.getFileName();
                    fileByte = task.getFile();

                    InternetToasts.DownloadSuccessToast(markCommitActivity);
                    StoreDownloadFile.getInstance(markCommitActivity).storeStudyTaskFile(storeHandler, fileNameStr, fileByte);
                }

                return true;
            }
        });

        DownloadCommitFile.getInstance().DownloadFile(downloadHandler, commit.getCommitId());
    }
}