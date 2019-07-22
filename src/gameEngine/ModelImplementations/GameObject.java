package gameEngine.ModelImplementations;

import authoringEnvironment.frontend.Grid.Components.Map;
import gameEngine.ModelInterfaces.GameObjectInterface;
import javafx.scene.image.Image;
import playerEnvironment.controller.Sprite;

import java.net.URI;
import java.util.Observable;

/**
 * Represents a generic game object on the map.
 * All specific game objects extend this superclass.
 */
public abstract class GameObject extends Observable implements GameObjectInterface, Comparable<GameObject>{
    protected static final int DEFAULT_X = 0;
    protected static final int DEFAULT_Y = 0;
    //if collectible is true, it is an item
    private boolean collectible = false;
    protected int id;
    protected Position position = new Position(DEFAULT_X,DEFAULT_Y, 0);
    protected String imagePath;
    protected double scale = 1.0;
    protected Size size = new Size(1, 1);
    protected boolean walkable;
    protected GameMap map;

    protected Sprite defaultSprite;

    public GameObject(int id, String imagePath){
        this.id = id;
        this.imagePath = imagePath;
        this.defaultSprite = Sprite.imageToSprite(imagePath, scale);
    }

    //---------------Position--------------------

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position p) {
        if (position != null && map!= null) setCoveredCells(false);
        position = p;
        if (map!= null) setCoveredCells(true);
    }

    //---------------Size-------------------------

    public Size getSize() { return size;}

    public void setScale(double scale) {
        if (position != null && map!= null) setCoveredCells(false);
        this.scale = scale;
        if(scale<1.0) {
            size.setHeight(1);
            size.setWidth(1);
        } else {
            size.setWidth((int)scale);
            size.setHeight((int)scale);
        }
        if (position != null && map!= null) setCoveredCells(true);
        defaultSprite.setSizeFactor(scale);
    }

    public double getScale() {
        return scale;
    }

    //-------------Map---------------------
    public GameMap getMap(){
        return map;
    }

    public void setMap(GameMap m) {
        if (map != null && position != null) setCoveredCells(false);
        this.map = m;
        if (position != null) setCoveredCells(true);
    }

    //---------------Walkable-------------------

    public boolean isWalkable(){return walkable; }

    public void setWalkable(boolean w){walkable = w; }

    public void setCoveredCells(boolean f){
        // f is true when placing the game obj(creating or moving), false when removing(move away from or wipe out)
        if(this instanceof GameCharacter) return;

        if (f) {
            map.setTraversible(position, scale, walkable);
        }
        else{
            map.restoreTraversible(position, scale);
        }
    }

    public void restoreCellTraversibility() {
        setCoveredCells(false);
    }


    //----------------ID--------------------------

    public int getID(){ return id;}

    //---------------Image------------------------

    public String getImagePath() {
        return imagePath;
    }

    //---------------update------------------------

    public void update(){
        notifyObservers();
    }
    public boolean isCollectible(){
        return collectible;
    }

    protected void setCollectible(){
        collectible=true;
    }

    public boolean coverCell(Position p){
        if (p.getX()>=position.getX() && p.getX()<=position.getX()+size.getWidth()
                && p.getY()>=position.getY() && p.getY()<=position.getY()+size.getHeight()){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkInRange(GameObjectInterface otherObject) {
        return position.getX()>otherObject.getPosition().getX()-size.getHeight() &&
               position.getX()<otherObject.getPosition().getX()+otherObject.getSize().getHeight() &&
                position.getY()>otherObject.getPosition().getY()-size.getWidth() &&
                position.getY()<otherObject.getPosition().getY()+otherObject.getSize().getWidth();
    }

    @Override
    public int compareTo(GameObject o){
        if (id == o.getID())
            return 0;
        else if (id < o.getID())
            return -1;
        else return 1;
    }

    @Override
    public boolean equals(Object o){
        return id == ((GameObject)o).getID();
    }

    public Sprite getDefaultSprite(){
        return defaultSprite;
    }

}
