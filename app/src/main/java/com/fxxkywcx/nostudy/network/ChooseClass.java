package com.fxxkywcx.nostudy.network;
import android.os.Handler;
import android.os.Message;
import com.fxxkywcx.nostudy.Final;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

public class ChooseClass extends NetworkPackage {
    private final static ChooseClass instance = new ChooseClass();

    private final String url1 = servletUrl + "/checkClass";
    private final String url2 = servletUrl + "/checkRequest";
    private final String url3 = servletUrl + "/selectRequest";

    private ChooseClass() {}

    public static ChooseClass getInstance() {
        return instance;
    }

    public void checkClass(Handler handler, int uid) {
        FormBody body = new FormBody.Builder()
                .add("uid", String.valueOf(uid))
                .build();
        Request req = new Request.Builder()
                .url(url1)
                .post(body)
                .build();
        Call call = okpClient.newCall(req);

        call.enqueue(new Callback() {
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message msg = Message.obtain();
                msg.arg2 = NETWORK_FAILURE;
                handler.sendMessage(msg);
            }

            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("response.code:"+response.code());
                if (response.isSuccessful()) {
                    Message msg=Message.obtain();
                    msg.arg2=SUCCEED;
                    handler.sendMessage(msg);
                }else {
                    System.out.println("2:"+response.code());
                    Message msg=Message.obtain();
                    msg.arg2 = NETWORK_FAILURE;
                    msg.what = uid;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public void checkRequest(Handler handler, int uid) {
        FormBody body = new FormBody.Builder()
                .add("uid", String.valueOf(uid))
                .build();
        Request req = new Request.Builder()
                .url(url2)
                .post(body)
                .build();
        Call call = okpClient.newCall(req);

        call.enqueue(new Callback() {
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message msg = Message.obtain();
                msg.arg2 = NETWORK_FAILURE;
                handler.sendMessage(msg);
            }

            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Message msg=Message.obtain();
                    msg.arg2=SUCCEED;
                    handler.sendMessage(msg);
                }else {
                    Message msg=Message.obtain();
                    msg.arg2 = NETWORK_FAILURE;
                    msg.what = uid;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public void selectRequest(Handler handler, int uid) {
        FormBody body = new FormBody.Builder()
                .add("uid", String.valueOf(uid))
                .build();
        Request req = new Request.Builder()
                .url(url3)
                .post(body)
                .build();
        Call call = okpClient.newCall(req);

        call.enqueue(new Callback() {
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message msg = Message.obtain();
                msg.arg2 = NETWORK_FAILURE;
                handler.sendMessage(msg);
            }

            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Message msg=Message.obtain();
                    msg.arg2=SUCCEED;
                    handler.sendMessage(msg);
                }else {
                    Message msg=Message.obtain();
                    msg.arg2 = NETWORK_FAILURE;
                    handler.sendMessage(msg);
                }
            }
        });
    }
}
