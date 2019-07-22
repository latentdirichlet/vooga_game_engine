package playerEnvironment.controller;

import gameEngine.ModelImplementations.Position;

import java.util.HashMap;
import java.util.Map;

public class PathManager {
    private Map<Integer, Path> pathMap = new HashMap<>();
    private Map<Integer, Coordinate> coordinateMap = new HashMap<>();
    private Map<Integer, Boolean> activateMap = new HashMap<>();

    public void setObjectPath(int id, Path path){
        registerID(id);
        if(!activateMap.get(id)) {
            pathMap.put(id, path);
        }
    }

    public void setObjectCoordinate(int id, Coordinate coordinate){
        registerID(id);
        coordinateMap.put(id, coordinate);
    }

    public Coordinate getObjectCoordinate(int id) {
//        System.out.println("getObjectCoordinate");
        registerID(id);
        if(activateMap.get(id)) {
            Path p = pathMap.get(id);
            if (p.hasNext()) {
                Coordinate res = p.next();
                coordinateMap.put(id, res);
                return res;
            } else {
                deactivatePath(id);
            }
        }
        return coordinateMap.get(id);
    }

    public boolean isMoving(int id){
        registerID(id);
        return activateMap.get(id);
    }

    public void activatePath(int id){
        if(pathMap.containsKey(id)) {
            activateMap.put(id, true);
        }
    }

    public void deactivatePath(int id){
        if(pathMap.containsKey(id)) {
            activateMap.put(id, false);
        }
    }

    private void registerID(int id){
        if(!activateMap.containsKey(id)){
            activateMap.put(id, false);
        }
    }

    public void clear(){
        pathMap.clear();
        coordinateMap.clear();
        activateMap.clear();
    }

}
