package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fxxkywcx.nostudy.entity.HomeworkEntity;
import com.google.gson.reflect.TypeToken;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class GetHomeworks extends NetworkPackage{
    private final static GetHomeworks instance=new GetHomeworks();
    private final String url = servletUrl + "/GetHomework";
    private GetHomeworks(){
        super();
    }

    public static GetHomeworks getInstance(){
        return instance;
    }
    public static final int LAST = 0;
    public static final int PREVIOUS = 1;
    public static final int ALL = 2;

    // arg2
    public static final int NO_MORE = 2;
    private final String TAG = "GetHomeworks";
    private static final int loadMoreCount = 2;
    private static final int firstLoadCount = 3;

    public void getLastHomework(Handler handler) {//获取最新的作业列表
        //构建表单请求体
        FormBody body = new FormBody.Builder()
                .add("operation", "getLast")
                .add("refreshCount", String.valueOf(firstLoadCount))
                .build();
        //构建请求
        Request req = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        //okpClient发起网络请求
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

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String message = "Request Unsuccessful " + response.code();
                    //网络请求失败时被调用
                    onFailure(call, new IOException(message));
                    return;
                }
                List<HomeworkEntity> respList;

                Message msg = Message.obtain();
                msg.what = LAST;
                msg.arg2 = SUCCEED;

                if (response.body() != null) {
                    String json = response.body().string();
                    respList = gson.fromJson(json, new TypeToken<List<HomeworkEntity>>(){}.getType());
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
    public void getPreviousHomework(android.os.Handler handler, int index) {
        FormBody body=new FormBody.Builder()
                .add("operation", "getPrevious")
                .add("lastIndex", String.valueOf(index))
                .add("refreshCount", String.valueOf(loadMoreCount))
                .build();
        Request req=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call=okpClient.newCall(req);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Message msg = Message.obtain();
                msg.what = PREVIOUS;
                msg.arg2 = NETWORK_FAILURE;
                msg.obj = null;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    onFailure(call, new IOException());
                    return;
                }
                List<HomeworkEntity> respList;

                Message msg = Message.obtain();
                msg.what = PREVIOUS;
                msg.arg2 = SUCCEED;
                if (response.body() != null) {
                    String json = response.body().string();
                    respList = gson.fromJson(json, new TypeToken<List<HomeworkEntity>>(){}.getType());
                    msg.obj = respList;
                }
                else {
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
