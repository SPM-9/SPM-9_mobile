package com.fxxkywcx.nostudy.activities.ResourceDownloadActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import com.fxxkywcx.nostudy.entity.ResourceItemEntity;
import com.fxxkywcx.nostudy.entity.StudyTaskEntity;
import com.fxxkywcx.nostudy.file_io.FileIO;
import com.fxxkywcx.nostudy.file_io.StoreStudyTaskFile;
import com.fxxkywcx.nostudy.network.DownloadStudyTaskFile;
import com.fxxkywcx.nostudy.network.GetAnnouncementInfos;
import com.fxxkywcx.nostudy.network.GetResources;
import com.fxxkywcx.nostudy.utils.IOToasts;
import com.fxxkywcx.nostudy.utils.InternetToasts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ResourceDownloadActivity extends AppCompatActivity {

    private List<ResourceItemEntity> data =new ArrayList<>();
    private  ResourceDownloadActivity resourceDownloadActivity;
    private ResourceDownloadAdaptor resourceDownloadAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_download);
        // 假设您已经使用 ResourceItemEntity 对象初始化了数据列表（data）。

        // 初始化 RecyclerView 并设置其布局管理器
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 使用数据初始化适配器
        resourceDownloadAdaptor = new ResourceDownloadAdaptor(data);

        // 为 RecyclerView 设置适配器
        recyclerView.setAdapter(resourceDownloadAdaptor);

        // 将数据填充到适配器中（您可能会从某个数据源获取数据）
        // resourceDownloadAdaptor.setData(yourDataList);

    }

}


