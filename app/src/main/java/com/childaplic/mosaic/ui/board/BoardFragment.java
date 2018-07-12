package com.childaplic.mosaic.ui.board;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
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

import com.childaplic.mosaic.R;
import com.childaplic.mosaic.presenters.board.PalettePieceItem;
import com.childaplic.mosaic.services.assets.AssetsService;
import com.childaplic.mosaic.services.imageloader.ImageLoaderService;
import com.childaplic.mosaic.ui.common.Margin;
import com.childaplic.mosaic.ui.common.Size;
import com.childaplic.mosaic.utils.LayoutHelper;
import com.childaplic.mosaic.utils.ScreenUtil;
import dagger.android.support.DaggerFragment;

public class BoardFragment extends DaggerFragment implements BoardContract.View {

    // region Constants

    private final String TAG = BoardFragment.class.getCanonicalName();

    private final String ASSETS_BACKGROUND = "images/board/bg_board.png";
    private final String ASSETS_BOARD_FOLDER = "images/board/";

    private final int PALETTE_COLUMNS_COUNT = 2;

    // endregion


    // region Injections

    @Inject
    protected BoardContract.Presenter mPresenter;
    @Inject
    protected ImageLoaderService mImageLoaderService;
    @Inject
    protected AssetsService mAssetsService;

    // endregion


    // region View Components

    private FrameLayout mRootView;

    private FrameLayout mPaletteContainer;
    private BoardView mBoardView;

    private int mFragmentPadding;
    private Size mBtnSize;

    private ImageView mBtnBack;
    private ImageView mBtnReplay;
    private ImageView mBtnMusic;

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
    private int mSoundIncorrectChecked;
    private int mSoundLevelComplete;

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
        calculateBtnSize();
        calculatePaletteSize();
        calcPaletteViewMargin();

        addBackground();
        addPaletteContainer();

        mCachedPieceImages = new HashMap<>();
        addPalettePieces();

        addBoardView();
        initBoardView();

        addBackButton();
        addMusicButton();
        addReplayButton();

        return mRootView;
    }

    private void calculateFragmentVerticalPadding() {
        mFragmentPadding = (int) getResources().getDimension(R.dimen.board_fragment__padding);
    }

    private void calculateBtnSize() {
        int width = (int) getResources().getDimension(R.dimen.board_fragment__btn_width);
        int height = (int) getResources().getDimension(R.dimen.board_fragment__btn_height);

        mBtnSize = new Size(width, height);
    }

    private void calculatePaletteSize() {
        mPalettePieceSize = (int) getResources().getDimension(R.dimen.board_fragment__palette_piece_size);
        mPalettePieceMargin = (int) getResources().getDimension(R.dimen.board_fragment__palette_piece_margin);

        int width = mPalettePieceSize*PALETTE_COLUMNS_COUNT + (PALETTE_COLUMNS_COUNT+1)*mPalettePieceMargin;

        Size screenSize = ScreenUtil.getScreenSize(getContext());
        int height = screenSize.getHeight() - 2* mFragmentPadding;

        mPaletteContainerSize = new Size(width, height);
    }

    private void calcPaletteViewMargin() {
        mPaletteContainerMargin = new Margin(mFragmentPadding, mFragmentPadding, mFragmentPadding, mFragmentPadding);
    }

    private void addBackground() {
        ImageView imageBackground = new ImageView(getContext());
        imageBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mRootView.addView(imageBackground, LayoutHelper.createFramePx(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        Size screenSize = ScreenUtil.getScreenSize(getContext());
        mImageLoaderService.loadAssets(ASSETS_BACKGROUND, screenSize.getWidth(), screenSize.getHeight(), imageBackground);
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

    private void addBackButton() {
        mBtnBack = new ImageView(getContext());
        mBtnBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mBtnBack.setImageDrawable(createBtnBackDrawable());
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mRootView.addView(mBtnBack, LayoutHelper.createFramePx(mBtnSize, Gravity.END | Gravity.TOP, mFragmentPadding, mFragmentPadding, mFragmentPadding, mFragmentPadding));
    }

    private Drawable createBtnBackDrawable() {
        Bitmap pressed = mImageLoaderService.getAssets(ASSETS_BOARD_FOLDER + "back_pressed.png", mBtnSize.getWidth(), mBtnSize.getHeight());
        Bitmap normal = mImageLoaderService.getAssets(ASSETS_BOARD_FOLDER + "back.png", mBtnSize.getWidth(), mBtnSize.getHeight());

        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, new BitmapDrawable(getResources(), pressed));
        drawable.addState(new int[]{}, new BitmapDrawable(getResources(), normal));

        return drawable;
    }

    private void addMusicButton() {
        mBtnMusic = new ImageView(getContext());
        mBtnMusic.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mBtnMusic.setSelected(mPresenter.isSoundEnabled());
        mBtnMusic.setImageDrawable(createBtnMusicDrawable());
        mBtnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isPlaying = mPresenter.toggleSoundEnabled();
                view.setSelected(isPlaying);
            }
        });

        mRootView.addView(mBtnMusic, LayoutHelper.createFramePx(mBtnSize, Gravity.END | Gravity.BOTTOM, mFragmentPadding, mFragmentPadding, mFragmentPadding, mFragmentPadding));
    }

    private Drawable createBtnMusicDrawable() {
        Bitmap enabledPressed = mImageLoaderService.getAssets(ASSETS_BOARD_FOLDER + "music_pressed.png", mBtnSize.getWidth(), mBtnSize.getHeight());
        Bitmap enabledNormal = mImageLoaderService.getAssets(ASSETS_BOARD_FOLDER + "music.png", mBtnSize.getWidth(), mBtnSize.getHeight());
        Bitmap disabledPressed = mImageLoaderService.getAssets(ASSETS_BOARD_FOLDER + "no_music_pressed.png", mBtnSize.getWidth(), mBtnSize.getHeight());
        Bitmap disabledNormal = mImageLoaderService.getAssets(ASSETS_BOARD_FOLDER + "no_music.png", mBtnSize.getWidth(), mBtnSize.getHeight());

        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_selected}, new BitmapDrawable(getResources(), enabledPressed));
        drawable.addState(new int[]{android.R.attr.state_selected}, new BitmapDrawable(getResources(), enabledNormal));
        drawable.addState(new int[]{android.R.attr.state_pressed}, new BitmapDrawable(getResources(), disabledPressed));
        drawable.addState(new int[]{}, new BitmapDrawable(getResources(), disabledNormal));

        return drawable;
    }

    private void addReplayButton() {
        mBtnReplay = new ImageView(getContext());
        mBtnReplay.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mBtnReplay.setImageDrawable(createBtnReplayDrawable());
        mBtnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.resetBoard();
                mBoardView.init(mPresenter.getPalette(), mPresenter.getBoard());
            }
        });

        mRootView.addView(mBtnReplay, LayoutHelper.createFramePx(mBtnSize, Gravity.END | Gravity.CENTER_VERTICAL, mFragmentPadding, mFragmentPadding, mFragmentPadding, mFragmentPadding));
    }

    private Drawable createBtnReplayDrawable() {
        Bitmap pressed = mImageLoaderService.getAssets(ASSETS_BOARD_FOLDER + "replay_pressed.png", mBtnSize.getWidth(), mBtnSize.getHeight());
        Bitmap normal = mImageLoaderService.getAssets(ASSETS_BOARD_FOLDER + "replay.png", mBtnSize.getWidth(), mBtnSize.getHeight());

        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, new BitmapDrawable(getResources(), pressed));
        drawable.addState(new int[]{}, new BitmapDrawable(getResources(), normal));

        return drawable;
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
        int boardPadding = (int) getResources().getDimension(R.dimen.board_fragment__board_view_vertical_margin);

        int rows = mPresenter.getBoard().length;
        int cols = mPresenter.getBoard().length > 0 ? mPresenter.getBoard()[0].length : 0;

        float cellSize = calculateCellSize(rows, boardPadding);

        int width = (int) (2*boardPadding + cellSize*cols);
        int height = (int) (2*boardPadding + cellSize*rows);

        mBoardViewSize = new Size(width, height);
    }

    private float calculateCellSize(int rows, int boardPadding) {
        Size screenSize = ScreenUtil.getScreenSize(getContext());
        int availableHeight = screenSize.getHeight() - 2* mFragmentPadding - 2*boardPadding;
        return rows > 0 ? availableHeight/rows : 0;
    }

    private void calcBoardViewMargin() {
        Size screenSize = ScreenUtil.getScreenSize(getContext());
        float paletteContainerRight = mPaletteContainerMargin.getLeft() + mPaletteContainerSize.getWidth();
        int buttonsContainerWidth = mBtnSize.getWidth() + mFragmentPadding;
        float availableWidth = screenSize.getWidth() - paletteContainerRight - buttonsContainerWidth;

        int left = (int) (paletteContainerRight + (availableWidth - mBoardViewSize.getWidth())/2f);
        int top = mFragmentPadding;

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

        mSoundCorrectChecked = loadSound("sounds/board/попадение в точку.wav");
        mSoundIncorrectChecked = loadSound("sounds/board/Phazed bang.wav");
        mSoundLevelComplete = loadSound("sounds/board/level_complete.wav");
    }

    private int loadSound(String fileName) {
        AssetFileDescriptor fileDescriptor = mAssetsService.getFileDescriptor(fileName);
        if (fileDescriptor == null) {
            return 0;
        }

        return mSoundPool.load(fileDescriptor, 1);
    }

    private void playSound(int soundId) {
        if (mPresenter.isSoundEnabled() == false) {
            return;
        }

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
                    view.setOnTouchListener(null);

                    Rect movedPieceRect = new Rect();
                    mMovedPiece.getGlobalVisibleRect(movedPieceRect);
                    movedPieceRect.offset(-mBoardViewMargin.getLeft(), -mBoardViewMargin.getTop());

                    PositionedPieceItem pieceInside = mBoardView.getPieceInside(movedPieceRect);
                    if (pieceInside != null) {
                        if (pieceInside.getColor() == mPalettePieceItem.getColor()) {
                            mBoardView.hookPiece(pieceInside);
                            mRootView.removeView(mMovedPiece);
                            enableLostBeadsTouches();

                            mPresenter.hookPiece(pieceInside.getRow(), pieceInside.getCol());
                            if (mBoardView.isCompleted()) {
                                playSound(mSoundLevelComplete);
                                mPresenter.levelCompleted();
                            } else {
                                playSound(mSoundCorrectChecked);
                            }
                        } else {
                            playSound(mSoundIncorrectChecked);
                            moveHome(mMovedPiece);
                        }
                    } else {
                        moveHome(mMovedPiece);
                    }

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
            if (mMovedPiece.getWidth() == 0 || mMovedPiece.getHeight() == 0) {
                return;
            }

            float xScaleFactor = (float)mBoardView.getCellSize()/mMovedPiece.getWidth();
            float yScaleFactor = (float)mBoardView.getCellSize()/mMovedPiece.getHeight();

            mMovedPiece.setScaleX(xScaleFactor);
            mMovedPiece.setScaleY(yScaleFactor);
        }

        private void toPalettePieceViewSize() {
            if (mMovedPiece.getWidth() == 0 || mMovedPiece.getHeight() == 0) {
                return;
            }

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
                    enableLostBeadsTouches();
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