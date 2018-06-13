package com.childaplic.mosaic.ui.board;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import com.childaplic.mosaic.R;
import com.childaplic.mosaic.presenters.board.CellItem;
import com.childaplic.mosaic.presenters.board.PalettePieceItem;
import com.childaplic.mosaic.repositories.levels.domain.LevelData;
import com.childaplic.mosaic.utils.ImageLoaderHelper;

public class BoardView extends View {

    // region Constants

    private int GRID_COLOR = Color.GRAY;
    private int EMPTY_COLOR = -1;

    // endregion


    // region Fields

    private Map<String, PalettePieceItem> mPalette;

    private int mRows;
    private int mCols;
    private CellItem[][] mBoard;

    private CellView[][] mCellViews;
    private Map<String, Bitmap> mCachedPieceImages;

    private int mGridStokeWidth;
    private int mColoredStokeWidth;
    private int mCellSize;

    private Paint mCellPaint;

    private Canvas mCanvas;
    private int mMargin;

    // endregion


    // region Constructors

    public BoardView(Context context) {
        super(context);

        createCellPaint();
        mCachedPieceImages = new HashMap<>();
        initStokes();
    }

    // endregion


    // region Public Methods

    public void init(PalettePieceItem[] palette, CellItem[][] board) {
        createPalette(palette);

        mRows = board.length;
        mCols = board.length > 0 ? board[0].length : 0;
        mBoard = board;

        initBoardCells();

        invalidate();
    }

    public int getCellSize() {
        return mCellSize;
    }

    public PositionedPieceItem getPieceInside(Rect rect) {
        int row = (rect.centerY() - mMargin) / mCellSize;
        if (row < 0 || row >= mBoard.length) {
            return null;
        }

        int col = (rect.centerX() - mMargin) / mCellSize;
        if (col < 0 || col >= mBoard[0].length) {
            return null;
        }

        String palettePieceId = mBoard[row][col].getPieceId();
        PalettePieceItem palettePieceItem = mPalette.get(palettePieceId);
        if (palettePieceItem == null || mCellViews[row][col].isPicked()) {
            return null;
        }

        return new PositionedPieceItem(row, col, palettePieceItem);
    }

    public void hookPiece(PositionedPieceItem positionedPieceItem) {
        mCellViews[positionedPieceItem.getRow()][positionedPieceItem.getCol()].setPicked(true);

        invalidate();
    }

    public boolean isCompleted() {
        for (int i = 0; i< mCellViews.length; i++) {
            for (int j = 0; j< mCellViews[i].length; j++) {
                CellView cellView = mCellViews[i][j];
                if (cellView.isEmpty() == false && cellView.isPicked() == false) {
                    return false;
                }
            }
        }

        return true;
    }

    // endregion


    // region Extends View

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCanvas = canvas;

        drawBackground();

        initVerticalMargin();
        calcCellSize();
        drawGrid();
    }

    // endregion


    // region Private Methods

    private void createCellPaint() {
        mCellPaint = new Paint();
        mCellPaint.setStyle(Paint.Style.STROKE);         // set to STOKE
        mCellPaint.setDither(true);                      // set the dither to true
        mCellPaint.setAntiAlias(true);                   // set anti alias so it smooths
    }

    private void initStokes() {
        mGridStokeWidth = (int) getResources().getDimension(R.dimen.board_fragment__board_view_grid_stoke_width);
        mColoredStokeWidth = (int) getResources().getDimension(R.dimen.board_fragment__board_view_colored_stoke_width);
    }

    private void createPalette(PalettePieceItem[] palette) {
        mPalette = new HashMap<>();
        for (PalettePieceItem pieceItem : palette) {
            mPalette.put(pieceItem.getId(), pieceItem);
        }
    }

    private void initBoardCells() {
        mCellViews = new CellView[mRows][mCols];

        for (int i=0; i<mRows; i++) {
            for (int j=0; j<mCols; j++) {
                addCell(i, j);
            }
        }
    }

    private void addCell(int row, int col) {
        if (mBoard[row][col].isEmpty()) {
            mCellViews[row][col] = createEmptyCell();
        } else if (mBoard[row][col].isPicked()) {
            mCellViews[row][col] = createPickedCell(mBoard[row][col].getPieceId());
        } else {
            mCellViews[row][col] = createColoredCell(mBoard[row][col].getPieceId());
        }
    }

    private CellView createEmptyCell() {
        return new CellView();
    }

    private CellView createPickedCell(String palettePieceId) {
        PalettePieceItem palettePieceItem = mPalette.get(palettePieceId);
        return new CellView(palettePieceItem.getColor(), true);
    }

    private CellView createColoredCell(String palettePieceId) {
        PalettePieceItem palettePieceItem = mPalette.get(palettePieceId);
        return new CellView(palettePieceItem.getColor());
    }

    private void drawBackground() {
        mCanvas.drawARGB(255, 253, 250, 243);
    }

    private void drawGrid() {
        for (int i=0; i<mRows; i++) {
            for (int j=0; j<mCols; j++) {
                drawCell(i, j);
            }
        }
    }

    private void drawCell(int row, int col) {
        CellView cellView = mCellViews[row][col];
        if (cellView.isEmpty()) {
            drawEmptyCell(row, col);
        } else if (cellView.isPicked()) {
            drawPickedCell(row, col);
        } else {
            drawColoredCell(cellView, row, col);
        }
    }

    private void drawEmptyCell(int row, int col) {
        mCellPaint.setColor(GRID_COLOR);
        mCellPaint.setStrokeWidth(mGridStokeWidth);

        int left = mMargin + col * mCellSize;
        int top = mMargin + row * mCellSize;
        int right = mMargin + (col+1) * mCellSize;
        int bottom = mMargin + (row+1) * mCellSize;

        mCanvas.drawRect(left, top, right, bottom, mCellPaint);
    }

    private void drawColoredCell(CellView cellView, int row, int col) {
        drawEmptyCell(row, col);

        mCellPaint.setColor(cellView.getColor());
        mCellPaint.setStrokeWidth(mColoredStokeWidth);

        float shift = mGridStokeWidth + mColoredStokeWidth/2f;

        float left = mMargin + col * mCellSize + shift;
        float top = mMargin + row * mCellSize + shift;
        float right = mMargin + (col+1) * mCellSize - shift;
        float bottom = mMargin + (row+1) * mCellSize - shift;

        mCanvas.drawRect(left, top, right, bottom, mCellPaint);
    }

    private void drawPickedCell(int row, int col) {
        drawEmptyCell(row, col);

        String palettePieceId = mBoard[row][col].getPieceId();
        cachePieceImage(palettePieceId);
        Bitmap bitmap = mCachedPieceImages.get(palettePieceId);

        int left = mMargin + col * mCellSize;
        int top = mMargin + row * mCellSize;

        mCanvas.drawBitmap(bitmap, left, top, null);
    }

    private void cachePieceImage(String palettePieceId) {
        if (mCachedPieceImages.containsKey(palettePieceId)) {
            return;
        }

        PalettePieceItem palettePieceItem = mPalette.get(palettePieceId);
        Bitmap image = loadCellImage(palettePieceItem);
        mCachedPieceImages.put(palettePieceId, image);
    }

    private Bitmap loadCellImage(PalettePieceItem palettePieceItem) {
        Bitmap bitmap = ImageLoaderHelper.loadAssets(getContext(), palettePieceItem.getImagePath(), mCellSize, mCellSize);
        if (bitmap == null) {
            return null;
        }

        return Bitmap.createScaledBitmap(bitmap, mCellSize, mCellSize, true);
    }

    private void initVerticalMargin() {
        mMargin = (int) getResources().getDimension(R.dimen.board_fragment__board_view_vertical_margin);
    }

    private void calcCellSize() {
        int availableHeight = mCanvas.getHeight() - 2 * mMargin;
        mCellSize = mRows > 0 ? availableHeight/mRows : 0;
    }

    // endregion


    // region Nested Classes

    private class CellView {
        private int mColor;
        private boolean mIsPicked;
        private boolean mIsEmpty;


        CellView() {
            mColor = EMPTY_COLOR;
            mIsPicked = false;
            mIsEmpty = true;
        }

        CellView(int color) {
            mColor = color;
            mIsPicked = false;
            mIsEmpty = false;
        }

        CellView(int color, boolean isPicked) {
            mColor = color;
            mIsPicked = isPicked;
            mIsEmpty = false;
        }

        public int getColor() {
            return mColor;
        }

        public void setColor(int color) {
            mColor = color;
        }

        public boolean isPicked() {
            return mIsPicked;
        }

        public void setPicked(boolean picked) {
            mIsPicked = picked;
        }

        public boolean isEmpty() {
            return mIsEmpty;
        }

        public void setEmpty(boolean empty) {
            mIsEmpty = empty;
        }
    }

    // endregion

}
