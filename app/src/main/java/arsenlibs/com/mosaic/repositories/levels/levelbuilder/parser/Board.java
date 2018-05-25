package arsenlibs.com.mosaic.repositories.levels.levelbuilder.parser;

import com.google.gson.annotations.SerializedName;

public class Board {

    public int rows;

    @SerializedName("columns")
    public int cols;

    @SerializedName("filled_cells")
    public Cell[] filledCells;

}
