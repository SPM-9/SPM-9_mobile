package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.fxxkywcx.nostudy.entity.UserSignEntity;
import com.fxxkywcx.nostudy.entity.UserSignsEntity;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


public class GetSignInfos extends NetworkPackage{
    private final static GetSignInfos instance = new GetSignInfos();
    private static final String TAG = "GetSignInfos";
    //    private final String url = servletUrl + "/GetSignInfos";
    private final String url = servletUrl + "/GetSignInfor";

    private GetSignInfos() {}
    public static GetSignInfos getInstance() {
        return instance;
    }

    //获取当前存在的签到
    public void getSignInfo(Handler handler, UserSignEntity userSign) {
        Log.e(TAG,":已发送请求1");

        String userSignJson = gson.toJson(userSign);
        Log.e(TAG,"GetSignInfor:"+userSignJson);
        FormBody body = new FormBody.Builder()
                .add("userSign", userSignJson).add("action","getSign")
                .build();
        Request req = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = okpClient.newCall(req);
        Log.e(TAG,":已发送请求2");

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message msg = Message.obtain();
                msg.arg2 = NETWORK_FAILURE;
                msg.obj = null;
                Log.e(TAG,"请求未连接");
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String message = "Request Unsuccessful " + response.code();
                    onFailure(call, new IOException(message));
                    Log.e(TAG,"请求没有收到应答"+message);
                }else {
                    Message msg = Message.obtain();
                    msg.arg2 = SUCCEED;
                    if (response.body() != null) {
                        String json = response.body().string();
                        Log.e(TAG,"收到的json数据："+json);
                        if (!json.isEmpty()){
                            Type type = new TypeToken<List<UserSignsEntity>>(){}.getType();
                            List<UserSignsEntity> signInfo = gson.fromJson(json, type);
                            msg.obj = signInfo;
                        }
                        //测试msg
                    } else {
                        msg.obj = null;
                    }

                    handler.sendMessage(msg);
                }
            }
        });

    }

    //签到是否成功
    public void isSuccuss(Handler handler, UserSignEntity userSign) {
        String userSignJson = gson.toJson(userSign);
        FormBody body = new FormBody.Builder()
                .add("userSign", userSignJson).add("action","submitSign")
                .build();
        Request req = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = okpClient.newCall(req);
        Log.e(TAG,":已发送签到请求2");

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message msg = Message.obtain();
                msg.arg2 = NETWORK_FAILURE;
                msg.obj = null;
                handler.sendMessage(msg);
                Log.e(TAG,":已发送签到请求2");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String message = "Request Unsuccessful " + response.code();
                    onFailure(call, new IOException(message));
                    Log.e(TAG,":已发送签到请求3");

                }

                Message msg = Message.obtain();
                msg.arg2 = SUCCEED;
                if (response.body() != null) {
                    String json = response.body().string();
                    String isSuccess = gson.fromJson(json,String.class);
                    msg.obj = isSuccess;
                    //测试msg

                    Log.e(TAG,":已发送签到请求4isSuccess："+isSuccess);
                } else {
                    msg.obj = null;
                    Log.e(TAG,":已发送签到请求5");

//                    System.out.println("成功，无返回值："+msg.obj);
                }
                handler.sendMessage(msg);
            }
        });

    }

}
