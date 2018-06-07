package arsenlibs.com.mosaic.ui.selectlevel.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import arsenlibs.com.mosaic.ui.common.Size;
import arsenlibs.com.mosaic.utils.GlideLoaderHelper;
import arsenlibs.com.mosaic.utils.LayoutHelper;

public class LevelItemView extends FrameLayout {

    // region Fields

    private Size mItemSize;

    // endregion


    // region View Components

    private ImageView mImagePreview;

    // endregion


    // region Constructors

    public LevelItemView(@NonNull Context context, Size itemSize) {
        super(context);

        mItemSize = itemSize;
        createView();
    }

    // endregion


    // region Public Methods

    public void setPreview(String assetsPath) {
        GlideLoaderHelper.loadAssets(getContext(), assetsPath, mImagePreview);
    }

    // endregion


    // region Private Methods

    private void createView() {
        addPreviewImage();
    }

    private void addPreviewImage() {
        mImagePreview = new ImageView(getContext());
        mImagePreview.setScaleType(ImageView.ScaleType.FIT_CENTER);

        addView(mImagePreview, LayoutHelper.createFramePx(mItemSize, Gravity.CENTER));
    }

    // endregion

}
