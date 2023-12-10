package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.entity.StudyTaskEntity;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class UploadStudyTask extends NetworkPackage{
    private final static UploadStudyTask instance = new UploadStudyTask();
    private final String url = servletUrl + "/UploadStudyTask";
    private final String TAG = "UploadStudyTask";
    private UploadStudyTask() {}
    public static UploadStudyTask getInstance() {
        return instance;
    }

    public void uploadStudyTask(Handler handler, StudyTaskEntity studyTask) {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("teacherId", String.valueOf(studyTask.getFori_teacherId()))
                .addFormDataPart("taskType", String.valueOf(studyTask.getTaskType()))
                .addFormDataPart("title", studyTask.getTitle())
                .addFormDataPart("body", studyTask.getBody())
                .addFormDataPart("startTime", Final.format.format(studyTask.getStartTime()))
                .addFormDataPart("endTime", Final.format.format(studyTask.getEndTime()));
        if (studyTask.getFileName() != null && studyTask.getFile() != null) {
            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody filePart = RequestBody.create(mediaType, studyTask.getFile());
            bodyBuilder.addFormDataPart("file", studyTask.getFileName(), filePart);
        }
        MultipartBody body = bodyBuilder.build();

        Request req = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = okpClient.newCall(req);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message msg = Message.obtain();
                msg.arg2 = NETWORK_FAILURE;
                msg.obj = null;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String message = "Request Unsuccessful " + response.code();
                    onFailure(call, new IOException(message));
                    return;
                }

                Message msg = Message.obtain();
                msg.arg2 = SUCCEED;

                handler.sendMessage(msg);
            }
        });
    }
}
