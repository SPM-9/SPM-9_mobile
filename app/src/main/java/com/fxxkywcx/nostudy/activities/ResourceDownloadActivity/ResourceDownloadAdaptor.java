package com.fxxkywcx.nostudy.activities.ResourceDownloadActivity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.ResourceItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ResourceDownloadAdaptor extends RecyclerView.Adapter<ResourceDownloadAdaptor.ItemHolder>{

    private ArrayList<ResourceItem> items=new ArrayList<>();
    private Context context;

    public ResourceDownloadAdaptor() {
    }

    public ResourceDownloadAdaptor(ArrayList<ResourceItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ResourceDownloadAdaptor.ItemHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.activity_resource_download, null);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ResourceDownloadAdaptor.ItemHolder holder, int position) {
        holder.textView.setText(items.get(position).getTextView().getText());
    }

    @Override
    public int getItemCount() {
        return items==null?0:items.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.iv);
            textView=itemView.findViewById(R.id.tv);
        }
    }
}
