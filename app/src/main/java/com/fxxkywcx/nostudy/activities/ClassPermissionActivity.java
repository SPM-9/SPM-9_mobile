package com.fxxkywcx.nostudy.activities;

import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.network.ChooseClass;
import com.fxxkywcx.nostudy.network.ClassPermission;
import com.fxxkywcx.nostudy.utils.InternetToasts;
import static com.fxxkywcx.nostudy.Variable.currentTeacher;

public class ClassPermissionActivity extends AppCompatActivity {
    TextView inputtext1 ;
    EditText inputtext2 ;
    Button check_button;
    TextView showtext2;
    Button nopermit_button2;
    Button permit_button2;

    private final ClassPermissionActivity classPermissionActivity;
    public ClassPermissionActivity() {
        this.classPermissionActivity=this;
    }
    public ClassPermissionActivity(ClassPermissionActivity classPermissionActivity) {
        this.classPermissionActivity = classPermissionActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classpermission);

        inputtext1 = findViewById(R.id.inputtext1);
        inputtext2 = findViewById(R.id.inputtext2);
        check_button = findViewById(R.id.check_button);
        showtext2 = findViewById(R.id.showtext2);
        nopermit_button2= findViewById(R.id.nopermit_button2);
        permit_button2= findViewById(R.id.permit_button2);

        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int uid = Integer.parseInt(inputtext2.getText().toString());
                Handler handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        int status1= msg.arg2;
                        int status2= (int) msg.what;
                        if (status1 ==ChooseClass.NETWORK_FAILURE) {
                            if(status2 == uid){//未发送请求
                                InternetToasts.RequestFailureToast(classPermissionActivity);
                            }else {//网络错误
                                InternetToasts.NoInternetToast(classPermissionActivity);
                            }
                        } else {//已发送请求
                            inputtext1.setVisibility(View.INVISIBLE);
                            inputtext2.setVisibility(View.INVISIBLE);
                            check_button.setVisibility(View.INVISIBLE);
                            showtext2.setText("该学生已发送选课请求");
                            nopermit_button2.setVisibility(View.VISIBLE);
                            permit_button2.setVisibility(View.VISIBLE);
                            nopermit_button2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Handler handler = new Handler(new Handler.Callback() {
                                        @Override
                                        public boolean handleMessage(@NonNull Message msg) {
                                            int status1 = msg.arg2;
                                            int status2 = (int) msg.what;
                                            if (status1 == ChooseClass.NETWORK_FAILURE) {
                                                if (status2 == uid) {
                                                    InternetToasts.unAllowSuccessToast(classPermissionActivity);

                                                    //进入数据库修改user表的ischosenclass为false
                                                    Handler handler = new Handler(new Handler.Callback() {
                                                        @Override
                                                        public boolean handleMessage(@NonNull Message msg) {
                                                            int status1 = msg.arg2;
                                                            int status2 = (int)msg.what;
                                                            if (status1 ==ChooseClass.NETWORK_FAILURE) {
                                                                if(status2 != uid){
                                                                    InternetToasts.NoInternetToast(classPermissionActivity);
                                                                }
                                                            }
                                                            return true;
                                                        }
                                                    });

                                                    ClassPermission.getInstance().modifychooseclass(handler,uid,false);

                                                } else {
                                                    InternetToasts.NoInternetToast(classPermissionActivity);
                                                }
                                            }
                                            return true;
                                        }
                                    });

                                    ClassPermission.getInstance().classPermission(handler,currentTeacher.getTeacherId(),uid,false);
                                }
                            });

                            permit_button2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Handler handler = new Handler(new Handler.Callback() {
                                        @Override
                                        public boolean handleMessage(@NonNull Message msg) {
                                            int status1 = msg.arg2;
                                            if (status1 ==ChooseClass.NETWORK_FAILURE) {
                                                InternetToasts.NoInternetToast(classPermissionActivity);
                                            }else {
                                                InternetToasts.AllowSuccessToast(classPermissionActivity);

                                                //进入数据库修改user表的ischosenclass为true
                                                Handler handler = new Handler(new Handler.Callback() {
                                                    @Override
                                                    public boolean handleMessage(@NonNull Message msg) {
                                                        int status1 = msg.arg2;
                                                        if (status1 ==ChooseClass.NETWORK_FAILURE) {
                                                            InternetToasts.NoInternetToast(classPermissionActivity);
                                                        }
                                                        return true;
                                                    }
                                                });

                                                ClassPermission.getInstance().modifychooseclass(handler,uid,true);


                                            }

                                            return true;
                                        }
                                    });

                                    ClassPermission.getInstance().classPermission(handler,currentTeacher.getTeacherId(),uid,true);
                                }
                            });
                        }
                        return true;
                    }
                });

                ChooseClass.getInstance().checkRequest(handler,uid);
            }
        });
    }

}
