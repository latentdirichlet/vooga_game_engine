package playerEnvironment.controller;

import javafx.scene.image.Image;

public class Sprite {

    private double xStart;
    private double xEnd;
    private double yStart;
    private double yEnd;

    private int sheetRows;
    private int sheetCols;
    private String imagePath;

    private double sizeFactor;
    private int priority;

    public static final int HIGH_PRIORITY = 1;

    public Sprite(double x1, double x2, double y1, double y2, String ip, int sheetRows, int sheetCols, double sizeFactor){
        this.xStart = x1;
        this.xEnd = x2;
        this.yStart = y1;
        this.yEnd = y2;
        this.imagePath = ip;
        this.sheetRows = sheetRows;
        this.sheetCols = sheetCols;
        this.sizeFactor = sizeFactor;
    }

    public static Sprite imageToSprite(String imagePath, double sizeFactor){
        return new Sprite(0, 1, 0, 1, imagePath, 1, 1, sizeFactor);
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public double getxStart() {
        return xStart;
    }

    public double getxEnd() {
        return xEnd;
    }

    public double getyStart() {
        return yStart;
    }

    public double getyEnd() {
        return yEnd;
    }

    public String getImagePath() {
        return imagePath;
    }

    public double getSizeFactor() {
        return sizeFactor;
    }

    public int getSheetCols() {
        return sheetCols;
    }

    public int getSheetRows() {
        return sheetRows;
    }

    public void setSizeFactor(double sizeFactor) {
        this.sizeFactor = sizeFactor;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "(" + xStart + ", " + xEnd + ", " + yStart + ", " + yEnd + ")" + imagePath + " sizeFactor: " + sizeFactor;
    }
}
