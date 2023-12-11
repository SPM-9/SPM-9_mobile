package com.fxxkywcx.nostudy.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.network.ChooseClass;
import com.fxxkywcx.nostudy.utils.InternetToasts;
import static com.fxxkywcx.nostudy.Variable.currentUser;

public class ChooseClassActivity extends AppCompatActivity {
    Button button;
    TextView showstate;
    TextView showwait;
    private final ChooseClassActivity chooseClassActivity;

    public ChooseClassActivity(ChooseClassActivity chooseClassActivity) {
        this.chooseClassActivity = chooseClassActivity;
    }

    public ChooseClassActivity() {
        this.chooseClassActivity = this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int uid=currentUser.getUid();

        setContentView(R.layout.activity_chooseclass);
        button=findViewById(R.id.chooseClass_button);
        showstate=findViewById(R.id.chooseClass_showstate);
        showwait=findViewById(R.id.chooseClass_showwait);


        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status1 = msg.arg2;
                int status2 = (int) msg.what;
                if (status1 ==ChooseClass.NETWORK_FAILURE) {
                    //未选课
                    if(status2 == uid){
                        Handler handler = new Handler(new Handler.Callback() {
                            @Override
                            public boolean handleMessage(@NonNull Message msg) {
                                int status1= msg.arg2;
                                int status2= (int) msg.what;
                                if (status1 ==ChooseClass.NETWORK_FAILURE) {
                                    if(status2 == uid){//未发送请求
                                        showstate.setText("选课状态：未选课，未发送选课请求");
                                        showwait.setText("");
                                    }else {//网络错误
                                        InternetToasts.NoInternetToast(chooseClassActivity);
                                    }
                                } else {//已发送请求
                                    showstate.setText("选课状态：未选课，已发送选课请求");
                                    showwait.setText("已请求选课,请耐心等待老师回复");
                                    button.setVisibility(View.INVISIBLE);
                                }
                                return true;
                            }
                        });

                        ChooseClass.getInstance().checkRequest(handler,uid);

                    }else {//网络错误
                        InternetToasts.NoInternetToast(chooseClassActivity);
                    }
                } else {//已选课
                    showstate.setText("选课状态：已选课");
                    showwait.setText("");
                    button.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });

        ChooseClass.getInstance().checkClass(handler, uid);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        int status1 = msg.arg2;
                        int status2 = (int) msg.what;
                        if (status1 ==ChooseClass.NETWORK_FAILURE) {
                            InternetToasts.NoInternetToast(chooseClassActivity);
                        }else {
                            InternetToasts.RequestSuccessToast(chooseClassActivity);
                            showstate.setText("选课状态：未选课");
                            showwait.setText("已请求选课,请耐心等待老师回复");
                            button.setVisibility(View.INVISIBLE);
                        }

                        return true;
                    }
                });

                ChooseClass.getInstance().selectRequest(handler, uid);
            }
        });
    }
}
