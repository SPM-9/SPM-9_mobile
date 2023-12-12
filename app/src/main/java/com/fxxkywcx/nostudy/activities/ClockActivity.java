package com.fxxkywcx.nostudy.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.Variable;
import com.fxxkywcx.nostudy.entity.AnnouncementEntity;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import com.fxxkywcx.nostudy.entity.UserSignEntity;
import com.fxxkywcx.nostudy.entity.UserSignsEntity;
import com.fxxkywcx.nostudy.network.GetAnnouncementInfos;
import com.fxxkywcx.nostudy.network.GetSignInfos;
import com.fxxkywcx.nostudy.utils.InternetToasts;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ClockActivity extends AppCompatActivity {
    Button btnSign;
    TextView tvLocation;
    TextView tvStatus;
    private static Integer signId;
    private boolean hasSign = false;
    private boolean isSignSuccess = false;
    private final ClockActivity clockActivity;
    private final String TAG = "ClockActivity";


    public ClockActivity() {
        clockActivity = ClockActivity.this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        btnSign = findViewById(R.id.clock_btn_sign);
        tvLocation = findViewById(R.id.clock_tv_location);
        tvStatus = findViewById(R.id.clock_tv_status);

        //查看是否有签到
        refresh();
    }

    //移动端点击刷新
    public void sign(View view) {
        //*获取当前用户信息
//        Intent intent = getIntent();
//        UserSignEntity userSign = (UserSignEntity) intent.getSerializableExtra("userSign");
//        if (userSign == null) {
//            finish();
//            return;
//        }

        //获取签到
        UserSignEntity userSign = new UserSignEntity(signId,1,new Date(),null);
        //设置handler向服务器发起签到请求，查询是否签到成功
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                //根据响应状态执行动作
                if (status == GetAnnouncementInfos.NETWORK_FAILURE) {//未连接失败
                    InternetToasts.NoInternetToast(clockActivity);
                } else {
                    String isSuccess = (String) msg.obj;
                    if (isSuccess == null) {//连接成功但未获取到资源
                        InternetToasts.NoInternetToast(clockActivity);
                        return true;
                    } else {//获取成功
                        if (isSuccess.equals("ontime")) {//签到成功
                            tvStatus.setText("签到成功");
                            tvStatus.setVisibility(View.VISIBLE);
                            btnSign.setVisibility(View.INVISIBLE);
                        } else if (isSuccess.equals("overtime")) {//签到失败
                            tvStatus.setText("签到失败,重新刷新尝试");
                            tvStatus.setVisibility(View.VISIBLE);
                            btnSign.setVisibility(View.VISIBLE);
                        }else if (isSuccess.equals("alreadySign")){//已签到
                            tvStatus.setText("已签到，请勿重新签到");
                            tvStatus.setVisibility(View.VISIBLE);
                            btnSign.setVisibility(View.VISIBLE);
                        }
                    }
                }
                return true;
            }
        });
        GetSignInfos.getInstance().isSuccuss(handler, userSign);
    }

    //点击文本刷新
    public void refreshSign(View view) {
        refresh();

    }
    //刷新签到状态
    public void refresh() {
        //*获取用户信息

        UserSignEntity userSign = new UserSignEntity(null, 111, new Date(), null);

        //设置handler向服务器发起查询请求，查询当前是否有签到
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if (status == GetAnnouncementInfos.NETWORK_FAILURE) {
                    InternetToasts.NoInternetToast(clockActivity);
                } else {
                    List<UserSignsEntity> userSigns = (List<UserSignsEntity>) msg.obj;
                    if (userSigns == null||userSigns.isEmpty()) {//无签到
                        tvStatus.setText("暂无签到,点击刷新");
                        return true;
                    } else {//有签到
                        btnSign.setVisibility(View.VISIBLE);
                        //*目前只实现最先发起的签到
                        tvStatus.setText("签到id："+userSigns.get(0).getSignId().toString()+"\n签到结束时间："+userSigns.get(0).getEndTime().toString());
                        signId=userSigns.get(0).getSignId();
                        tvStatus.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }
        });
        GetSignInfos.getInstance().getSignInfo(handler, userSign);
        Log.e(TAG,":已发送请求2");

    }
}



    //请求是否有签到
//    public void verifyHasSign(){
//        Request request = new Request.Builder().url("https://httpbin.org/get?a=1&b=2").build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            }
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if(response.isSuccessful()){
//                    //获取数据
//                    hasSign = response.body()
//                }
//            }
//        });

//    }
    //提交签到
//    public void postSignInfor(UserSignEntity signEntity){
//        FormBody formBody = new FormBody.Builder()
//                .add("a","1").add("b","2").build();
//        Request request = new Request.Builder().url("https://httpbin.org/post").post(formBody).build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            }
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if(response.isSuccessful()){
//                    //获取数据
//                    hasSign = response.body()
//                    //签到成功
//                    if(){
//
//                    }else {//签到失败
//                        //设置“签到失败，请刷新”tv
//
//                    }
//                }
//            }
//        });
//
//
//    }
//
//
//}

//    @Override
//    protected void onResume() {
//        //判断数据库当前是否存在签到
//        if(hasSign){
//            //设置签到button可见
//            btnSign.setVisibility(View.VISIBLE);
//
//        }else if (){
//            //设置“暂无签到”tv可见
//            tvStatus.setText("暂无签到,点击刷新");
//            tvStatus.setVisibility(View.VISIBLE);
//        }else //已签到
//        //当前定位
//        super.onResume();
//    }

//签到