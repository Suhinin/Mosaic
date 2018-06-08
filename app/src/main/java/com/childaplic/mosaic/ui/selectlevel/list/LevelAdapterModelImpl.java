package com.childaplic.mosaic.ui.selectlevel.list;

import com.childaplic.mosaic.presenters.selectlevel.LevelItem;
import com.childaplic.mosaic.utils.EqualsChecker;

class LevelAdapterModelImpl implements LevelAdapterModel {

    // region Fields

    private String mId;
    private String mPreviewPath;
    private int mNumber;

    // endregion


    // region Constructors

    LevelAdapterModelImpl(LevelItem levelItem) {
        mId = levelItem.getId();
        mPreviewPath = levelItem.getPreviewPath();
        mNumber = levelItem.getNumber();
    }

    // endregion


    // region Public Methods

    @Override
    public String getId() {
        return mId;
    }

    public String getPreviewPath() {
        return mPreviewPath;
    }

    @Override
    public int getNumber() {
        return mNumber;
    }

    @Override
    public boolean areItemsTheSame(LevelAdapterModel adapterModel) {
        return adapterModel != null && mId.equals(adapterModel.getId());
    }

    @Override
    public boolean areContentsTheSame(LevelAdapterModel adapterModel) {
        if (EqualsChecker.equals(mPreviewPath, adapterModel.getPreviewPath()) == false) {
            return false;
        }
        if (mNumber != adapterModel.getNumber()) {
            return false;
        }

        return true;
    }

    // endregion


    // region Public Methods



    // endregion

}
