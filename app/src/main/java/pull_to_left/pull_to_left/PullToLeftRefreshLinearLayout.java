package pull_to_left.pull_to_left;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import android.widget.RelativeLayout;



/**
 * 创建： TangZd
 * 创建时间： on 2017/8/2.
 * 备注：
 * <p>
 * 修改人：
 * 修改时间：
 * 修改描述：
 */

public class PullToLeftRefreshLinearLayout extends RelativeLayout
        implements ViewTreeObserver.OnGlobalLayoutListener, IPullToLeftRefresh{


    private static final float DRAG_RATE = 0.40f;
    /**
     * 必须是RecyclerView
     */
    private View mChildView;

    /**
     * 第二个控件，必须实现IRefreshMovedViewLayout接口
     */
    private IRefreshMovedViewLayout mIRefreshMovedViewLayout;

    /**
     *  用于记录childView（RecyclerView）正常的布局位置
     */
    private Rect originalRect = new Rect();

    /**
     * 是否松开了手指
     */
    private boolean isReleaseFinger;

    /**
     * 按下时候的X坐标
     */
    private float startDownX;

    /**
     * 刷新监听
     */
    OnPullToLeftListener mOnPullToLeftListener;

    /**
     * 设置回掉监听
     * @param leftListener
     */
    public void setOnPullToLeftListener(OnPullToLeftListener leftListener){
        this.mOnPullToLeftListener = leftListener;
    }

    /**
     * 回掉接口
     */
    public interface OnPullToLeftListener{
        void onPullToLeftRefresh();
    }

    public PullToLeftRefreshLinearLayout(Context context) {
        super(context);
    }

    public PullToLeftRefreshLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToLeftRefreshLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        /**
         * 初始值设置
         */
        originalRect.set(mChildView.getLeft(), mChildView.getTop(), mChildView.getRight(), mChildView.getBottom());

    }

    public RecyclerView getChildView(){
        if (mChildView == null){
            return null;
        }
        return  (RecyclerView) mChildView;
    }


    /**
     * 加载布局后初始化,这个方法会在加载完布局后调用
     */
    @Override
    protected void onFinishInflate() {

        if (getChildCount() > 0) {

            if (getChildCount() != 2){
                throw new RuntimeException("必须有两个子控件且仅能有两个子控件");
            }

            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) instanceof RecyclerView) {
                    if (mChildView == null) {
                        mChildView = getChildAt(i);
                    } else {
                        throw new RuntimeException("只能存在一个RecyclerView");
                    }
                } else if (getChildAt(i) instanceof IRefreshMovedViewLayout) {
                    if (this.mIRefreshMovedViewLayout == null) {
                        this.mIRefreshMovedViewLayout = (IRefreshMovedViewLayout) getChildAt(i);
                    }else {
                        throw new RuntimeException("只能存在一个实现IRefreshMovedViewLayout接口的控件");
                    }
                }
            }
        }

        if (mChildView == null) {
            throw new RuntimeException("子容器中必须有一个RecyclerView");
        }
        if (this.mIRefreshMovedViewLayout == null) {
            throw new RuntimeException("子容器中必须有一个实现IRefreshMovedViewLayout接口的控件");
        }

        getViewTreeObserver().addOnGlobalLayoutListener(this);
        super.onFinishInflate();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onGlobalLayout() {
        requestLayout();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public boolean isCanPullToLeftDirector() {
        final RecyclerView.Adapter adapter = ((RecyclerView) mChildView).getAdapter();

        if (null == adapter) {
            return true;
        }

        final int lastItemPosition = adapter.getItemCount() - 1;
        if (((RecyclerView) mChildView)
                .getLayoutManager() instanceof LinearLayoutManager) {

            final int lastVisiblePosition = ((LinearLayoutManager) ((RecyclerView) mChildView).getLayoutManager()).findLastVisibleItemPosition();

            if (lastVisiblePosition >= lastItemPosition) {

                final int childIndex = lastVisiblePosition - ((LinearLayoutManager) ((RecyclerView) mChildView)
                        .getLayoutManager()).findFirstVisibleItemPosition();

                final int childCount = ((RecyclerView) mChildView).getChildCount();
                final int index = Math.max(childIndex, childCount - 1);
                final View lastVisibleChild = ((RecyclerView) mChildView).getChildAt(index);
                if (lastVisibleChild != null) {
                    boolean isArriveToMostRight = lastVisibleChild.getRight() <= mChildView.getRight() - mChildView.getLeft();
                    return isArriveToMostRight;
                }
            }
        }
        return false;
    }

    @Override
    public void doWhatCompleteToRefresh() {
        mIRefreshMovedViewLayout.refreshComplete();
    }

    @Override
    public void doWhatRecoverLayout() {
        mChildView.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom);
        requestLayout();
    }

    @Override
    public void setMoveViews(View movedView) {
        if (movedView instanceof IRefreshMovedViewLayout) {

            this.mIRefreshMovedViewLayout = (IRefreshMovedViewLayout) movedView;
            requestLayout();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /**
         * 不拦截,直接传递给子的view。
         */
        return false;
    }

    /**
     * 事件分发
     *  false-->转给父类onTouchEvent
     *  dispatchTouchEvent(ev)-->事件向下分发onInterceptTouchEvent
     *  true-->事件被自身消耗
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mChildView == null) {
            return super.dispatchTouchEvent(ev);
        }


        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                startDownX = ev.getX();
                isReleaseFinger = false;

            case MotionEvent.ACTION_MOVE:

                float nowX = ev.getX();
                int scrollX = (int) (nowX - startDownX);
                isReleaseFinger = false;

                if ((isCanPullToLeftDirector() && scrollX < 0)) {// 继续向左边做移动

                    int delta = (int) (scrollX * DRAG_RATE);
                    mIRefreshMovedViewLayout.doToMove(delta);

                    if (mIRefreshMovedViewLayout.getVisibleWidth() > 0
                            && mIRefreshMovedViewLayout.getRefreshState() < IPullToLeftRefreshState.STATE_REFRESHING) {

                        return super.dispatchTouchEvent(ev);
                    }

                    return true;

                } else {
                    startDownX = ev.getX();

                    return super.dispatchTouchEvent(ev);
                }
            case MotionEvent.ACTION_UP:
                isReleaseFinger = true;

            default:
                if (isReleaseFinger) {
                    isReleaseFinger = false;
                    if (mIRefreshMovedViewLayout.releaseAction()) {

                        if (mOnPullToLeftListener != null) {
                            mOnPullToLeftListener.onPullToLeftRefresh();

                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    doWhatCompleteToRefresh();
                                }
                            }, 1200);
                        }
                    } else {
                        return super.dispatchTouchEvent(ev);
                    }
                } else {
                    return super.dispatchTouchEvent(ev);
                }
                break;
        }
        return true;
    }
}
