package pull_to_left.pull_to_left;

import android.animation.ValueAnimator;
import android.content.Context;

import android.os.Handler;

import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.example.yixiang.testdemoapplication.R;


import static pull_to_left.pull_to_left.IPullToLeftRefreshState.STATE_RELEASE_TO_REFRESH;

/**
 * 创建： TangZd
 * 创建时间： on 2017/8/2.
 * 备注：
 * <p>
 * 修改人：
 * 修改时间：
 * 修改描述：
 */

public class MovedViewLayout extends LinearLayout implements IRefreshMovedViewLayout{
    /**
     * 默认状态是正常
     */
    private @IPullToLeftRefreshState int mRefreshState = IPullToLeftRefreshState.STATE_NORMAL;

    public TextView tv_moved_view;
    public ImageView iv_moved_view;
    public LinearLayout mContainer;

    private int mMeasuredWidth;

    IPullRefreshStateListener mIPullRefreshStateListener;

    public void setIPullRefreshStateListener(IPullRefreshStateListener zIPullRefreshStateListener){
        this.mIPullRefreshStateListener = zIPullRefreshStateListener;
    }

    public MovedViewLayout(Context context) {
        this(context, null);
    }

    public MovedViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovedViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSetLayout(context);
    }

    private void initSetLayout(Context context){

        LayoutInflater.from(context).inflate(R.layout.look_more, this);


        mContainer = (LinearLayout) findViewById(R.id.ll_moved_view);
        iv_moved_view = (ImageView) findViewById(R.id.iv_moved_view);
        tv_moved_view = (TextView) findViewById(R.id.tv_moved_view);

        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setGravity(Gravity.CENTER);

        mContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mMeasuredWidth = getMeasuredWidth();
        setVisibility(GONE);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    /**
     * 重置状态。
     */
    public void resetState() {
        /**
         * 移动至0会移到屏幕外，不可见
         */
        smoothScrollTo(0);
        setRefreshState(IPullToLeftRefreshState.STATE_NORMAL);
    }

    @Override
    public void doToMove(float delta) {


        if (getVisibleWidth() > 0 || delta < 0) {

            if(getVisibility() == View.GONE)
                setVisibility(VISIBLE);

            setVisibleWidth((int) Math.abs(delta) + getVisibleWidth());

            if (mRefreshState <= IPullToLeftRefreshState.STATE_RELEASE_TO_REFRESH) {

                if (getVisibleWidth() > mMeasuredWidth) {
                    setRefreshState(IPullToLeftRefreshState.STATE_RELEASE_TO_REFRESH);

                } else if (mRefreshState == IPullToLeftRefreshState.STATE_RELEASE_TO_REFRESH){
                    setRefreshState(IPullToLeftRefreshState.STATE_REFRESHING);

                } else {
                    setRefreshState(IPullToLeftRefreshState.STATE_NORMAL);
                }
                Log.d("TangZd", mRefreshState + "<<--mRefreshState" + "\n ");
            }
        }
    }

    @Override
    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int visibleWidth = getVisibleWidth();
        if (visibleWidth == 0)
            isOnRefresh = false;


        if (getVisibleWidth() > mMeasuredWidth && mRefreshState < IPullToLeftRefreshState.STATE_REFRESHING) {
            setRefreshState(IPullToLeftRefreshState.STATE_REFRESHING);
            isOnRefresh = true;
        }

        if (mRefreshState == IPullToLeftRefreshState.STATE_REFRESHING && visibleWidth <= mMeasuredWidth) {
            //return;
        }

        int destWidth = 0;

        if (mRefreshState == IPullToLeftRefreshState.STATE_REFRESHING) {
            destWidth = mMeasuredWidth;
        }


        /**
         * destWidth = 0：移动至不可见
         */
        smoothScrollTo(destWidth);

        return isOnRefresh;
    }

    @Override
    public void refreshComplete() {
        setRefreshState(IPullToLeftRefreshState.STATE_DONE_FINISHED);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                resetState();
            }
        }, 1000);
    }

    @Override
    public int getVisibleWidth() {

        int zWidth = mContainer.getWidth();
        return zWidth;
    }

    @Override
    public void setVisibleWidth(int value) {
        if (value < 0)
            value = 0;

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();//new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.width = value;
        mContainer.setLayoutParams(lp);
        invalidate();

    }



    private void smoothScrollTo(int destWidth) {

        ValueAnimator animator = ValueAnimator.ofInt(getVisibleWidth(), destWidth);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleWidth((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }


    @Override
    public void setRefreshState(@IPullToLeftRefreshState int state) {

        if (state == mRefreshState)
            return;

        switch (state){
            case IPullToLeftRefreshState.STATE_NORMAL:
                if(getVisibility() == View.GONE)
                    setVisibility(VISIBLE);
                tv_moved_view.setText("");

                break;
            case STATE_RELEASE_TO_REFRESH:
                if(getVisibility() == View.GONE) setVisibility(VISIBLE);
                tv_moved_view.setText("松开手指刷新更多");

                break;
            case IPullToLeftRefreshState.STATE_REFRESHING:
                if(getVisibility() == View.GONE) setVisibility(VISIBLE);
                tv_moved_view.setText("正在刷新和加载");

                break;
            case IPullToLeftRefreshState.STATE_DONE_FINISHED:
                if(getVisibility() == View.GONE) setVisibility(VISIBLE);
                tv_moved_view.setText("刷新完成");

                break;
        }

        if (mIPullRefreshStateListener != null){
            mIPullRefreshStateListener.onPullRefreshState(mRefreshState, state);
        }

        mRefreshState = state;
    }

    @Override
    public int getRefreshState() {
        return mRefreshState;
    }

    @Override
    public void doInitRefreshState(@IPullToLeftRefreshState int state, boolean isArriveToMostRight) {


        if (isArriveToMostRight)
            smoothScrollTo(mMeasuredWidth);
        else
            smoothScrollTo(0);

        this.mRefreshState = state;
    }
}
