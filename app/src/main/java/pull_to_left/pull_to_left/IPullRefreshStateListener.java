package pull_to_left.pull_to_left;

/**
 * 创建： TangZd
 * 创建时间： on 2017/8/3.
 * 备注：
 * <p>
 * 修改人：
 * 修改时间：
 * 修改描述：
 */

public interface IPullRefreshStateListener {

    void onPullRefreshState(@IPullToLeftRefreshState int fromState, @IPullToLeftRefreshState int toState);
}
