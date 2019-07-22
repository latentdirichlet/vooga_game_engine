package gameEngine.ModelImplementations.Events;

import gameEngine.ModelImplementations.Actor;
import gameEngine.ModelImplementations.GameObject;
import gameEngine.ModelInterfaces.GameObjectInterface;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The ConditionManager holds all of the created conditions in map associated with their type. It has several notify methods which are used by the rest of the Game to communicate with conditions. Depending on the notify method called, the ConditionManager retrieves the relevant conditions and notifies each of them of the change in the game. The condition then handles that change internally and determines whether or not it is relevant to it.
 * @author mpz5
 */

public class ConditionManager  {
    /**
     * Map Index to Condition Type to Conditions
     */
    HashMap<ConditionType, List<Condition>> conditionsMap;

    public ConditionManager(){
        conditionsMap = new HashMap<>();
    }

    
    public void notifyKeyPress(KeyCode keyCode) {
        if(!conditionsMap.containsKey(ConditionType.USERINTERACTION)){
            return;
        }
        List<Condition> conditions = conditionsMap.get(ConditionType.USERINTERACTION);
        for(Condition condition : conditions){
            ((UserInteractionCondition)condition).notify(keyCode);
        }
    }

    
    public void notifyWalkOffMap(int mapId) {
        if(!conditionsMap.containsKey(ConditionType.WALKOFF)){
            return;
        }
        List<Condition> conditions = conditionsMap.get(ConditionType.WALKOFF);
        for(Condition condition : conditions){
            ((WalkOffCondition)condition).notify(mapId);
        }

    }

    
    public void notifyCollision(GameObject object, GameObject otherObject) {
        if(!conditionsMap.containsKey(ConditionType.COLLISION)){
            return;
        }
        List<Condition> conditions = conditionsMap.get(ConditionType.COLLISION);
        for(Condition condition : conditions){
            ((CollisionCondition)condition).notify(object, otherObject);
        }
    }

    
    public void notifyItemUsage(GameObjectInterface object, int itemId) {
        if(!conditionsMap.containsKey(ConditionType.ITEMUSAGE)){
            return;
        }
        List<Condition> conditions = conditionsMap.get(ConditionType.ITEMUSAGE);
        for(Condition condition : conditions){
            ((ItemUsageCondition)condition).notify(itemId);
        }
    }

    
    public void addCondition(Condition newCondition, ConditionType type) {
        if(!conditionsMap.containsKey(type)){
            conditionsMap.put(type, new ArrayList<>());
        }
        conditionsMap.get(type).add(newCondition);
    }

    public void clearConditionsMap() {
        conditionsMap.clear();
    }


}
