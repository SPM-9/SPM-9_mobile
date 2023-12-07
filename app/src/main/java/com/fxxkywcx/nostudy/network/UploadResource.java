package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import com.fxxkywcx.nostudy.entity.ResourceEntity;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class UploadResource extends NetworkPackage {
    private final static UploadResource instance = new UploadResource();
    private final String url = servletUrl + "/UploadResource";
    private final String TAG = "UploadResource";
    private UploadResource() {}
    public static UploadResource getInstance() {
        return instance;
    }

    public void uploadResource(Handler handler, ResourceEntity resource) {
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody filePart = RequestBody.create(mediaType, resource.getFile());

        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", resource.getTitle())
                .addFormDataPart("body", resource.getBody())
                .addFormDataPart("file", resource.getFileName(), filePart)
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
