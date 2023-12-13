package com.fxxkywcx.nostudy.ui.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentActivity;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {
    private FragmentActivity rootActivity;
    private FragmentUserBinding binding;
    private MyImageView imageView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootActivity = getActivity();

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic_1);
        imageView= (MyImageView) root.findViewById(R.id.user_image);
        imageView.setBitmap(bitmap);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        rootActivity.setTitle("我的");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}