package com.fxxkywcx.nostudy.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.fxxkywcx.nostudy.entity.ResourceEntity;
import com.fxxkywcx.nostudy.entity.StudyTaskEntity;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

public class DownloadResourceFile extends NetworkPackage{
    private final static DownloadResourceFile instance = new DownloadResourceFile();
    private final String url = servletUrl + "/GetResourceFile";
    private final String TAG = "GetResourceFile";
    private DownloadResourceFile() {}

    public static DownloadResourceFile getInstance() {
        return instance;
    }

    public void DownloadFile(Handler handler, int taskId) {
        FormBody body = new FormBody.Builder()
                .add("resourceId", String.valueOf(taskId))
                .build();
        Request req = new Request.Builder()
                .post(body)
                .url(url)
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
                String fileName = response.header("Content-Disposition");
                ResponseBody respBody = response.body();
                if (respBody != null && fileName != null) {
                    fileName = URLDecoder.decode(fileName, "UTF-8"); // 用UTF-8解码
                    fileName = fileName.split("attachment;filename=")[1]; // 删除前缀 attachment;filename=

                    Log.e(TAG, fileName);
                    // 流读取下载文件
                    InputStream is = response.body().byteStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    IOUtils.copy(is, baos);
                    byte[] file = baos.toByteArray();

                    ResourceEntity resource = new ResourceEntity();
                    resource.setFileName(fileName);
                    resource.setFile(file);

                    msg.obj = resource;
                } else {
                    msg.obj = null;
                }

                handler.sendMessage(msg);
            }
        });
    }
}
