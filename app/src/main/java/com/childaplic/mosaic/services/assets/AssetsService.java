package com.childaplic.mosaic.services.assets;


import android.content.res.AssetFileDescriptor;

public interface AssetsService {

    String[] getList(String folder);

    String readText(String path);

    AssetFileDescriptor getFileDescriptor(String fileName);

}
