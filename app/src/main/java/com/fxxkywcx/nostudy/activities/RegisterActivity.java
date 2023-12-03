package com.fxxkywcx.nostudy.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.UserEntity;
import com.fxxkywcx.nostudy.network.LoginRegister;
import com.fxxkywcx.nostudy.network.NetworkPackage;
import com.fxxkywcx.nostudy.utils.InternetToasts;
import com.fxxkywcx.nostudy.utils.LoginRegisterViews;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "Register";
    private final RegisterActivity registerActivity;
    public RegisterActivity() {
        registerActivity = RegisterActivity.this;
    }
    private EditText userName;
    private EditText email;
    private EditText password1;
    private EditText password2;
    private EditText nickName;
    private CheckBox agreement;
    AlertDialog waiting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.register_userName);
        password1 = findViewById(R.id.register_password1);
        password2 = findViewById(R.id.register_password2);
        email = findViewById(R.id.register_email);
        nickName = findViewById(R.id.register_nickname);
        agreement = findViewById(R.id.register_userAgreementCheckBox);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            int status = msg.arg2;
            int loginStatus = msg.arg1;
            if (status == NetworkPackage.NETWORK_FAILURE) {
                waiting.cancel();
                InternetToasts.NoInternetToast(registerActivity);
            } else {
                if (loginStatus == LoginRegister.LOGIN_FAILED) {
                    waiting.cancel();
                    LoginRegisterViews.DuplicateUsername(registerActivity);
                } else {
                    waiting.cancel();
                    LoginRegisterViews.RegisterSuccess(registerActivity);
                    finish();
                }
            }
            return true;
        }
    });

    private final int VERIFIED = 0;
    private final int USER_BLANK = -1;
    private final int PASSWORD_BLANK = -2;
    private final int AGREE_BLANK = -3;
    private final int NICKNAME_BLANK = -4;
    private final int EMAIL_BLANK = -5;
    private final int PASSWORD_NOT_DUPLICATE = -5;

    private int CheckReadyLogin() {
        if (userName.getText().toString().trim().isEmpty()) {
            LoginRegisterViews.getBlankCommitAlert(registerActivity, "用户名不能为空")
                    .show();
            return USER_BLANK;
        }
        if (password1.getText().toString().trim().isEmpty() ||
                password2.getText().toString().trim().isEmpty()) {
            LoginRegisterViews.getBlankCommitAlert(registerActivity, "密码不能为空")
                    .show();
            return PASSWORD_BLANK;
        }
        if (nickName.getText().toString().trim().isEmpty()) {
            LoginRegisterViews.getBlankCommitAlert(registerActivity, "昵称不能为空")
                    .show();
            return NICKNAME_BLANK;
        }
        if (email.getText().toString().trim().isEmpty()) {
            LoginRegisterViews.getBlankCommitAlert(registerActivity, "邮箱不能为空")
                    .show();
            return EMAIL_BLANK;
        }
        if (!password1.getText().toString().trim().equals(password2.getText().toString().trim())) {
            LoginRegisterViews.getBlankCommitAlert(registerActivity, "两次输入的密码不一致")
                    .show();
            return PASSWORD_NOT_DUPLICATE;
        }
        if (!agreement.isChecked()) {
            LoginRegisterViews.getBlankCommitAlert(registerActivity, "请同意用户协议")
                    .show();
            return AGREE_BLANK;
        }
        return VERIFIED;
    }

    public void PopupAgreement(View view) {
        LoginRegisterViews.getAgreementAlert(registerActivity)
                .show();
    }

    public void Register(View view) {
        if (CheckReadyLogin() != VERIFIED) {
            return;
        }

        UserEntity user = new UserEntity();
        user.setUserName(userName.getText().toString());
        user.setPassword(password1.getText().toString());
        user.setEmail(email.getText().toString());
        user.setNickName(nickName.getText().toString());

        waiting = LoginRegisterViews.getWaitingAlert(registerActivity)
                .show();

        LoginRegister.getInstance().register(handler, user);
    }
}
