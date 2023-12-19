package com.fxxkywcx.nostudy.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.Variable;
import com.fxxkywcx.nostudy.entity.CommitEntity;
import com.fxxkywcx.nostudy.entity.FileEntity;
import com.fxxkywcx.nostudy.entity.StudyTaskEntity;
import com.fxxkywcx.nostudy.file_io.FileIO;
import com.fxxkywcx.nostudy.file_io.ReadUploadFile;
import com.fxxkywcx.nostudy.network.GetAnnouncementInfos;
import com.fxxkywcx.nostudy.network.UploadCommit;
import com.fxxkywcx.nostudy.network.UploadStudyTask;
import com.fxxkywcx.nostudy.utils.FileUtils;
import com.fxxkywcx.nostudy.utils.IOToasts;
import com.fxxkywcx.nostudy.utils.InternetToasts;
import com.fxxkywcx.nostudy.utils.UploadStudyTaskToast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UploadStudyTaskActivity extends AppCompatActivity {
    private UploadStudyTaskActivity uploadStudyTaskActivity;
    public UploadStudyTaskActivity() {
        this.uploadStudyTaskActivity = UploadStudyTaskActivity.this;
    }
    private final int SELECT_PICTURE = 1;
    private final int SELECT_FILE = 2;
    private final String TAG = "UploadStudyTaskActivity";
    private RelativeLayout fileWindow;
    private TextView fileName;
    private TextView fileSize;
    private FileEntity uploadFile = null;
    private EditText title;
    private EditText body;
    private RadioGroup studyTaskType;
    private TextView startDate;
    private TextView startTime;
    private TextView endDate;
    private TextView endTime;
    DatePickerDialog startDatePicker;
    TimePickerDialog startTimePicker;
    DatePickerDialog endDatePicker;
    TimePickerDialog endTimePicker;
    Date startDatetime;
    Date endDatetime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_studytask);

        fileWindow = findViewById(R.id.uploadStudyTask_file);
        fileName = findViewById(R.id.uploadStudyTask_fileName);
        fileSize = findViewById(R.id.uploadStudyTask_fileSize);
        title = findViewById(R.id.uploadStudyTask_title);
        body = findViewById(R.id.uploadStudyTask_body);
        studyTaskType = findViewById(R.id.uploadStudyTask_type);
        startDate = findViewById(R.id.uploadStudyTask_startDate);
        startTime = findViewById(R.id.uploadStudyTask_startTime);
        endDate = findViewById(R.id.uploadStudyTask_endDate);
        endTime = findViewById(R.id.uploadStudyTask_endTime);

        fileWindow.setVisibility(View.GONE);

        startDatetime = new Date();
        endDatetime = new Date();

        startTimePicker = getTimePicker(startDatetime, startTime);
        startDatePicker = getDatePicker(startDatetime, startDate, startTimePicker);
        endTimePicker = getTimePicker(endDatetime, endTime);
        endDatePicker = getDatePicker(endDatetime, endDate, endTimePicker);
    }

    private DatePickerDialog getDatePicker(Date date, TextView textView, TimePickerDialog timePicker) {
        Calendar calendar = new GregorianCalendar();
        DatePickerDialog picker = new DatePickerDialog(
                uploadStudyTaskActivity,
                DatePickerDialog.THEME_DEVICE_DEFAULT_DARK,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setYear(year-1900);
                        date.setMonth(month);
                        date.setDate(dayOfMonth);
                        textView.setText(Final.dateFormat.format(date));
                        timePicker.show();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        return picker;
    }

    private TimePickerDialog getTimePicker(Date date, TextView textView) {
        Calendar calendar = new GregorianCalendar();
        TimePickerDialog picker = new TimePickerDialog(
                uploadStudyTaskActivity,
                DatePickerDialog.THEME_DEVICE_DEFAULT_DARK,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.setHours(hourOfDay);
                        date.setMinutes(minute);
                        date.setSeconds(0);
                        textView.setText(Final.timeFormat.format(date));
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        return picker;
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
        if (startDate.getText().toString().equals("请选择开始日期") ||
                startTime.getText().toString().equals("请选择开始时间")) {
            UploadStudyTaskToast.NoStartTimeToast(uploadStudyTaskActivity);
            return;
        }
        if (endDate.getText().toString().equals("请选择结束日期") ||
                endTime.getText().toString().equals("请选择结束时间")) {
            UploadStudyTaskToast.NoEndTimeToast(uploadStudyTaskActivity);
            return;
        }
        if (!startDatetime.before(endDatetime)) {
            UploadStudyTaskToast.StartLaterThanEnd(uploadStudyTaskActivity);
            return;
        }
        Date now = new Date(System.currentTimeMillis());
        if (endDatetime.before(now)) {
            UploadStudyTaskToast.TimeEarlyThanNow(uploadStudyTaskActivity);
            return;
        }
        if (title.getText().toString().isEmpty()) {
            UploadStudyTaskToast.NoTitle(uploadStudyTaskActivity);
            return;
        }

        RadioButton selectType = findViewById(studyTaskType.getCheckedRadioButtonId());
        String type = selectType.getText().toString();

        StudyTaskEntity studyTask = new StudyTaskEntity();
        studyTask.setFori_teacherId(Variable.currentTeacher.getTeacherId());
        studyTask.setTitle(title.getText().toString());
        studyTask.setBody(body.getText().toString());
        studyTask.setStartTime(startDatetime);
        studyTask.setEndTime(endDatetime);
        if (type.equals("作业模式"))
            studyTask.setTaskType(StudyTaskEntity.HOMEWORK);
        else if (type.equals("考试模式"))
            studyTask.setTaskType(StudyTaskEntity.EXAM);

        if (uploadFile != null && uploadFile.getFileName() != null && uploadFile.getFile() != null) {
            studyTask.setFileName(uploadFile.getFileName());
            studyTask.setFile(uploadFile.getFile());
        }

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if (status == GetAnnouncementInfos.NETWORK_FAILURE) {
                    InternetToasts.NoInternetToast(uploadStudyTaskActivity);
                } else {
                    InternetToasts.UploadSuccessToast(uploadStudyTaskActivity);
                    finish();
                }

                return true;
            }
        });

        UploadStudyTask.getInstance().uploadStudyTask(handler, studyTask);
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
                    IOToasts.ReadFailedToast(uploadStudyTaskActivity);
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

            ReadUploadFile.getInstance(uploadStudyTaskActivity).readUploadFile(handler, fileUri);
        }
    }

    private void PermissionGrant() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                !Environment.isExternalStorageManager()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(uploadStudyTaskActivity)
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

    public void setStartTime(View view) {
        startDatePicker.show();
    }

    public void setEndTime(View view) {
        endDatePicker.show();
    }
}