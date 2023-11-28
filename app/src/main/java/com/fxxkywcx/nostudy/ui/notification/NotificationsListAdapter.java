package com.fxxkywcx.nostudy.ui.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import com.fxxkywcx.nostudy.utils.ViewUtils;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationHolder>{

    List<NotificationEntity> notifList = new ArrayList<>(114);

    public NotificationsListAdapter(List<NotificationEntity> notifList) {
        this.notifList = notifList;
    }
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    // 创建条目：
    @NonNull
    @NotNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        NotificationHolder notifHolder = new NotificationHolder(view);
        notifHolder.notifItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                int position = notifHolder.getAdapterPosition();
                NotificationEntity notification = notifList.get(position);

//                Intent startNotifInfoActivity = new Intent(context, NotificationInfoActivity.class);
//                startNotifInfoActivity.putExtra("notification", notification);
//                context.startActivity(startNotifInfoActivity);
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
        holder.date.setText(Final.format.format(notification.getUploadTime()));
        if (position != 0) {
            NotificationEntity previous = notifList.get(position-1);
            String previousTime = format.format(previous.getUploadTime());
            String thisItemTIme= format.format(notification.getUploadTime());
            if (previousTime.equals(thisItemTIme)) {
                holder.date.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.notifPad.getLayoutParams();
                params.setMargins(
                        ViewUtils.dp2px(holder.context, 20),
                        ViewUtils.dp2px(holder.context, 20),
                        ViewUtils.dp2px(holder.context, 20),
                        ViewUtils.dp2px(holder.context, 20)
                        );
                holder.notifPad.setLayoutParams(params);
            }
        }
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }
}
