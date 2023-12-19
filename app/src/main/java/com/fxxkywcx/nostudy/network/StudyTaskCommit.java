package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import com.fxxkywcx.nostudy.entity.CommitEntity;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class StudyTaskCommit extends NetworkPackage{
    private final static StudyTaskCommit instance = new StudyTaskCommit();
    private final String url1 = servletUrl + "/GetStudyTaskCommit";
    private final String url2 = servletUrl + "/MarkCommit";
    private StudyTaskCommit() {}
    public static StudyTaskCommit getInstance() {
        return instance;
    }
    public void getStudyTaskCommit(Handler handler, int studyTaskId) {
        FormBody body = new FormBody.Builder()
                .add("studyTaskId", String.valueOf(studyTaskId))
                .build();
        Request req = new Request.Builder()
                .url(url1)
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
                if (response.body() != null) {
                    String json = response.body().string();
                    List<CommitEntity> commitList = gson.fromJson(json, new TypeToken<List<CommitEntity>>(){}.getType());
                    msg.obj = commitList;
                } else {
                    msg.obj = null;
                }

                handler.sendMessage(msg);
            }
        });
    }

    public void markCommit(Handler handler, int commitId, int score) {
        FormBody body = new FormBody.Builder()
                .add("commitId", String.valueOf(commitId))
                .add("result", String.valueOf(score))
                .build();
        Request req = new Request.Builder()
                .url(url2)
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
