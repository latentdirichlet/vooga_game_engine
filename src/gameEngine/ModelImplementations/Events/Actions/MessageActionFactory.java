package gameEngine.ModelImplementations.Events.Actions;


import gameEngine.ModelImplementations.GameCharacter;
import gameEngine.ModelInterfaces.GameInterface;
import gameEngine.ModelInterfaces.PlayerInterface;

import java.util.Map;
import java.util.function.Consumer;

public class MessageActionFactory extends ActionFactory{

    @Override
    public Map<Object, Consumer<Object>> createAction(Object o, ActionType type, String stringArgs, int intArgs) throws ClassCastException{
        System.out.println("in action");
        System.out.println(o);
        System.out.println(type);
        System.out.println(stringArgs);
        if(type == ActionType.CREATE_TEXTBOX)
            return new DefaultPlayerAction().createTextBoxAction(((GameCharacter) o).getGame(), stringArgs);
        return null;
    }
}
