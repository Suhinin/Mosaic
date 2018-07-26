package com.childaplic.mosaic.repositories.levels.levelbuilder;

import android.graphics.Color;

import com.google.gson.Gson;

import javax.inject.Inject;

import com.childaplic.mosaic.repositories.levels.domain.LevelData;
import com.childaplic.mosaic.repositories.levels.domain.PalettePiece;
import com.childaplic.mosaic.repositories.levels.levelbuilder.parser.Board;
import com.childaplic.mosaic.repositories.levels.levelbuilder.parser.Cell;
import com.childaplic.mosaic.repositories.levels.levelbuilder.parser.Level;
import com.childaplic.mosaic.repositories.levels.levelbuilder.parser.Piece;
import com.childaplic.mosaic.services.assets.AssetsService;


public class LevelBuilderStandard implements LevelBuilder {

    // region Constants

    private final String TAG = LevelBuilderStandard.class.getCanonicalName();

    private final String ASSETS_LEVELS_FOLDER = "levels";

    // endregion


    // region Fields

    private Gson mGson;

    // endregion


    // region Injections

    private AssetsService mAssetsService;

    // endregion


    // region Constructors

    @Inject
    public LevelBuilderStandard(AssetsService assetsService) {
        mAssetsService = assetsService;

        mGson = new Gson();
    }

    // endregion


    // region Implements

    @Override
    public LevelData[] createLevels() {
        String[] levelConfigs = mAssetsService.getList(ASSETS_LEVELS_FOLDER);
        if (levelConfigs == null || levelConfigs.length == 0) {
            return new LevelData[0];
        }

        return readLevels(levelConfigs);
    }

    // endregion


    // region Private Methods

    private LevelData[] readLevels(String[] levelConfigs) {
        LevelData[] levelDatas = new LevelData[levelConfigs.length];
        for (int i=0; i<levelConfigs.length; i++) {
            String configName = levelConfigs[i];
            levelDatas[i] = readLevel(configName);
        }

        return levelDatas;
    }

    private LevelData readLevel(String configName) {
        String jsonConfig = mAssetsService.readText(ASSETS_LEVELS_FOLDER + "/" + configName);

        Level level = mGson.fromJson(jsonConfig, Level.class);
        return createLevelData(level, configName);
    }

    private LevelData createLevelData(Level level, String levelName) {
        LevelData levelData = new LevelData();
        levelData.setId(levelName);
        levelData.setNumber(level.number);
        levelData.setOpen(level.open);
        levelData.setPreviewPath(level.previewPath);
        levelData.setPalette(createPalette(level.palette));
        levelData.setBoard(createBoardCells(level.board));

        return levelData;
    }

    private PalettePiece[] createPalette(Piece[] pieces) {
        if (pieces == null || pieces.length == 0) {
             return new PalettePiece[0];
        }

        PalettePiece[] palettePieces = new PalettePiece[pieces.length];
        for (int i=0; i<pieces.length; i++) {
            Piece piece = pieces[i];
            palettePieces[i] = createPalettePiece(piece);
        }

        return palettePieces;
    }

    private PalettePiece createPalettePiece(Piece piece) {
        PalettePiece palettePiece = new PalettePiece();
        palettePiece.setId(piece.id);
        palettePiece.setColor(Color.parseColor(piece.colorHex));
        palettePiece.setImagePath(piece.imagePath);

        return palettePiece;
    }

    private String[][] createBoardCells(Board board) {
        if (isBoardValid(board) == false) {
            return new String[0][0];
        }

        String[][] boardCells = new String[board.rows][board.cols];
        for (int i=0; i<board.rows; i++) {
            for (int j=0; j<board.cols; j++) {
                boardCells[i][j] = LevelData.EMPTY_CELL;
            }
        }

        for (Cell cell : board.filledCells) {
            boardCells[cell.row][cell.col] = cell.pieceId;
        }
        return boardCells;
    }

    private boolean isBoardValid(Board board) {
        if (board == null) {
            return false;
        }

        if (board.cols == 0 || board.rows == 0) {
            return false;
        }

        if (board.filledCells == null || board.filledCells.length == 0) {
            return false;
        }

        return true;
    }

    // endregion

}
