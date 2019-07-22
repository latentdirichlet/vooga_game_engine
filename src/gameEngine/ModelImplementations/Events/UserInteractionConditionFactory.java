package gameEngine.ModelImplementations.Events;

import gameEngine.ModelInterfaces.ConditionFactory;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public class UserInteractionConditionFactory implements ConditionFactory {

    @Override
    public Condition createNewCondition(List<List<Object>> arguments) {
        List<Object> keys = arguments.get(0);
        List<KeyCode> keyCodes = new ArrayList<>();
        for(Object key : keys){
            keyCodes.add((KeyCode)key);
        }
        return new UserInteractionCondition(keyCodes);
    }

}
