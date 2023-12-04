package com.fxxkywcx.nostudy.ui.user;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.fxxkywcx.nostudy.R;

public class MyImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Paint mPaint;//设置画笔
    private Bitmap mBitmap;//获取图片资源
    private int width, height;//获取控件宽高
    private int mOuterRing = 0;//设置外圈大小
    private int outerRingAlpha = 30;//设置外圈透明度
    private int color = Color.BLUE;//设置外圈颜色

    public MyImageView(Context context) {
        this(context, null);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public int getmOuterRing() {
        return mOuterRing;
    }

    public void setmOuterRing(int mOuterRing) {
        this.mOuterRing = mOuterRing;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getOuterRingAlpha() {
        return outerRingAlpha;
    }

    public void setOuterRingAlpha(int outerRingAlpha) {
        this.outerRingAlpha = outerRingAlpha;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取空间的宽度和高度，并保存在width和height变量中
        width = View.getDefaultSize(getMeasuredWidth(), widthMeasureSpec);
        height = View.getDefaultSize(getMeasuredHeight(), heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();
        Bitmap bitmap;
        if (drawable != null) {
            if (mBitmap != null) {
//                获取要绘制的图片drawable对象，并将其转换为Bitmap对象
                bitmap = mBitmap;
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic_1);
            }
            Matrix matrix = new Matrix();
            //根据控件的宽高和图片的宽高计算缩放比例,并使用Matrix对象对Bitmap进行缩放操作
            if (mOuterRing>0){
                if (width > height) {
                    matrix.setScale((float) (height) / bitmap.getHeight(), (float) (height) / bitmap.getHeight());
                } else {
                    matrix.setScale((float) (width) / bitmap.getWidth(), (float) (width) / bitmap.getWidth());
                }
            }else {
                if (width > height) {
                    matrix.setScale((float) (width) / bitmap.getWidth(), (float) (width) / bitmap.getWidth());
                } else {
                    matrix.setScale((float) (height) / bitmap.getHeight(), (float) (height) / bitmap.getHeight());
                }
            }

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            //将图片设置为圆形
            bitmap = toRoundBitmap(bitmap);

            //当外圈大于0的时候要设置外圈加图片的宽度不能大于控件宽度
            if (mOuterRing+bitmap.getWidth()>width){
                mOuterRing = (width-bitmap.getWidth())/2;
            }

            //绘制外圈
            Paint cPaint = new Paint();
            cPaint.setStrokeWidth(2);
            cPaint.setColor(color);
            cPaint.setAlpha(outerRingAlpha);
            // cPaint.setFilterBitmap(true);
            cPaint.setAntiAlias(true);
            canvas.drawCircle(bitmap.getWidth()/2+mOuterRing,bitmap.getHeight()/2+mOuterRing,bitmap.getWidth()/2+mOuterRing,cPaint);

            //绘制图片
            canvas.drawBitmap(bitmap, mOuterRing, mOuterRing, mPaint);

        } else {
//            没有设置Drawable对象，则调用父类的onDraw方法绘制默认的内容
            super.onDraw(canvas);
        }
    }

    /**
     * 设置图片为圆形
     *
     * @param bitmap
     * @return
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
//        根据宽度和高度的大小关系，确定圆形的半径roundPx,确定绘制圆形的起始位置和结束位置
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
//          创建一个新的Bitmap对象output，用于绘制圆形图片。创建一个Canvas对象，将output设置为绘制的目标
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst_left, dst_top, dst_right, dst_bottom);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);//绘制一个圆角矩形，作为圆形的背景
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//设置绘制模式为SRC_IN，使得绘制的内容只显示在圆角矩形的内部
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }
}
