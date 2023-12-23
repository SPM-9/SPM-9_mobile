package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import com.fxxkywcx.nostudy.entity.AnnouncementEntity;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GetAnnouncementInfos extends NetworkPackage{
    private final static GetAnnouncementInfos instance = new GetAnnouncementInfos();
    private final String url = servletUrl + "/GetAnnouncementInfo";
    private GetAnnouncementInfos() {}

    public static GetAnnouncementInfos getInstance() {
        return instance;
    }

    public void getAnnouncementInfo(Handler handler, int annId) {
        FormBody body = new FormBody.Builder()
                .add("annId", String.valueOf(annId))
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
                    AnnouncementEntity announcement = gson.fromJson(json, AnnouncementEntity.class);
                    msg.obj = announcement;
                } else {
                    msg.obj = null;
                }

                handler.sendMessage(msg);
            }
        });
    }
}
