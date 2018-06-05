package arsenlibs.com.mosaic.ui.selectlevel.list;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import arsenlibs.com.mosaic.presenters.selectlevel.LevelItem;
import arsenlibs.com.mosaic.utils.LayoutHelper;

public class LevelsAdapter extends RecyclerView.Adapter<LevelViewHolder> {

    // region Constants

    private final String TAG = LevelsAdapter.class.getCanonicalName();

    // endregion


    // region Fields

    private List<LevelAdapterModel> mItems;
    private List<LevelItem> mModelItems;

    private LevelClickHandler mItemClickHandler;

    // endregion


    // region Constructors

    public LevelsAdapter(LevelClickHandler itemClickHandler) {
        mItemClickHandler = itemClickHandler;

        mItems = new ArrayList<>();
        mModelItems = new ArrayList<>();
    }

    // endregion


    // region Implements RecyclerView.Adapter

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LevelItemView view = new LevelItemView(parent.getContext());
        return new LevelViewHolder(view, mItemClickHandler);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
        LevelAdapterModel item = mItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // endregion


    // region Public Methods

    public void updateItems(LevelItem[] modelItems) {
        mModelItems.clear();
        List<LevelAdapterModel> adapterModels = new ArrayList<>();
        if (modelItems != null) {
            mModelItems.addAll(Arrays.asList(modelItems));

            for (LevelItem modelItem : modelItems) {
                adapterModels.add(new LevelAdapterModelImpl(modelItem));
            }
        }

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new LevelsListDiffCallback(adapterModels, mItems));
        mItems = adapterModels;
        diffResult.dispatchUpdatesTo(this);
    }

    public LevelItem getItem(int position) {
        return mModelItems.get(position);
    }

    public List<LevelItem> getItems() {
        return mModelItems;
    }

    public int getItemPosition(LevelItem levelItem) {
        for (int i=0; i<mModelItems.size(); i++) {
            if (mModelItems.get(i).getId().equals(levelItem.getId())) {
                return i;
            }
        }

        return RecyclerView.NO_POSITION;
    }

    public void removeItem (int position) throws IndexOutOfBoundsException {
        mItems.remove(position);
        mModelItems.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        mItems.clear();
        mModelItems.clear();
        notifyDataSetChanged();
    }

    // endregion


    // region Private Methods


    // endregion

}
