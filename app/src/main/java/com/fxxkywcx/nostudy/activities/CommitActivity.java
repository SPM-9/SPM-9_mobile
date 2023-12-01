package com.fxxkywcx.nostudy.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.CommitEntity;
import com.fxxkywcx.nostudy.entity.FileEntity;
import com.fxxkywcx.nostudy.file_io.FileIO;
import com.fxxkywcx.nostudy.file_io.ReadUploadFile;
import com.fxxkywcx.nostudy.network.GetAnnouncementInfos;
import com.fxxkywcx.nostudy.network.UploadCommit;
import com.fxxkywcx.nostudy.utils.FileUtils;
import com.fxxkywcx.nostudy.utils.IOToasts;
import com.fxxkywcx.nostudy.utils.InternetToasts;

public class CommitActivity extends AppCompatActivity {
    private CommitActivity commitActivity;
    public CommitActivity() {
        this.commitActivity = CommitActivity.this;
    }
    private final int SELECT_PICTURE = 1;
    private final int SELECT_FILE = 2;
    private final String TAG = "CommitActivity";
    private RelativeLayout fileWindow;
    private TextView fileName;
    private TextView fileSize;
    private FileEntity uploadFile = null;
    private EditText body;
    private int taskId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit);

        Intent intent = getIntent();
        taskId = intent.getIntExtra("taskId", -1);
        if (taskId == -1) {
            finish();
            return;
        }

        fileWindow = findViewById(R.id.commit_file);
        fileName = findViewById(R.id.commit_fileName);
        fileSize = findViewById(R.id.commit_fileSize);
        body = findViewById(R.id.commit_body);

        fileWindow.setVisibility(View.GONE);
    }

    public void UploadPicture(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setDataAndType(, "image/*");
        startActivityForResult(intent, SELECT_PICTURE);
    }

    public void UploadFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    public void DeleteUploadFile(View view) {
        uploadFile = null;
        fileWindow.setVisibility(View.GONE);
    }

    public void Commit(View view) {
        String commitBody = body.getText().toString();
        CommitEntity commit = new CommitEntity();
        commit.setFori_taskId(taskId);
        commit.setBody(commitBody);
        if (uploadFile != null && uploadFile.getFileName() != null && uploadFile.getFile() != null) {
            commit.setFileName(uploadFile.getFileName());
            commit.setFile(uploadFile.getFile());
        }

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if (status == GetAnnouncementInfos.NETWORK_FAILURE) {
                    InternetToasts.NoInternetToast(commitActivity);
                } else {
                    InternetToasts.UploadSuccessToast(commitActivity);
                }

                finish();
                return true;
            }
        });

        // TODO: 2023/12/2 此处userId暂时设为1，等到账户系统做完之后再修改
        UploadCommit.getInstance().uploadCommit(handler, commit, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
            return;

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if (status == FileIO.IO_ERROR) {
                    IOToasts.ReadFailedToast(commitActivity);
                } else {
                    uploadFile = (FileEntity) msg.obj;
                    fileName.setText(uploadFile.getFileName());
                    fileSize.setText(FileUtils.sizeToString(uploadFile.getFile().length, 2));
                    fileWindow.setVisibility(View.VISIBLE);
                }

                return true;
            }
        });

        if (requestCode == SELECT_PICTURE || requestCode == SELECT_FILE) {
            Uri fileUri = data.getData();

            PermissionGrant();

            ReadUploadFile.getInstance(commitActivity).readUploadFile(handler, fileUri);
        }
    }

    private void PermissionGrant() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                !Environment.isExternalStorageManager()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(commitActivity)
                    .setMessage("本程序需要您同意允许访问所有文件权限")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                            startActivity(intent);
                        }
                    });
            builder.show();
        }
    }
}