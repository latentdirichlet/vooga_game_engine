package gameEngine.ModelImplementations.Events;

import gameEngine.ModelInterfaces.ConditionFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @mpz5
 */
public class ItemUsageConditionFactory implements ConditionFactory {

    @Override
    public Condition createNewCondition(List<List<Object>> arguments) {
        List<Object> items = arguments.get(0);
        List<Integer> itemObjects = new ArrayList<>();
        for (Object item : items) {
            itemObjects.add((Integer)item);
        }
        return new ItemUsageCondition(itemObjects);
    }

}
