package gameEngine.ModelImplementations.Events;

import gameEngine.ModelImplementations.Actor;
import gameEngine.ModelInterfaces.ConditionFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dc273
 */
public class WalkOffConditionFactory implements ConditionFactory {
    @Override
    public Condition createNewCondition(List<List<Object>> arguments) {

        var actors = new ArrayList<Actor>();
        arguments.get(0).forEach(e -> actors.add((Actor)e));

        return new WalkOffCondition(actors, (int)arguments.get(1).get(0));
    }
}
