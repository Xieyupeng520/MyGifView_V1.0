/******************************************************************
 *
 *    Java Lib For Android, Powered By Shenzhen Jiuzhou.
 *
 *    Copyright (c) 2001-2014 Digital Telemedia Co.,Ltd
 *    http://www.d-telemedia.com/
 *
 *    Package:     com.azz.mygifview
 *
 *    Filename:    MyGifView.java
 *
 *    Description: TODO(用一句话描述该文件做什么)
 *
 *    Copyright:   Copyright (c) 2001-2014
 *
 *    Company:     Digital Telemedia Co.,Ltd
 *
 *    @author:     sxszhangz
 *
 *    @version:    1.0.0
 *
 *    Create at:   2015年7月8日 上午8:46:17
 *
 *    Revision:
 *
 *    2015年7月8日 上午8:46:17
 *        - first revision
 *
 *****************************************************************/
package com.azz.mygifview;

import java.io.InputStream;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;


/**
 * @ClassName MyGifView
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author sxszhangz
 * @Date 2015年7月8日 上午8:46:17
 * @version 1.0.0
 */
public class MyGifView extends ImageView {
    /**
     * @Field @TAG : 打印表签
     */
    private static final String TAG = "MyGifView";
    /**
     * @Field @mMovie : 动图播放
     */
    private Movie mMovie;
    /**
     * @Field @mImageWidth : 图片宽
     */
    private int mImageWidth;
    /**
     * @Field @mImageHeight : 图片高
     */
    private int mImageHeight = -1;
    /**
     * @Field @mMovieStart : 播放开始时间
     */
    private long mMovieStart = -1;
    /**
     * @Description 构造方法
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MyGifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.d(TAG, "构造方法（，，）");
    }

    /**
     * @Description 构造方法
     * @param context
     * @param attrs
     */
    public MyGifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "构造方法（，）");
        int count = attrs.getAttributeCount();
        for (int i = 0; i < count; i++) {
            Log.d(TAG, "attrs = " + attrs.getAttributeName(i));
        }
        init(context, attrs);
    }

    /**
     * @Description 构造方法
     * @param context
     */
    public MyGifView(Context context) {
        super(context);
        Log.d(TAG, "构造方法（）");
    }

    /**
     * @Description 初始化
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GifView);
        //打印出属性值
        int count = typedArray.getIndexCount();
        Log.d(TAG, "typedArray.count = " + count);
        for (int i = 0; i <count; i++) {
            Log.d(TAG, "typedArray = " + typedArray.getText(i));
        }
        
//        Drawable drawable = typedArray.getDrawable(R.styleable.GifView_gif_src);
        int resId = typedArray.getResourceId(R.styleable.GifView_gif_src, 0);
//        Log.d(TAG, R.drawable.coffee + " = " + resId);
        typedArray.recycle();
        //转输入流
        InputStream iStream = getResources().openRawResource(resId);
        Movie movie = Movie.decodeStream(iStream);
        setMovie(movie, iStream);
        
    }
    /* (非 Javadoc)
     * Description:
     * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");
            if (mMovie != null) {
                 playMovie(canvas);
                 invalidate();
            }
    }
    /* (非 Javadoc)
     * Description:
     * @see android.widget.ImageView#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "noMeasure-width = " + widthMeasureSpec + " height = " + heightMeasureSpec);
        if (mMovie != null) {
            //如果是GIF图片，重新设定大小
            setMeasuredDimension(mImageWidth, mImageHeight);
        }
    }
    /* (非 Javadoc)
     * Description:
     * @see android.view.View#onLayout(boolean, int, int, int, int)
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout");
    }
    /**  
     * 开始播放GIF动画，播放完成返回true，未完成返回false。  
     *   
     * @param canvas  
     * @return 播放完成返回true，未完成返回false。  
     */ 
    private boolean playMovie(Canvas canvas) {
        if (mMovie == null) { //普通图片
            return false;
        }
        long now = SystemClock.uptimeMillis();  
        //第一次播放
        if (mMovieStart == 0) {  
            mMovieStart = now;  
        }  
        int duration = mMovie.duration();  
        Log.d(TAG, "movie duration is " + duration);
        if (duration == 0) {  
            duration = 1000;  
        }  
        int relTime = (int) ((now - mMovieStart) % duration);  
        mMovie.setTime(relTime); 
        mMovie.draw(canvas, 0, 0); 
//        if ((now - mMovieStart) >= duration) {  
//            mMovieStart = 0;  
//            return true;  
//        }  
        return false;  
    } 
    /**
     * 设置
     * @param movie
//     * @param is 输入流
     */
    private void setMovie(final Movie movie, final InputStream is) {
        mMovie = movie;
        if (mMovie != null) { //不为空说明是GIF
            Bitmap bitmap = BitmapFactory.decodeStream(is);  
            mImageWidth = bitmap.getWidth();  
            mImageHeight = bitmap.getHeight();  
            Log.i(TAG, "setMovie---width = " + mImageWidth + ", height = " + mImageHeight);
            bitmap.recycle();  
        }
    }
}
