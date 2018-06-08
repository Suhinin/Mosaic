package com.childaplic.mosaic.ui.board;

import com.childaplic.mosaic.presenters.board.PalettePieceItem;

public interface BoardContract {

    interface View {

        void onInit();

        void onInitError(String message);

        void onLevelCompleted();

    }

    interface Presenter {

        void onAttachView(View view);

        void onDetachView();

        PalettePieceItem[] getPalette();

        String[][] getBoard();

        void incIncorrectCount();

        void levelCompleted();

    }
}
