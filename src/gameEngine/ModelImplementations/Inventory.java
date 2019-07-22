package gameEngine.ModelImplementations;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<Integer, Integer> idQuantityMap = new HashMap<>();
    private Map<Integer, Item> idMap = new HashMap<>();

    public void addItem(Item item){
        idMap.put(item.getID(), item);
        int id = item.getID();
        updateItemCount(id);
    }


    private void updateItemCount(int id){
        if(idQuantityMap.containsKey(id)){
            idQuantityMap.put(id, idQuantityMap.get(id)+1);
        }
        else{
            idQuantityMap.put(id, 1);

        }
    }

    public void dropItem(Item item){
        int id = item.getID();
        dropItem(id);
    }

    public void dropItem(int id){
        if(idQuantityMap.containsKey(id)){
            if(idQuantityMap.get(id)==1){
                idQuantityMap.remove(id);
            }
            else{
                idQuantityMap.put(id, idQuantityMap.get(id)-1);
            }
        }
    }

    public boolean containsItem(int id){
        return idQuantityMap.containsKey(id);
    }

    public boolean containsItem(Item item){
        return idQuantityMap.containsValue(item);
    }

    public Map<Item, Integer> getItemMap(){
        Map<Item, Integer> copyMap = new HashMap<>();
        for(Integer i: idQuantityMap.keySet()){
            copyMap.put(idMap.get(i), idQuantityMap.get(i));
        }
        return copyMap;
    }

}
