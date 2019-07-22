package gameEngine.ModelImplementations;

import com.thoughtworks.xstream.core.ReferenceByIdMarshaller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ItemGenerator {
    public static final String ITEM_RESOURCE_PATH = "ItemResources.Items";
    private List<Item> potentialItems;

    /**
     * Load default items from resource file
     */
    public ItemGenerator() {
        ResourceBundle resources = ResourceBundle.getBundle(ITEM_RESOURCE_PATH);
        potentialItems = new ArrayList<>();
    }

    /**
     * returns list of all available items to be put into the game
     *
     * @return
     */
    public List<Item> getPotentialItems() {
        return potentialItems;
    }

    /**
     * returns the ID of the new item created
     * @param name
     * @param filePath
     * @return
     */
//    public Item createItem(int id, String name, String filePath){
//        Item newItem = new Item(itemCount, name, filePath);
//        potentialItems.add(newItem);
//        itemCount++;
//        return newItem;
//    }
//
//    public Item createItem(String filePath){
//        return createItem("Item " + itemCount, filePath);
//    }
//}
    }
