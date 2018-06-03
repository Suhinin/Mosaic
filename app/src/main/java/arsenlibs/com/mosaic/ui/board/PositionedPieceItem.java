package arsenlibs.com.mosaic.ui.board;

import arsenlibs.com.mosaic.presenters.board.PalettePieceItem;

public class PositionedPieceItem extends PalettePieceItem {

    private int mRow;
    private int mCol;

    public PositionedPieceItem(int row, int col, PalettePieceItem palettePieceItem) {
        mRow = row;
        mCol = col;

        setId(palettePieceItem.getId());
        setColor(palettePieceItem.getColor());
        setImagePath(palettePieceItem.getImagePath());
    }

    public int getRow() {
        return mRow;
    }

    public int getCol() {
        return mCol;
    }

}
