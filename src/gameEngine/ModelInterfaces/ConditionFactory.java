package gameEngine.ModelInterfaces;

import gameEngine.ModelImplementations.Events.Condition;

import java.util.List;

/**
 * This interface is implemented by all condition factories. The createNewCondition method takes in arguments, converts them into the proper form, and then creates a functional condition that can trigger an event.
 */
public interface ConditionFactory {

    Condition createNewCondition(List<List<Object>> arguments);

}

