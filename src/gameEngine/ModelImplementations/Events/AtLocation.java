package gameEngine.ModelImplementations.Events;

import gameEngine.ModelImplementations.Actor;
import gameEngine.ModelImplementations.GameObject;
import gameEngine.ModelInterfaces.CellInterface;

import java.util.ArrayList;
import java.util.List;

/**
 *  Concrete instance of a PropertyCondition. This condition is used to check if location is at a particular cell. The location can be at any of the cells specified in cells.
 *  @author mpz5
 */
public class AtLocation extends PropertyCondition {
    List<CellInterface> cells;

    /**
     * The constructor adds this condition as a listener to the x and y values in the Position class of the GameObject passed to it. The GameObject must be an actor.
     * @param pMovingObject
     * @param pCells
     */
    public AtLocation(GameObject pMovingObject, List<Object> pCells){
        super((Actor) pMovingObject);
        cells = new ArrayList<>();
        for(Object o : pCells){
            cells.add((CellInterface)o);
        }
        pMovingObject.getPosition().addPropertyListener(this);
    }

    /**
     * Method determines if the location of the game object is equal to any of the cells passed into the constructor
     * @param o
     * @return
     */
    @Override
    public boolean checkCondition(GameObject o){
        for(CellInterface cell : cells){
            if(o.getPosition().equals(cell.getPosition())){
               return true;
            }
        }
        return false;
    }

}
