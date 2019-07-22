//package gameEngine.ModelTests;
//
//import gameEngine.ModelImplementations.*;
//import gameEngine.ModelImplementations.Events.*;
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Consumer;
//
//public class EventTest {
//    Game g = new Game();
//    ConditionManager cm = new ConditionManager();
//    ConditionFactoryManager cfm = new ConditionFactoryManager("ConditionResources.ConditionResource", cm);
//    GameCharacter gc = g.getGameCharacter();
//
//    @Test
//    public void testHealthConditions(){
//        List<Object> type = new ArrayList<>();
//        type.add(PropertyType.HEALTH);
//        List<Object> mainObject = new ArrayList();
//        mainObject.add(gc);
//        List<Object> healthLevels = new ArrayList();
//        healthLevels.add(0.0);
//        List<List<Object>> arguments = new ArrayList();
//        arguments.add(type);
//        arguments.add(mainObject);
//        arguments.add(healthLevels);
//        Condition c = cfm.createCondition(arguments, ConditionType.PROPERTIES);
//        gc.decreaseHealth(gc.getHealth());
//        System.out.println(gc.getHealth());
//        Assert.assertEquals(c.checkMet(), true);
//    }
//
//    @Test
//    public void testLocationConditions(){
//        List<Object> type = new ArrayList<>();
//        type.add(PropertyType.LOCATION);
//        List<Object> mainObject = new ArrayList();
//        mainObject.add(gc);
//        List<Object> locations = new ArrayList();
//        locations.add(new Cell(10, 10, "fakepath"));
//        List<List<Object>> arguments = new ArrayList();
//        arguments.add(type);
//        arguments.add(mainObject);
//        arguments.add(locations);
//        Condition c = cfm.createCondition(arguments, ConditionType.PROPERTIES);
//        gc.move(10,10);
//        System.out.println(gc.getPosition().toString());
//        Assert.assertEquals(c.checkMet(), true);
//    }
//
//}
