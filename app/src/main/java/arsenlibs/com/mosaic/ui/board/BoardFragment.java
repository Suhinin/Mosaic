package arsenlibs.com.mosaic.ui.board;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import arsenlibs.com.mosaic.R;
import arsenlibs.com.mosaic.presenters.board.BoardPresenter;
import arsenlibs.com.mosaic.presenters.board.PalettePieceItem;
import arsenlibs.com.mosaic.services.imageloader.ImageLoaderService;
import arsenlibs.com.mosaic.ui.common.Margin;
import arsenlibs.com.mosaic.ui.common.Size;
import arsenlibs.com.mosaic.utils.LayoutHelper;
import arsenlibs.com.mosaic.utils.ScreenUtil;
import dagger.android.support.DaggerFragment;

public class BoardFragment extends DaggerFragment implements BoardContract.View {

    // region Constants

    private final String TAG = BoardFragment.class.getCanonicalName();

    private final String ASSETS_BACKGROUND = "images/board/bg_board.png";

    private final int PALETTE_COLUMNS_COUNT = 2;

    // endregion


    // region Injections

    @Inject
    protected BoardPresenter mPresenter;
    @Inject
    protected ImageLoaderService mImageLoaderService;

    // endregion


    // region View Components

    private FrameLayout mRootView;

    private FrameLayout mPaletteContainer;
    private BoardView mBoardView;

    private int mFragmentVerticalPadding;

    private Size mBoardViewSize;
    private Margin mBoardViewMargin;

    private Size mPaletteContainerSize;
    private Margin mPaletteContainerMargin;
    private int mPalettePieceSize;
    private int mPalettePieceMargin;

    private Map<String, Bitmap> mCachedPieceImages;
    private List<View> mPalettePieceViews;
    private List<PalettePieceTouchListener> mPalettePieceTouchListeners;

    // endregion


    // region Fields

    private BoardInteraction mInteractionListener;

    private SoundPool mSoundPool;
    private int mSoundCorrectChecked;

    // endregion


    // region Constructors

    public static BoardFragment newInstance() {
        Bundle args = new Bundle();

        BoardFragment fragment = new BoardFragment();
        fragment.setArguments(args);

        return fragment;
    }

    // endregion


    // region Implements DaggerFragment

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BoardInteraction) {
            mInteractionListener = (BoardInteraction) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement BoardInteraction");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return createView();
    }

    @Override
    public void onResume() {
        super.onResume();

        initSoundPool();
        mPresenter.onAttachView(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        releaseSoundPool();
        mPresenter.onDetachView();
    }

    // endregion


    // region Init View

    private View createView() {
        mRootView = new FrameLayout(getContext());

        calculateFragmentVerticalPadding();
        calculatePaletteSize();
        calcPaletteViewMargin();

        addBackground();
        addPaletteContainer();

        mCachedPieceImages = new HashMap<>();
        addPalettePieces();

        addBoardView();
        initBoardView();

        return mRootView;
    }

    private void calculateFragmentVerticalPadding() {
        int paddingPx = getResources().getInteger(R.integer.board_fragment__vertical_margin_px);
        mFragmentVerticalPadding = ScreenUtil.getScreenY(getContext(), paddingPx);
    }

    private void calculatePaletteSize() {
        int pieceSizePx = getResources().getInteger(R.integer.board_fragment__palette_piece_size_px);
        mPalettePieceSize = ScreenUtil.getScreenX(getContext(), pieceSizePx);

        int pieceMarginPx = getResources().getInteger(R.integer.board_fragment__palette_piece_margin_px);
        mPalettePieceMargin = ScreenUtil.getScreenX(getContext(), pieceMarginPx);

        int width = mPalettePieceSize*PALETTE_COLUMNS_COUNT + (PALETTE_COLUMNS_COUNT+1)* mPalettePieceMargin;

        Size screenSize = ScreenUtil.getScreenSize(getContext());
        int height = screenSize.getHeight() - 2*mFragmentVerticalPadding;

        mPaletteContainerSize = new Size(width, height);
    }

    private void calcPaletteViewMargin() {
        int marginPx = getResources().getInteger(R.integer.board_fragment__palette_horizontal_margin_px);
        int margin = ScreenUtil.getScreenX(getContext(), marginPx);

        mPaletteContainerMargin = new Margin(margin, mFragmentVerticalPadding, margin, mFragmentVerticalPadding);
    }

    private void addBackground() {
        ImageView imageBackground = new ImageView(getContext());
        imageBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mRootView.addView(imageBackground, LayoutHelper.createFramePx(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        int width = ScreenUtil.getScreenSize(getContext()).getWidth();
        int height = ScreenUtil.getScreenSize(getContext()).getHeight();
        mImageLoaderService.loadAssets(ASSETS_BACKGROUND, width, height, imageBackground);
    }

    private void addBoardView() {
        mBoardView = new BoardView(getContext());
        mRootView.addView(mBoardView);
    }

    private void addPaletteContainer() {
        mPaletteContainer = new FrameLayout(getContext());
        mPaletteContainer.setBackgroundResource(R.drawable.bg_board_fragment__palette);
        mRootView.addView(mPaletteContainer, LayoutHelper.createFramePx(mPaletteContainerSize, Gravity.START | Gravity.TOP, mPaletteContainerMargin));
    }

    private void addPalettePieces() {
        mPalettePieceViews = new ArrayList<>();
        mPalettePieceTouchListeners = new ArrayList<>();
        int i=0;
        for (PalettePieceItem pieceItem : mPresenter.getPalette()) {
            addPalettePiece(pieceItem, i++);
        }
    }

    private void addPalettePiece(final PalettePieceItem palettePieceItem, int index) {
        cachePieceImage(palettePieceItem);

        int column = index % PALETTE_COLUMNS_COUNT;
        final int leftMargin = (column+1)*mPalettePieceMargin + column*mPalettePieceSize;

        int row = index / PALETTE_COLUMNS_COUNT;
        final int topMargin = (row+1)*mPalettePieceMargin + row*mPalettePieceSize;

        Bitmap pieceImage = mCachedPieceImages.get(palettePieceItem.getId());

        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageBitmap(pieceImage);

        PalettePieceTouchListener touchListener = new PalettePieceTouchListener(palettePieceItem, leftMargin, topMargin);
        imageView.setOnTouchListener(touchListener);
        mPalettePieceTouchListeners.add(touchListener);

        mPaletteContainer.addView(imageView, LayoutHelper.createFramePx(mPalettePieceSize, mPalettePieceSize, Gravity.START | Gravity.TOP, leftMargin, topMargin, 0, 0));
        mPalettePieceViews.add(imageView);
    }

    private ImageView addMovedPalettePiece(PalettePieceItem palettePieceItem, int leftMargin, int topMargin) {
        Bitmap pieceImage = mCachedPieceImages.get(palettePieceItem.getId());

        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageBitmap(pieceImage);

        mRootView.addView(imageView, LayoutHelper.createFramePx(mPalettePieceSize, mPalettePieceSize, Gravity.START | Gravity.TOP, leftMargin, topMargin, 0, 0));

        return imageView;
    }

    private void cachePieceImage(PalettePieceItem palettePieceItem) {
        if (mCachedPieceImages.containsKey(palettePieceItem.getId())) {
            return;
        }

        Bitmap image = loadCellImage(palettePieceItem);
        mCachedPieceImages.put(palettePieceItem.getId(), image);
    }

    private Bitmap loadCellImage(PalettePieceItem palettePieceItem) {
        Bitmap bitmap = mImageLoaderService.getAssets(palettePieceItem.getImagePath(), mPalettePieceSize, mPalettePieceSize);
        if (bitmap == null) {
            return null;
        }

        return Bitmap.createScaledBitmap(bitmap, mPalettePieceSize, mPalettePieceSize, true);
    }

    // endregion


    // region Implements BoardContract.View

    @Override
    public void onInit() {
        initBoardView();
        addPalettePieces();
    }

    @Override
    public void onInitError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLevelCompleted() {
        mInteractionListener.onLevelCompleted();
    }

    // endregion


    // region Private Methods

    private void initBoardView() {
        calcBoardViewSize();
        calcBoardViewMargin();

        ViewGroup.LayoutParams layoutParams = LayoutHelper.createFramePx(mBoardViewSize, Gravity.START | Gravity.TOP, mBoardViewMargin);
        mBoardView.setLayoutParams(layoutParams);

        mBoardView.init(mPresenter.getPalette(), mPresenter.getBoard());
    }

    private void calcBoardViewSize() {
        int boardPaddingPx = getResources().getInteger(R.integer.board_fragment__board_view_vertical_margin_px);
        int boardPadding =  ScreenUtil.getScreenY(getContext(), boardPaddingPx);

        int rows = mPresenter.getBoard().length;
        int cols = mPresenter.getBoard().length > 0 ? mPresenter.getBoard()[0].length : 0;

        Size screenSize = ScreenUtil.getScreenSize(getContext());
        int availableHeight = screenSize.getHeight() - 2*mFragmentVerticalPadding - 2*boardPadding;
        float cellSize = rows > 0 ? availableHeight/rows : 0;

        int width = (int) (2*boardPadding + cellSize*cols);
        int height = (int) (2*boardPadding + cellSize*rows);

        mBoardViewSize = new Size(width, height);
    }

    private void calcBoardViewMargin() {
        Size screenSize = ScreenUtil.getScreenSize(getContext());
        float paletteContainerRight = mPaletteContainerMargin.getLeft() + mPaletteContainerSize.getWidth();
        float availableWidth = screenSize.getWidth() - paletteContainerRight;

        int left = (int) (paletteContainerRight + (availableWidth - mBoardViewSize.getWidth())/2f);
        int top = mFragmentVerticalPadding;

        mBoardViewMargin = new Margin(left, top, 0, 0);
    }

    private void enableLostBeadsTouches() {
        for (int i=0; i<mPalettePieceViews.size(); i++) {
            enablePieceTouch(i);
        }
    }

    private void enablePieceTouch(int index) {
        PalettePieceTouchListener touchListener = mPalettePieceTouchListeners.get(index);
        mPalettePieceViews.get(index).setOnTouchListener(touchListener);
    }

    private void disablePaletteTouches(View excludedView) {
        for (View lostBeadView : mPalettePieceViews) {
            if (lostBeadView != excludedView) {
                lostBeadView.setOnTouchListener(null);
            }
        }
    }

    private void initSoundPool() {
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);

//        mSoundCorrectChecked = mSoundPool.load(getContext(), R.raw.sound_professions_correct_checked, 1);
    }

    private void playSound(int soundId) {
        mSoundPool.play(soundId, 1, 1, 0, 0, 1);
    }

    private void releaseSoundPool() {
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
    }

    // endregion


    // region Nested Classes

    private class PalettePieceTouchListener implements View.OnTouchListener {

        private PalettePieceItem mPalettePieceItem;
        private int mLeftMargin;
        private int mTopMargin;

        private View mMovedPiece;
        private float dX, dY;

        PalettePieceTouchListener(PalettePieceItem palettePieceItem, int leftMargin, int topMargin) {
            mPalettePieceItem = palettePieceItem;
            mLeftMargin = mPaletteContainerMargin.getLeft() + leftMargin;
            mTopMargin = mPaletteContainerMargin.getTop() + topMargin;
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mMovedPiece = addMovedPalettePiece(mPalettePieceItem, mLeftMargin, mTopMargin);
                    disablePaletteTouches(view);

                    int[] viewScreenLocation = new int[2];
                    view.getLocationOnScreen(viewScreenLocation);

                    dX = viewScreenLocation[0] - event.getRawX();
                    dY = viewScreenLocation[1] - event.getRawY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    move(mMovedPiece, event.getRawX() + dX, event.getRawY() + dY);
                    if (isInsideBoard()) {
                        toBoardPieceViewSize();
                    } else {
                        toPalettePieceViewSize();
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    Rect movedPieceRect = new Rect();
                    mMovedPiece.getGlobalVisibleRect(movedPieceRect);
                    movedPieceRect.offset(-mBoardViewMargin.getLeft(), -mBoardViewMargin.getTop());

                    PositionedPieceItem pieceInside = mBoardView.getPieceInside(movedPieceRect);
                    if (pieceInside != null) {
                        if (pieceInside.getColor() == mPalettePieceItem.getColor()) {
//                            playSound(mSoundCorrectChecked);

                            mBoardView.hookPiece(pieceInside);
                            mRootView.removeView(mMovedPiece);

                            if (mBoardView.isCompleted()) {
                                mPresenter.levelCompleted();
//                                playSound(getCorrectSound());
                            }
                        } else {
                            mPresenter.incIncorrectCount();
//                            int incorrectSound = getWrongSoundId();
//                            playSound(incorrectSound);
                            moveHome(mMovedPiece);
                        }
                    } else {
//                        playSound(R.raw.ball_back);
                        moveHome(mMovedPiece);
                    }

                    enableLostBeadsTouches();
                    return true;
                default:
                    return false;
            }
        }

        private boolean isInsideBoard() {
            Rect movedPieceRect = new Rect();
            mMovedPiece.getGlobalVisibleRect(movedPieceRect);

            Rect boardRect = new Rect();
            mBoardView.getGlobalVisibleRect(boardRect);

            return boardRect.contains(movedPieceRect);
        }

        private void toBoardPieceViewSize() {
            float xScaleFactor = (float)mBoardView.getCellSize()/mMovedPiece.getWidth();
            float yScaleFactor = (float)mBoardView.getCellSize()/mMovedPiece.getHeight();

            mMovedPiece.setScaleX(xScaleFactor);
            mMovedPiece.setScaleY(yScaleFactor);
        }

        private void toPalettePieceViewSize() {
            float xScaleFactor = mPalettePieceSize/mMovedPiece.getWidth();
            float yScaleFactor = mPalettePieceSize/mMovedPiece.getHeight();

            mMovedPiece.setScaleX(xScaleFactor);
            mMovedPiece.setScaleY(yScaleFactor);
        }

        private void move(View view, float x, float y) {
            moveView(view, x, y, 0);
        }

        private void moveHome(View view) {
            moveView(view, mLeftMargin, mTopMargin, 250, new Runnable() {
                @Override
                public void run() {
                    mRootView.removeView(mMovedPiece);
                }
            });
        }

        private void moveView(View view, float x, float y, long duration) {
            view.animate()
                    .x(x)
                    .y(y)
                    .setDuration(duration)
                    .start();
        }

        private void moveView(View view, float x, float y, long duration, Runnable endAction) {
            view.animate()
                    .x(x)
                    .y(y)
                    .setDuration(duration)
                    .withEndAction(endAction)
                    .start();
        }
    }

    // endregion

}