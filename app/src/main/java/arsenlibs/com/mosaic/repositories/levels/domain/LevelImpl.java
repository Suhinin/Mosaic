package arsenlibs.com.mosaic.repositories.levels.domain;


public class LevelImpl implements Level {

    // region Fields

    private String mId;
    private int mNumber;
    private String mPreviewPath;
    private LevelState mState;
    private boolean mIsShowOnBoarding;
    private int mIncorrectAnswers;
    private PalettePiece[] mPalette;
    private String[][] mBoard;

    // endregion


    // region Implements Level

    @Override
    public int getIncorrectAnswers() {
        return mIncorrectAnswers;
    }

    @Override
    public void setIncorrectAnswers(int count) {
        mIncorrectAnswers = count;
    }

    @Override
    public int getNumber() {
        return mNumber;
    }

    @Override
    public String getPreviewPath() {
        return mPreviewPath;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public LevelState getState() {
        return mState;
    }

    @Override
    public void setState(LevelState levelState) {
        mState = levelState;
    }

    @Override
    public boolean isShowOnBoarding() {
        return mIsShowOnBoarding;
    }

    @Override
    public void setShowOnBoarding(boolean showOnBoarding) {
        mIsShowOnBoarding = showOnBoarding;
    }

    @Override
    public PalettePiece[] getPalette() {
        return mPalette;
    }

    @Override
    public String[][] getBoard() {
        return mBoard;
    }

    // endregion


    // region Public Methods

    public void setId(String id) {
        mId = id;
    }

    public void setNumber(int number) {
        mNumber = number;
    }

    public void setPreviewPath(String previewPath) {
        mPreviewPath = previewPath;
    }

    public void setPalette(PalettePiece[] palette) {
        mPalette = palette;
    }

    public void setBoard(String[][] board) {
        mBoard = board;
    }

    // endregion

}
