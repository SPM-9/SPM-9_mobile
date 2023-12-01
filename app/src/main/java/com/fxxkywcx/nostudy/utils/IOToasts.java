package com.fxxkywcx.nostudy.utils;

import android.content.Context;
import android.widget.Toast;

public class IOToasts {
    public static void IOSuccessToast(Context context) {
        Toast.makeText(context, "保存成功", Toast.LENGTH_LONG)
                .show();
    }

    public static void IOFailedToast(Context context) {
        Toast.makeText(context, "保存失败", Toast.LENGTH_LONG)
                .show();
    }

    public static void ReadFailedToast(Context context) {
        Toast.makeText(context, "读取文件失败，请检查程序权限", Toast.LENGTH_LONG)
                .show();
    }
}
