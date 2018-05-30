package arsenlibs.com.mosaic.utils;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import arsenlibs.com.mosaic.ui.common.Margin;
import arsenlibs.com.mosaic.ui.common.Size;

public class LayoutHelper {

    public static final int MATCH_PARENT = -1;
    public static final int WRAP_CONTENT = -2;

    private static int getSize (Context context, float size) {
        return (int) (size < 0 ? size : DensityConverter.dpToPx(context, size));
    }

    public static FrameLayout.LayoutParams createScroll(Context context, int widthDp, int heightDp, int gravity) {
        return new ScrollView.LayoutParams(getSize(context, widthDp), getSize(context, heightDp), gravity);
    }

    public static FrameLayout.LayoutParams createFramePx(int widthPx, int heightPx, int gravity, int leftMarginPx, int topMarginPx, int rightMarginPx, int bottomMarginPx) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(widthPx, heightPx, gravity);
        layoutParams.setMargins(leftMarginPx, topMarginPx, rightMarginPx, bottomMarginPx);
        return layoutParams;
    }

    public static FrameLayout.LayoutParams createFramePx(int widthPx, int heightPx, int gravity, Margin margin) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(widthPx, heightPx, gravity);
        layoutParams.setMargins(margin.getLeft(), margin.getTop(), margin.getRight(), margin.getBottom());
        return layoutParams;
    }

    public static FrameLayout.LayoutParams createFramePx(Size size, int gravity, Margin margin) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size.getWidth(), size.getHeight(), gravity);
        layoutParams.setMargins(margin.getLeft(), margin.getTop(), margin.getRight(), margin.getBottom());
        return layoutParams;
    }

    public static FrameLayout.LayoutParams createFrame(Context context, int widthDp, int heightDp, int gravity, int leftMarginDp, int topMarginDp, int rightMarginDp, int bottomMarginDp) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(getSize(context, widthDp), getSize(context, heightDp), gravity);
        layoutParams.setMargins(DensityConverter.dpToPx(context, leftMarginDp), DensityConverter.dpToPx(context, topMarginDp), DensityConverter.dpToPx(context, rightMarginDp), DensityConverter.dpToPx(context, bottomMarginDp));
        return layoutParams;
    }

    public static FrameLayout.LayoutParams createFramePx(int widthPx, int heightPx, int gravity) {
        return new FrameLayout.LayoutParams(widthPx, heightPx, gravity);
    }

    public static FrameLayout.LayoutParams createFramePx(Size size, int gravity) {
        return new FrameLayout.LayoutParams(size.getWidth(), size.getHeight(), gravity);
    }

    public static FrameLayout.LayoutParams createFrame (Context context, int widthDp, int heightDp, int gravity) {
        return new FrameLayout.LayoutParams(getSize(context, widthDp), getSize(context, heightDp), gravity);
    }

    public static FrameLayout.LayoutParams createFramePx(int widthPx, int heightPx) {
        return new FrameLayout.LayoutParams(widthPx, heightPx);
    }

    public static FrameLayout.LayoutParams createFrame (Context context, int widthDp, int heightDp) {
        return new FrameLayout.LayoutParams(getSize(context, widthDp), getSize(context, heightDp));
    }

    public static RelativeLayout.LayoutParams createRelative (Context context, float widthDp, float heightDp, int leftMargin, int topMargin, int rightMargin, int bottomMargin, int alignParent, int alignRelative, int anchorRelative) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getSize(context, widthDp), getSize(context, heightDp));

        if (alignParent >= 0) {
            layoutParams.addRule(alignParent);
        }
        if (alignRelative >= 0 && anchorRelative >= 0) {
            layoutParams.addRule(alignRelative, anchorRelative);
        }

        layoutParams.leftMargin = DensityConverter.dpToPx(context, leftMargin);
        layoutParams.topMargin = DensityConverter.dpToPx(context, topMargin);
        layoutParams.rightMargin = DensityConverter.dpToPx(context, rightMargin);
        layoutParams.bottomMargin = DensityConverter.dpToPx(context, bottomMargin);

        return layoutParams;
    }

    public static RelativeLayout.LayoutParams createRelativePx(int widthPx, int heightPx, int leftMarginPx, int topMarginPx, int rightMarginPx, int bottomMarginPx, int alignParent, int alignRelative, int anchorRelative) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(widthPx, heightPx);

        if (alignParent >= 0) {
            layoutParams.addRule(alignParent);
        }
        if (alignRelative >= 0 && anchorRelative >= 0) {
            layoutParams.addRule(alignRelative, anchorRelative);
        }

        layoutParams.setMargins(leftMarginPx, topMarginPx, rightMarginPx, bottomMarginPx);

        return layoutParams;
    }

    public static RelativeLayout.LayoutParams createRelative(Context context, int width, int height, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        return createRelative(context, width, height, leftMargin, topMargin, rightMargin, bottomMargin, -1, -1, -1);
    }

    public static RelativeLayout.LayoutParams createRelativePx(int widthPx, int heightPx, int leftMarginPx, int topMarginPx, int rightMarginPx, int bottomMarginPx) {
        return createRelativePx(widthPx, heightPx, leftMarginPx, topMarginPx, rightMarginPx, bottomMarginPx, -1, -1, -1);
    }

    public static RelativeLayout.LayoutParams createRelative(Context context, int width, int height, int leftMargin, int topMargin, int rightMargin, int bottomMargin, int alignParent) {
        return createRelative(context, width, height, leftMargin, topMargin, rightMargin, bottomMargin, alignParent, -1, -1);
    }

    public static RelativeLayout.LayoutParams createRelative(Context context, float width, float height, int leftMargin, int topMargin, int rightMargin, int bottomMargin, int alignRelative, int anchorRelative) {
        return createRelative(context, width, height, leftMargin, topMargin, rightMargin, bottomMargin, -1, alignRelative, anchorRelative);
    }

    public static RelativeLayout.LayoutParams createRelative(Context context, int width, int height, int alignParent, int alignRelative, int anchorRelative) {
        return createRelative(context, width, height, 0, 0, 0, 0, alignParent, alignRelative, anchorRelative);
    }

    public static RelativeLayout.LayoutParams createRelative(Context context, int width, int height) {
        return createRelative(context, width, height, 0, 0, 0, 0, -1, -1, -1);
    }

    public static RelativeLayout.LayoutParams createRelative(Context context, int width, int height, int alignParent) {
        return createRelative(context, width, height, 0, 0, 0, 0, alignParent, -1, -1);
    }

    public static RelativeLayout.LayoutParams createRelative(Context context, int width, int height, int alignRelative, int anchorRelative) {
        return createRelative(context, width, height, 0, 0, 0, 0, -1, alignRelative, anchorRelative);
    }

    public static LinearLayout.LayoutParams createLinear(Context context, int width, int height, float weight, int gravity, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height), weight);
        layoutParams.setMargins(DensityConverter.dpToPx(context, leftMargin), DensityConverter.dpToPx(context, topMargin), DensityConverter.dpToPx(context, rightMargin), DensityConverter.dpToPx(context, bottomMargin));
        layoutParams.gravity = gravity;
        return layoutParams;
    }

    public static LinearLayout.LayoutParams createLinear(Context context, int width, int height, float weight, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height), weight);
        layoutParams.setMargins(DensityConverter.dpToPx(context, leftMargin), DensityConverter.dpToPx(context, topMargin), DensityConverter.dpToPx(context, rightMargin), DensityConverter.dpToPx(context, bottomMargin));
        return layoutParams;
    }

    public static LinearLayout.LayoutParams createLinear(Context context, int width, int height, int gravity, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        layoutParams.setMargins(DensityConverter.dpToPx(context, leftMargin), DensityConverter.dpToPx(context, topMargin), DensityConverter.dpToPx(context, rightMargin), DensityConverter.dpToPx(context, bottomMargin));
        layoutParams.gravity = gravity;
        return layoutParams;
    }

    public static LinearLayout.LayoutParams createLinear(Context context, int width, int height, float leftMargin, float topMargin, float rightMargin, float bottomMargin) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        layoutParams.setMargins(DensityConverter.dpToPx(context, leftMargin), DensityConverter.dpToPx(context, topMargin), DensityConverter.dpToPx(context, rightMargin), DensityConverter.dpToPx(context, bottomMargin));
        return layoutParams;
    }

    public static LinearLayout.LayoutParams createLinearPx(int widthPx, int heightPx, int leftMarginPx, int topMarginPx, int rightMarginPx, int bottomMarginPx) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPx, heightPx);
        layoutParams.setMargins(leftMarginPx, topMarginPx, rightMarginPx, bottomMarginPx);
        return layoutParams;
    }

    public static LinearLayout.LayoutParams createLinear(Context context, int width, int height, float weight, int gravity) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height), weight);
        layoutParams.gravity = gravity;
        return layoutParams;
    }

    public static LinearLayout.LayoutParams createLinear(Context context, int width, int height, int gravity) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        layoutParams.gravity = gravity;
        return layoutParams;
    }

    public static LinearLayout.LayoutParams createLinear(Context context, int width, int height, float weight) {
        return new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height), weight);
    }

    public static LinearLayout.LayoutParams createLinear(Context context, int width, int height) {
        return new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
    }

    public static LinearLayout.LayoutParams createLinearPx(int widthPx, int heightPx) {
        return new LinearLayout.LayoutParams(widthPx, heightPx);
    }

}
