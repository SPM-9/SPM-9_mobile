package com.fxxkywcx.nostudy.activities.ResourceDownloadActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.ResourceItemEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ResourceDownloadAdaptor extends RecyclerView.Adapter<ResourceDownloadAdaptor.ItemHolder>{

    private List<ResourceItemEntity> data;

    public ResourceDownloadAdaptor(List<ResourceItemEntity> data) {
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
        ResourceItemEntity item = data.get(position);

        // 使用 ResourceItemEntity 中的数据填充 ViewHolder 中的视图
        // 例如：
        // holder.fileNameTextView.setText(item.getFilename());
        // holder.fileSizeTextView.setText(item.getFileSize());
        // ...

        // 设置点击监听器或执行其他必要的操作
        // holder.itemView.setOnClickListener(v -> {
        //     // 处理项目点击事件
        // });
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.resource_name);
            textView=itemView.findViewById(R.id.resource_size);
        }
    }
}
