package gameEngine.ModelImplementations.Events;

import gameEngine.ModelImplementations.Actor;
import gameEngine.ModelImplementations.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete instance of a PropertyCondition. This condition is used to check if health is at a particular level. The health can be at any of the levels specified in healths.
 * @author mpz5
 */
public class AtHealth extends PropertyCondition {
    List<Double> healths;

    /**
     *Constructor for an AtHealth condition. Converts healths to integers and saves them in healths. Adds condition as observer for GameObject.
     * @param pMovingObject GameObject that the condition will check. This GameObject must be an Actor.
     * @param pHealths list of Objects, these should be Integers and will be casted as such.
     */
    public AtHealth(GameObject pMovingObject, List<Object> pHealths){
        super((Actor)pMovingObject);
        healths = new ArrayList<>();
        for(Object o : pHealths){
            healths.add((Double) o);
        }
        ((Actor)pMovingObject).getStats().addPropertyListeners(this);
    }

    /**
     * Determines if the current health level of the game object is at any of the values listed in healths.
     * @param o
     * @return
     */
    @Override
    public boolean checkCondition(GameObject o){
        Actor actor = (Actor) (o);
        System.out.println("Actor Level: " + actor.getStats().getHealth());
        for(Double health : healths){
            System.out.println("Health Level: " + health);
           if(actor.getStats().getHealth() == health){
               return true;
           }
        }
        return false;
    }
}

