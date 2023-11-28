package com.fxxkywcx.nostudy.ui.notification;

import android.content.Context;
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
    LinearLayout notifPad;
    TextView title;
    TextView body;
    TextView date;
    boolean dateVisible;
    Context context;
    public NotificationHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        context = itemView.getContext();
        dateVisible = true;
        notifItem = itemView.findViewById(R.id.notifItem_item);
        notifPad = itemView.findViewById(R.id.notifItem_itemPad);
        date = itemView.findViewById(R.id.notifItem_date);
        title = itemView.findViewById(R.id.notifItem_title);
        body = itemView.findViewById(R.id.notifItem_body);
    }
}
