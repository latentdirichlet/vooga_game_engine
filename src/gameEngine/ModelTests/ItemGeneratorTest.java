//package gameEngine.ModelTests;
//
//import gameEngine.ModelImplementations.ItemGenerator;
//import org.junit.Assert;
//import org.junit.Test;
//
//
//public class ItemGeneratorTest {
//    @Test
//    public void testItemGen(){
//        ItemGenerator ig = new ItemGenerator();
//        Assert.assertTrue(ig.getPotentialItems().size() >0);
//        Assert.assertEquals(ig.getPotentialItems().get(0).getImagePath(), "Objects.generic-rpg-fish01.png");
//        Assert.assertEquals(ig.getPotentialItems().get(0).getName(), "Fish");
//        Assert.assertEquals(ig.getPotentialItems().get(0).getId(), 0);
//        int newId = ig.createItem("HealthPotion", "HealthPotionPicture.png");
//        Assert.assertEquals(newId, 1);
//        Assert.assertEquals(ig.getPotentialItems().size(), 2);
//    }
//}
