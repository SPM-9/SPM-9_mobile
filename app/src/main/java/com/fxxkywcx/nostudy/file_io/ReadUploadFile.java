package com.fxxkywcx.nostudy.file_io;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import androidx.documentfile.provider.DocumentFile;
import com.fxxkywcx.nostudy.entity.FileEntity;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class ReadUploadFile extends FileIO{
    private static ReadUploadFile instance;
    private Context context;
    private String TAG = "ReadUploadFile";
    private ReadUploadFile(Context context) {
        super(context);
        this.context = context;
    }
    public static ReadUploadFile getInstance(Context context) {
        if (instance == null)
            instance = new ReadUploadFile(context);
        return instance;
    }

    public void readUploadFile(Handler handler, String filePath) {
        File file = new File(filePath);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();

                BufferedInputStream bis = null;
                ByteArrayOutputStream baos = null;
                String fileName = null;
                byte[] fileByte = null;
                try {
                    fileName = file.getName();
                    bis = new BufferedInputStream(new FileInputStream(file));
                    baos = new ByteArrayOutputStream();
                    IOUtils.copy(bis, baos);
                    msg.arg2 = SUCCEED;
                } catch (IOException e) {
                    msg.arg2 = IO_ERROR;
                    e.printStackTrace();
                } finally {
                    try {
                        if (bis != null)
                            bis.close();
                    } catch (IOException e) {
                        msg.arg2 = IO_ERROR;
                        e.printStackTrace();
                    }
                    try {
                        if (baos != null)
                            baos.close();
                    } catch (IOException e) {
                        msg.arg2 = IO_ERROR;
                        e.printStackTrace();
                    }
                }
                if (msg.arg2 == SUCCEED) {
                    fileByte = baos.toByteArray();
                    FileEntity fileBean = new FileEntity(fileName, fileByte);
                    msg.obj = fileBean;
                }

                handler.sendMessage(msg);
            }
        }).start();
    }

    public void readUploadFile(Handler handler, Uri fileUri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();

                BufferedInputStream bis = null;
                ByteArrayOutputStream baos = null;
                String fileName = null;
                byte[] fileByte = null;
                try {
                    DocumentFile file = DocumentFile.fromSingleUri(context, fileUri);
                    fileName = file.getName();
                    InputStream is = context.getContentResolver().openInputStream(fileUri);
                    bis = new BufferedInputStream(is);
                    baos = new ByteArrayOutputStream();
                    IOUtils.copy(bis, baos);
                    msg.arg2 = SUCCEED;
                } catch (IOException e) {
                    msg.arg2 = IO_ERROR;
                    e.printStackTrace();
                } finally {
                    try {
                        if (bis != null)
                            bis.close();
                    } catch (IOException e) {
                        msg.arg2 = IO_ERROR;
                        e.printStackTrace();
                    }
                    try {
                        if (baos != null)
                            baos.close();
                    } catch (IOException e) {
                        msg.arg2 = IO_ERROR;
                        e.printStackTrace();
                    }
                }
                if (msg.arg2 == SUCCEED) {
                    fileByte = baos.toByteArray();
                    FileEntity fileBean = new FileEntity(fileName, fileByte);
                    msg.obj = fileBean;
                }

                handler.sendMessage(msg);
            }
        }).start();
    }
}
