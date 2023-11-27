package com.fxxkywcx.nostudy.ui.notification;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import org.jetbrains.annotations.NotNull;

public class NotificationHolder extends RecyclerView.ViewHolder {
    LinearLayout notifItem;
    TextView title;
    TextView body;
    public NotificationHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        notifItem = itemView.findViewById(R.id.notifItem_item);
        title = itemView.findViewById(R.id.notifItem_title);
        body = itemView.findViewById(R.id.notifItem_body);
    }
}
