package com.example.yixiang.testdemoapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * 创建： TangZd
 * 创建时间： on 2017/7/31.
 * 备注：
 * <p>
 * 修改人：
 * 修改时间：
 * 修改描述：
 */

public class GradientTextView extends TextView{

    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private Paint mPaint;
    private int mViewWidth = 0;
    private int mTranslate = 0;
    private boolean mAnimating = true;

    int mViewHight = 0;

    private Rect mTextBound = new Rect();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Log.d("TangZd", "GradientTextView-->" + "\n" + w + "\n" + h
                + "\n" + oldh + "\n" + oldh
                +"\n" + getMeasuredHeight()/getLineCount());
        if (mViewWidth == 0)
        {
            //getWidth得到是某个view的实际尺寸.
            //getMeasuredWidth是得到某view想要在parent view里面占的大小.
            mViewWidth = getMeasuredWidth();
            if (mViewWidth > 0)
            {
                mPaint = getPaint();
                //线性渐变
                mLinearGradient = new LinearGradient(0,
                        0,
                        0,
                        getMeasuredHeight()/getLineCount(),
                        new int[]{ 0x333333, 0x666666, 0x99ffffff },
                        new float[]{ 0.0f, 0.5f, 1.0f }, Shader.TileMode.CLAMP);
                mPaint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }


    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mViewWidth = getMeasuredWidth();
//        mPaint = getPaint();
//        String mTipText = getText().toString();
//        mPaint.getTextBounds(mTipText, 0, mTipText.length(), mTextBound);
//        mLinearGradient = new LinearGradient(0, 0, 0, getMeasuredHeight()/getLineCount(),
//                new int[] {  0x99ffffff, 0x11FFFFFF , 0x333333},
//                new float[] { 0, 0.5f, 1 }, Shader.TileMode.CLAMP);
//        mPaint.setShader(mLinearGradient);
//        canvas.drawText(mTipText,
//                getMeasuredWidth() / 2 - mTextBound.width() / 2,
//                getMeasuredHeight() / 2 +   mTextBound.height()/2,
//                mPaint);

        Log.d("TangZd", "GradientTextView -- " );
        if (mAnimating && mGradientMatrix != null)
        {
            mTranslate += getMeasuredHeight()/getLineCount() / 10;
            if (mTranslate >= getMeasuredHeight()/getLineCount())
            {
                mTranslate = getMeasuredHeight()/getLineCount();
            }
            mGradientMatrix.setTranslate(0, mTranslate);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            //50ms刷新一次
            postInvalidateDelayed(50);
        }
    }
}
