package playerEnvironment.view;

import gameEngine.ModelImplementations.Item;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * @author Rohit Das (rvd5)
 * @author Peter Ciporin (pbc9)
 *
 * This class is used to represent a single item in the player's inventory display, consisting of its name, image, quantity, and "use" button
 */

public class ItemDisplay extends VBox {
    private Item myItem;
    private Button itemButton;

    public ItemDisplay(Item item, Image image, int quantity) {
        myItem = item;
        ImageView itemIcon = new ImageView(image);
        itemButton = new Button(item.getName() + " (" + quantity + ")");
        itemButton.getStyleClass().add("menuButton");
        getChildren().addAll(itemIcon, itemButton);
    }

    public Button getItemButton() {
        return itemButton;
    }

}
