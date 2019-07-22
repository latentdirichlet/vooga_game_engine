package gameEngine.ModelImplementations.Events.Actions;

import gameEngine.ModelImplementations.Actor;
import gameEngine.ModelInterfaces.PlayerInterface;

import java.util.Map;
import java.util.function.Consumer;

public class UserDefinedActionFactory extends ActionFactory {
    @Override
    public Map<Object, Consumer<Object>> createAction(Object o, ActionType type, String stringArg, int intArg) throws ClassCastException{
        if (type == ActionType.DIY_ACTORACTION)
            return new UserDefinedAction().DIYActorAction((Actor) o, stringArg);
        else
            return new UserDefinedAction().DIYPlayerAction((PlayerInterface) o, stringArg);
    }
}
