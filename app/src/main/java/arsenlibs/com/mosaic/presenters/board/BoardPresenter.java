package arsenlibs.com.mosaic.presenters.board;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.Arrays;

import javax.inject.Inject;

import arsenlibs.com.mosaic.businesslogics.LevelsLogic;
import arsenlibs.com.mosaic.repositories.levels.LevelsRepository;
import arsenlibs.com.mosaic.repositories.levels.domain.Level;
import arsenlibs.com.mosaic.repositories.levels.domain.PalettePiece;
import arsenlibs.com.mosaic.ui.board.BoardContract;
import arsenlibs.com.mosaic.ui.board.BoardViewNull;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BoardPresenter implements BoardContract.Presenter {

    // region Constants

    private final String TAG = BoardPresenter.class.getCanonicalName();

    // endregion


    // region Fields

    private BoardContract.View mView;

    private InitState mInitState;
    private String mInitErrorMsg;

    private PalettePieceItem[] mPalette;
    private String[][] mBoard;

    // endregion


    // region Injections

    private LevelsLogic mLevelsLogic;

    // endregion


    // region Constructors

    @Inject
    public BoardPresenter(LevelsLogic levelsLogic) {
        mLevelsLogic = levelsLogic;

        mPalette = new PalettePieceItem[0];
        mBoard = new String[0][];

        mInitState = InitState.NONE;
    }

    // endregion


    // region Implements BoardContract.Presenter

    @Override
    public void onAttachView(BoardContract.View view) {
        mView = view;

        if (mInitState == InitState.NONE) {
            startInitTask();
        }
    }

    @Override
    public void onDetachView() {
        mView = new BoardViewNull();
    }

    @Override
    public PalettePieceItem[] getPalette() {
        return mPalette;
    }

    @Override
    public String[][] getBoard() {
        return mBoard;
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
        Level level = mLevelsLogic.getCurrentLevel();
        mBoard = level.getBoard();
        createPalette(level.getPalette());
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

    // endregion

}
