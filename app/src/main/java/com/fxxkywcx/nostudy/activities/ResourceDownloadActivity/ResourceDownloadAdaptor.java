package com.fxxkywcx.nostudy.activities.ResourceDownloadActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.CommitEntity;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import com.fxxkywcx.nostudy.entity.ResourceEntity;
import com.fxxkywcx.nostudy.file_io.FileIO;
import com.fxxkywcx.nostudy.file_io.StoreDownloadFile;
import com.fxxkywcx.nostudy.network.DownloadCommitFile;
import com.fxxkywcx.nostudy.network.DownloadResourceFile;
import com.fxxkywcx.nostudy.network.GetAnnouncementInfos;
import com.fxxkywcx.nostudy.utils.FileUtils;
import com.fxxkywcx.nostudy.utils.IOToasts;
import com.fxxkywcx.nostudy.utils.InternetToasts;
import com.fxxkywcx.nostudy.utils.ViewUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResourceDownloadAdaptor extends RecyclerView.Adapter<ResourceDownloadAdaptor.ItemHolder>{

    private List<ResourceEntity> data=new ArrayList<>();

    public ResourceDownloadAdaptor() {
    }

    public ResourceDownloadAdaptor(List<ResourceEntity> data) {
        this.data = data;
    }
    String fileNameStr = null;
    byte[] fileByte = null;

    @NonNull
    @NotNull
    @Override
    public ResourceDownloadAdaptor.ItemHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_resource_download_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ResourceDownloadAdaptor.ItemHolder holder, int position) {
        ResourceEntity resourceItem = data.get(position);

        // 使用 ResourceItemEntity 中的数据填充 ViewHolder 中的视图
        // 例如：
        // holder.fileNameTextView.setText(item.getFilename());
        // holder.fileSizeTextView.setText(item.getFileSize());
        // ...

        // 设置点击监听器或执行其他必要的操作
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Handler storeHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        int status = msg.arg2;
                        if (status == FileIO.IO_ERROR) {
                            IOToasts.IOFailedToast(context);
                        } else {
                            Log.e("dir: ", context.getFilesDir().getAbsolutePath());
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            File filePointer = (File) msg.obj;
                            Uri fileUri;
                            //Android 7.0之后，分享文件需要授予临时访问权限
                            fileUri = FileProvider.getUriForFile(context, "com.fxxkywcx.app.fileprovider", filePointer);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//给目标文件临时授权
                            //intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//系统会检查当前所有已创建的Task中是否有该要启动的Activity的Task;
                            // 若有，则在该Task上创建Activity；若没有则新建具有该Activity属性的Task，并在该新建的Task上创建Activity。
                            intent.setDataAndType(fileUri, context.getContentResolver().getType(fileUri));
                            context.startActivity(intent);
                        }
                        return true;
                    }
                });
                Handler downloadHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        int status = msg.arg2;
                        if (status == GetAnnouncementInfos.NETWORK_FAILURE) {
                            InternetToasts.NoInternetToast(context);
                        } else {
                            ResourceEntity task = (ResourceEntity) msg.obj;
                            if (task == null)
                                return true;
                            fileNameStr = task.getFileName();
                            fileByte = task.getFile();

                            InternetToasts.DownloadSuccessToast(context);
                            StoreDownloadFile.getInstance(context).storeStudyTaskFile(storeHandler, fileNameStr, fileByte);
                        }

                        return true;
                    }
                });

                DownloadResourceFile.getInstance().DownloadFile(downloadHandler, resourceItem.getResId());
            }
        });
        // 绑定数据
        ResourceEntity resourceItemEntity=data.get(position);
        holder.textView_name.setText(resourceItemEntity.getFileName());
        holder.textView_size.setText(FileUtils.sizeToString(resourceItemEntity.getFileSize(), 2));
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private LinearLayoutCompat item;
        private TextView textView_name;
        private TextView textView_size;
        private ImageView imageView;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.resource_item);
            textView_name=itemView.findViewById(R.id.resource_name);
            textView_size=itemView.findViewById(R.id.resource_size);
        }
    }
}
