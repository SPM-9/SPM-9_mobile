package com.fxxkywcx.nostudy.ui.home;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fxxkywcx.nostudy.R;
import org.jetbrains.annotations.NotNull;

public class ToDoHolder extends RecyclerView.ViewHolder {
    LinearLayout item;
    ImageView icon;
    TextView title;
    TextView date;
    private Context context;
    public ToDoHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        context = itemView.getContext();
        item = itemView.findViewById(R.id.todo_item);
        icon = itemView.findViewById(R.id.todo_img);
        title = itemView.findViewById(R.id.todo_title);
        date = itemView.findViewById(R.id.todo_time);
    }
}
