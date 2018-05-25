package arsenlibs.com.mosaic.repositories.levels.domain;

public class PalettePiece {

    private String mId;
    private int mColor;
    private String mImagePath;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

}
