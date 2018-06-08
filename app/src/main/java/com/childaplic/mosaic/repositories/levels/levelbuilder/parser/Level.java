package com.childaplic.mosaic.repositories.levels.levelbuilder.parser;


import com.google.gson.annotations.SerializedName;

public class Level {

    public int number;

    @SerializedName("preview_path")
    public String previewPath;

    public Piece[] palette;

    public Board board;

}
