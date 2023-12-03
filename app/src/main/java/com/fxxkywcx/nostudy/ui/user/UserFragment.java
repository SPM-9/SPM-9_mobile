package com.fxxkywcx.nostudy.ui.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private MyImageView imageView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.baseline_cancel_24);
        imageView= (MyImageView) root.findViewById(R.id.user_image);
        imageView.setBitmap(bitmap);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}