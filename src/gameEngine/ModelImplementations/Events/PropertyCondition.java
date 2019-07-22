package gameEngine.ModelImplementations.Events;

import gameEngine.ModelImplementations.Actor;
import gameEngine.ModelImplementations.Events.Condition;
import gameEngine.ModelImplementations.GameObject;

import java.util.Observable;
import java.util.Observer;

public abstract class PropertyCondition extends Condition {
    private Actor actor;

    public PropertyCondition(Actor pObject){
        super();
        actor = pObject;
    }

    public void notifyChanged(){

        if(checkMet()){
            reset();
        }
        else{
            if(checkCondition(actor)){
                setConditionMet();
            };
        }
    }

    protected abstract boolean checkCondition(GameObject o);

}
