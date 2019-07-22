package gameEngine.ModelInterfaces;

/**
 * Defines destruction related function for obstacles. (like destroying rocks and trees in the way in pokemon)
 * All obstacles must implement this interface if they can be destroyed
 */

public interface Destructible {

    /**
     * determine whether autonomous character can destroy the obstacle
     */
    boolean canDestroy();

    /**
     * destroy the game object, wiping it from the map
     */
    void destroy();

}
