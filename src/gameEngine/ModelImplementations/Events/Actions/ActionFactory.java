package gameEngine.ModelImplementations.Events.Actions;

import java.util.Map;
import java.util.function.Consumer;
/**
 * @author: Shenghong Zhao
 * Abstract class for actionFactories
 */
public abstract class ActionFactory {
    public abstract Map<Object, Consumer<Object>> createAction(Object o, ActionType type, String stringArg, int intArg);
}
