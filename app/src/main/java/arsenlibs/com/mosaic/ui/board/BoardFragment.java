package arsenlibs.com.mosaic.ui.board;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

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

    // endregion


    // region Fields

    private int mFragmentVerticalPadding;

    private Size mBoardViewSize;
    private Margin mBoardViewMargin;

    private Size mPaletteContainerSize;
    private Margin mPaletteContainerMargin;

    private FrameLayout mPaletteContainer;
    private BoardView mBoardView;

    private List<PalettePieceItem> mPalettePieceItems;
    private List<View> mPalettePieceViews;
//    private List<PalettePieceTouchListener> mPalettePieceTouchListeners;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return createView();
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.onAttachView(this);
    }

    @Override
    public void onPause() {
        super.onPause();

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
        int pieceSize = ScreenUtil.getScreenX(getContext(), pieceSizePx);

        int columnsMarginPx = getResources().getInteger(R.integer.board_fragment__palette_columns_margin_px);
        int columnsMargin = ScreenUtil.getScreenX(getContext(), columnsMarginPx);

        int width = pieceSize*PALETTE_COLUMNS_COUNT + (PALETTE_COLUMNS_COUNT+1)*columnsMargin;

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

    private void addPalettePieces(PalettePieceItem[] pieceItems) {
//        mPalettePieceViews = new ArrayList<>();
//        mPalettePieceTouchListeners = new ArrayList<>();
//        for (int i=0; i<pieceItems.size(); i++) {
//            addLostBeadView(i);
//        }
    }

//    private void addLostBeadView(int index) {
//        LocatedBeadItem locatedBeadItem = mLostBeadItems.get(index);
//
//        Bitmap beadImage = mNecklaceView.loadBeadImage(locatedBeadItem.getBeadItem());
//
//        int leftMargin = mPaletteInitContainerMargin.getLeft() + locatedBeadItem.getPoint().x;
//        int topMargin = mPaletteInitContainerMargin.getTop() + locatedBeadItem.getPoint().y;
//
//        PalettePieceTouchListener touchListener = new PalettePieceTouchListener(leftMargin, topMargin, locatedBeadItem);
//        mPalettePieceTouchListeners.add(touchListener);
//
//        ImageView imageView = new ImageView(getContext());
//        imageView.setScaleType(ImageView.ScaleType.FIT_CENT ER);
//        imageView.setImageBitmap(beadImage);
//        imageView.setOnTouchListener(touchListener);
//
//        mLostBeadsContainer.addView(imageView, LayoutHelper.createFramePx(beadImage.getWidth(), beadImage.getHeight(), Gravity.START | Gravity.TOP, leftMargin, topMargin, 0, 0));
//        mLostBeadsViews.add(imageView);
//    }

    // endregion


    // region Implements BoardContract.View

    @Override
    public void onInit() {
        initBoardView();
    }

    @Override
    public void onInitError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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
//        for (int i=0; i<mLostBeadItems.size(); i++) {
//            enableLostBeadTouch(i);
//        }
    }

    private void enableLostBeadTouch(int index) {
//        PalettePieceTouchListener touchListener = mPalettePieceTouchListeners.get(index);
//        mLostBeadsViews.get(index).setOnTouchListener(touchListener);
    }

    private void disableLostBeadsTouches(View excludedView) {
//        for (View lostBeadView : mLostBeadsViews) {
//            if (lostBeadView != excludedView) {
//                lostBeadView.setOnTouchListener(null);
//            }
//        }
    }

    // endregion


    // region Nested Classes

//    private class PalettePieceTouchListener implements View.OnTouchListener {
//
//        private LocatedBeadItem mMovedBeadItem;
//        private int mStartX, mStartY;
//
//        private float dX, dY;
//
//        PalettePieceTouchListener(int startX, int startY, LocatedBeadItem movedBeadItem) {
//            mMovedBeadItem = movedBeadItem;
//            mStartX = startX;
//            mStartY = startY;
//        }
//
//        @Override
//        public boolean onTouch(View view, MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    disableLostBeadsTouches(view);
//
//                    dX = view.getX() - event.getRawX();
//                    dY = view.getY() - event.getRawY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    move(view, event.getRawX() + dX, event.getRawY() + dY);
//                    break;
//                case MotionEvent.ACTION_UP:
//                    int[] location = new int[2];
//                    mNecklaceView.getLocationOnScreen(location);
//                    int necklaceViewLeft = location[0];
//                    int necklaceViewTop = location[1];
//
//                    Rect beadViewRect = new Rect();
//                    view.getGlobalVisibleRect(beadViewRect);
//                    beadViewRect.offset(-necklaceViewLeft, -necklaceViewTop);
//
//                    IndexedBeadItem lostBeadItem = mNecklaceView.getLostBeadInside(beadViewRect);
//                    if (lostBeadItem != null) {
//                        if (lostBeadItem.getBeadItem().equals(mMovedBeadItem.getBeadItem())) {
//                            playSound(mSoundCorrectChecked);
//
//                            mNecklaceView.hitchBead(lostBeadItem);
//                            removeLostBead(mMovedBeadItem, view, this);
//                            boolean isLastBead = mPresenter.hitchBead(lostBeadItem);
//                            if (isLastBead) {
//                                playSound(getCorrectSound());
//                            }
//                        } else {
//                            mPresenter.incIncorrectCount();
//                            int incorrectSound = getWrongSoundId();
//                            playSound(incorrectSound);
//                            moveHome(view);
//                        }
//                    } else {
//                        playSound(R.raw.ball_back);
//                        moveHome(view);
//                    }
//
//                    enableLostBeadsTouches();
//                    break;
//                default:
//                    return false;
//            }
//
//            return true;
//        }
//
//        private void move(View view, float x, float y) {
//            moveView(view, x, y, 0);
//        }
//
//        private void moveHome(View view) {
//            moveView(view, mStartX, mStartY, 250);
//        }
//
//        private void moveView(View view, float x, float y, long duration) {
//            view.animate()
//                    .x(x)
//                    .y(y)
//                    .setDuration(duration)
//                    .start();
//        }
//
//    }

    // endregion

}