package authoringEnvironment.utils;

import gameEngine.ModelImplementations.Events.Actions.ActionType;
import gameEngine.ModelImplementations.Events.ConditionType;

import java.util.List;

/**
 * @author dc273
 *
 * Holds parameters for creating events
 */
public class EventPackage {

    public List<List<ConditionPackage>> left;
    public List<List<ConditionPackage>> right;
    public List<ConditionType> conditionTypes;

    public List<ConditionPackage> objs;
    public List<ActionType> actionTypes;
    public List<List<Object>> arguments;

    public EventPackage(List<List<ConditionPackage>> left, List<List<ConditionPackage>> right, List<ConditionType> conditionTypes,
                        List<ConditionPackage> objs, List<ActionType> actionTypes, List<List<Object>> arguments) {
        this.left = left;
        this.right = right;
        this.conditionTypes = conditionTypes;

        this.objs = objs;
        this.actionTypes = actionTypes;
        this.arguments = arguments;
    }
}
