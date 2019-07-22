package gameEngine.ModelInterfaces;

import gameEngine.ModelImplementations.GameMap;
import gameEngine.ModelImplementations.Position;

/**
 * Defines movement related function for game objects.
 * All game objects must implement this interface in order to change position on grid
 */
public interface Moveable {

    void move(int x, int y);

    void forwardNorth();

    void forwardSouth();

    void forwardEast();

    void forwardWest();

    void turnNorth();

    void turnSouth();

    void turnWest();

    void turnEast();
}
