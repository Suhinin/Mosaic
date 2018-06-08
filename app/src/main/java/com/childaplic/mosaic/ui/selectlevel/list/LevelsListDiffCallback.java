package com.childaplic.mosaic.ui.selectlevel.list;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class LevelsListDiffCallback extends DiffUtil.Callback {

    private final String TAG = LevelsListDiffCallback.class.getCanonicalName();

    private List<LevelAdapterModel> newItems;
    private List<LevelAdapterModel> oldItems;

    public LevelsListDiffCallback(List<LevelAdapterModel> newItems, List<LevelAdapterModel> oldItems) {
        this.newItems = newItems;
        this.oldItems = oldItems;
    }

    @Override
    public int getOldListSize () {
        return oldItems.size();
    }

    @Override
    public int getNewListSize () {
        return newItems.size();
    }

    @Override
    public boolean areItemsTheSame (int oldItemPosition, int newItemPosition) {
        LevelAdapterModel oldModel = oldItems.get(oldItemPosition);
        LevelAdapterModel newModel = newItems.get(newItemPosition);

        return oldModel.areItemsTheSame(newModel);
    }

    @Override
    public boolean areContentsTheSame (int oldItemPosition, int newItemPosition) {
        LevelAdapterModel oldModel = oldItems.get(oldItemPosition);
        LevelAdapterModel newModel = newItems.get(newItemPosition);

        return oldModel.areContentsTheSame(newModel);
    }

}
