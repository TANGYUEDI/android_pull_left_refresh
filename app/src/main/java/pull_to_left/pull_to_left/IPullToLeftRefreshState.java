package pull_to_left.pull_to_left;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 创建： TangZd
 * 创建时间： on 2017/8/2.
 * 备注：
 * <p>
 * 修改人：
 * 修改时间：
 * 修改描述：
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({
        IPullToLeftRefreshState.STATE_DONE_FINISHED,
        IPullToLeftRefreshState.STATE_NORMAL,
        IPullToLeftRefreshState.STATE_REFRESHING,
        IPullToLeftRefreshState.STATE_RELEASE_TO_REFRESH
})
//@Documented()
public @interface IPullToLeftRefreshState {
    /**
     * 正常状态下
     */
    int STATE_NORMAL = 10;
    /**
     * 松开手指去刷新
     */
    int STATE_RELEASE_TO_REFRESH = 11;
    /**
     * 正在刷新
     */
    int STATE_REFRESHING = 12;
    /**
     * 刷新完成
     */
    int STATE_DONE_FINISHED = 13;
}
