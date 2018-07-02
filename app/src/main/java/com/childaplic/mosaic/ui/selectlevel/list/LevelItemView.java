package com.childaplic.mosaic.ui.selectlevel.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.childaplic.mosaic.ui.common.Size;
import com.childaplic.mosaic.utils.GlideLoaderHelper;
import com.childaplic.mosaic.utils.LayoutHelper;

public class LevelItemView extends FrameLayout {

    // region Constants

    private final String COMPLETED_LEVEL_ICON_PATH = "images/selectlevel/ok.png";

    // endregion


    // region Fields

    private Size mItemSize;

    // endregion


    // region View Components

    private ImageView mImagePreview;
    private ImageView mImageComplete;

    // endregion


    // region Constructors

    public LevelItemView(@NonNull Context context, Size itemSize) {
        super(context);

        mItemSize = itemSize;
        createView();
    }

    // endregion


    // region Public Methods

    public void setPreview(String assetsPath) {
        GlideLoaderHelper.loadAssets(getContext(), assetsPath, mImagePreview);
    }

    public void setCompleted(boolean isCompleted) {
        if (isCompleted) {
            mImageComplete.setVisibility(VISIBLE);
            GlideLoaderHelper.loadAssets(getContext(), COMPLETED_LEVEL_ICON_PATH, mImageComplete);
        } else {
            mImageComplete.setVisibility(GONE);
        }
    }

    // endregion


    // region Private Methods

    private void createView() {
        addPreviewImage();
        addCompleteImage();
    }

    private void addPreviewImage() {
        mImagePreview = new ImageView(getContext());
        mImagePreview.setScaleType(ImageView.ScaleType.FIT_CENTER);

        addView(mImagePreview, LayoutHelper.createFramePx(mItemSize, Gravity.CENTER));
    }

    private void addCompleteImage() {
        mImageComplete = new ImageView(getContext());
        mImageComplete.setScaleType(ImageView.ScaleType.FIT_CENTER);

        addView(mImageComplete, LayoutHelper.createFramePx(mItemSize.getWidth()/2, mItemSize.getHeight()/2, Gravity.CENTER));
    }

    // endregion

}
