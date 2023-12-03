package com.fxxkywcx.nostudy.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private View root;
    private Context context;
    private final int FLING_MIN_DISTANCE = 100, FLING_MIN_X_VELOCITY = 200;
    private final int IMAGE = 0;
    private final int GIF = 1;
    private final int PREVIOUS = 0;
    private final int NEXT = 1;
    private final int FLIP_INTERVAL = 5000;
    private ADsFlipper flipper;
    private TextView flipperTextView;
    private Animation in_lefttoright;
    private Animation out_lefttoright;
    private Animation in_righttoleft;
    private Animation out_righttoleft;
    private final int[] flipperContent = {
            R.drawable.pic_1,
            R.drawable.pic_2,
            R.drawable.pic_3
    };
    private final int[] flipperContentType = {
            IMAGE,
            GIF,
            IMAGE
    };
    private final String[] flipperText = {
            "我超，初音未来！",
            "原神，启动！",
            "关注ASOUL喵，关注ASOUL谢谢喵~"
    };
    private final String[] flipperADLink = {
            "https://space.bilibili.com/399918500?spm_id_from=333.337.0.0",
            "https://ys.mihoyo.com/",
            "https://live.bilibili.com/22637261?broadcast_type=0&is_room_feed=1&spm_id_from=333.999.to_liveroom.0.click&live_from=86002"
    };
    Handler nextAD;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        context = root.getContext();

        flipper = root.findViewById(R.id.home_viewFlipper);
        flipperTextView = root.findViewById(R.id.home_flipperText);
        in_lefttoright = AnimationUtils.loadAnimation(context,R.anim.in_lefttoright);
        out_lefttoright = AnimationUtils.loadAnimation(context,R.anim.out_lefttoright);
        in_righttoleft = AnimationUtils.loadAnimation(context,R.anim.in_righttoleft);
        out_righttoleft = AnimationUtils.loadAnimation(context,R.anim.out_righttoleft);
        nextAD = new Handler();
        nextAD.postDelayed(flipToNext, FLIP_INTERVAL);

        flipperTextView.setText(flipperText[0]);
        for (int i = 0; i < flipperContent.length; i++)
            flipper.addView(getImageView(flipperContent[i], flipperContentType[i]));

        flipper.setOnGestureListener(new ADsGestureListener());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private ImageView getImageView(int id, int type){
        ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(id);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(param);
        if (type == GIF) {
            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + id);
            Glide.with(context)
                    .load(uri)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }
        return imageView;
    }

    private final class ADsGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_X_VELOCITY) {
                // Fling right
                ADSwitchHandler(PREVIOUS);
                RefreshInterval(nextAD);
            } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_X_VELOCITY) {
                // Fling left
                ADSwitchHandler(NEXT);
                RefreshInterval(nextAD);
            }
            return true;
        }
        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(flipperADLink[flipper.getDisplayedChild()]));
            startActivity(intent);
            return super.onSingleTapUp(e);
        }
    }

    Runnable flipToNext = new Runnable() {
        @Override
        public void run() {
            ADSwitchHandler(PREVIOUS);
            nextAD.postDelayed(this, FLIP_INTERVAL);
        }
    };

    private void RefreshInterval(Handler handler) {
        handler.removeCallbacks(flipToNext);
        handler.postDelayed(flipToNext, FLIP_INTERVAL);
    }

    private void ADSwitchHandler(int ACTION) {
        if (ACTION == PREVIOUS) {
            flipper.setInAnimation(in_righttoleft); // 进来时的动画
            flipper.setOutAnimation(out_righttoleft); // 出去时的动画
            flipper.showPrevious();
        } else if (ACTION == NEXT) {
            flipper.setInAnimation(in_lefttoright); // 进来时的动画
            flipper.setOutAnimation(out_lefttoright); // 出去时的动画
            flipper.showNext();
        }
        int position = flipper.getDisplayedChild();
        flipperTextView.setText(flipperText[position]);
    }
}