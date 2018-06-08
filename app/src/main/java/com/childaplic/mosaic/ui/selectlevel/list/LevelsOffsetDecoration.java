package com.childaplic.mosaic.ui.selectlevel.list;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.childaplic.mosaic.R;

public class LevelsOffsetDecoration extends RecyclerView.ItemDecoration {

    private int mFragmentMargin;
    private int mGridSpace;
    private int mSpanCount;

    public LevelsOffsetDecoration(Context context, int spanCount) {
        mFragmentMargin = (int) context.getResources().getDimension(R.dimen.select_level_fragment__margin);
        mGridSpace = (int) context.getResources().getDimension(R.dimen.select_level_fragment__grid_margin);

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