package gameEngine.ModelImplementations.Events;

import gameEngine.ModelImplementations.GameObject;
import gameEngine.ModelInterfaces.GameObjectInterface;

import java.util.List;

/**
 * This condition is notified by the notifyCollision method in the ConditionManager and determines if the main object passed to the constructor has collided with any of the objects in the list of collision objects passed to the constructor.
 * @author mpz5
 */
public class Collision extends CollisionCondition {
    public static final String description = "Collision of One MainGame Object to a list of other MainGame Objects";
    GameObject mainObject;
    List<GameObject> collisionObjects;

    public Collision(GameObject pMainObject, List<GameObject> pCollisionObjects){
        super();
        mainObject = pMainObject;
        collisionObjects = pCollisionObjects;
    }

    /**
     * This m
     * @param object1
     * @param object2
     */
    @Override
    public void notify(GameObject object1, GameObject object2){
        if(checkCondition(object1, object2) || checkCondition(object2, object1)){
            setCollisionConditionMet(object1, object2);
        }
    }

    private boolean checkCondition(GameObject mainObj, GameObject collisionObject){
        if(mainObj.compareTo(mainObj) == 0){
            return collisionObjects.contains(collisionObject);
        }
        return false;
    }

    public String toString(){
        return description;
    }


}
