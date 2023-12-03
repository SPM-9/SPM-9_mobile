package com.fxxkywcx.nostudy.ui.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.activities.AboutInfoActivity;
import com.fxxkywcx.nostudy.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private Button modifypwd;
    private Button exit;
    private Button about;
    private MyImageView imageView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.baseline_cancel_24);
        imageView= (MyImageView) root.findViewById(R.id.user_image);
        imageView.setBitmap(bitmap);
//        imageView.setmOuterRing(5);
//        imageView.setColor(Color.RED);
//        imageView.setOuterRingAlpha(50);

        modifypwd=root.findViewById(R.id.user_modify_password);
        exit=root.findViewById(R.id.user_exit);
        about=root.findViewById(R.id.user_about_info);

        modifypwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutInfoActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}