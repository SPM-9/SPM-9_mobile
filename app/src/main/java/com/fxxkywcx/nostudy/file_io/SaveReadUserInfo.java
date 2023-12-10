package com.fxxkywcx.nostudy.file_io;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import com.fxxkywcx.nostudy.entity.TeacherEntity;
import com.fxxkywcx.nostudy.entity.UserEntity;
import com.fxxkywcx.nostudy.network.LoginRegister;
import com.fxxkywcx.nostudy.utils.IOToasts;

public class SaveReadUserInfo extends FileIO{
    private static SaveReadUserInfo instance;
    private Context context;
    private SaveReadUserInfo(Context context) {
        super(context);
        this.context = context;
    }
    public static SaveReadUserInfo getInstance(Context context) {
        if (instance == null)
            instance = new SaveReadUserInfo(context);
        return instance;
    }

    public void SaveUserLoginInfo(Handler handler, String userName, String password, int userType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = context.getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("userType", userType);
                editor.putString("userName", userName);
                editor.putString("password", password);
                editor.commit();

                Message msg = Message.obtain();
                msg.arg2 = SUCCEED;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void DeleteUserLoginInfo(Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = context.getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();

                Message msg = Message.obtain();
                msg.arg2 = SUCCEED;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public static final int NO_AUTOLOGIN_DATA = 0;
    public static final int HAS_AUTOLOGIN_DATA = 1;

    public void ReadUserLoginInfo(Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.arg2 = SUCCEED;
                SharedPreferences sp = context.getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                if (sp.contains("userName") && sp.contains("password")) {
                    msg.arg1 = HAS_AUTOLOGIN_DATA;
                    int userType = sp.getInt("userType", LoginRegister.USER_LOGIN);
                    String userName = sp.getString("userName", null);
                    String password = sp.getString("password", null);
                    if (userType == LoginRegister.USER_LOGIN) {
                        UserEntity user = new UserEntity();
                        user.setUserName(userName);
                        user.setPassword(password);
                        msg.obj = user;
                    } else if (userType == LoginRegister.TEACHER_LOGIN) {
                        TeacherEntity teacher = new TeacherEntity();
                        teacher.setUserName(userName);
                        teacher.setPassword(password);
                        msg.obj = teacher;
                    }
                    msg.what = userType;
                } else {
                    msg.arg1 = NO_AUTOLOGIN_DATA;
                    msg.obj = null;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }
}
