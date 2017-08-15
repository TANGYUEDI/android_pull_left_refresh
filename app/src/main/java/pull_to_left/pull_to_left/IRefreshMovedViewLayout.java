package pull_to_left.pull_to_left;

/**
 * 创建： TangZd
 * 创建时间： on 2017/8/2.
 * 备注：
 * <p>
 * 修改人：
 * 修改时间：
 * 修改描述：
 */

public interface IRefreshMovedViewLayout {

    void doToMove(float delta);

    boolean releaseAction();

    void refreshComplete();

    int getVisibleWidth();

    void setVisibleWidth(int value);

    void setRefreshState(@IPullToLeftRefreshState int state);// 设置状态

    @IPullToLeftRefreshState int getRefreshState();// 获取刷新状态


    void doInitRefreshState(@IPullToLeftRefreshState int state, boolean isArriveToMostRight);

}
