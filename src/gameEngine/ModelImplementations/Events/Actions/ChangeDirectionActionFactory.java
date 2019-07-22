package gameEngine.ModelImplementations.Events.Actions;

import gameEngine.ModelImplementations.GameCharacter;

import java.util.Map;
import java.util.function.Consumer;
import gameEngine.ModelImplementations.Events.Actions.DefaultAction;

public class ChangeDirectionActionFactory extends ActionFactory  {
    @Override
    public Map<Object, Consumer<Object>> createAction (Object o, ActionType type, String stringArgs, int intArgs) throws ClassCastException{
        GameCharacter c = (GameCharacter) o;
        DefaultAction da = new DefaultAction();
        if(type == ActionType.MOVENORTH_DIRECTION)
            return da.moveNorthAction(c);
        else if(type == ActionType.MOVEEAST_DIRECTION)
            return da.moveEastAction(c);
        else if(type == ActionType.MOVESOUTH_DIRECTION)
            return da.moveSouthAction(c);
        else if(type == ActionType.MOVEWEST_DIRECTION)
            return da.moveWestAction(c);
        else
            return da.reverseDirectionAction(c);
    }
}
