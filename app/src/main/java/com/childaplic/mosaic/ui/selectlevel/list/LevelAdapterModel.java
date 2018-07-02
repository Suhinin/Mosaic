package com.childaplic.mosaic.ui.selectlevel.list;

import com.childaplic.mosaic.repositories.levels.domain.LevelState;

public interface LevelAdapterModel {

    boolean areItemsTheSame(LevelAdapterModel adapterModel);

    boolean areContentsTheSame(LevelAdapterModel adapterModel);

    String getId();

    String getPreviewPath();

    int getNumber();

    LevelState getState();

}
