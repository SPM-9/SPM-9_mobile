package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import com.fxxkywcx.nostudy.entity.AnnouncementEntity;
import com.fxxkywcx.nostudy.entity.TeacherEntity;
import com.fxxkywcx.nostudy.entity.UserEntity;
import com.fxxkywcx.nostudy.utils.LoginRegisterViews;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class LoginRegister extends NetworkPackage {
    private final static LoginRegister instance = new LoginRegister();
    private final String urlLogin = servletUrl + "/UserLogin";
    private final String urlRegister = servletUrl + "/UserRegister";
    private final String urlTeacherLogin = servletUrl + "/TeacherLogin";
    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAILED = 1;
    public static final int REGISTER_SUCCESS = 0;
    public static final int DUPLICATE_USERNAME = 1;
    public static final int USER_LOGIN = 0;
    public static final int TEACHER_LOGIN = 1;
    private LoginRegister() {}
    public static LoginRegister getInstance() {
        return instance;
    }

    public void login(Handler handler, UserEntity user) {
        FormBody body = new FormBody.Builder()
                .add("userName", user.getUserName())
                .add("password", user.getPassword())
                .build();
        Call call = okpClient.newCall(new Request.Builder()
                .url(urlLogin)
                .post(body)
                .build());
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
                    String respText = response.body().string();
                    if (respText.equals("false")) {
                        msg.arg1 = LOGIN_FAILED;
                        msg.obj = null;
                    } else {
                        msg.arg1 = LOGIN_SUCCESS;
                        msg.what = USER_LOGIN;
                        UserEntity user = gson.fromJson(respText, UserEntity.class);
                        msg.obj = user;
                    }
                } else {
                    msg.obj = null;
                }

                handler.sendMessage(msg);
            }
        });
    }
    public void register(Handler handler, UserEntity user) {
        FormBody body = new FormBody.Builder()
                .add("userName", user.getUserName())
                .add("password", user.getPassword())
                .add("userEmail", user.getEmail())
                .add("userNickName", user.getNickName())
                .build();
        Call call = okpClient.newCall(new Request.Builder()
                .url(urlRegister)
                .post(body)
                .build());
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
                msg.arg2 = SUCCEED;
                if (response.body() != null) {
                    String respText = response.body().string();
                    if (respText.equals("false")) {
                        msg.arg1 = DUPLICATE_USERNAME;
                    } else {
                        msg.arg1 = REGISTER_SUCCESS;
                    }
                }
                handler.sendMessage(msg);
            }
        });
    }

    public void teacherLogin(Handler handler, TeacherEntity teacher) {
        FormBody body = new FormBody.Builder()
                .add("userName", teacher.getUserName())
                .add("password", teacher.getPassword())
                .build();
        Call call = okpClient.newCall(new Request.Builder()
                .url(urlTeacherLogin)
                .post(body)
                .build());
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
                    String respText = response.body().string();
                    if (respText.equals("false")) {
                        msg.arg1 = LOGIN_FAILED;
                        msg.obj = null;
                    } else {
                        msg.arg1 = LOGIN_SUCCESS;
                        msg.what = TEACHER_LOGIN;
                        TeacherEntity user = gson.fromJson(respText, TeacherEntity.class);
                        msg.obj = user;
                    }
                } else {
                    msg.obj = null;
                }

                handler.sendMessage(msg);
            }
        });
    }

}
