package arsenlibs.com.mosaic.ui.board;

import arsenlibs.com.mosaic.presenters.board.PalettePieceItem;

public interface BoardContract {

    interface View {

        void onInit();

        void onInitError(String message);

    }

    interface Presenter {

        void onAttachView(View view);

        void onDetachView();

        PalettePieceItem[] getPalette();

        String[][] getBoard();

    }
}
