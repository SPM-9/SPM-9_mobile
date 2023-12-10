package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import com.fxxkywcx.nostudy.entity.CommitEntity;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class UploadCommit extends NetworkPackage{
    private final static UploadCommit instance = new UploadCommit();
    private final String url = servletUrl + "/UploadCommit";
    private final String TAG = "UploadCommit";
    private UploadCommit() {}
    public static UploadCommit getInstance() {
        return instance;
    }

    public void uploadCommit(Handler handler, CommitEntity commit, int userId) {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", String.valueOf(userId))
                .addFormDataPart("taskId", commit.getFori_taskId().toString())
                .addFormDataPart("body", commit.getBody());
        if (commit.getFileName() != null && commit.getFile() != null) {
            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody filePart = RequestBody.create(mediaType, commit.getFile());
            bodyBuilder.addFormDataPart("file", commit.getFileName(), filePart);
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
                }

                Message msg = Message.obtain();
                msg.arg2 = SUCCEED;

                handler.sendMessage(msg);
            }
        });
    }
}
