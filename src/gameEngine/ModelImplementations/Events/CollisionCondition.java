package gameEngine.ModelImplementations.Events;

import gameEngine.ModelImplementations.GameObject;
import gameEngine.ModelInterfaces.GameObjectInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * CollisionCondition is an abstract class extending Condition that is extended by GroupCollision and Collision. CollisionConditions have an additional abstract method notify, that is implemented by GroupCollision and Collision.
 *
 * This method takes the two GameObjects that have collided and correctly checks to see if its collision has been satisfied.
 *
 * A GroupCollision refers to a condition into which a single list of objects is passed. If any of the objects in that list collide with any of the other objects in that list this condition will be activated.
 *
 * Alternatively a Collision checks to see if a particular game object has collided with a game object in a different list. The CollisionCondition class also allows include the functionality to keep track of an access the most recent collision used to meet the condition.
 * @author mpz5
 */
public abstract class CollisionCondition extends Condition {
    private List<GameObjectInterface> recentCollision;

    public CollisionCondition(){
        super();
        recentCollision = new ArrayList<>();
    }

    /**
     * This method is implemented by the Collision and GroupCollision classes. The method is used to determine if the collision that occurred is one of the collisions that the condition is dependent on.
     * @param object1
     * @param object2
     */
    public abstract void notify(GameObject object1, GameObject object2);

    /**
     * This function returns a list of the the two game objects that most recently triggered the collision.
     * @return
     */
    public List<GameObjectInterface> getRecentCollision() {
        return recentCollision;
    }

    /**
     * Collision conditions must keep track of the most recent colliders. This method does that and then uses the setConditionMet method from the abstract parent class Condition.
     * @param obj1
     * @param obj2
     */
    protected void setCollisionConditionMet(GameObjectInterface obj1, GameObjectInterface obj2){
        recentCollision.clear();
        recentCollision.add(obj1);
        recentCollision.add(obj2);
        setConditionMet();
    };
}
