package arsenlibs.com.mosaic.repositories.levels.levelbuilder.parser;

import com.google.gson.annotations.SerializedName;

public class Piece {

    @SerializedName("piece_id")
    public String id;

    @SerializedName("color_hex")
    public String colorHex;

    @SerializedName("image_path")
    public String imagePath;

}
