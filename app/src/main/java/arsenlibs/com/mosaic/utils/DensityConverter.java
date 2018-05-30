package arsenlibs.com.mosaic.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by maxim on 28.12.2017.
 */

public class DensityConverter {

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param context Context to getByGroup resources and device specific display metrics
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int dpToPx(Context context, float dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        return (int)px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param context Context to getByGroup resources and device specific display metrics
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public static float pxToDp(Context context, int px){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
