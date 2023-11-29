package com.fxxkywcx.nostudy.file_io;

import android.content.Context;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.*;

public class FileIO {
    public static String publicAppDir;
    public static final int SUCCEED = 0;
    public static final int IO_ERROR = 1;
    public FileIO(Context context) {
        publicAppDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();
        Log.e("DownloadDir", publicAppDir);
    }
}
