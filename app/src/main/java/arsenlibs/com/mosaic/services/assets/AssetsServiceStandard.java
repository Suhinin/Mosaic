package arsenlibs.com.mosaic.services.assets;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;


public class AssetsServiceStandard implements AssetsService {

    private Context mContext;
    private AssetManager mAssetsManager;

    @Inject
    public AssetsServiceStandard(Context context) {
        mContext = context;
        mAssetsManager = mContext.getAssets();
    }

    @Override
    public String[] getList(String folder) {
        try
        {
            return mAssetsManager.list(folder);
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
            InputStream inputStream = mAssetsManager.open(path);
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

    @Override
    public AssetFileDescriptor getFileDescriptor(String fileName) {
        try {
            return mAssetsManager.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
