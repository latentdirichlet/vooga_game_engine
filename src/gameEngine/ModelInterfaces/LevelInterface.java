package gameEngine.ModelInterfaces;

import gameEngine.ModelImplementations.GameMap;

public interface LevelInterface {
    /**
     * the map that the level holds on to
     */
     GameMap map= null;

     int level = 0;

    /**
     * starts a new level
     * @param character
     */
    void start(Character character);

    /**
     * ends a level
     */

    Character end();

}
