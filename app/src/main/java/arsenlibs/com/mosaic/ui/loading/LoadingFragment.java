package arsenlibs.com.mosaic.ui.loading;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import javax.inject.Inject;

import arsenlibs.com.mosaic.presenters.loading.LoadingPresenter;
import arsenlibs.com.mosaic.services.imageloader.ImageLoaderService;
import dagger.android.support.DaggerFragment;

public class LoadingFragment extends DaggerFragment implements LoadingContract.View {

    // region Constants

    private final String ASSETS_BACKGROUND = "images/loading/bg_loading.png";

    // endregion

    // region Injections

    @Inject
    protected LoadingPresenter mPresenter;
    @Inject
    protected ImageLoaderService mImageLoaderService;

    // endregion

    // region View Components

    private FrameLayout mRootView;
    private ImageView mImageBackground;

    // endregion

    // region Constructors

    public static LoadingFragment newInstance() {
        Bundle args = new Bundle();

        LoadingFragment fragment = new LoadingFragment();
        fragment.setArguments(args);

        return fragment;
    }

    // endregion

    // region Implements DaggerAppCompatActivity

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
        mImageBackground = new ImageView(getContext());
        mRootView.addView(mImageBackground);
        mImageBackground.setCropToPadding(true);
        mImageBackground.setScaleType(ImageView.ScaleType.FIT_XY);
        mImageBackground.setAdjustViewBounds(true);
        loadBackground();
        return mRootView;
    }

    private void loadBackground() {
        mImageLoaderService.loadAssets(ASSETS_BACKGROUND, mImageBackground);
    }

    // endregion
}