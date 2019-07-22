package gameEngine.ModelImplementations.Events.Actions;

import gameEngine.ModelImplementations.Events.CollisionCondition;
import gameEngine.ModelImplementations.Game;
import gameEngine.ModelInterfaces.GameObjectInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The CombatAction is the only action that depends on the condition. This occurs because in order to trigger combat, the consumer function needs to know what to objects collided. Therefore, this constructor takes in the CollisionCondition and creates an action that accesses the conditions most recent collision and triggers a combat with the game objects that collided.
 * @mpz5
 */
public class CombatActionFactory{

    public Map<Object, Consumer<Object>> createAction(Object o, ActionType type, Object conditionArgument) {
        Game g = (Game)o;
        CollisionCondition c = (CollisionCondition) conditionArgument;
        Consumer<Object> action = game -> {
            List<GameObjectInterface> recentCollision = c.getRecentCollision();
            ((Game)game).triggerCombat(recentCollision.get(0), recentCollision.get(1));
        };
        Map<Object, Consumer<Object>> actionMap = new HashMap<>();
        actionMap.put(g, action);
        return actionMap;
    }
}
