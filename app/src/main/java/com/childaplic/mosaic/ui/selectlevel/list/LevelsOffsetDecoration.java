package com.childaplic.mosaic.ui.selectlevel.list;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.childaplic.mosaic.R;

public class LevelsOffsetDecoration extends RecyclerView.ItemDecoration {

    private int mGridOffset;

    public LevelsOffsetDecoration(Context context) {
        mGridOffset = (int) context.getResources().getDimension(R.dimen.select_level_fragment__grid_horizontal_offset);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.set(mGridOffset/2, 0, mGridOffset/2, 0);
    }

    public int getGridOffset() {
        return mGridOffset;
    }
}