package gameEngine.ModelInterfaces;
/**
 * Event classes are used to initiate interactions between GameObjects as well as board wide storyline events. These events are bound to the state of any object within the game and are triggered when that object changes. If the correct condition is met, the event is started and all relevant GameObjects are notified.
 */

public interface EventInterface{

    /**
     * This function will start the event. It will consist of iterating through the collection of commands associated with the event and running all of them.
     */
    void triggerEvent(Boolean conditionMet);

}