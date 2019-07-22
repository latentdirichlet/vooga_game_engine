package gameEngine.ModelImplementations.Events;

import gameEngine.ModelImplementations.GameObject;
import gameEngine.ModelInterfaces.ConditionFactory;
import javafx.scene.input.KeyCode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Concrete factory to create ActorPropertyConditions. Uses the PropertyResources folder to associate a particular type of condition class with the factory.
 *
 * @author mpz5
 */
public class ActorPropertyConditionFactory implements ConditionFactory {
    public static final String RESOURCE_FILE_PATH = "ConditionResources.PropertyResources";
    private Map<PropertyType, Constructor> conditionClassConstructors;

    /**
     * Reads from the resource file to correlate PropertyType with a particular Condition class.
     */
    public ActorPropertyConditionFactory(){
        conditionClassConstructors = new HashMap<>();
        ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_FILE_PATH);
        for (String key : Collections.list(resources.getKeys())) {
            String className = resources.getString(key);
            try{
                Class c = Class.forName(ConditionFactoryManager.PATH + className);
                Constructor constructor = c.getDeclaredConstructors()[0];
                conditionClassConstructors.put(PropertyType.valueOf(key), constructor);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * expect arguments to hold : {{PropertyType}, {GameObject to observe}, {either list of cells (for location) or list of integers for health to check property against}}
     * @param arguments
     * @return
     */
    @Override
    public Condition createNewCondition(List<List<Object>> arguments){


        PropertyType pt = (PropertyType)arguments.get(0).get(0);
        Constructor constructor = conditionClassConstructors.get(pt);
        try {
            for(int i = 0; i < constructor.getParameterTypes().length; i++){
                System.out.println(constructor.getParameterTypes()[i]);
            }

            return (Condition)constructor.newInstance(arguments.get(1).get(0), arguments.get(2));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


}
