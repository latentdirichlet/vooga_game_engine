package gameEngine.ModelImplementations.Events.Actions;

import gameEngine.ModelInterfaces.GameInterface;
import gameEngine.ModelInterfaces.PlayerInterface;

import java.util.Map;
import java.util.function.Consumer;
import gameEngine.ModelImplementations.Events.Actions.DefaultAction.*;

public class DefaultPlayerAction extends DefaultAction{
    public Map<Object, Consumer<Object>> createTextBoxAction(GameInterface g, String message ){
        Consumer<Object> createText = obj -> {
            ((GameInterface)obj).createTextBox(message);
        };
        System.out.println("in create texbox");
        System.out.println(createAction(g,createText));
        return createAction(g, createText);
    }
}
