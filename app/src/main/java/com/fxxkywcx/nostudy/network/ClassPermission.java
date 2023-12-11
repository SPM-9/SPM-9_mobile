package com.fxxkywcx.nostudy.network;
import android.os.Handler;
import android.os.Message;
import com.fxxkywcx.nostudy.Final;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
public class ClassPermission extends NetworkPackage{
    private final static ClassPermission instance = new ClassPermission();
    private final String url1 = "http://192.168.137.1:8080" + "/YiWangNoChangXue" + "/classpermission";
    private final String url2 = "http://192.168.137.1:8080" + "/YiWangNoChangXue" + "/modifychooseclass";

    private ClassPermission(){}

    public static ClassPermission getInstance() {
        return instance;
    }

    public void classPermission(Handler handler, int TeacherId,int uid,boolean isAccept){
        FormBody body = new FormBody.Builder()
                .add("TeacherId", String.valueOf(TeacherId))
                .add("isAccept",String.valueOf(isAccept))
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

    public void modifychooseclass(Handler handler,int uid,boolean ischosencourse){
        FormBody body = new FormBody.Builder()
                .add("ischosencourse",String.valueOf(ischosencourse))
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

}
