package arsenlibs.com.mosaic.ui.selectlevel.list;

public interface LevelAdapterModel {

    boolean areItemsTheSame(LevelAdapterModel adapterModel);

    boolean areContentsTheSame(LevelAdapterModel adapterModel);

    String getId();

    String getPreviewPath();

    int getNumber();

}
