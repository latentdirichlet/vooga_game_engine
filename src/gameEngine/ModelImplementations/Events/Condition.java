package gameEngine.ModelImplementations.Events;

import gameEngine.ModelInterfaces.ConditionInterface;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Abstract class used to implement conditions. Conditions are created using the ConditionFactoryManager, which follows the abstract factory pattern. The back end of the authoring environment connects with this manager to allow authors to create specific conditions. This factory depends on the ConditionType enum, the PropertyType enum, the ConditionResources.properties file and the PropertyResources.properties file.
 *
 * Conditions either observe values directly through the Observer design pattern, or are notified of particular changes through the ConditionManager notification system. When conditions are met, they indicate this by changing a BooleanProperty called ConditionMet. Event objects observe these properties, and trigger the actions attached to them when all of their conditions have been met.
 *
 * All conditions extend the abstract parent class Condition, which implements the ConditionInterface. All conditions therefore have a conditionMet BooleanProperty and an active boolean. Conditions have the ability to add an Event observer, which adds the Event passed as a listener to its conditionMet BooleanProperty. This is how events are alerted when the conditions they depend on are met. Additionally, all conditions can be reset, and, to enforce a sequential meeting of conditions for an event, set to be active.
 * @author mpz5
 */

public abstract class Condition implements ConditionInterface{
    private BooleanProperty conditionMet = new SimpleBooleanProperty();
    private boolean active;

    public Condition(){
        conditionMet.setValue(false);
        active = false;
    }

    /**
     * This method adds a change listener to the conditionMet BooleanProperty. Anytime the conditionMet property is changed, this change event is fired. The change event is a call to the triggerEvent method in the Event class with the conditionMet value as the argument.
     * @param e
     */
    public void addEventObserver(Event e){
        conditionMet.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                e.triggerEvent(conditionMet.getValue());
            }
        });
    }

    /**
     * This function returns whether this condition is satisfied or not.
     * @return
     */
    public boolean checkMet(){
        return conditionMet.getValue();
    };

    /**
     * This method sets the condition to be active. Once the condition is active it can be met.
     */
    public void setActiveTrue(){
        active = true;
    }

    /**
     * This method is used in the events class as well as in some of the Condition subclasses to reset the condition back to its original state of inactive and not met.
     */
    public void reset(){
        conditionMet.setValue(false);
        active = false;
    }


    /**
     * This method determines if the condition is active. If it is then it sets the conditionMet value to be true.
     */
    protected void setConditionMet(){
        if(active){
            conditionMet.setValue(true);
        }
    }
}
