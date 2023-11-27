package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import com.fxxkywcx.nostudy.utils.InternetUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetNotifications {
    private final static GetNotifications instance = new GetNotifications();
    private static final String url = "http://" + Final.tomcatSocket + "/YiWangNoChangXue" + "/GetNotification";
    private GetNotifications() {}
    public static GetNotifications getInstance() {
        return instance;
    }
    // what
    public static final int LAST = 0;
    public static final int PREVIOUS = 1;
    public static final int ALL = 2;

    // arg2
    public static final int SUCCEED = 0;
    public static final int NETWORK_FAILURE = 1;
    public static final int NO_MORE = 2;
    private final String TAG = "GetNotifications";
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    public static final int loadMoreCount = 2;
    public static final int firstLoadCount = 3;

    public void getLastNotif(Handler handler) {
        OkHttpClient okp = InternetUtils.okpClient;

        FormBody body = new FormBody.Builder()
                .add("operation", "getLast")
                .add("refreshCount", String.valueOf(firstLoadCount))
                .build();
        Request req = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = okp.newCall(req);


        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message msg = Message.obtain();
                msg.what = LAST;
                msg.arg2 = NETWORK_FAILURE;
                msg.obj = null;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    onFailure(call, new IOException());
                    return;
                }
                List<NotificationEntity> respList;

                Message msg = Message.obtain();
                msg.what = LAST;
                msg.arg2 = SUCCEED;

                if (response.body() != null) {
                    String json = response.body().string();
                    respList = gson.fromJson(json, new TypeToken<List<NotificationEntity>>(){}.getType());
                    msg.obj = respList;
                    Log.e(TAG, json);
                }
                else {
                    respList = new ArrayList<>();
                    msg.obj = respList;
                }
                // 是否全部加载完毕
                if (respList.size() != firstLoadCount) // 已加载完毕
                    msg.arg2 = NO_MORE;

                handler.sendMessage(msg);
            }
        });
    }

    public void getPreviousNotif(Handler handler, int index) {
        OkHttpClient okp = InternetUtils.okpClient;

        FormBody body = new FormBody.Builder()
                .add("operation", "getPrevious")
                .add("lastIndex", String.valueOf(index))
                .add("refreshCount", String.valueOf(loadMoreCount))
                .build();
        Request req = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = okp.newCall(req);


        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message msg = Message.obtain();
                msg.what = PREVIOUS;
                msg.arg2 = NETWORK_FAILURE;
                msg.obj = null;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    onFailure(call, new IOException());
                    return;
                }
                List<NotificationEntity> respList;

                Message msg = Message.obtain();
                msg.what = PREVIOUS;
                msg.arg2 = SUCCEED;
                if (response.body() != null) {
                    String json = response.body().string();
                    respList = gson.fromJson(json, new TypeToken<List<NotificationEntity>>(){}.getType());
                    msg.obj = respList;
                } else {
                    respList = new ArrayList<>();
                    msg.obj = respList;
                }
                // 是否全部加载完毕
                if (respList.size() != loadMoreCount) // 已加载完毕
                    msg.arg2 = NO_MORE;
                handler.sendMessage(msg);
            }
        });
    }
}
