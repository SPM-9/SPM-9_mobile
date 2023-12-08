package com.fxxkywcx.nostudy.activities.ResourceDownloadActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.ResourceItem;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class ResourceDownloadActivity extends AppCompatActivity {
    private ArrayList<ResourceItem> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_download);

        for (int i = 0; i < 1000; i++) {
            ResourceItem resourceItem = new ResourceItem();
//            resourceItem.getImageView().s
            resourceItem.getTextView().setText("文件i");
            data.add(resourceItem);
        }



        RecyclerView recycleView = findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(linearLayoutManager);

        ResourceDownloadAdaptor resourceDownloadAdaptor = new ResourceDownloadAdaptor(data,this);
        recycleView.setAdapter(resourceDownloadAdaptor);
    }
}