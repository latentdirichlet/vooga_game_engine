package gameEngine.ModelImplementations.Events.Actions;


import authoringEnvironment.backend.gameBuilder.GameCharacterBuilder;
import gameEngine.ModelImplementations.GameCharacter;

import java.lang.reflect.Constructor;
import java.util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static gameEngine.ModelImplementations.Events.Actions.ActionType.*;
/**
 * @author: Shenghong Zhao @ Marley Zelinger
 * A FactoryManager that instantiates different action factories according to different keys
 */
public class ActionFactoryManager {

    private HashMap<ActionType, ActionFactory> typeToFactory;
    public static final String RESOURCE_FILE = "ActionResources.ActionResources";
    public static final String PATH = "gameEngine.ModelImplementations.Events.Actions.";

    // prepare the map of type_to_factory using the resource bundle
    public ActionFactoryManager(){
        typeToFactory = new HashMap<>();
        ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_FILE);
        for (String key : Collections.list(resources.getKeys())) {
            String className = resources.getString(key);
            try{
                Class c = Class.forName(PATH + className);
                Constructor[] allConstructors = c.getDeclaredConstructors();
                var factory = allConstructors[0].newInstance();
                typeToFactory.put(ActionType.valueOf(key), (ActionFactory)factory);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    //Make action using objects, actionTypes and potential arguments
    public Map<Object, Consumer<Object>> makeActions(List<Object> objs, List<ActionType> types,
                                                     List<List<Object>>args) {
        Map<Object, Consumer<Object>> toReturn = new HashMap<>();
        Map<Object, Consumer<Object>> produced = new HashMap<>();
        for (int i = 0; i< objs.size(); i++)
        {
            if(types.get(i) == COMBAT){
                produced = new CombatActionFactory().createAction(objs.get(i), types.get(i), args.get(0).get(0));

            }
            else{
                List<String> strArgs = new ArrayList<>();
                List<Integer> intArgs = new ArrayList<>();
                for (int j = 0; j < args.get(i).size();j++){
                    if(args.get(i).get(j) instanceof Integer)
                        intArgs.add((Integer)args.get(i).get(j));
                    else
                        strArgs.add((String) args.get(i).get(j));
                }
                if (types.get(i)==LOCATION){
                    produced = new LocationActionFactory().createAction(objs.get(i), types.get(i),
                            intArgs.get(0),intArgs.get(1),intArgs.get(2));
                }
                else {
                    if(strArgs.size()==0)
                        strArgs.add("");

                    if(intArgs.size()==0)
                        intArgs.add(0);

                    System.out.println("Types " + types);
                    System.out.println("");
                    produced = typeToFactory.get(types.get(i)).createAction(
                            objs.get(i), types.get(i), strArgs.get(0), intArgs.get(0));
                }
            }
            if(types.get(i)== CREATE_TEXTBOX)
                toReturn.put(((GameCharacterBuilder)objs.get(i)).getGame(), produced.get(((GameCharacterBuilder)objs.get(i)).getGame()));
            else
                toReturn.put(objs.get(i), produced.get(objs.get(i)));
        }
        System.out.println("mm");
        System.out.println(toReturn.values());
        return toReturn;
    }

}
