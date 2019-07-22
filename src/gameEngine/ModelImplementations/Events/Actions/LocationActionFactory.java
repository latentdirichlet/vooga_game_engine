package gameEngine.ModelImplementations.Events.Actions;

import gameEngine.ModelImplementations.GameCharacter;

import java.util.Map;
import java.util.function.Consumer;

public class LocationActionFactory{

    public Map<Object, Consumer<Object>> createAction(Object o, ActionType type, int x, int y, int mapIndex) {

        return new LocationAction().createLocationAction((GameCharacter) o, mapIndex, x, y);
    }
}
