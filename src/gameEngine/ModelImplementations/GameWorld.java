package gameEngine.ModelImplementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: aks64
 * this class keeps track of the different maps of the game.
 */
public class GameWorld {
    protected ArrayList<GameMap> mapsOftheGame = new ArrayList<>();
    protected HashMap<Integer, GameMap> MAPS = new HashMap<>();
    protected GameMap startingMap;
    protected IDGen mapIdGen= new IDGen();
    protected GameMap currentMap;

    /**
     * constructor for map. Originally designed to take unlimited number of Maps to initialize the GameWorld with
     * @param maps
     */
    public GameWorld(GameMap... maps){
//        startingMap = maps[0];
//        for(int i = 0; i<maps.length; i++ ){
//            mapsOftheGame.add(maps[i]);
//            MAPS.put(maps[i].getId(), maps[i]);
//        }
    }


    /**
     * sets the first map that game will start with
     * @param startingMap
     */
    public void setStartingMap(GameMap startingMap) {
        this.startingMap = startingMap;
    }

    /**
     * adds map to the the collection of maps
     * @param map
     */
    public void addMap(GameMap map){
        mapsOftheGame.add(map);
        map.setId(mapIdGen.next());
        MAPS.put(map.getId(), map);
    }
    /**
     * sets the active map to be used by other parts of the project
     * @param map
     */
    public void setCurrentMap(GameMap map){
        currentMap= map;

    }

    public GameMap getMapById(int id){
        return MAPS.get(id);
    }

    /**
     * returns map based on index.
     * @param index
     * @return
     */
    public GameMap getMapByIndex(int index) {
        return mapsOftheGame.get(index);
    }
}
