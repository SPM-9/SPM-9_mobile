package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import com.fxxkywcx.nostudy.entity.AnnouncementEntity;
import com.fxxkywcx.nostudy.entity.UserSignsEntity;
import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class UploadSign extends NetworkPackage {
    private final static UploadSign instance = new UploadSign();
    private final String url = servletUrl + "/postClock";
    private UploadSign() {}

    public static UploadSign getInstance() {
        return instance;
    }
    public void uploadSign(Handler handler, UserSignsEntity userSigns) {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        RequestBody body = RequestBody
                .create(JSON, gson.toJson(userSigns));
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
