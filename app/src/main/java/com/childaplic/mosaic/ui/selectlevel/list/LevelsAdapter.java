package com.childaplic.mosaic.ui.selectlevel.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.childaplic.mosaic.R;
import com.childaplic.mosaic.presenters.selectlevel.LevelItem;
import com.childaplic.mosaic.ui.common.Size;
import com.childaplic.mosaic.utils.ScreenUtil;

public class LevelsAdapter extends RecyclerView.Adapter<LevelViewHolder> {

    // region Constants

    private final String TAG = LevelsAdapter.class.getCanonicalName();

    private final int SCREEN_COLUMN_COUNT = 3;
    private final int SCREEN_ROW_COUNT = 2;

    // endregion


    // region Fields

    private Context mContext;

    private List<LevelAdapterModel> mItems;
    private List<LevelItem> mModelItems;

    private LevelClickHandler mItemClickHandler;
    private Size mItemSize;

    // endregion


    // region Constructors

    public LevelsAdapter(Context context, LevelClickHandler itemClickHandler) {
        mContext = context;
        mItemClickHandler = itemClickHandler;

        mItems = new ArrayList<>();
        mModelItems = new ArrayList<>();

        calcItemSize();
    }

    // endregion


    // region Implements RecyclerView.Adapter

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LevelItemView view = new LevelItemView(parent.getContext(), mItemSize);

        return new LevelViewHolder(view, mItemClickHandler);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
        LevelAdapterModel item = mItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // endregion


    // region Public Methods

    public void updateItems(LevelItem[] modelItems) {
        mModelItems.clear();
        List<LevelAdapterModel> adapterModels = new ArrayList<>();
        if (modelItems != null) {
            mModelItems.addAll(Arrays.asList(modelItems));

            for (LevelItem modelItem : modelItems) {
                adapterModels.add(new LevelAdapterModelImpl(modelItem));
            }
        }

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new LevelsListDiffCallback(adapterModels, mItems));
        mItems = adapterModels;
        diffResult.dispatchUpdatesTo(this);
    }

    public LevelItem getItem(int position) {
        return mModelItems.get(position);
    }

    // endregion


    // region Private Methods

    private void calcItemSize() {
        int fragmentMargin = (int) mContext.getResources().getDimension(R.dimen.select_level_fragment__margin);
        int gridMargin = (int) mContext.getResources().getDimension(R.dimen.select_level_fragment__grid_margin);

        Size screenSize = ScreenUtil.getScreenSize(mContext);
        int width = (screenSize.getWidth() - 2*fragmentMargin - (SCREEN_COLUMN_COUNT -1)*gridMargin) / SCREEN_COLUMN_COUNT;
        int height = (screenSize.getHeight() - 2*fragmentMargin - (SCREEN_ROW_COUNT -1)*gridMargin) / SCREEN_ROW_COUNT;

        mItemSize = new Size(width, height);
    }

    // endregion

}
