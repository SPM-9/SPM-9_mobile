package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import com.fxxkywcx.nostudy.entity.AnnouncementEntity;
import com.fxxkywcx.nostudy.entity.CommitEntity;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GetCommit extends NetworkPackage{
    private final static GetCommit instance = new GetCommit();
    private final String url = servletUrl + "/GetIsCommit";
    private final String TAG = "GetCommit";
    private GetCommit() {}
    public static GetCommit getInstance() {
        return instance;
    }

    public static final int NO_FILE = 2;
    public void getCommit(Handler handler, int userId, int taskId) {
        FormBody body = new FormBody.Builder()
                .add("userId", String.valueOf(userId))
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
                }

                Message msg = Message.obtain();
                if (response.body() != null) {
                    String json = response.body().string();
                    CommitEntity commit = gson.fromJson(json, CommitEntity.class);
                    msg.arg2 = SUCCEED;
                    msg.obj = commit;
                } else {
                    msg.arg2 = NO_FILE;
                    msg.obj = null;
                }

                handler.sendMessage(msg);
            }
        });
    }
}
