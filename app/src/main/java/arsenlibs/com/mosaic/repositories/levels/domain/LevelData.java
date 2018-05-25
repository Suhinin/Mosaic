package arsenlibs.com.mosaic.repositories.levels.domain;


public class LevelData {

    private String mId;
    private int mNumber;
    private PalettePiece[] mPalette;
    private String[][] mBoard;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
    }

    public PalettePiece[] getPalette() {
        return mPalette;
    }

    public void setPalette(PalettePiece[] palette) {
        mPalette = palette;
    }

    public String[][] getBoard() {
        return mBoard;
    }

    public void setBoard(String[][] board) {
        mBoard = board;
    }

}
