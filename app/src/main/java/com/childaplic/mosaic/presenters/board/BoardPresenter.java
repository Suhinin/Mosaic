package com.childaplic.mosaic.presenters.board;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;

import javax.inject.Inject;

import com.childaplic.mosaic.businesslogics.LevelsLogic;
import com.childaplic.mosaic.repositories.levels.LevelsRepository;
import com.childaplic.mosaic.repositories.levels.domain.Cell;
import com.childaplic.mosaic.repositories.levels.domain.Level;
import com.childaplic.mosaic.repositories.levels.domain.LevelState;
import com.childaplic.mosaic.repositories.levels.domain.PalettePiece;
import com.childaplic.mosaic.ui.board.BoardContract;
import com.childaplic.mosaic.ui.board.BoardViewNull;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BoardPresenter implements BoardContract.Presenter {

    // region Constants

    private final String TAG = BoardPresenter.class.getCanonicalName();

    private final int STARS_COUNT = 3;

    // endregion


    // region Fields

    private BoardContract.View mView;

    private Level mLevel;
    private long mLevelRequestTimeMillis;

    private InitState mInitState;
    private String mInitErrorMsg;

    private PalettePieceItem[] mPalette;
    private CellItem[][] mBoard;

    private int mIncorrectAnswersCount;

    private Handler mNextLevelHandler;
    private boolean mSoundEnabled;

    // endregion


    // region Injections

    private LevelsLogic mLevelsLogic;
    private LevelsRepository mLevelsRepository;

    // endregion


    // region Constructors

    @Inject
    public BoardPresenter(LevelsLogic levelsLogic, LevelsRepository levelsRepository) {
        mLevelsLogic = levelsLogic;
        mLevelsRepository = levelsRepository;

        mPalette = new PalettePieceItem[0];
        mBoard = new CellItem[0][];
        mIncorrectAnswersCount = 0;

        mSoundEnabled = true;
        mInitState = InitState.NONE;
    }

    // endregion


    // region Implements BoardContract.Presenter

    @Override
    public void onAttachView(BoardContract.View view) {
        mView = view;

        if (mInitState == InitState.NONE || needRequestLevel()) {
            startInitTask();
        }

        resumeNextLevelHandler();
    }

    @Override
    public void onDetachView() {
        mView = new BoardViewNull();
        saveLevelState();
        releaseNextLevelHandler();
    }

    @Override
    public PalettePieceItem[] getPalette() {
        if (needRequestLevel()) {
            return  new PalettePieceItem[0];
        }

        return mPalette;
    }

    @Override
    public CellItem[][] getBoard() {
        if (needRequestLevel()) {
            return new CellItem[0][];
        }

        return mBoard;
    }

    @Override
    public void resetBoard() {
        mIncorrectAnswersCount = 0;

        mLevel = mLevelsRepository.resetLevel(mLevel.getId());
        initBoard();
        mLevelRequestTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void incIncorrectCount() {
        mIncorrectAnswersCount++;
    }

    @Override
    public void levelCompleted() {
        saveLevelState();
        mNextLevelHandler.postDelayed(mNextLevelRunnable, 4000);
    }

    @Override
    public void hookPiece(int row, int col) {
        mBoard[row][col].setPicked(true);
    }

    @Override
    public synchronized boolean toggleSoundEnabled() {
        mSoundEnabled = !mSoundEnabled;
        return mSoundEnabled;
    }

    @Override
    public boolean isSoundEnabled() {
        return mSoundEnabled;
    }

    // endregion


    // region Private Methods

    @SuppressLint("CheckResult")
    private void startInitTask() {
        mInitState = InitState.LOADING;

        Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        init();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        mInitState = InitState.COMPLETE;
                        mView.onInit();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "startInitTask error: " + throwable.getMessage());
                        mInitErrorMsg = throwable.getMessage();
                        mInitState = InitState.ERROR;
                        mView.onInitError(throwable.getMessage());
                    }
                });
    }

    private void init() {
        mLevel = mLevelsLogic.getCurrentLevel();
        initBoard();
        createPalette(mLevel.getPalette());

        mLevelRequestTimeMillis = System.currentTimeMillis();
    }

    private void initBoard() {
        int rows = mLevel.getBoard().length;
        int cols = mLevel.getBoard().length > 0 ? mLevel.getBoard()[0].length : 0;

        mBoard = new CellItem[rows][cols];
        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                Cell cell = mLevel.getBoard()[i][j];
                mBoard[i][j] = createCellItem(cell);
            }
        }
    }

    private CellItem createCellItem(Cell cell) {
        CellItem cellItem = new CellItem();
        cellItem.setPieceId(cell.getPieceId());
        cellItem.setPicked(cell.isPicked());

        return cellItem;
    }

    private void createPalette(PalettePiece[] palettePieces) {
        mPalette = new PalettePieceItem[palettePieces.length];
        for (int i=0; i<palettePieces.length; i++) {
            mPalette[i] = createPalettePieceItem(palettePieces[i]);
        }
    }

    private PalettePieceItem createPalettePieceItem(PalettePiece palettePiece) {
        PalettePieceItem pieceItem = new PalettePieceItem();
        pieceItem.setId(palettePiece.getId());
        pieceItem.setColor(palettePiece.getColor());
        pieceItem.setImagePath(palettePiece.getImagePath());

        return pieceItem;
    }

    private void resumeNextLevelHandler() {
        mNextLevelHandler = new Handler();
    }

    private void releaseNextLevelHandler() {
        if (mNextLevelHandler != null) {
            mNextLevelHandler.removeCallbacks(mNextLevelRunnable);
            mNextLevelHandler = null;
        }
    }

    private Runnable mNextLevelRunnable = new Runnable() {
        @Override
        public void run() {
            mView.onLevelCompleted();
        }
    };

    private void saveLevelState() {
        int levelStars = calculateLevelStars();
        mLevel.setState(getLevelState(levelStars));
        mLevel.setIncorrectAnswers(mIncorrectAnswersCount);
        mLevel.setBoard(getBoardCells());
        mLevelsRepository.saveLevel(mLevel);
    }

    private Cell[][] getBoardCells() {
        int rows = mBoard.length;
        int cols = mBoard.length > 0 ? mBoard[0].length : 0;

        Cell[][] cells = new Cell[rows][cols];
        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                CellItem cellItem = mBoard[i][j];
                cells[i][j] = createCell(cellItem);
            }
        }

        return cells;
    }

    private Cell createCell(CellItem cellItem) {
        Cell cell = new Cell();
        cell.setPieceId(cellItem.getPieceId());
        cell.setPicked(cellItem.isPicked());

        return cell;
    }

    private int calculateLevelStars() {
        int correctAnswers = calculateCorrectAnswers();

        int allAnswers = correctAnswers + mIncorrectAnswersCount;
        float percent = (float) correctAnswers / allAnswers;
        return Math.round(percent * STARS_COUNT);
    }

    private int calculateCorrectAnswers() {
        int answers = 0;
        for(int i=0; i<mBoard.length; i++) {
            for (int j=0; j<mBoard[i].length; j++) {
                answers += mBoard[i][j] != null ? 1 : 0;
            }
        }

        return answers;
    }

    private LevelState getLevelState(int stars) {
        switch (stars) {
            default:
            case 0: return LevelState.OPEN;
            case 1: return LevelState.ONE_STAR;
            case 2: return LevelState.TWO_STARS;
            case 3: return LevelState.THREE_STARS;
        }
    }

    private boolean needRequestLevel() {
        return mLevelsLogic.getLevelSelectedTimeMillis() > mLevelRequestTimeMillis;
    }

    // endregion

}
