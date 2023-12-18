package com.fxxkywcx.nostudy.activities.ResourceDownloadActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import com.fxxkywcx.nostudy.entity.ResourceEntity;
import com.fxxkywcx.nostudy.utils.ViewUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ResourceDownloadAdaptor extends RecyclerView.Adapter<ResourceDownloadAdaptor.ItemHolder>{

    private List<ResourceEntity> data=new ArrayList<>();

    public ResourceDownloadAdaptor() {
    }

    public ResourceDownloadAdaptor(List<ResourceEntity> data) {
        this.data = data;
    }

    @NonNull
    @NotNull
    @Override
    public ResourceDownloadAdaptor.ItemHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_resource_download_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ResourceDownloadAdaptor.ItemHolder holder, int position) {
        ResourceEntity item = data.get(position);

        // 使用 ResourceItemEntity 中的数据填充 ViewHolder 中的视图
        // 例如：
        // holder.fileNameTextView.setText(item.getFilename());
        // holder.fileSizeTextView.setText(item.getFileSize());
        // ...

        // 设置点击监听器或执行其他必要的操作
         holder.setOnClickListener(v -> {
             // 处理项目点击事件
         });
        ResourceEntity resourceItemEntity=data.get(position);
        holder.textView_name.setText(resourceItemEntity.getFileName());
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView textView_name;
        private TextView textView_size;
        private ImageView imageView;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            textView_name=itemView.findViewById(R.id.resource_name);
            textView_size=itemView.findViewById(R.id.resource_size);
        }
    }
}
