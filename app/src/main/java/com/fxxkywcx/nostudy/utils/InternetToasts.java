package com.fxxkywcx.nostudy.utils;

import android.content.Context;
import android.widget.Toast;

public class InternetToasts {
    public static void DownloadFilesToast(Context context) {
        Toast.makeText(context, "正在下载文件", Toast.LENGTH_SHORT)
                .show();
    }

    public static void DownloadSuccessToast(Context context) {
        Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT)
                .show();
    }

    public static void DownloadFailedToast(Context context) {
        Toast.makeText(context, "下载失败", Toast.LENGTH_LONG)
                .show();
    }

    public static void NoInternetToast(Context context) {
        Toast.makeText(context, "请检查网络连接", Toast.LENGTH_LONG)
                .show();
    }

    public static void UploadSuccessToast(Context context) {
        Toast.makeText(context, "上传成功", Toast.LENGTH_LONG)
                .show();
    }

    public static void RequestSuccessToast(Context context) {
        Toast.makeText(context, "请求选课成功", Toast.LENGTH_LONG)
                .show();
    }

    public static void RequestFailureToast(Context context) {
        Toast.makeText(context, "该学生未发送选课请求", Toast.LENGTH_LONG)
                .show();
    }

    public static void AllowSuccessToast(Context context) {
        Toast.makeText(context, "审批成功", Toast.LENGTH_LONG)
                .show();
    }

    public static void unAllowSuccessToast(Context context) {
        Toast.makeText(context, "不审批成功", Toast.LENGTH_LONG)
                .show();
    }
}
