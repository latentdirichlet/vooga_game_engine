package gameEngine.ModelImplementations;
import gameEngine.ModelInterfaces.CellInterface;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.Observable;

public class Cell extends Observable implements CellInterface{
    private int myColumn;
    private int myRow;
    private String imagepath;
    private boolean available;
    private boolean originalAvailability;
    private double scale = 1.0;
    private int map;

    public Cell(int col, int row, int map, String url){
        this.myColumn = col;
        this.myRow = row;
        this.map = map;
        this.imagepath = url; //TODO: set the type instead of the image
        originalAvailability = myType.isWalkable();
        available = originalAvailability;
    }

    public void setCellImagePath(String path){
        imagepath = path;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getCellImage(){
        return imagepath;
    }

    @Override
    public Position getPosition() {
        return new Position(myColumn, myRow, map);
    }

    @Override
    public CellType getMyType() {
        return myType;
    }

    public boolean isAvailable(){
        return available;
    }

    public boolean getOriginalAvailability() {return originalAvailability;}

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
}
