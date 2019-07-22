package gameEngine.ModelImplementations.Events.Actions;

import gameEngine.ModelImplementations.GameCharacter;
import gameEngine.ModelInterfaces.GameInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LocationAction {
    public Map<Object, Consumer<Object>> createLocationAction(GameCharacter g, int mapIndex, int x, int y){
        Consumer<Object> createText = obj -> {
            GameCharacter c = ((GameCharacter)obj);

            var map = c.getGame().getWorld().getMapByIndex(mapIndex);

            c.setMap(map);
            c.teleport(x, y, mapIndex);
            c.getGame().updateCurrentMap(map);
            c.getGame().repaintDisplay();


        };
        return createAction(g, createText);
    }

    private Map<Object, Consumer<Object>> createAction(Object o, Consumer<Object> action){
        Map<Object, Consumer<Object>> reaction = new HashMap<>();
        reaction.put(o, action);
        return reaction;
    }
}
