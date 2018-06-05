package arsenlibs.com.mosaic.ui.selectlevel.list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import arsenlibs.com.mosaic.R;
import arsenlibs.com.mosaic.ui.common.Size;
import arsenlibs.com.mosaic.utils.GlideLoaderHelper;
import arsenlibs.com.mosaic.utils.LayoutHelper;
import arsenlibs.com.mosaic.utils.ScreenUtil;

public class LevelItemView extends FrameLayout {

    // region Constants

    private final int SCREEN_COLUMN_COUNT = 3;
    private final int SCREEN_ROW_COUNT = 2;

    // endregion


    // region Fields

    private int mFragmentMargin;
    private int mGridMargin;

    private Size mItemSize;

    // endregion


    // region View Components

    private ImageView mImagePreview;
    private TextView mLabelNumber;

    // endregion


    // region Constructors

    public LevelItemView(@NonNull Context context) {
        super(context);

        createView();
    }

    // endregion


    // region Public Methods

    public void setPreview(String assetsPath) {
        GlideLoaderHelper.loadAssets(getContext(), assetsPath, mImagePreview);
    }

    public void setNumber(int number) {
        mLabelNumber.setText(String.valueOf(number));
    }

    // endregion


    // region Private Methods

    private void createView() {
        calcItemSize();
        addPreviewImage();
        addNumberView();
    }

    private void calcItemSize() {
        int fragmentMarginPx = getResources().getInteger(R.integer.select_level_fragment__margin_px);
        mFragmentMargin = ScreenUtil.getScreenY(getContext(), fragmentMarginPx);

        int gridMarginPx = getResources().getInteger(R.integer.select_level_fragment__grid_margin_px);
        mGridMargin = ScreenUtil.getScreenY(getContext(), gridMarginPx);

        Size screenSize = ScreenUtil.getScreenSize(getContext());
        int width = (screenSize.getWidth() - 2*mFragmentMargin - (SCREEN_COLUMN_COUNT -1)*mGridMargin) / SCREEN_COLUMN_COUNT;
        int height = (screenSize.getHeight() - 2*mFragmentMargin - (SCREEN_ROW_COUNT -1)*mGridMargin) / SCREEN_ROW_COUNT;
        mItemSize = new Size(width, height);
    }

    private void addPreviewImage() {
        mImagePreview = new ImageView(getContext());
        mImagePreview.setScaleType(ImageView.ScaleType.FIT_CENTER);

        addView(mImagePreview, LayoutHelper.createFramePx(mItemSize, Gravity.CENTER));
    }

    private void addNumberView() {
        mLabelNumber = new TextView(getContext());
        mLabelNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        mLabelNumber.setTypeface(null, Typeface.BOLD);
        mLabelNumber.setTextColor(Color.BLACK);

        addView(mLabelNumber, LayoutHelper.createFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.BOTTOM));
    }

    // endregion

}
