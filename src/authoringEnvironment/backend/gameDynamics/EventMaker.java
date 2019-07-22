package authoringEnvironment.backend.gameDynamics;

import authoringEnvironment.backend.MainModel;
import authoringEnvironment.utils.ConditionPackage;
import gameEngine.ModelImplementations.Events.*;
import gameEngine.ModelImplementations.Events.Actions.ActionFactoryManager;
import gameEngine.ModelImplementations.Events.Actions.ActionType;
import gameEngine.ModelImplementations.GameObject;
import gameEngine.ModelInterfaces.ConditionInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
/**
 * @author: Shenghong Zhao
 * This is a util class to create conditions and actions for events
 */
public class EventMaker {
    private ActionFactoryManager actionFactoryManager = new ActionFactoryManager();
    private ConditionFactoryManager conditionFactoryManager;
    private MainModel mm;

    //pass in conditionManager and mainModel to use their functionalities
    public EventMaker(String resource, ConditionManager cm, MainModel mm){
        conditionFactoryManager = new ConditionFactoryManager(resource, cm);
        this.mm = mm;
    }

    //return a list of prepared objects on which actions will happen
    public List<Object> createActionObjects(List<ConditionPackage> objs, List<ActionType> types,
                                                       List<List<Object>>args) throws IllegalArgumentException,ClassCastException
    {
        List<Object> pass = new ArrayList<>();
        parseConditionObjs(objs, pass, null, false);
        return pass;
    }

    //create conditions using args passed from the frontend
    public List<ConditionInterface> createConditions(List<List<ConditionPackage>> left,
                                                     List<List<ConditionPackage>> right,
                                                     List<ConditionType> type)throws IllegalArgumentException{
        List<ConditionInterface> toReturn = new ArrayList<>();
        for(int i = 0; i<type.size(); i++)
        {
            if (type.get(i)==ConditionType.COLLISION)
                toReturn.add(createCondition(left.get(i), right.get(i), ConditionType.COLLISION,
                        ConditionPackage.Type.OBJECT, ConditionPackage.Type.OBJECT,null));
            else if(type.get(i) == ConditionType.LOCATION)
                toReturn.add(createCondition(left.get(i), right.get(i), ConditionType.PROPERTIES,
                        ConditionPackage.Type.OBJECT, ConditionPackage.Type.CELL, PropertyType.LOCATION));
            else if(type.get(i) == ConditionType.PROPERTIES)
                toReturn.add(createCondition(left.get(i), right.get(i), ConditionType.PROPERTIES,
                        ConditionPackage.Type.OBJECT, ConditionPackage.Type.PROPERTY,PropertyType.HEALTH));
            else if(type.get(i) == ConditionType.ITEMUSAGE)
                toReturn.add(createCondition(left.get(i),right.get(i), ConditionType.ITEMUSAGE,
                        ConditionPackage.Type.KEYPRESS, null, null));
            else if(type.get(i) == ConditionType.DIY)
                toReturn.add(createCondition(left.get(i), right.get(i), ConditionType.DIY,
                        null,null,null));
            else if(type.get(i) == ConditionType.WALKOFF)
                toReturn.add(createCondition(left.get(i), right.get(i), ConditionType.WALKOFF, null,
                        null, null));

//                toReturn.add(createCondition(left.get(i),right.get(i), ConditionType.ITEMUSAGE,
//                        ))

        }
        return toReturn;
    }

    public void makeMonster(GameObject gameObject) {
        List<List<Object>> arguments = new ArrayList<>();

        List<Object> gc = new ArrayList<>();
        gc.add(mm.getCharacter());

        List<Object> monsters = new ArrayList<>();
        monsters.add(gameObject);

        arguments.add(gc);
        arguments.add(monsters);

        Condition c = conditionFactoryManager.createCondition(arguments, ConditionType.COLLISION);
        List<List<Object>> args = new ArrayList<>();
        args.add(List.of(c));
        Event combat = new Event(List.of(c),List.of(mm.getGame()),List.of(ActionType.COMBAT), args);
//        Event combat = new CombatEvent(mm.getGame(), c);
//        mm.getGame().addEvent(combat);
    }

    //create a single condition
    public ConditionInterface createCondition(List<ConditionPackage> left,
                                                             List<ConditionPackage> right,
                                                       ConditionType condType,
                                                       ConditionPackage.Type type1,
                                                       ConditionPackage.Type type2,
                                                        PropertyType propertyType
                                              ) throws IllegalArgumentException
    {
        List<List<Object>> arguments= new ArrayList<>();
        if(propertyType!=null)
            arguments.add(List.of(propertyType));
        if (left.isEmpty() && right.isEmpty())
        {
            return null;
        }
        List<Object> first = new ArrayList<>();
        parseConditionObjs(left, first, type1, type1!=null);
        arguments.add(first);
        if(right.isEmpty())
            return conditionFactoryManager.createCondition(arguments,condType);
        List<Object> second = new ArrayList<>();

        parseConditionObjs(right, second, type2, type2!=null);
        arguments.add(second);
        return conditionFactoryManager.createCondition(arguments,condType);

    }

    //parse frontend objects to real objects while checking for type mismatch
    private void parseConditionObjs(List<ConditionPackage> left, List<Object> first, ConditionPackage.Type type, boolean check) throws IllegalArgumentException{
        for (int i = 0; i<left.size(); i++)
        {
            ConditionPackage obj = left.get(i);
//            if (obj.type== Ccheck && obj.type!= type)
//            {
//                throw new IllegalArgumentException("one or more of the condition objects you passed in are of illegal type");
//            }
            Object toAdd = null;
            if(obj.type== ConditionPackage.Type.OBJECT)
                 toAdd= mm.getWorld().getMapAt(obj.map).getObject(obj.x,obj.y);
            else if(obj.type == ConditionPackage.Type.CELL)
                toAdd=mm.getWorld().getMapAt(obj.map).getCellGrid()[obj.y][obj.x];
            else if(obj.type == ConditionPackage.Type.PROPERTY)
                toAdd=left.get(i).value;
            else if(obj.type == ConditionPackage.Type.CHARACTER)
                toAdd= mm.getGame().getCharacter();
            else if(obj.type == ConditionPackage.Type.KEYPRESS)
                toAdd=left.get(i).keyCode;


            if (toAdd==null)
                throw new IllegalArgumentException("empty condition object!");
            first.add(toAdd);
        }
    }

    public void createLinkEvent(ConditionPackage from, ConditionPackage to) {
        var cp1 = new ConditionPackage(ConditionPackage.Type.CHARACTER);
        var cp2 = new ConditionPackage(ConditionPackage.Type.CELL, from.x, from.y, from.map);

        var left1 = new ArrayList<ConditionPackage>();
        left1.add(cp1);

        var right1 = new ArrayList<ConditionPackage>();
        right1.add(cp2);

        var locationCondition = createCondition(left1, right1,
                ConditionType.PROPERTIES, ConditionPackage.Type.OBJECT, ConditionPackage.Type.CELL,
                PropertyType.LOCATION);

        var left2 = new ArrayList<>();
        left2.add(mm.getCharacter());

        var right2 = new ArrayList<>();
        right2.add(from.map);

        var arguments = new ArrayList<List<Object>>();
        arguments.add(left2);
        arguments.add(right2);

        var walkoffCondition = conditionFactoryManager.createCondition(arguments, ConditionType.WALKOFF);

        var conditions = new ArrayList<ConditionInterface>();
        conditions.add(locationCondition);
        conditions.add(walkoffCondition);

        var objs = new ArrayList<>();
        objs.add(mm.getCharacter());

        var actionTypes = new ArrayList<ActionType>();
        actionTypes.add(ActionType.LOCATION);

        var actionArgs = new ArrayList<List<Object>>();
        var actionArgs1 = new ArrayList<>();
        actionArgs1.add(to.x);
        actionArgs1.add(to.y);
        actionArgs1.add(to.map);
        actionArgs.add(actionArgs1);


        Event eve = new Event(conditions, objs, actionTypes, actionArgs);
        mm.getGame().addEvent(eve);
    }

    public void clearConditions() {
        conditionFactoryManager.clearConditionsMap();
    }

}
