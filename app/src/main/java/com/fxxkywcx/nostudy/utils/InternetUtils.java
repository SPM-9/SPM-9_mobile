package com.fxxkywcx.nostudy.utils;

import android.content.Context;
import android.widget.Toast;
import okhttp3.OkHttpClient;

public class InternetUtils {
    public static final OkHttpClient okpClient = new OkHttpClient();

    public static void NoInternetToast(Context context) {
        Toast.makeText(context, "请检查网络连接", Toast.LENGTH_LONG)
                .show();
    }
}
