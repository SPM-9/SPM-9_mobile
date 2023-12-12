package com.fxxkywcx.nostudy.activities.Homework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.HomeworkEntity;
import com.fxxkywcx.nostudy.network.GetHomeworks;
import com.fxxkywcx.nostudy.utils.InternetToasts;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class HomeworkActivity extends AppCompatActivity {
    RecyclerView homeworkList;
    HomeworkListAdapter adapter;
    List<HomeworkEntity> list=new ArrayList<>(45);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        homeworkList=findViewById(R.id.homework_notification_list);
        RefreshLayout refreshLayout=findViewById(R.id.homework_notification_refreshLayout);

        //设置下拉刷新的头部样式和上拉加载更多的底部样式
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(refreshListener);
        refreshLayout.setOnLoadMoreListener(loadMoreListener);

        adapter=new HomeworkListAdapter(list);
        homeworkList.setAdapter(adapter);

        // 添加五条
        Handler handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if(status== GetHomeworks.NETWORK_FAILURE){
                    //显示一个提示消息，提示用户当前没有网络连接
                    InternetToasts.NoInternetToast(HomeworkActivity.this);
                }else {
                    List<HomeworkEntity> respList=(List<HomeworkEntity>) msg.obj;
                    list.clear();
                    list.addAll(respList);
                    //通知适配器数据已经改变，需要更新UI界面
                    adapter.notifyDataSetChanged();
                    homeworkList.setAdapter(adapter);
                }
                return true;
            }
        });
        GetHomeworks.getInstance().getLastHomework(handler);
    }

    OnRefreshListener refreshListener=new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            list.clear();
            adapter.notifyDataSetChanged();
            refreshLayout.resetNoMoreData();
            Handler handler=new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    int status = msg.arg2;
                    if (status == GetHomeworks.NETWORK_FAILURE) {
                        InternetToasts.NoInternetToast(HomeworkActivity.this);
                        refreshLayout.finishRefresh(false);
                    } else {
                        List<HomeworkEntity> respList = (List<HomeworkEntity>) msg.obj;
                        list.addAll(respList);
                        adapter.notifyDataSetChanged();
                        homeworkList.setAdapter(adapter);
                        if (status == GetHomeworks.NO_MORE)
                            //结束下拉刷新并标记没有更多数据可加载
                            refreshLayout.finishRefreshWithNoMoreData();
                        else
                            //结束下拉刷新并通知刷新成功
                            refreshLayout.finishRefresh(true);
                    }
                    return true;
                }
            });
            GetHomeworks.getInstance().getLastHomework(handler);
        }
    };
    OnLoadMoreListener loadMoreListener=new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            Handler handler=new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    int status = msg.arg2;
                    if (status == GetHomeworks.NETWORK_FAILURE) {
                        InternetToasts.NoInternetToast(HomeworkActivity.this);
                        refreshLayout.finishRefresh(false);
                    } else {
                        List<HomeworkEntity> respList = (List<HomeworkEntity>) msg.obj;
                        list.addAll(respList);
                        adapter.notifyDataSetChanged();
                        homeworkList.setAdapter(adapter);
                        if (status == GetHomeworks.NO_MORE)
                            refreshLayout.finishRefreshWithNoMoreData();
                        else
                            refreshLayout.finishRefresh(true);
                    }
                    return true;
                }
            });
            int lastHomeNotifId=list.get(list.size()-1).getHomeworkId();
            GetHomeworks.getInstance().getPreviousHomework(handler,lastHomeNotifId);
        }
    };
}