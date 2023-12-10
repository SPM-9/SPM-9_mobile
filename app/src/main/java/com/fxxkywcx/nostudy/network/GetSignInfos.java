package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import com.fxxkywcx.nostudy.entity.StudyTaskEntity;
import com.fxxkywcx.nostudy.entity.UserSignEntity;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;


public class GetSignInfos extends NetworkPackage{
    private final static GetSignInfos instance = new GetSignInfos();
//    private final String url = servletUrl + "/GetSignInfos";
    private final String url = "http://121.41.1.13:8080/YiWangNoChangXue-1.0-SNAPSHOT/GetSignInfos";

    private GetSignInfos() {}
    public static GetSignInfos getInstance() {
        return instance;
    }

    //获取当前存在的签到
    public void getSignInfo(Handler handler, UserSignEntity userSign) {
        String userSignJson = gson.toJson(userSign);
        FormBody body = new FormBody.Builder()
                .add("userSign", userSignJson)
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
                    System.out.println("失败");
                }

                Message msg = Message.obtain();
                msg.arg2 = SUCCEED;
                if (response.body() != null) {
                    String json = response.body().string();
                    UserSignEntity signInfo = gson.fromJson(json, UserSignEntity.class);
                    msg.obj = signInfo;
                    //测试msg
                    System.out.println("成功，有返回值："+msg.obj);
                } else {
                    msg.obj = null;
                    System.out.println("成功，无返回值："+msg.obj);
                }

                handler.sendMessage(msg);
            }
        });

    }

    //签到是否成功
    public void isSuccuss(Handler handler, UserSignEntity userSign) {
        String userSignJson = gson.toJson(userSign);
        FormBody body = new FormBody.Builder()
                .add("userSign", userSignJson)
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
                    System.out.println("失败");
                }

                Message msg = Message.obtain();
                msg.arg2 = SUCCEED;
                if (response.body() != null) {
                    String json = response.body().string();
                    String isSuccess = gson.fromJson(json,String.class);
                    msg.obj = isSuccess;
                    //测试msg
                    System.out.println("成功，有返回值："+msg.obj);
                } else {
                    msg.obj = null;
                    System.out.println("成功，无返回值："+msg.obj);
                }
                handler.sendMessage(msg);
            }
        });

    }

}
