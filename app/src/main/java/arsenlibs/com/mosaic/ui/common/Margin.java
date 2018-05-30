package arsenlibs.com.mosaic.ui.common;

public class Margin {

    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;

    public Margin(int left, int top, int right, int bottom) {
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;
    }

    public int getLeft() {
        return mLeft;
    }

    public void setLeft(int left) {
        mLeft = left;
    }

    public int getTop() {
        return mTop;
    }

    public void setTop(int top) {
        mTop = top;
    }

    public int getRight() {
        return mRight;
    }

    public void setRight(int right) {
        mRight = right;
    }

    public int getBottom() {
        return mBottom;
    }

    public void setBottom(int bottom) {
        mBottom = bottom;
    }

}
