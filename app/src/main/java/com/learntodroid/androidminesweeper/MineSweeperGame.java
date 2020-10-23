package com.learntodroid.androidminesweeper;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MineSweeperGame {
    private MineGrid mineGrid;
    private boolean gameOver;
    private boolean flagMode;
    private boolean clearMode;
    private int flagCount;
    private int numberBombs;
    private boolean timeExpired;

    public MineSweeperGame(int size, int numberBombs) {
        this.gameOver = false;
        this.flagMode = false;
        this.clearMode = true;
        this.timeExpired = false;
        this.flagCount = 0;
        this.numberBombs = numberBombs;
        mineGrid = new MineGrid(size);
        mineGrid.generateGrid(numberBombs);
    }

    public void handleCellClick(Cell cell) {
        if (!gameOver && !isGameWon() && !timeExpired && !cell.isRevealed()) {
            if (clearMode) {
                clear(cell);
            } else if (flagMode) {
                flag(cell);
            }
        }
    }

    public void clear(Cell cell) {
        int index = getMineGrid().getCells().indexOf(cell);
        getMineGrid().getCells().get(index).setRevealed(true);

        if (cell.getValue() == Cell.BOMB) {
            gameOver = true;
        } else if (cell.getValue() == Cell.BLANK) {
            List<Cell> toClear = new ArrayList<>();
            List<Cell> toCheckAdjacents = new ArrayList<>();

            toCheckAdjacents.add(cell);

            while (toCheckAdjacents.size() > 0) {
                Cell c = toCheckAdjacents.get(0);
                int cellIndex = getMineGrid().getCells().indexOf(c);
                int[] cellPos = getMineGrid().toXY(cellIndex);
                for (Cell adjacent: getMineGrid().adjacentCells(cellPos[0], cellPos[1])) {
                    if (adjacent.getValue() == Cell.BLANK) {
                        if (!toClear.contains(adjacent)) {
                            if (!toCheckAdjacents.contains(adjacent)) {
                                toCheckAdjacents.add(adjacent);
                            }
                        }
                    } else {
                        if (!toClear.contains(adjacent)) {
                            toClear.add(adjacent);
                        }
                    }
                }
                toCheckAdjacents.remove(c);
                toClear.add(c);
            }

            for (Cell c: toClear) {
                c.setRevealed(true);
            }
        }
    }

    public void flag(Cell cell) {
        cell.setFlagged(!cell.isFlagged());
        int count = 0;
        for (Cell c: getMineGrid().getCells()) {
            if (c.isFlagged()) {
                count++;
            }
        }
        flagCount = count;
    }

    public boolean isGameWon() {
        int numbersUnrevealed = 0;
        for (Cell c: getMineGrid().getCells()) {
            if (c.getValue() != Cell.BOMB && c.getValue() != Cell.BLANK && !c.isRevealed()) {
                numbersUnrevealed++;
            }
        }

        if (numbersUnrevealed == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void toggleMode() {
        clearMode = !clearMode;
        flagMode = !flagMode;
    }

    public void outOfTime() {
        timeExpired = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public MineGrid getMineGrid() {
        return mineGrid;
    }

    public boolean isFlagMode() {
        return flagMode;
    }

    public boolean isClearMode() {
        return clearMode;
    }

    public int getFlagCount() {
        return flagCount;
    }

    public int getNumberBombs() {
        return numberBombs;
    }
}
