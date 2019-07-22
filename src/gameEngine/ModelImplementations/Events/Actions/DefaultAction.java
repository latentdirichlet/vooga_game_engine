package gameEngine.ModelImplementations.Events.Actions;

import gameEngine.ModelImplementations.Actor;
import gameEngine.ModelImplementations.Direction;
import gameEngine.ModelImplementations.GameCharacter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DefaultAction {

    public Map<Object, Consumer<Object>> createAction(Object o, Consumer<Object> action){
        Map<Object, Consumer<Object>> reaction = new HashMap<>();
        reaction.put(o, action);
        return reaction;
    }


    public Map<Object, Consumer<Object>> moveNorthAction(GameCharacter c){
        Consumer<Object> moveNorth = obj -> {
            GameCharacter gameCharacter = (GameCharacter)obj;
            Direction dir = gameCharacter.getDirection();

            if(dir.getYdir() >= 0)
                gameCharacter.forwardNorth();
            else
                gameCharacter.turnNorth();
        };
        return createAction(c, moveNorth);
    };

    public Map<Object, Consumer<Object>> moveSouthAction(GameCharacter c){
        Consumer<Object> moveSouth = obj -> {
            GameCharacter gameCharacter = (GameCharacter)obj;

            System.out.println("haha");
            if (gameCharacter.getDirection().getYdir() <= 0) {
                System.out.println("if");
                System.out.println(gameCharacter.getDirection().getYdir());
                gameCharacter.forwardSouth();
            } else{
                System.out.println("else");
                gameCharacter.turnSouth();
            }
        };
        return createAction(c, moveSouth);
    };

    public Map<Object, Consumer<Object>> moveEastAction(GameCharacter c){
        Consumer<Object> moveEast = obj -> {
            GameCharacter gameCharacter = (GameCharacter)obj;
            if (gameCharacter.getDirection().getXdir() <= 0) {
                gameCharacter.forwardEast();
            } else{
                gameCharacter.turnEast();
            }
        };
        return createAction(c, moveEast);
    };

    public Map<Object, Consumer<Object>> moveWestAction(GameCharacter c){
        Consumer<Object> moveEast = obj -> {
            GameCharacter gameCharacter = (GameCharacter)obj;
            if (gameCharacter.getDirection().getXdir() >= 0) {
                gameCharacter.forwardWest();
            } else{
                gameCharacter.turnWest();
            }
        };
        return createAction(c, moveEast);
    };

//    public Map<Object, Consumer<Object>> moveDirectionAction(Actor a, String direction){
//        Consumer<Object> moveDirection = obj -> {
//            Actor actor = (Actor)obj;
//            Direction dir = new Direction(1,1);
//            dir.setDirection(direction);
//            actor.setDirection(dir);
//            actor.();
//        };
//        return createAction(a, moveDirection);
//    }

    public Map<Object, Consumer<Object>> reverseDirectionAction(Actor a){
        Consumer<Object> reverseDirection = obj -> {
            Actor actor = (Actor)obj;
            actor.getDirection().reverseDirection();
        };
        return createAction(a, reverseDirection);
    }

}
