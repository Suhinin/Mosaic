package com.childaplic.mosaic.ui.selectlevel;

import java.util.Collection;

import com.childaplic.mosaic.presenters.selectlevel.LevelItem;

public interface SelectLevelContract {

    interface View {

        void onInit();
        void onInitError(String message);

    }

    interface Presenter {

        void onAttachView(View view);
        void onDetachView();

        LevelItem[] getLevels();
        void selectLevel(LevelItem levelItem);

        LevelItem getVisibleLevel();

    }

}
