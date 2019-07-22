package authoringEnvironment.backend.gameBuilder;

import gameEngine.ModelImplementations.GameWorld;

/**
 * @author: Shenghong Zhao
 * This is a simple extension from gameEngine/GameWorld with some simple add-ons
 */
public class GameWorldBuilder extends GameWorld {
    public GameWorldBuilder(GameMapBuilder... maps){
        super(maps);
    }

    public GameMapBuilder getMapAt(int index){
        return (GameMapBuilder) mapsOftheGame.get(index);
    }

    public void deleteMap(int index){
        mapsOftheGame.set(index, null);
    }

    public int getNumMaps() {
        return mapsOftheGame.size();
    }
}
