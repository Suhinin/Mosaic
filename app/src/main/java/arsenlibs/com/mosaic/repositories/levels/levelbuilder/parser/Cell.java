package arsenlibs.com.mosaic.repositories.levels.levelbuilder.parser;

import com.google.gson.annotations.SerializedName;

public class Cell {

    public int row;

    public int col;

    @SerializedName("piece_id")
    public String pieceId;

}
