package com.childaplic.mosaic.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideLoaderHelper {

    private final static String ASSETS_PREFIX = "file:///android_asset/";

    public static void loadAssets(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(Uri.parse(ASSETS_PREFIX + path))
                .into(imageView);
    }

}
