package playerEnvironment.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * @author Rohit Das (rvd5)
 * @author Peter Ciporin (rvd5)
 *
 * This class represents a display for the player inventory, where they can see and use all of the items that they have collected
 */

public class InventoryDisplay extends StackPane {

    private final static int INVENTORY_SIZE = 500;
    private final static String inventoryImagePath = "/inventory.png";
    private final static int SIDE_MARGIN_SIZE = 60;
    private final static int TOP_BOTTOM_MARGIN_SIZE = 20;
    private final static int MAX_GRID_WIDTH = 3;
    private final static int GRID_SPACING = 5;

    private VBox itemDisplay;
    private GridPane items;
    private ImageView inventoryImage;
    private ScrollPane myInventoryScroller;
    private int currX = -1;
    private int currY = 0;

    public InventoryDisplay() {
        super();
        setMaxSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);
        inventoryImage = new ImageView(inventoryImagePath);
        inventoryImage.setFitWidth(INVENTORY_SIZE);
        inventoryImage.setFitHeight(INVENTORY_SIZE);
        inventoryImage.setPreserveRatio(true);
        inventoryImage.setSmooth(false);
        getChildren().add(0, inventoryImage);
        setAlignment(inventoryImage, Pos.CENTER);

        itemDisplay = new VBox(10);
        getChildren().add(1, itemDisplay);

        itemDisplay.setMaxHeight(INVENTORY_SIZE - SIDE_MARGIN_SIZE);

        itemDisplay.setAlignment(Pos.CENTER);


        Label title = new Label("INVENTORY");
        myInventoryScroller = new ScrollPane();
        myInventoryScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        myInventoryScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        myInventoryScroller.setPannable(true);
        myInventoryScroller.setStyle("-fx-background-color:transparent;");

        items = new GridPane();
        items.setHgap(GRID_SPACING);
        items.setVgap(GRID_SPACING);
        myInventoryScroller.setContent(items);

        itemDisplay.getChildren().addAll(title, myInventoryScroller);


        setAlignment(itemDisplay, Pos.TOP_CENTER);
        setMargin(itemDisplay, new Insets(TOP_BOTTOM_MARGIN_SIZE,SIDE_MARGIN_SIZE,TOP_BOTTOM_MARGIN_SIZE,SIDE_MARGIN_SIZE));
    }

    /**
     *
     * @param item ItemDisplay to be added to this InventoryDisplay at the correct location in the item grid
     */
    public void addItem(ItemDisplay item) {
        if(currX >= MAX_GRID_WIDTH - 1) {
            currX = 0;
            currY++;
        } else {
            currX++;
        }
        items.add(item, currX, currY);
    }

    /**
     * clear all ItemDisplays from this InventoryDisplay
     */
    public void clear() {
        items.getChildren().clear();
        currX = -1;
        currY = 0;
    }
}
