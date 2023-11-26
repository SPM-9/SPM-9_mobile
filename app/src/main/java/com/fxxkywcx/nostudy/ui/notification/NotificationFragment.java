package com.fxxkywcx.nostudy.ui.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.databinding.FragmentNotificationBinding;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import org.jetbrains.annotations.NotNull;

public class NotificationFragment extends Fragment {
    private FragmentNotificationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RefreshLayout refreshLayout = root.findViewById(R.id.notification_refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(root.getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(root.getContext()));
        refreshLayout.setOnRefreshListener(refreshListener);
        refreshLayout.setOnLoadMoreListener(loadMoreListener);


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
            refreshLayout.finishRefresh(2000, true, false);
        }
    };

    OnLoadMoreListener loadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
            refreshLayout.finishLoadMore(2000, true, false);
        }
    };
}