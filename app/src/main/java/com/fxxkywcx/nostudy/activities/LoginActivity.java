package com.fxxkywcx.nostudy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.fxxkywcx.nostudy.MainActivity;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.Variable;
import com.fxxkywcx.nostudy.entity.UserEntity;
import com.fxxkywcx.nostudy.file_io.FileIO;
import com.fxxkywcx.nostudy.file_io.SaveReadUserInfo;
import com.fxxkywcx.nostudy.network.LoginRegister;
import com.fxxkywcx.nostudy.network.NetworkPackage;
import com.fxxkywcx.nostudy.utils.IOToasts;
import com.fxxkywcx.nostudy.utils.InternetToasts;
import com.fxxkywcx.nostudy.utils.LoginRegisterViews;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";
    private final LoginActivity loginActivity;
    public LoginActivity() {
        loginActivity = LoginActivity.this;
    }
    EditText userName;
    EditText password;
    TextView forgetPassword;
    CheckBox agree;
    CheckBox autoLogin;
    AlertDialog waiting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.login_userName);
        password = findViewById(R.id.login_password);
        forgetPassword = findViewById(R.id.login_forgetPassword);
        agree = findViewById(R.id.login_agree);
        autoLogin = findViewById(R.id.login_autoLogin);

        SaveReadUserInfo.getInstance(loginActivity).ReadUserLoginInfo(readAutoLoginHandler);
    }

    Handler readAutoLoginHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            int status = msg.arg2;
            int hasData = msg.arg1;
            if (status == FileIO.IO_ERROR) {
                IOToasts.IOFailedToast(loginActivity);
            } else {
                if (hasData == SaveReadUserInfo.HAS_AUTOLOGIN_DATA) {
                    UserEntity user = (UserEntity) msg.obj;
                    userName.setText(user.getUserName());
                    password.setText(user.getPassword());
                    agree.setChecked(true);
                    autoLogin.setChecked(false);
                    Login(agree); // 相当于自动点击了“登录”
                }
            }

            return true;
        }
    });

    Handler saveAutoLoginHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            int status = msg.arg2;
            if (status == FileIO.IO_ERROR) {
                IOToasts.IOFailedToast(loginActivity);
            }

            return true;
        }
    });

    Handler loginHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            int status = msg.arg2;
            int loginStatus = msg.arg1;
            if (status == NetworkPackage.NETWORK_FAILURE) {
                waiting.cancel();
                InternetToasts.NoInternetToast(loginActivity);
            } else {
                if (loginStatus == LoginRegister.LOGIN_FAILED) {
                    waiting.cancel();
                    LoginRegisterViews.UsernamePasswordWrong(loginActivity);
                } else {
                    Variable.currentUser = (UserEntity) msg.obj;
                    Log.e(TAG, Variable.currentUser.toString());

                    if (autoLogin.isChecked())
                        SaveReadUserInfo.getInstance(loginActivity)
                                .SaveUserLoginInfo(saveAutoLoginHandler, Variable.currentUser);

                    waiting.cancel();
                    LoginRegisterViews.LoginSuccess(loginActivity);

                    startActivity(new Intent(loginActivity, MainActivity.class));
                    finish();
                }
            }
            return true;
        }
    });

    public void Login(View view) {
        if (CheckReadyLogin() != VERIFIED) {
            return;
        }
        UserEntity user = new UserEntity();
        user.setUserName(userName.getText().toString());
        user.setPassword(password.getText().toString());

        waiting = LoginRegisterViews.getWaitingAlert(loginActivity)
                .show();

        LoginRegister.getInstance().login(loginHandler, user);
    }

    private final int VERIFIED = 0;
    private final int USER_BLANK = -1;
    private final int PASSWORD_BLANK = -2;
    private final int AGREE_BLANK = -3;

    private int CheckReadyLogin() {
        if (userName.getText().toString().trim().isEmpty()) {
            LoginRegisterViews.getBlankCommitAlert(loginActivity, "用户名不能为空")
                    .show();
            return USER_BLANK;
        }
        if (password.getText().toString().trim().isEmpty()) {
            LoginRegisterViews.getBlankCommitAlert(loginActivity, "密码不能为空")
                    .show();
            return PASSWORD_BLANK;
        }
        if (!agree.isChecked()) {
            LoginRegisterViews.getBlankCommitAlert(loginActivity, "请同意用户协议")
                    .show();
            return AGREE_BLANK;
        }
        return VERIFIED;
    }

    public void PopupAgreement(View view) {
        LoginRegisterViews.getAgreementAlert(loginActivity)
                .show();
    }

    public void toRegister(View view) {
        startActivity(new Intent(loginActivity, RegisterActivity.class));
    }

    public void ForgetPassword(View view) {
    }
}