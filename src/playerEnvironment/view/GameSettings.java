package playerEnvironment.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Ciporin (pbc9)
 * @author Rohit Das (rvd5)
 *
 * The view component used to render game settings when requested from the menu
 * Settings are displayed inside a ScrollPane on top of an image graphic pulled from the backend
 * It should be noted that this class is not currently functional, holding a single slider bar to demonstrate its use case
 */
public class GameSettings extends StackPane {

    private final static int BOX_SIZE = 300;
    private final static String imagePath = "/inventory.png";
    private final static int SIDE_MARGIN_SIZE = 50;
    private final static int TOP_BOTTOM_MARGIN_SIZE = 10;

    private VBox container;
    private VBox items;
    private ImageView statBox;
    private GameDisplay myDisplay;
    private ScrollPane myStatScroller;

    private Map<String, Double> currentStats = new HashMap<>();

    private double currentHealth;
    private double maxHealth;

    public GameSettings() {
        super();
        setMaxSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);
        statBox = new ImageView(imagePath);
        statBox.setFitWidth(BOX_SIZE);
        statBox.setFitHeight(BOX_SIZE);
        statBox.setPreserveRatio(true);
        statBox.setSmooth(false);
        getChildren().add(0, statBox);
        setAlignment(statBox, Pos.CENTER);

        container = new VBox(20);
        getChildren().add(1, container);
        setAlignment(container, Pos.CENTER);

        container.setMaxHeight(BOX_SIZE - SIDE_MARGIN_SIZE);
        container.setAlignment(Pos.CENTER);

        Label title = new Label("SETTINGS");

        myStatScroller = new ScrollPane();
        myStatScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        myStatScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        myStatScroller.setPannable(true);
        setAlignment(myStatScroller, Pos.CENTER);

        items = new VBox(10);
        setAlignment(items, Pos.CENTER);
        items.setAlignment(Pos.CENTER);
        container.getChildren().addAll(title, myStatScroller);
        container.setAlignment(Pos.CENTER);
        myStatScroller.setStyle("-fx-background-color:transparent;");
        //getChildren().add(2, items);
        myStatScroller.setContent(items);

        setAlignment(container, Pos.TOP_CENTER);
        setMargin(container, new Insets(TOP_BOTTOM_MARGIN_SIZE,SIDE_MARGIN_SIZE,TOP_BOTTOM_MARGIN_SIZE,SIDE_MARGIN_SIZE));

        addSliders();
    }

    /**
     * This method could be modified to add various sliders that adjust various game settings
     */
    private void addSliders() {
        HBox box = new HBox(10);
        Label label = new Label("Adjust Game Volume");
        Slider slider = new Slider();
        items.getChildren().addAll(label, slider);
    }

}
