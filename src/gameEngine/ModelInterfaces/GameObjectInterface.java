package gameEngine.ModelInterfaces;

import gameEngine.ModelImplementations.Position;
import gameEngine.ModelImplementations.Size;

public interface GameObjectInterface {

    Position getPosition();
    Size getSize();

    /**
     * determine if the other game object is within range of this one. Uses position and size of both game objects.
     * @param otherObject
     * @return
     */
    boolean checkInRange(GameObjectInterface otherObject);

    boolean checkIsFightable();
}
