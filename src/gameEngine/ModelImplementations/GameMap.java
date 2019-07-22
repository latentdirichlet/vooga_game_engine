package gameEngine.ModelImplementations;
import gameEngine.ModelInterfaces.*;

import java.util.*;
import java.util.List;

/**
 *  @author: aks64
 *
 * This class represents individual maps in the game. In addition to storing the cellGrid, it keeps track of the
 * gameObject on the map, and manages the addition and deletion of gameObjects from the map.
 */
public class GameMap implements GameMapInterface {
    protected Cell[][] cellGrid;
    private Map<Integer, GameObject> objectsOnMap;
    private int id;
    private int gridSizeX;
    private int gridSizeY;
    protected String defaultType = "/CellGraphics/Blocks/grass.jpg";
    /**
     * Constructor for GameMap. Will create grid with requested number of rows and columns and set id for the map to be
     * used by the GameWorld. Also will initialize cells on the grid with the default image path.
     * @param sizeX
     * @param sizeY
     * @param id
     */
    public GameMap(int sizeX, int sizeY, int id){
        this.cellGrid = createCellGrid(sizeX,sizeY);
        for(int i = 0 ; i<sizeY ; i++){
            for(int j = 0; j<sizeX; j++){
                cellGrid[i][j] = new Cell(j,i, id, defaultType);
            }
        }
        gridSizeX=sizeX;
        gridSizeY=sizeY;
        objectsOnMap = new HashMap<>();
    }
    /**
     * Will return the number of rows on the cell grid
     * @return
     */
    public int getGridSizeX() {
        return gridSizeX;
    }
    /**
     * Will return the number of columns on the cell grid
     * @return
     */
    public int getGridSizeY() {
        return gridSizeY;
    }
    /**
     * Will add the Gameobject passed as parameter to the map and will use GameObject's id to put it on the object map.
     * @param object
     */
    @Override
    public void addGameObject(GameObject object) {
        objectsOnMap.put(object.getID(), object);
        System.out.println("Map.addGameObject is called, adding id "+ object.getID()+ " to map");
    }
    /**
     * will remove the GameObject whose id is passed as parameter from the map
     * @param id of the Object
     */
    @Override
    public void removeGameObject(int id) {
        GameObject o = objectsOnMap.get(id);
        o.setCoveredCells(false);
        objectsOnMap.remove(id);
    }
    /**
     * will remove the GameObject passed in as a parameter from the map
     * @param go
     */
    public void removeGameObject(GameObject go){
        objectsOnMap.remove(go.getID());
    }
    /**
     * will set the cell's background on location row y and column c to image whose path is passed in as a parameter. Will
     * also set scale for the cell.
     * @param x
     * @param y
     * @param bg
     * @param scale
     */
    public void setCellBG(int x, int y, String bg, double scale) {
        setCellBG(x, y, bg);
        cellGrid[y][x].setScale(scale);
    }
    /**
     * will set the cell's background on location row y and column c to image whose path is passed in as a parameter.
     * @param x
     * @param y
     * @param bg
     */
    @Override
    public void setCellBG(int x, int y, String bg) {
        cellGrid[y][x].setCellImagePath(bg);
    }
    /**
     * Will create the grid for the constructor. Takes in the desired number of rows and columns as parameters.
     * @param sizeX
     * @param sizeY
     * @return
     */
    @Override
    public Cell[][] createCellGrid(int sizeX, int sizeY) {
        return new Cell[sizeY][sizeX];
    }
    /**
     * will check for object on the same location on the map and will return an ArrayList of ArrayLists of GameObjects on
     * the same location
     * @return
     */
    @Override
    public List<List<GameObject>> checkClashes() {
        ArrayList<GameObject> objectsList = new ArrayList(objectsOnMap.keySet());
        List<List<GameObject>> allClashes = new ArrayList<>();
        for(int i = 0; i<objectsList.size(); i++){
            GameObject object = objectsList.get(i);
            Position objectLocation = object.getPosition();
            ArrayList<GameObject> clashes = new ArrayList<>();
            for(int j = i+1; j<objectsList.size(); j++){
                GameObject object2 = objectsList.get(j);
                Position object2Location = object2.getPosition();
                if(object2Location.getX()==objectLocation.getX() && objectLocation.getY()==object2Location.getY()){
                    clashes.add(object2);
                }
            }
            if(clashes.size()>0){
                clashes.add(object);
                allClashes.add(clashes);
            }
        }
        return allClashes;

    }

    /**
     * will check whether the given location is a valid location on the map. Will return true if the given locations are
     * present on the grid
     * @param x
     * @param y
     * @return
     */
    public boolean checkInBound(int x, int y){
        return x>=0 && y>=0 && x<gridSizeX  && y<gridSizeY;
    }
    /**
     * will return a copy of cell grid
     * @return
     */
    public Cell[][] getCellGrid() {
        Cell[][] toSend = Arrays.copyOf(cellGrid, cellGrid.length);
        return toSend;
    }
    /**
     * Will return a two dimensional String Array that stores the image paths for every cell.
     * @return
     */
    public String[][] getGridImage(){
        String[][] imageGrid = new String[cellGrid.length][];
        for(int i = 0; i<cellGrid.length; i++){
           String[] imagesOfRow = new String[cellGrid[i].length];
           for(int j = 0; j<cellGrid[i].length; j++){
               imagesOfRow[j]= cellGrid[i][j].getCellImage();
           }
           imageGrid[i]=imagesOfRow;
        }
        return imageGrid;
    }
    /**
     * will return a two dimensional double array that stores the scales of every cell on the grid.
     * @return
     */
    public double[][] getGridScale() {
        double[][] scaleGrid = new double[cellGrid.length][];
        for(int i = 0; i<cellGrid.length; i++){
            double[] scalesOfRow = new double[cellGrid[i].length];
            for(int j = 0; j<cellGrid[i].length; j++){
                scalesOfRow[j]= cellGrid[i][j].getScale();
            }
            scaleGrid[i]=scalesOfRow;
        }
        return scaleGrid;
    }

    public Map<Position, String> getObjectPositions(){
        Map<Position, String> pMap = new HashMap<>();
        for(GameObject go: objectsOnMap.values()){
            pMap.put(go.getPosition(), go.getImagePath());
        }
        return pMap;
    }
    /**
     * will return an object on a specific location on the cellGrid. Will return false if the there are no objects.
     * @param x
     * @param y
     * @return
     */
    public GameObject getObject(int x, int y){
        ArrayList<Integer> ids = new ArrayList(objectsOnMap.keySet());
        for(int id: ids){
            GameObject object = objectsOnMap.get(id);
            Position objectPosition = object.getPosition();
            if(objectPosition.getY()==y && objectPosition.getX()==x){
                return object;
            }
        }
        return null;
    }

//    public int hasObjectId(int x, int y){
//        ArrayList<Integer> ids = new ArrayList(objectsOnMap.keySet());
//        for(int id: ids){
//            GameObject object = objectsOnMap.get(id);
//            Position objectPosition = object.getPosition();
//            if(objectPosition.getY()==y && objectPosition.getX()==x){
//                return id;
//            }
//        }
//        return -1;
//    }

    //TODO: are these the same thing?
    /**
     * will check the availability of a specific cell in cellGrid
     * @param x
     * @param y
     * @return
     */
    public boolean isAvailable(int x, int y){
        return cellGrid[y][x].isAvailable();
    }
    /**
     * will return if there are any objects on a given location on the map by checking the positions of every GameObject
     * on the Map. If there are none, will return false.
     * @param x
     * @param y
     * @return
     */
    public boolean hasObject(int x, int y, int map){
        Position tmp = new Position(x, y, map);
//        System.out.println("intended Position " + tmp);
        for(GameObject o: objectsOnMap.values()){
//            System.out.println("object Position " + o.getPosition());
            if(o.getPosition().equals(tmp)){
                return true;
            }
        }
        return false;
    }
    /**
     * will return an ArrayList of GameObjects on the Map
     * @return
     */
    public List<GameObject> getObjectList(){
        List<GameObject> res = new ArrayList<>();
//        System.out.println("objectsOnMap: " + objectsOnMap);
        for(GameObject go: objectsOnMap.values()){
            res.add(go);
        }
        return res;
    }

    /**
     * will set the availability of a cell on a specific Position to the passed in boolean value.
     * @param pos
     * @param size
     * @param f
     */
    public void setTraversible(Position pos, double size, boolean f){
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                cellGrid[pos.getY()+i][pos.getX()+j].setAvailable(f);
                int xx = pos.getX()+i;
                int yy = pos.getY()+j;
                System.out.println("(" + xx+", "+yy+") on map is set to "+f);
            }
        }
    }

    /**
     * Will set availability of the cell to its initial state
     * @param pos
     * @param size
     */
    public void restoreTraversible(Position pos, double size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cellGrid[pos.getY() + i][pos.getX() + j].setAvailable(cellGrid[pos.getY() + i][pos.getX() + j].getOriginalAvailability());
            }
        }
    }
    /**
     * will return id of the map
     * @return
     */
    public int getId(){
        return id;
    }

    /**
     * will set id for map
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }
}
