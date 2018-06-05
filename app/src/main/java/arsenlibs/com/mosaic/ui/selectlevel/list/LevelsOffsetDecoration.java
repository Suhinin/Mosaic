package arsenlibs.com.mosaic.ui.selectlevel.list;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import arsenlibs.com.mosaic.R;
import arsenlibs.com.mosaic.utils.ScreenUtil;

public class LevelsOffsetDecoration extends RecyclerView.ItemDecoration {

    private int mFragmentMargin;
    private int mGridSpace;
    private int mSpanCount;

    public LevelsOffsetDecoration(Context context, int spanCount) {
        int fragmentMarginPx = context.getResources().getInteger(R.integer.select_level_fragment__margin_px);
        mFragmentMargin = ScreenUtil.getScreenY(context, fragmentMarginPx);

        int gridSpacePx = context.getResources().getInteger(R.integer.select_level_fragment__grid_margin_px);
        mGridSpace = ScreenUtil.getScreenY(context, gridSpacePx);

        mSpanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        int row = position % mSpanCount;

        if (position == 0) {
            outRect.set(mFragmentMargin, mFragmentMargin, mGridSpace, mGridSpace);
        } else if (position == 1) {
            outRect.set(mFragmentMargin, 0, mGridSpace, mFragmentMargin);
        } else if (row == 0) {
            outRect.set(0, mFragmentMargin, mGridSpace, mGridSpace);
        } else {
            outRect.set(0, 0, mGridSpace, mFragmentMargin);
        }
    }

}