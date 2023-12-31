package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import com.fxxkywcx.nostudy.entity.ResourceEntity;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetResources extends NetworkPackage{
    private final static GetResources instance = new GetResources();
    private final String url = servletUrl + "/DownloadResource";
    private GetResources() {
        super();
    }
    public static GetResources getInstance() {
        return instance;
    }
    // what
    public static final int LAST = 0;
    public static final int PREVIOUS = 1;
    public static final int ALL = 2;

    // arg2
    public static final int NO_MORE = 2;

    private final String TAG = "GetResources";
    private static final int loadMoreCount = 2;
    private static final int firstLoadCount = 3;

    public void getResources(Handler handler) {
        FormBody body = new FormBody.Builder().add("sb","qunima")
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
                msg.what = LAST;
                msg.arg2 = NETWORK_FAILURE;
                msg.obj = null;
                handler.sendMessage(msg);
            }

            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String message = "Request Unsuccessful " + response.code();
                    onFailure(call, new IOException(message));
                    return;
                }
                List<ResourceEntity> respList;

                Message msg = Message.obtain();
                msg.what = LAST;
                msg.arg2 = SUCCEED;

                if (response.body() != null) {
                    String json = response.body().string();
                    respList = gson.fromJson(json, new TypeToken<List<ResourceEntity>>(){}.getType());
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


}

