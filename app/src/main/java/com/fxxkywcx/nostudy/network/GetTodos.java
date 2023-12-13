package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import com.fxxkywcx.nostudy.entity.StudyTaskEntity;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class GetTodos extends NetworkPackage {
    private final static GetTodos instance = new GetTodos();
    private final String url = servletUrl + "/GetTodos";
    private GetTodos() {}
    public static GetTodos getInstance() {
        return instance;
    }

    public void getTodos(Handler handler, int userId) {
        FormBody body = new FormBody.Builder()
                .add("userId", String.valueOf(userId))
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
                    List<StudyTaskEntity> task = gson.fromJson(json, new TypeToken<List<StudyTaskEntity>>(){}.getType());
                    msg.obj = task;
                } else {
                    msg.obj = null;
                }

                handler.sendMessage(msg);
            }
        });
    }
}
