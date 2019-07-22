package gameEngine.ModelImplementations.Events;

import gameEngine.ModelImplementations.Events.Actions.ActionFactoryManager;
import gameEngine.ModelImplementations.Events.Actions.ActionType;
import gameEngine.ModelInterfaces.ConditionInterface;
import gameEngine.ModelInterfaces.EventInterface;

import java.util.*;
import java.util.function.Consumer;

/**
 * This class implements the EventInterface and provides the functionality to create self-managing events. Events are created with the following arguments:
 *  1) A reaction map
 *  2) a list of conditions or a single condition
 *
 * The Reaction Map consists of a mapping of an object to a consumer for that object. For example a MainGame Object and a function to move that game object. Once handed ot the Event constructor, this map is converted into a list of Commands. Commands hold a single object and a single action (consumer for that object). Upon the calling of the Command run function, this consumer accepts the object.
 *
 * The conditions passed must all be met and must all be met in order. Conditions alert events to their being met using the triggerEvent function. Each Event (can) listen to the conditionMet boolean property of many conditions.
 *
 * Events are used to tie together a cause and effect within a game. Causes are conditions and effects are Commands.
 * @see ConditionFactoryManager for more information on various types of conditions
 * @see Command for more information on commands
 *
 * RECENTLY CHANGED: due to a bug where lambdas could not be serialized the Event constructor now takes the same arguments that used to be passed into the ActionFactoryManager.createAction method in the EventMaker class. In the main model class, in the newEvent method rather than call the em.createAction method it calls the em.createActionObjects method which creates the pass list of object using the parseConditionObjs function. It then passes this list of objects, the list of action types, and the list of arguments to the Event constructor. When an event is first triggered, it checks to see if its commands have been setup yet. If they have not, then it calls the initializeAction method, which uses the ActionFactoryManager. The current bugs are that when doing a property action the Object is unable to be casted to an Actor. This most likely means that the object being added to the objs list is incorrect or there is a problem with the order of the arguments.
 *
 * @author mpz5
 */
public class Event implements EventInterface{
    private List<Command> commands;
    private List<ConditionInterface> conditions;
    List<Object> objs;
    List<ActionType> types;
    List<List<Object>> args;

    /**
     * This constructor converts the reaction map passed into a list of commands and adds this event as a listener onto the conditionMet BooleanProperty of each condition passed.
     * @param pConditions list of Conditions that must all be met (in order) to trigger the event
     */

    public Event(List<ConditionInterface> pConditions, List<Object> pObjs, List<ActionType> pTypes, List<List<Object>> pArgs){

        for(ConditionInterface condition: pConditions){
            condition.addEventObserver(this);
        }
        conditions = pConditions;
        conditions.get(0).setActiveTrue();
        objs = pObjs;
        types = pTypes;
        args = pArgs;
        System.out.println(args.get(0).get(0));
    }

    public void initializeAction(){
        commands = new ArrayList<>();
        ActionFactoryManager afm = new ActionFactoryManager();
        Map<Object, Consumer<Object>> reactionMap = afm.makeActions(objs, types, args);
        for(Map.Entry<Object, Consumer<Object>> e : reactionMap.entrySet()){
            commands.add(new Command(e.getKey(), e.getValue()));
        }
    }

    /**
     * This function is called when the conditionMet BooleanProperty of the conditions that the event is listening to change. The result of this can either be a resetting of conditions if the conditionMet has turned off, or a progression through the list of conditions that must be met if the conditionMet is switched to on.
     * @param conditionMet
     */
    public void triggerEvent(Boolean conditionMet){
        updateConditions(conditionMet);
        if(commands == null){
            initializeAction();
        }
        if(checkComplete()){
            for(Command command : commands){
                command.run();
            }
            for(ConditionInterface c : conditions){
                c.reset();
            }
        }
    }

    /**
     * This helper function is used in triggerEvent to update the state of the conditions that this event relies on. This can involve resetting previous conditions due to a change or activating the next condition to be met.
     * @param conditionMet the most recent value of the most recently changed conditionMet property.
     */
    private void updateConditions(Boolean conditionMet){
        if(!conditionMet){
            for(ConditionInterface c : conditions){
                c.reset();
            }
            conditions.get(0).setActiveTrue();
        }
        else{
             for(ConditionInterface c : conditions){
                 if(!c.checkMet()){
                     c.setActiveTrue();
                     break;
                 }
             }
        }
    }

    /**
     * This helper function is used in the triggerEvent method to determine if the event has had all necessary conditions met and is ready to be triggered.
     * @return
     */
    private boolean checkComplete(){
        for(ConditionInterface c : conditions){
            if(!c.checkMet()){
                return false;
            }
        }
        return true;
    }
}
