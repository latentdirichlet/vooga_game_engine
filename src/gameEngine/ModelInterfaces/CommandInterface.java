package gameEngine.ModelInterfaces;

/**
 * This interface will be used by the Event classes in order to run their events. The implementations of this interface will likely hold a consumer and a game object. The run method will then cause the consumer to accept that game object.
 */

public interface CommandInterface {

    /**
     * This method will run the command.
     */
    void run();

}