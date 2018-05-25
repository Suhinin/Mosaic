package arsenlibs.com.mosaic.services.assets;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
            return mContext.getAssets().list(folder);
        }
        catch(IOException ex)
        {
            // TODO logger. Log error
            return null;
        }
    }

    @Override
    public String readText(String path) {
        try {
            InputStream inputStream = mContext.getAssets().open(path);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
