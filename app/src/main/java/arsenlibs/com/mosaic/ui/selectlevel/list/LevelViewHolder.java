package arsenlibs.com.mosaic.ui.selectlevel.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

class LevelViewHolder extends RecyclerView.ViewHolder {

    private LevelItemView mLevelItemView;

    private LevelClickHandler mItemClickHandler;

    LevelViewHolder(LevelItemView itemView, LevelClickHandler itemClickHandler) {
        super(itemView);

        mLevelItemView = itemView;

        mItemClickHandler = itemClickHandler;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickHandler.onClick(view, getAdapterPosition());
            }
        });
    }

    public void bind(LevelAdapterModel adapterModel) {
        mLevelItemView.setPreview(adapterModel.getPreviewPath());
        mLevelItemView.setNumber(adapterModel.getNumber());
    }

}
