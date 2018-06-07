package arsenlibs.com.mosaic.ui.selectlevel;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import javax.inject.Inject;

import arsenlibs.com.mosaic.presenters.selectlevel.LevelItem;
import arsenlibs.com.mosaic.services.imageloader.ImageLoaderService;
import arsenlibs.com.mosaic.ui.selectlevel.list.LevelClickHandler;
import arsenlibs.com.mosaic.ui.selectlevel.list.LevelsAdapter;
import arsenlibs.com.mosaic.ui.selectlevel.list.LevelsOffsetDecoration;
import arsenlibs.com.mosaic.utils.LayoutHelper;
import arsenlibs.com.mosaic.utils.ScreenUtil;
import dagger.android.support.DaggerFragment;

public class SelectLevelFragment extends DaggerFragment implements SelectLevelContract.View {

    // region Constants

    private final String ASSETS_BACKGROUND = "images/board/bg_board.png";

    private final int GRID_ROWS_COUNT = 2;

    // endregion


    // region Injections

    @Inject
    protected SelectLevelContract.Presenter mPresenter;
    @Inject
    protected ImageLoaderService mImageLoaderService;

    // endregion


    // region Fields

    private SelectLevelInteraction mInteractionListener;
    private LevelsAdapter mLevelsAdapter;

    // endregion


    // region View Components

    private FrameLayout mRootView;

    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;

    // endregion


    // region Constructors

    public static SelectLevelFragment newInstance() {
        Bundle args = new Bundle();

        SelectLevelFragment fragment = new SelectLevelFragment();
        fragment.setArguments(args);

        return fragment;
    }

    // endregion


    // region Implements DaggerFragment

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof SelectLevelInteraction) {
            mInteractionListener = (SelectLevelInteraction) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SelectLevelInteraction");
        }
    }

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


    // region Implements SelectLevelContract.View

    @Override
    public void onInit() {
        updateList();
        scrollToCurrentLevel();
    }

    @Override
    public void onInitError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    // endregion


    // region Init View

    private View createView() {
        mRootView = new FrameLayout(getContext());

        addBackground();
        addLevelsList();
        updateList();
        scrollToCurrentLevel();

        return mRootView;
    }

    private void addBackground() {
        ImageView imageBackground = new ImageView(getContext());
        imageBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mRootView.addView(imageBackground, LayoutHelper.createFramePx(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        int width = ScreenUtil.getScreenSize(getContext()).getWidth();
        int height = ScreenUtil.getScreenSize(getContext()).getHeight();
        mImageLoaderService.loadAssets(ASSETS_BACKGROUND, width, height, imageBackground);
    }

    private void addLevelsList() {
        mLevelsAdapter = new LevelsAdapter(getContext(), mLevelClickHandler);

        initGridLayoutManager();
        addLevelsListView();
        initSnapHelper();
    }

    private void initGridLayoutManager() {
        mGridLayoutManager = new GridLayoutManager(getContext(), GRID_ROWS_COUNT);
        mGridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    }

    private void addLevelsListView() {
        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mLevelsAdapter);
        mRecyclerView.addItemDecoration(new LevelsOffsetDecoration(getContext(), GRID_ROWS_COUNT));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(@io.reactivex.annotations.NonNull RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        });

        mRootView.addView(mRecyclerView, LayoutHelper.createFramePx(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
    }

    private void initSnapHelper() {
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(mRecyclerView);
    }

    // endregion


    // region Private Methods

    private void updateList() {
        LevelItem[] levelItems = mPresenter.getLevels();
        mLevelsAdapter.updateItems(levelItems);
    }

    private void scrollToCurrentLevel() {
        LevelItem levelItem = mPresenter.getVisibleLevel();
        if (levelItem == null) {
            return;
        }

        int position = mLevelsAdapter.getItemPosition(levelItem);
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

        if (position > 2) {
            position -= 2;          // centered
        }

        mGridLayoutManager.scrollToPosition(position);
    }

    // endregion


    // region Listeners

    private LevelClickHandler mLevelClickHandler = new LevelClickHandler() {

        @Override
        public void onClick(View view, int adapterPosition) {
            LevelItem levelItem = mLevelsAdapter.getItem(adapterPosition);
            mPresenter.selectLevel(levelItem);

            mInteractionListener.onLevelSelected();
        }

    };

    // endregion

}
