package com.fxxkywcx.nostudy.file_io;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class StoreStudyTaskFile extends FileIO{
    private static StoreStudyTaskFile instance;
    private StoreStudyTaskFile(Context context) {
        super(context);
    }
    public static StoreStudyTaskFile getInstance(Context context) {
        if (instance == null)
            instance = new StoreStudyTaskFile(context);
        return instance;
    }

    public void storeStudyTaskFile(Handler handler, String fileName, byte[] file) {
        String fileDir = publicAppDir + "/" + fileName;
        File downloadFile = new File(fileDir);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                try {
                    bis = new BufferedInputStream(new ByteArrayInputStream(file));
                    bos = new BufferedOutputStream(new FileOutputStream(downloadFile));
                    IOUtils.copy(bis, bos);
                    msg.arg2 = SUCCEED;
                    msg.obj = downloadFile;
                } catch (IOException e) {
                    msg.arg2 = IO_ERROR;
                    e.printStackTrace();
                } finally {
                    if (bis != null && bos != null) {
                        try {
                            bis.close();
                            bos.close();
                        } catch (IOException e) {
                            msg.arg2 = IO_ERROR;
                            e.printStackTrace();
                        }
                    }
                }

                handler.sendMessage(msg);
            }
        }).start();
    }
}
