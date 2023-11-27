package com.fxxkywcx.nostudy.ui.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.activities.NotificationInfoActivity;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import com.fxxkywcx.nostudy.utils.InternetUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationHolder>{

    List<NotificationEntity> notifList = new ArrayList<>(114);

    public NotificationsListAdapter(List<NotificationEntity> notifList) {
        this.notifList = notifList;
    }

    // 创建条目：
    @NonNull
    @NotNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, null);
        NotificationHolder notifHolder = new NotificationHolder(view);
        notifHolder.notifItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                int position = notifHolder.getAdapterPosition();
                NotificationEntity notification = notifList.get(position);

                Intent startNotifInfoActivity = new Intent(context, NotificationInfoActivity.class);
                startNotifInfoActivity.putExtra("notification", notification);
                context.startActivity(startNotifInfoActivity);
            }
        });
        return notifHolder;
    }

    // 为条目绑定数据
    @Override
    public void onBindViewHolder(@NonNull @NotNull NotificationHolder holder, int position) {
        NotificationEntity notification = notifList.get(position);

        holder.title.setText(notification.getTitle());
        holder.body.setText(notification.getBody());
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }
}
