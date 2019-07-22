package playerEnvironment.controller;

import javafx.scene.image.Image;

public class SpriteSheet {

    private int numRow;
    private int numCol;
    private String imagePath;
    private double colWidth;
    private double rowHeight;
    private double sizeFactor;

    public SpriteSheet(String imagePath, int numRow, int numCol, double sizeFactor){
        this.imagePath = imagePath;
        this.numRow = numRow;
        this.numCol = numCol;
        this.colWidth = 1.0 / numCol;
        this.rowHeight = 1.0 / numRow;
        this.sizeFactor = sizeFactor;
    }

    /**
     * get sprite specified by row and col index
     * ex:
     * 1 2 3 4
     * 5 6 7 8
     * @param row
     * @param col
     * @return a sprite object
     */
    public Sprite getSprite(int row, int col){
        if(row >= numRow || row < 0 || col >= numCol || col < 0){
            return null;
        }
        double xStart = colWidth * col;
        double xEnd = colWidth * (col+1);
        double yStart = rowHeight * row;
        double yEnd = rowHeight * (row+1);
        return new Sprite(xStart, xEnd, yStart, yEnd, imagePath, numRow, numCol, sizeFactor);
    }


    public String getImagePath(){
        return imagePath;
    }

}
