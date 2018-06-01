package arsenlibs.com.mosaic.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import arsenlibs.com.mosaic.R;
import arsenlibs.com.mosaic.ui.common.Size;


public class ScreenUtil {

    private static final String TAG = ScreenUtil.class.getCanonicalName();

    public static int getScreenX(Context context, int frameX) {
        int screenWidth = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) ? getRealScreenSize(context).getWidth() : getAppUsableScreenSize(context).getWidth();
        int frameWidth = context.getResources().getInteger(R.integer.frame_width_px);

        return getScreenX(frameX, frameWidth, screenWidth);
    }

    public static int getScreenX(int frameX, int frameWidth, int screenWidth) {
        float y = screenWidth * frameX / frameWidth;

        return (int)y;
    }

    public static int getScreenY(Context context, int frameY) {
        int screenHeight = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) ? getRealScreenSize(context).getHeight() : getAppUsableScreenSize(context).getHeight();
        int frameHeight = context.getResources().getInteger(R.integer.frame_height_px);

        return getScreenY(frameY, frameHeight, screenHeight);
    }

    public static int getScreenY(int frameY, int frameHeight, int screenHeight) {
        float height = screenHeight * frameY / frameHeight;

        return (int)height;
    }

    public static int getFrameX(int screenX, int frameWidth, int screenWidth) {
        float x = frameWidth * screenX / screenWidth;

        return (int)x;
    }

    public static int getFrameY(int screenY, int frameHeight, int screenHeight) {
        float y = frameHeight * screenY / screenHeight;

        return (int)y;
    }

    public static Size getNavigationBarSize(Context context) {
        Size appUsableSize = getAppUsableScreenSize(context);
        Size realScreenSize = getRealScreenSize(context);

        // navigation bar on the right
        if (appUsableSize.getWidth() < realScreenSize.getWidth()) {
            return new Size(realScreenSize.getWidth() - appUsableSize.getWidth(), appUsableSize.getHeight());
        }

        // navigation bar at the bottom
        if (appUsableSize.getHeight() < realScreenSize.getHeight()) {
            return new Size(appUsableSize.getWidth(), realScreenSize.getHeight() - appUsableSize.getHeight());
        }

        // navigation bar is not present
        return new Size(0, 0);
    }

    public static Size getScreenSize(Context context) {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) ? getRealScreenSize(context) : getAppUsableScreenSize(context);
    }

    public static Size getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);

        return new Size(point.x, point.y);
    }

    public static Size getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= 17) {
            Point point = new Point();
            display.getRealSize(point);

            return new Size(point.x, point.y);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                Integer width = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                Integer height = (Integer) Display.class.getMethod("getRawHeight").invoke(display);

                return new Size(width, height);
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        return new Size(0, 0);
    }

}
