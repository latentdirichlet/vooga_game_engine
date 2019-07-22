package gameEngine.ModelInterfaces; /**
 * @author aks64
 */

import gameEngine.ModelImplementations.Cell;
import gameEngine.ModelImplementations.CellType;
import gameEngine.ModelImplementations.GameObject;

import java.util.List;
import java.util.Map;

/**
 * this class represents the map for a level of the game. It will be hold two variables, which are the grid (a
 * collection of cells) and a list of GameObjects that are to be on the map. Using this interface, grid will be
 * initialized, cell types will be set. Also  new game objects could be added to or deleted from the map.
 */
public interface GameMapInterface {
    Cell[][] cellGrid = null;
    List<GameObject>[][] objectGrid = null;
    Map<Integer, GameObject> objectsOnMap = null;

    /**
     * This function will be used whenever a new GameObject needs to be on the map. It will take a GameObject as a
     * parameter and add it to the list of GameObjects, ObjectsOnMap.
     *
     * @param thing
     */
     void addGameObject(GameObject thing);

    /**
     * This function will be used whenever a  GameObject needs to be removed from the map. It will take a GameObject as a
     * parameter and remove it from the list of GameObjects, ObjectsOnMap.
     *
     * @param id of the Object
     */
    void removeGameObject(int id);
    /**
     * Will change the type of cell at a specific location on the grid. The parameter x and y are the coordinates of the
     * cell to be set and type is the target cell type to be set.
     *
     * @param x
     * @param y
     * @param bg
     */
    void setCellBG(int x, int y, String bg);

    /**
     * will create a sizeX by sizeY grid and set all cells to default cellType
     *
     * @param sizeX
     * @param sizeY
     */
     Cell[][] createCellGrid(int sizeX, int sizeY);

    /**
     * will check if any two objects are clashing and return an arraylist of GameObjects that are clashing
     * @return
     */
    List<List<GameObject>> checkClashes();

}