package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import com.fxxkywcx.nostudy.entity.StudyTaskEntity;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GetStudyTaskInfos extends NetworkPackage{
    private final static GetStudyTaskInfos instance = new GetStudyTaskInfos();
    private final String url = servletUrl + "/GetStudyTaskInfo";
    private GetStudyTaskInfos() {}
    public static GetStudyTaskInfos getInstance() {
        return instance;
    }

    public void getStudyTaskInfo(Handler handler, int taskId) {
        FormBody body = new FormBody.Builder()
                .add("taskId", String.valueOf(taskId))
                .build();
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
                if (response.body() != null) {
                    String json = response.body().string();
                    StudyTaskEntity task = gson.fromJson(json, StudyTaskEntity.class);
                    msg.obj = task;
                } else {
                    msg.obj = null;
                }

                handler.sendMessage(msg);
            }
        });
    }
}
