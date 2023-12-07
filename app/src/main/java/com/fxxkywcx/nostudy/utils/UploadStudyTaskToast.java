package com.fxxkywcx.nostudy.utils;

import android.content.Context;
import android.widget.Toast;

public class UploadStudyTaskToast {
    public static void NoStartTimeToast(Context context) {
        Toast.makeText(context, "请选择开始时间", Toast.LENGTH_SHORT)
                .show();
    }

    public static void NoEndTimeToast(Context context) {
        Toast.makeText(context, "请选择结束时间", Toast.LENGTH_SHORT)
                .show();
    }

    public static void StartLaterThanEnd(Context context) {
        Toast.makeText(context, "结束时间不能早于开始时间", Toast.LENGTH_SHORT)
                .show();
    }

    public static void TimeEarlyThanNow(Context context) {
        Toast.makeText(context, "所选时间已过", Toast.LENGTH_SHORT)
                .show();
    }

    public static void NoTitle(Context context) {
        Toast.makeText(context, "请填写标题", Toast.LENGTH_SHORT)
                .show();
    }

    public static void NoFile(Context context) {
        Toast.makeText(context, "请选择文件", Toast.LENGTH_SHORT)
                .show();
    }
}
