package gameEngine.ModelImplementations.Events;

import gameEngine.ModelImplementations.GameObject;
import gameEngine.ModelInterfaces.GameObjectInterface;

import java.util.List;
/**
 * This condition is notified by the notifyCollision method in the ConditionManager and determines if the any of the objects passed to the constructor collided with each other.
 * @author mpz5
 */
public class GroupCollision extends CollisionCondition {
    List<GameObject> collisionObjects;

    public GroupCollision(List<GameObject> pCollisionObjects){
        super();
        collisionObjects = pCollisionObjects;
    }

    @Override
    public void notify(GameObject object1, GameObject object2) {
        if(collisionObjects.contains(object1) && collisionObjects.contains(object2)){
            setCollisionConditionMet(object1, object2);
        }

    }
}
