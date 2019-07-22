package gameEngine.ModelImplementations.Events;

import java.util.List;
/**
 * This condition is notified by the notifyItemUsage method in the ConditionManager and determines if the any of the items listed in the constructor were the item used.
 * @author mpz5
 */
public class ItemUsageCondition extends Condition{
        List<Integer> items;

        public ItemUsageCondition(List<Integer> pItems){
            super();
            items = pItems;
        }

        public void notify(int itemId){
            if(items.contains(itemId)){
                setConditionMet();
            }
        }
}
