package gameEngine.ModelInterfaces;

import gameEngine.ModelImplementations.Events.Event;

public interface ConditionInterface {

    void reset();

    void addEventObserver(Event e);

    boolean checkMet();

    void setActiveTrue();
}
