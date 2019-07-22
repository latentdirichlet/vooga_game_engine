package gameEngine.ModelImplementations.Events;

import gameEngine.ModelImplementations.Actor;
import gameEngine.ModelImplementations.GameCharacter;

import java.util.List;

/**
 * @author dc273
 */
public class WalkOffCondition extends Condition {
    private List<Actor> actorList;
    private int mapIndex;

    public WalkOffCondition(List<Actor> actorList, int mapIndex) {
        super();
        this.actorList = actorList;
        this.mapIndex = mapIndex;
    }

    public void notify(int mapId) {
        if(mapIndex  == mapId){
            setConditionMet();
        }
        else{
            reset();
        }
        return;
    }
}
