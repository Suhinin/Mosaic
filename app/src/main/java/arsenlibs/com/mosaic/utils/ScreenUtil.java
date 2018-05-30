package arsenlibs.com.mosaic.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import arsenlibs.com.mosaic.R;


public class ScreenUtil {

    private static final String TAG = ScreenUtil.class.getCanonicalName();

    public static int getScreenX(Context context, int frameX) {
        int screenWidth = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) ? getRealScreenSize(context).x : getAppUsableScreenSize(context).x;
        int frameWidth = context.getResources().getInteger(R.integer.frame_width_px);

        return getScreenX(frameX, frameWidth, screenWidth);
    }

    public static int getScreenX(int frameX, int frameWidth, int screenWidth) {
        float y = screenWidth * frameX / frameWidth;

        return (int)y;
    }

    public static int getScreenY(Context context, int frameY) {
        int screenHeight = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) ? getRealScreenSize(context).y : getAppUsableScreenSize(context).y;
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

    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    public static Point getScreenSize(Context context) {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) ? getRealScreenSize(context) : getAppUsableScreenSize(context);
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        return size;
    }

}
