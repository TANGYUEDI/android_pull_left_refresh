package pull_to_left;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.yixiang.testdemoapplication.R;

import java.util.LinkedList;
import java.util.List;

import pull_to_left.pull_to_left.IPullRefreshStateListener;
import pull_to_left.pull_to_left.IPullToLeftRefreshState;
import pull_to_left.pull_to_left.MovedViewLayout;
import pull_to_left.pull_to_left.PullToLeftRefreshLinearLayout;

/**
 * 创建： TangZd
 * 创建时间： on 2017/8/1.
 * 备注：
 * <p>
 * 修改人：
 * 修改时间：
 * 修改描述：
 */

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    Adapter mAdapter;

    PullToLeftRefreshLinearLayout pull_group;
    MovedViewLayout movedViewLayout;

    LinearLayout ll_moved_view;

    boolean isInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_hsv_type_content);

        pull_group = (PullToLeftRefreshLinearLayout) findViewById(R.id.pull_group) ;
        movedViewLayout = (MovedViewLayout) findViewById(R.id.movedViewLayout) ;
        ll_moved_view = (LinearLayout) findViewById(R.id.ll_moved_view) ;

        List<String> strings = new LinkedList<>();
        for (int ii=0; ii<6; ii++) {
            String str = "";
            strings.add(str);
        }
        mAdapter = new Adapter(strings);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.addItemDecoration(new Adapter.SpacesItemDecoration(0, 50, 0, 0));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastItemPosition = mAdapter.getItemCount()-1;
                final int lastVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();


                if (lastVisiblePosition == lastItemPosition) {

                } else {

                }
            }
        });

    }
}
