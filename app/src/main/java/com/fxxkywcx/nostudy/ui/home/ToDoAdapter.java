package com.fxxkywcx.nostudy.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.activities.StudyTaskInfoActivity;
import com.fxxkywcx.nostudy.entity.StudyTaskEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoHolder> {
    private List<StudyTaskEntity> toDoStudyTasks;
    private Context context;

    public ToDoAdapter(Context context, List<StudyTaskEntity> toDoStudyTasks) {
        this.toDoStudyTasks = toDoStudyTasks;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ToDoHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_todo_list, parent, false);
        ToDoHolder toDoHolder = new ToDoHolder(view);
        // 点击当前选项，跳转到对应的studytask界面
        toDoHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                int position = toDoHolder.getAdapterPosition();
                StudyTaskEntity homework = toDoStudyTasks.get(position);
                Intent jump = new Intent(context, StudyTaskInfoActivity.class);
                jump.putExtra("studyTask", homework);
                context.startActivity(jump);
            }
        });
        return toDoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ToDoHolder holder, int position) {
        StudyTaskEntity studyTask = toDoStudyTasks.get(position);
        holder.title.setText(studyTask.getTitle());
        holder.date.setText(Final.noSecondFormat.format(studyTask.getStartTime()));
    }

    @Override
    public int getItemCount() {
        return toDoStudyTasks.size();
    }
}
