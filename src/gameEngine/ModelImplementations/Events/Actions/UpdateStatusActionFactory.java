package gameEngine.ModelImplementations.Events.Actions;

import gameEngine.ModelImplementations.Actor;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * This class gets a map of Action type to method name from the StatusActions.properties file. It then gets that method from the stats class of the actor passed and calls it with the correct value. To add more action options:
 * 1) add the enum type to the ActionType class
 * 2) add the value to the corresponding ActionResource.properties file such that this factory is associated with it
 * 3) write a method in the statistics class to to the thing that you want to do that has a single Double argument.
 * 4) add the ActionType and the name of the method to the StatusActions.properties file.
 *
 * @author mpz5
 */
public class UpdateStatusActionFactory extends ActionFactory{
    public static final String RESOURCE_FILE = "ActionResources.StatusActions";
    Map<ActionType, String> methodsMap;
    DefaultAction da;

    /**
     * This constructor accesses the resource file referenced by RESOURCE_FILE and correlates the ActionType to the method name used to create that action.
     */
    public UpdateStatusActionFactory(){
        da = new DefaultAction();
        methodsMap = new HashMap<>();
        ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_FILE);
        for (String key : Collections.list(resources.getKeys())) {
            String methodName = resources.getString(key);
            methodsMap.put(ActionType.valueOf(key), methodName);
        }
    }

    /**
     * This method accesses the methods of the statistics class using reflection and invokes them to change property values.
     * @param o - object whos properties will be changed (must be an Actor)
     * @param type - ActionType
     * @param stringArg - not used
     * @param arg - the amount that the value will be changed by
     * @return
     * @throws ClassCastException
     */
    @Override
    public Map<Object, Consumer<Object>> createAction(Object o, ActionType type, String stringArg, int arg) throws ClassCastException{
            Consumer<Object> statsAction = obj -> {
                try {
                    System.out.println(obj);
                    Actor actor = (Actor)obj;
                    Class c = actor.getStats().getClass();
                    Method method = c.getMethod(methodsMap.get(type), Double.class);
                    System.out.println(arg);
                    method.invoke(actor.getStats(),(double)arg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            return da.createAction(o, statsAction);
    }
}
