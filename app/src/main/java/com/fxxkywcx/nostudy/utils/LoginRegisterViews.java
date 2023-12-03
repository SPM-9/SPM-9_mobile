package com.fxxkywcx.nostudy.utils;

import android.content.Context;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.fxxkywcx.nostudy.R;

public class LoginRegisterViews {
    public static void UsernamePasswordWrong(Context context) {
        Toast.makeText(context, "用户名或密码错误", Toast.LENGTH_SHORT)
                .show();
    }

    public static void LoginSuccess(Context context) {
        Toast.makeText(context, "登录成功，正在跳转", Toast.LENGTH_SHORT)
                .show();
    }

    public static void DuplicateUsername(Context context) {
        Toast.makeText(context, "用户名重复，请重新输入", Toast.LENGTH_SHORT)
                .show();
    }

    public static void RegisterSuccess(Context context) {
        Toast.makeText(context, "注册成功，正在跳转", Toast.LENGTH_SHORT)
                .show();
    }

    public static AlertDialog.Builder getWaitingAlert(Context context) {
        return new androidx.appcompat.app.AlertDialog.Builder(context)
                .setView(R.layout.child_waiting_for_server)
                .setCancelable(false);
    }

    public static AlertDialog.Builder getAgreementAlert(Context context) {
        return new AlertDialog.Builder(context)
                .setMessage("114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514114514")
                .setTitle("用户协议");
    }

    public static AlertDialog.Builder getBlankCommitAlert(Context context, String message) {
        return new AlertDialog.Builder(context)
                .setMessage(message);
    }
}
