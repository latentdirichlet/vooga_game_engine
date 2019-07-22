package gameEngine.ModelImplementations.Events;

import gameEngine.ModelImplementations.GameObject;
import gameEngine.ModelInterfaces.ConditionFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This factory determines if a GroupCollision or Collision must be created and then creates the correct class.
 * @mpz5
 */
public class CollisionConditionFactory implements ConditionFactory {

    public Condition createNewCondition(List<List<Object>> arguments){
        List<GameObject> collisionObjects = new ArrayList<>();
        if(arguments.size() > 1){
            addCollisionObjects(arguments.get(1), collisionObjects);
            return new Collision((GameObject)arguments.get(0).get(0), collisionObjects);
        }
        else{
            addCollisionObjects(arguments.get(0), collisionObjects);
            return new GroupCollision(collisionObjects);
        }
    }

    private void addCollisionObjects(List<Object> objects, List<GameObject> collisionObject){
        for(Object o : objects){
            collisionObject.add((GameObject) o);
            collisionObject.add((GameObject) o);
        }
    }
}
