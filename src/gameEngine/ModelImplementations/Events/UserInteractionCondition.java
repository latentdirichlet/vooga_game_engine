package gameEngine.ModelImplementations.Events;

import javafx.scene.input.KeyCode;

import java.util.List;
/**
 * This condition is notified by the notifyKeyPress method in the ConditionManager and determines if any of the button triggers passed to the constructor were the button that was pressed.
 * @author mpz5
 */

public class UserInteractionCondition extends Condition {
    List<KeyCode> buttonTriggers;

    public UserInteractionCondition(List<KeyCode> pButtonTriggers){
        super();
        buttonTriggers = pButtonTriggers;
    }

    public void notify(KeyCode keyCode){
        if(buttonTriggers.contains(keyCode)){
            setConditionMet();
        }
    }

}
