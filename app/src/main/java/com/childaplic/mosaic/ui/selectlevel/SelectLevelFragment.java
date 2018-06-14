package com.childaplic.mosaic.ui.selectlevel;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.childaplic.mosaic.presenters.selectlevel.LevelItem;
import com.childaplic.mosaic.services.imageloader.ImageLoaderService;
import com.childaplic.mosaic.services.shared.SharedService;
import com.childaplic.mosaic.ui.common.Size;
import com.childaplic.mosaic.ui.selectlevel.list.LevelClickHandler;
import com.childaplic.mosaic.ui.selectlevel.list.LevelsAdapter;
import com.childaplic.mosaic.ui.selectlevel.list.LevelsOffsetDecoration;
import com.childaplic.mosaic.utils.LayoutHelper;
import com.childaplic.mosaic.utils.ScreenUtil;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class SelectLevelFragment extends DaggerFragment implements SelectLevelContract.View {

    // region Constants

    private final String ASSETS_BACKGROUND = "images/board/bg_board.png";

    private final String SHARED__FIRST_VISIBLE_LIST_POSITION = "shared__first_visible_list_position";

    // endregion


    // region Injections

    @Inject
    protected SelectLevelContract.Presenter mPresenter;
    @Inject
    protected ImageLoaderService mImageLoaderService;
    @Inject
    protected SharedService mSharedService;

    // endregion


    // region Fields

    private SelectLevelInteraction mInteractionListener;
    private LevelsAdapter mLevelsAdapter;

    // endregion


    // region View Components

    private FrameLayout mRootView;

    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private SnapHelper mSnapHelper;

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

        mSharedService.remove(SHARED__FIRST_VISIBLE_LIST_POSITION);

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

        saveListScrollState();
        mPresenter.onDetachView();
    }

    // endregion


    // region Implements SelectLevelContract.View

    @Override
    public void onInit() {
        updateList();
        restoreListScrollState();
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
        restoreListScrollState();

        return mRootView;
    }

    private void addBackground() {
        ImageView imageBackground = new ImageView(getContext());
        imageBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mRootView.addView(imageBackground, LayoutHelper.createFramePx(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        Size screenSize = ScreenUtil.getScreenSize(getContext());
        mImageLoaderService.loadAssets(ASSETS_BACKGROUND, screenSize.getWidth(), screenSize.getHeight(), imageBackground);
    }

    private void addLevelsList() {
        mLevelsAdapter = new LevelsAdapter(getContext(), mLevelClickHandler);

        initGridLayoutManager();
        addLevelsListView();
        initSnapHelper();
    }

    private void initGridLayoutManager() {
        mGridLayoutManager = new GridLayoutManager(getContext(), LevelsAdapter.SCREEN_ROW_COUNT);
        mGridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    }

    private void addLevelsListView() {
        LevelsOffsetDecoration offsetDecoration = new LevelsOffsetDecoration(getContext());

        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mLevelsAdapter);
        mRecyclerView.addItemDecoration(offsetDecoration);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(@io.reactivex.annotations.NonNull RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        });
        mRecyclerView.setClipToPadding(false);

        int gridOffset = offsetDecoration.getGridOffset();
        mRecyclerView.setPadding(gridOffset/2, 0, gridOffset/2, 0);

        mRootView.addView(mRecyclerView, LayoutHelper.createFramePx(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
    }

    private void initSnapHelper() {
        mSnapHelper = new LinearSnapHelper();
        mSnapHelper.attachToRecyclerView(mRecyclerView);
    }

    // endregion


    // region Private Methods

    private void updateList() {
        LevelItem[] levelItems = mPresenter.getLevels();
        mLevelsAdapter.updateItems(levelItems);
    }

    private void restoreListScrollState() {
        int position = mSharedService.getInt(SHARED__FIRST_VISIBLE_LIST_POSITION, RecyclerView.NO_POSITION);
        if (position != RecyclerView.NO_POSITION && mLevelsAdapter.getItemCount() > 0) {
            mGridLayoutManager.scrollToPosition(position);
        }
    }

    private void saveListScrollState() {
        int position = mGridLayoutManager.findFirstCompletelyVisibleItemPosition();
        mSharedService.putInt(SHARED__FIRST_VISIBLE_LIST_POSITION, position);
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
