package gameEngine.ModelImplementations.Events;
/**
 * The ConditionFactoryManager is an abstract factory. This utilizes the resource file passed to correlate the ConditionType enums with the correct concrete factory for its creation. An example of this resource file is in resources.ConditionResource.property.
 *
 * In order to add an additional ConditionFactory the following steps must be followed:
 *  1) create the new condition class/classes that will be created by the new condition factory
 *  2) create the new ConditionFactory
 *  3) add a new Enum value to ConditionType that describes this new type of condition
 *  4) add this the key value pair of ConditionType = Factory Class name to the ActionResources.properties file.
 *  5) add an example below/comments in the specific factory class to demonstrate what order/kind of arguments are expected.
 *
 * In order to create conditions the following steps must be followed:
 * 1) A ConditionManager must be instantiated
 * 2) A resource file must be created and chosen (ActionResources.properties)
 * 3) A ConditionFactoryManager must be created using the two ConditionManager and resource file discussed above.
 * 4) The ConditionFactoryManager.createCondition function must be called with the propert list of arguments and condition type. Examples of the necessary arguments and condition types are below
 *
 * Example inputs for createCondition:
 * 1) to create a collision condition for one game object colliding with a list of other game objects:
 *      List<GameObject> mainObject = new ArrayList();
 *      mainObject.add(gameCharacter);
 *      List<GameObject> collisionObjects = new ArrayList();
 *      collisionObjects.add(gameObject);
 *      collisionObjects.add(otherGameObject);
 *      List<List<Object>> arguments = new ArrayList();
 *      arguments.add(mainObject);
 *      arguments.add(collisionObjects);
 *      Condition c = createCondition(arguments, ConditionType.COLLISION);
 *
 * 2) to create a collision condition such that the condition is triggered if any object in the list collides with any other object in the list:
 *      List<GameObject> collisionObjects = new ArrayList();
 *      collisionObjects.add(gameObject);
 *      collisionObjects.add(otherGameObject);
 *      List<List<Object>> arguments = new ArrayList();
 *      arguments.add(collisionObjects);
 *      Condition c = createCondition(arguments, ConditionType.COLLISION);
 *
 * 3) to create a Actor Property Condition such that the condition is triggered if an object's health or location reaches the values passed in the third argument list.
 *
 *  a) to create a location condition that is triggered if a game object lands on any of the given cells:
 *      List<Object> type = new ArrayList<>();
 *         type.add(PropertyType.LOCATION);
 *         List<Object> mainObject = new ArrayList();
 *         mainObject.add(gc);
 *         List<Object> locations = new ArrayList();
 *         locations.add(new Cell(10, 10, "fakepath"));
 *         List<List<Object>> arguments = new ArrayList();
 *         arguments.add(type);
 *         arguments.add(mainObject);
 *         arguments.add(locations);
 *         Condition c = cfm.createCondition(arguments, ConditionType.PROPERTIES);
 *  b) to create a health condition that is triggered if a game object's health reaches a certain level:
 *      List<Object> type = new ArrayList<>();
 *         type.add(PropertyType.HEALTH);
 *         List<Object> mainObject = new ArrayList();
 *         mainObject.add(gc);
 *         List<Object> healthLevels = new ArrayList();
 *         healthLevels.add(0);
 *         List<List<Object>> arguments = new ArrayList();
 *         arguments.add(type);
 *         arguments.add(mainObject);
 *         arguments.add(healthLevels);
 *         Condition c = cfm.createCondition(arguments, ConditionType.PROPERTIES);
 * @author mpz5
 */

import gameEngine.ModelInterfaces.ConditionFactory;


import java.lang.reflect.Constructor;
import java.util.*;

public class ConditionFactoryManager {
    public static final String PATH = "gameEngine.ModelImplementations.Events.";
    Map<ConditionType, ConditionFactory> factories;
    ConditionManager manager;


    /**
     * This constructor access the resource file passed and creates all necessary concrete factories designated in the file.
     * @param resource resource file to load
     * @param pManager ConditionManager
     */
    public ConditionFactoryManager(String resource, ConditionManager pManager){
        manager = pManager;
        factories = new HashMap<>();
        ResourceBundle resources = ResourceBundle.getBundle(resource);
        for (String key : Collections.list(resources.getKeys())) {
            String className = resources.getString(key);
            try{
                Class c = Class.forName(PATH + className);
                Constructor[] allConstructors = c.getDeclaredConstructors();
                var factory = allConstructors[0].newInstance();
                factories.put(ConditionType.valueOf(key), (ConditionFactory)factory);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * The createCondition function takes any list of list of arguments and condition type, determines the correct factory to handle the condition type, and passes the arguments on to that factory in order to create a new condition. That condition is added to the manager and then returned.
     * @param arguments
     * @param type
     * @return
     */
    public Condition createCondition(List<List<Object>> arguments, ConditionType type){
        ConditionFactory factory = factories.get(type);
        Condition newCondition = factory.createNewCondition(arguments);
        manager.addCondition(newCondition, type);
        return newCondition;
    }

    /**
     * This function clears all conditions from the manager effectively deleting any events associated with the game
     */
    public void clearConditionsMap() {
        manager.clearConditionsMap();
    }

}
