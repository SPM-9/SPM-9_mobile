package com.fxxkywcx.nostudy.ui.notification;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.databinding.FragmentNotificationBinding;
import com.fxxkywcx.nostudy.entity.NotificationEntity;
import com.fxxkywcx.nostudy.network.GetNotifications;
import com.fxxkywcx.nostudy.utils.InternetUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    private FragmentNotificationBinding binding;
    RecyclerView notifList;
    NotificationsListAdapter adapter;
    List<NotificationEntity> list = new ArrayList<>(45);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        notifList = root.findViewById(R.id.notification_list);
        RefreshLayout refreshLayout = root.findViewById(R.id.notification_refreshLayout);

        refreshLayout.setRefreshHeader(new ClassicsHeader(root.getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(root.getContext()));
        refreshLayout.setOnRefreshListener(refreshListener);
        refreshLayout.setOnLoadMoreListener(loadMoreListener);

//        NotificationEntity notification = new NotificationEntity();
//        notification.setTitle("114514");
//        notification.setBody("1919810");

        adapter = new NotificationsListAdapter(list);
        notifList.setAdapter(adapter);

        // 添加五条
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int status = msg.arg2;
                if (status == GetNotifications.NETWORK_FAILURE) {
                    InternetUtils.NoInternetToast(binding.getRoot().getContext());
                } else {
                    List<NotificationEntity> respList = (List<NotificationEntity>) msg.obj;
                    list.clear();
                    list.addAll(respList);
                    adapter.notifyDataSetChanged();
                    notifList.setAdapter(adapter);
                }
                return true;
            }
        });
        GetNotifications.getInstance().getLastNotif(handler);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
            list.clear();
            adapter.notifyDataSetChanged();
            refreshLayout.resetNoMoreData();
            Handler handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    int status = msg.arg2;
                    if (status == GetNotifications.NETWORK_FAILURE) {
                        InternetUtils.NoInternetToast(binding.getRoot().getContext());
                        refreshLayout.finishRefresh(false);
                    } else {
                        List<NotificationEntity> respList = (List<NotificationEntity>) msg.obj;
                        list.addAll(respList);
                        adapter.notifyDataSetChanged();
                        notifList.setAdapter(adapter);
                        if (status == GetNotifications.NO_MORE)
                            refreshLayout.finishRefreshWithNoMoreData();
                        else
                            refreshLayout.finishRefresh(true);
                    }
                    return true;
                }
            });
            GetNotifications.getInstance().getLastNotif(handler);
        }
    };

    OnLoadMoreListener loadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
            Handler handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    int status = msg.arg2;
                    if (status == GetNotifications.NETWORK_FAILURE) {
                        InternetUtils.NoInternetToast(binding.getRoot().getContext());
                        refreshLayout.finishLoadMore(false);
                    } else {
                        List<NotificationEntity> respList = (List<NotificationEntity>) msg.obj;
                        list.addAll(respList);
                        adapter.notifyDataSetChanged();
                        notifList.setAdapter(adapter);
                        if (status == GetNotifications.NO_MORE)
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        else
                            refreshLayout.finishLoadMore(true);
                    }
                    return true;
                }
            });
            int lastNotifId = list.get(list.size()-1).getNotifId();
            GetNotifications.getInstance().getPreviousNotif(handler, lastNotifId);
        }
    };


}









