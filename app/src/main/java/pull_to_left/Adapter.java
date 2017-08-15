package pull_to_left;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yixiang.testdemoapplication.R;

import java.util.List;

/**
 * 创建： TangZd
 * 创建时间： on 2017/8/1.
 * 备注：
 * <p>
 * 修改人：
 * 修改时间：
 * 修改描述：
 */

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<String> strings;

    public Adapter(List<String> strings){
        this.strings = strings;
    }



    public void addItem(String srt){
        strings.add(srt);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderTest(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    class ViewHolderTest extends RecyclerView.ViewHolder{

        public ViewHolderTest(View itemView) {
            super(itemView);
        }
    }

    static public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;
        private boolean isSpaceToSet = true;
        int leftSpace,  rightSpace,  topSpace,  bottomSpace;

        public SpacesItemDecoration(int space) {
            this.space = space;
            isSpaceToSet = true;
        }

         public SpacesItemDecoration(int leftSpace, int rightSpace, int topSpace, int bottomSpace){
            this.bottomSpace = bottomSpace;
            this.rightSpace = rightSpace;
            this.topSpace = topSpace;
            this.leftSpace = leftSpace;
            isSpaceToSet = false;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            if (isSpaceToSet){
                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;
                outRect.top = 0;
                // Add top margin only for the first item to avoid double space between items
                //if (parent.getChildPosition(view) == 0)
                //    outRect.top = space;
            } else {
                outRect.left = leftSpace;
                outRect.right = rightSpace;
                outRect.bottom = bottomSpace;
                outRect.top = topSpace;
            }
        }
    }
}
