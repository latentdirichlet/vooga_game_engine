//package gameEngine.ModelTests;
//import gameEngine.ModelImplementations.Actor;
//import gameEngine.ModelImplementations.Events.Actions.ActionType;
//import gameEngine.ModelImplementations.Events.Actions.UpdateStatusActionFactory;
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.util.Map;
//import java.util.function.Consumer;
//
//public class StatusActionTest {
//    @Test
//    public void testFactory(){
//        UpdateStatusActionFactory factory = new UpdateStatusActionFactory();
//        Actor a = new Actor(1, "fakePath") {
//            @Override
//            public boolean checkIsFightable() {
//                return false;
//            }
//        };
//        Map<Object, Consumer<Object>> map = factory.createAction(a, ActionType.HEALTH_INCREASE, "", 10);
//        for(Map.Entry<Object, Consumer<Object>> entry : map.entrySet()){
//            double currHealth = a.getStats().getHealth();
//            entry.getValue().accept(entry.getKey());
//            Assert.assertEquals(a.getStats().getHealth(), currHealth + 10, 0);
//        }
//    }
//}
