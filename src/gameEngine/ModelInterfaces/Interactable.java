package gameEngine.ModelInterfaces;

import gameEngine.ModelImplementations.Position;

/**
 * All game objects must implement this interface to be interacted with autonomous character
 */

public interface Interactable {

    /**
     * determine if the other game object is within range of this one. Uses position and size of both game objects.
     * @param otherObject
     * @return
     */
    boolean checkInRange(GameObjectInterface otherObject);

    void interact(GameObjectInterface otherObject);

}
