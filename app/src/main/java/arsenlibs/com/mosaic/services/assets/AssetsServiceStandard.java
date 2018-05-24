package arsenlibs.com.mosaic.services.assets;

import android.content.Context;

import java.io.IOException;

import javax.inject.Inject;


public class AssetsServiceStandard implements AssetsService {

    private Context mContext;

    @Inject
    public AssetsServiceStandard(Context context) {
        mContext = context;
    }

    @Override
    public String[] getList(String folder) {
        try
        {
            String[] imgPath = mContext.getAssets().list(folder);
            return imgPath;
        }
        catch(IOException ex)
        {
            // TODO logger. Log error
            return null;
        }
    }

}
