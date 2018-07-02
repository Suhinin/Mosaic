package com.childaplic.mosaic.ui.selectlevel.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.childaplic.mosaic.repositories.levels.domain.LevelState;

class LevelViewHolder extends RecyclerView.ViewHolder {

    // region Constants

    private final String DISABLED_LEVEL_PREVIEW_PATH = "images/selectlevel/closed_level.png";

    // endregion


    // region Fields

    private LevelItemView mLevelItemView;
    private LevelClickHandler mItemClickHandler;

    private LevelAdapterModel mAdapterModel;

    // endregion


    // region Constructors

    LevelViewHolder(LevelItemView itemView, LevelClickHandler itemClickHandler) {
        super(itemView);

        mLevelItemView = itemView;

        mItemClickHandler = itemClickHandler;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickHandler.onClick(view, getAdapterPosition());
            }
        });
    }

    // endregion


    // region Public Methods

    public void bind(LevelAdapterModel adapterModel) {
        mAdapterModel = adapterModel;

        bindPreview();
        bindCompleted();
    }

    // endregion


    // region Private Methods

    private void bindPreview() {
        if (mAdapterModel.getState() == LevelState.DISABLED) {
            mLevelItemView.setPreview(DISABLED_LEVEL_PREVIEW_PATH);
        } else {
            mLevelItemView.setPreview(mAdapterModel.getPreviewPath());
        }
    }

    private void bindCompleted() {
        switch (mAdapterModel.getState()) {
            case ONE_STAR:
            case TWO_STARS:
            case THREE_STARS:
                mLevelItemView.setCompleted(true);
                break;
            default:
            case OPEN:
            case DISABLED:
                mLevelItemView.setCompleted(false);
                break;
        }
    }

    // endregion

}
