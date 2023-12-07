package com.fxxkywcx.nostudy.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.Variable;
import com.fxxkywcx.nostudy.entity.FileEntity;
import com.fxxkywcx.nostudy.entity.ResourceEntity;
import com.fxxkywcx.nostudy.entity.StudyTaskEntity;
import com.fxxkywcx.nostudy.file_io.FileIO;
import com.fxxkywcx.nostudy.file_io.ReadUploadFile;
import com.fxxkywcx.nostudy.network.GetAnnouncementInfos;
import com.fxxkywcx.nostudy.network.UploadResource;
import com.fxxkywcx.nostudy.network.UploadStudyTask;
import com.fxxkywcx.nostudy.utils.FileUtils;
import com.fxxkywcx.nostudy.utils.IOToasts;
import com.fxxkywcx.nostudy.utils.InternetToasts;
import com.fxxkywcx.nostudy.utils.UploadStudyTaskToast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UploadResourceActivity extends AppCompatActivity {
    private UploadResourceActivity uploadResourceActivity;
    public UploadResourceActivity() {
        this.uploadResourceActivity = UploadResourceActivity.this;
    }
    private final int SELECT_PICTURE = 1;
    private final int SELECT_FILE = 2;
    private final String TAG = "UploadResourceActivity";
    private RelativeLayout fileWindow;
    private TextView fileName;
    private TextView fileSize;
    private FileEntity uploadFile = null;
    private EditText title;
    private EditText body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_resource);

        fileWindow = findViewById(R.id.uploadResource_file);
        fileName = findViewById(R.id.uploadResource_fileName);
        fileSize = findViewById(R.id.uploadResource_fileSize);
        title = findViewById(R.id.uploadResource_title);
        body = findViewById(R.id.uploadResource_body);

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

    public void Upload(View view) {
        if (title.getText().toString().isEmpty()) {
            UploadStudyTaskToast.NoTitle(uploadResourceActivity);
            return;
        }
        if (fileWindow.getVisibility() == View.GONE) {
            UploadStudyTaskToast.NoFile(uploadResourceActivity);
            return;
        }

        ResourceEntity resource = new ResourceEntity();
        resource.setTitle(title.getText().toString());
        resource.setBody(body.getText().toString());
        resource.setFileName(uploadFile.getFileName());
        resource.setFile(uploadFile.getFile());

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if (status == GetAnnouncementInfos.NETWORK_FAILURE) {
                    InternetToasts.NoInternetToast(uploadResourceActivity);
                } else {
                    InternetToasts.UploadSuccessToast(uploadResourceActivity);
                    finish();
                }

                return true;
            }
        });

        UploadResource.getInstance().uploadResource(handler, resource);
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
                    IOToasts.ReadFailedToast(uploadResourceActivity);
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

            ReadUploadFile.getInstance(uploadResourceActivity).readUploadFile(handler, fileUri);
        }
    }

    private void PermissionGrant() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                !Environment.isExternalStorageManager()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(uploadResourceActivity)
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