package com.fxxkywcx.nostudy.activities.Exam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fxxkywcx.nostudy.Final;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.activities.StudyTaskInfoActivity;
import com.fxxkywcx.nostudy.entity.StudyTaskEntity;
import com.fxxkywcx.nostudy.utils.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExamListAdapter extends RecyclerView.Adapter<ExamHolder> {
    List<StudyTaskEntity> homeworkList=new ArrayList<>(114);

    public ExamListAdapter(List<StudyTaskEntity> homeworkList) {
        this.homeworkList = homeworkList;
    }

    private  final SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");


    @NonNull
    @Override
    public ExamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
        ExamHolder examHolder=new ExamHolder(view);
        //点击当前作业，跳转到对应详情页面
        examHolder.notifItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context=v.getContext();

                int position=examHolder.getAdapterPosition();
                StudyTaskEntity homework=homeworkList.get(position);
                Intent jump = new Intent(context, StudyTaskInfoActivity.class);
                jump.putExtra("studyTask", homework);
                context.startActivity(jump);
            }
        });
        return examHolder;
    }

    // 为条目绑定数据
    @Override
    public void onBindViewHolder(@NonNull ExamHolder holder, int position) {
        StudyTaskEntity homework=homeworkList.get(position);

        holder.title.setText(homework.getTitle());
        holder.body.setText(homework.getBody());
        holder.date.setText(Final.format.format(homework.getStartTime()));

        if(position!=0){
            StudyTaskEntity previous=homeworkList.get(position-1);
            //获取前一项作业与当前作业时间
            String previousTime=format.format(previous.getStartTime());
            String thisItemTime=format.format(homework.getStartTime());
            //如果相同，将当前作业时间隐藏，调整与前一项作业边距
            if(previousTime.equals(thisItemTime)){
                holder.date.setVisibility(View.GONE);
                LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) holder.notifPad.getLayoutParams();
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
        return homeworkList.size();
    }

}
