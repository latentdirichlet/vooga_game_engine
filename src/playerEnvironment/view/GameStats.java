package playerEnvironment.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Ciporin (pbc9)
 * @author Rohit Das (rvd5)
 *
 * The view component used to render the game character's current statistics when requested from the menu
 * Settings are displayed inside a ScrollPane on top of an image graphic pulled from the backend
 * Stats are held in the currentStats map, where each statistics name (String) is mapped to a current value (double)
 */
public class GameStats extends StackPane {

    private final static int BOX_SIZE = 300;
    private final static String imagePath = "/inventory.png";
    private final static int SIDE_MARGIN_SIZE = 50;
    private final static int TOP_BOTTOM_MARGIN_SIZE = 10;

    private VBox container;
    private VBox items;
    private ImageView statBox;
    private ScrollPane myStatScroller;

    private Map<String, Double> currentStats = new HashMap<>();

    public GameStats(Map<String, Double> stats) {
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

        Label title = new Label("STATS");

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
        myStatScroller.setContent(items);

        setAlignment(container, Pos.TOP_CENTER);
        setMargin(container, new Insets(TOP_BOTTOM_MARGIN_SIZE,SIDE_MARGIN_SIZE,TOP_BOTTOM_MARGIN_SIZE,SIDE_MARGIN_SIZE));

        updateStats(stats);
    }

    /**
     * This public method can be called by consumers to add new game statistics to GameStats
     *
     * @param stats a map that pair statistics' names (Strings) with their values (doubles)
     */
    public void updateStats(Map<String, Double> stats) {
        currentStats = stats;
        initializeStats();
    }

    /**
     * This helper method creates a StatItem object from each statistic in the currentStats map and adds it to the next row of items
     * Note that the method calls clear() on item's children to avoid rendering duplicate stats to GameStats
     */
    private void initializeStats() {
        items.getChildren().clear();

        for (Map.Entry<String, Double> entry : currentStats.entrySet()) {
            StatItem item = new NonProgressItem(entry.getKey(), entry.getValue());
            items.getChildren().add(item);
        }
    }

}
