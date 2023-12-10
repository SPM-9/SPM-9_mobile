package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import com.fxxkywcx.nostudy.entity.AnnouncementEntity;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class UploadAnnouncement extends NetworkPackage {
    private final static UploadAnnouncement instance = new UploadAnnouncement();
    private final String url = servletUrl + "/UploadAnnouncement";
    private final String TAG = "UploadAnnouncement";
    private UploadAnnouncement() {}
    public static UploadAnnouncement getInstance() {
        return instance;
    }

    public void uploadAnnouncement(Handler handler, AnnouncementEntity announcement, int teacherId) {
        FormBody body = new FormBody.Builder()
                .add("teacherId", String.valueOf(teacherId))
                .add("title", announcement.getTitle())
                .add("body", announcement.getBody())
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

                handler.sendMessage(msg);
            }
        });
    }
}
