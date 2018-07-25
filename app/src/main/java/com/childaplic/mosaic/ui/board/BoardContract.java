package com.childaplic.mosaic.ui.board;

import com.childaplic.mosaic.presenters.board.CellItem;
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

        CellItem[][] getBoard();

        void resetBoard();

        void levelCompleted();

        void hookPiece(int row, int col);

        boolean isSoundEnabled();

        boolean toggleSoundEnabled();

        void logTerminateLevel();

    }
}
