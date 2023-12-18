package com.fxxkywcx.nostudy.activities.ResourceDownloadActivity;

import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.activities.AnnouncementInfoActivity;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import com.fxxkywcx.nostudy.entity.ResourceEntity;
import com.fxxkywcx.nostudy.network.GetNotifications;
import com.fxxkywcx.nostudy.network.GetResources;
import com.fxxkywcx.nostudy.ui.notification.NotificationsListAdapter;
import com.fxxkywcx.nostudy.utils.InternetToasts;

import java.util.ArrayList;
import java.util.List;


public class ResourceDownloadActivity extends AppCompatActivity {

    private List<ResourceEntity> data =new ArrayList<>();
    private  ResourceDownloadActivity resourceDownloadActivity;
    public ResourceDownloadActivity() {
        resourceDownloadActivity = ResourceDownloadActivity.this;
    }
    ResourceDownloadAdaptor resourceDownloadAdaptor;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_download);
        // 假设您已经使用 ResourceItemEntity 对象初始化了数据列表（data）。


        // 初始化 RecyclerView 并设置其布局管理器
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 使用数据初始化适配器
        resourceDownloadAdaptor = new ResourceDownloadAdaptor(data);

        // 为 RecyclerView 设置适配器
        recyclerView.setAdapter(resourceDownloadAdaptor);

        // 将数据填充到适配器中（您可能会从某个数据源获取数据）

//         resourceDownloadAdaptor.setData(data);
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if (status == GetResources.NETWORK_FAILURE) {
                    InternetToasts.NoInternetToast(resourceDownloadActivity);
                } else {
                    List<ResourceEntity> respList = (List<ResourceEntity>) msg.obj;
                    data.clear();
                    data.addAll(respList);
                    resourceDownloadAdaptor.notifyDataSetChanged();
                    recyclerView.setAdapter(resourceDownloadAdaptor);
                }
                return true;
            }
        });
        GetResources.getInstance().getResources(handler);

    }

}


