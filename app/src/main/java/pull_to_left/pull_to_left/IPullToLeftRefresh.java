package pull_to_left.pull_to_left;


import android.view.View;

/**
 * 创建： TangZd
 * 创建时间： on 2017/8/2.
 * 备注：
 * <p>
 * 修改人：
 * 修改时间：
 * 修改描述：
 */

public interface IPullToLeftRefresh {
    /**
     * 是否在最后能够进行向左拉
     * @return
     */
    boolean isCanPullToLeftDirector();

    /**
     * 完成刷新
     */
    void doWhatCompleteToRefresh();

    /**
     * 还原布局
     */
    void doWhatRecoverLayout();

    /**
     * 跟随移动的view
     */
    void setMoveViews(View movedView);
}
