/**
 * This class is used to pair a consumer of an object with a specific object. Commands are used by the Event class to run an action on an Object at a particular time.
 */

package gameEngine.ModelImplementations.Events;

import java.util.function.Consumer;

/**
 * @author mpz5
 */
public class Command {
    private Object obj;
    private Consumer<Object> action;

    public Command(Object pObj, Consumer<Object> pAction){
        obj = pObj;
        action = pAction;
        System.out.println(action);
    }

    /**
     * Calls the accept function on the consumer so that it consumes the necessary object.
     */
    public void run(){
        action.accept(obj);
    }
}
