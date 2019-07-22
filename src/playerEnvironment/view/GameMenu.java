package playerEnvironment.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * @author Rohit Das (rvd5)
 * @author Peter Ciporin (pbc9)
 *
 * This class represents the main in-game menu, which allows the player to access their inventory, map, stats, and settings as well as save and quit the game
 */

public class GameMenu extends StackPane {
    private final static int BUTTON_SPACING = 15;
    private final static int MENU_SIZE = 300;
    private final static String gameMenuImagePath = "/menu.png";

    private VBox menuButtons;
    private ImageView menuBox;
    private GameDisplay myDisplay;

    public GameMenu(GameDisplay display) {
        super();
        myDisplay = display;
        setMaxSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);
        menuBox = new ImageView(gameMenuImagePath);
        menuBox.setFitWidth(MENU_SIZE);
        menuBox.setFitHeight(MENU_SIZE);
        menuBox.setPreserveRatio(true);
        menuBox.setSmooth(false);
        getChildren().add(0, menuBox);
        menuButtons = new VBox(BUTTON_SPACING);
        menuButtons.getChildren().add(createInventoryButton(myDisplay));
        menuButtons.getChildren().add(createMapButton(myDisplay));
        menuButtons.getChildren().add(createStatsButton(myDisplay));
        menuButtons.getChildren().add(createSaveButton(myDisplay));
        menuButtons.getChildren().add(createSettingsButton(myDisplay));
        menuButtons.getChildren().add(createExitButton(myDisplay));
        menuButtons.setAlignment(Pos.CENTER);
        getChildren().add(1, menuButtons);
        setAlignment(menuButtons, Pos.CENTER);
    }

    private Button createInventoryButton(GameDisplay display) {
        Button inventoryButton = new Button("INVENTORY");
        inventoryButton.getStyleClass().add("menuButton");
        inventoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                display.openInventory(false);
            }
        });
        return inventoryButton;
    }

    private Button createMapButton(GameDisplay display) {
        Button mapButton = new Button("MAP");
        mapButton.getStyleClass().add("menuButton");
        mapButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                display.openMap();
            }
        });
        return mapButton;
    }

    private Button createStatsButton(GameDisplay display) {
        Button statsButton = new Button("STATS");
        statsButton.getStyleClass().add("menuButton");
        statsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                display.openGameStats();
                System.out.println("displaying game stats");
            }
        });
        return statsButton;
    }

    private Button createSaveButton(GameDisplay display) {
        Button saveButton = new Button("SAVE");
        saveButton.getStyleClass().add("menuButton");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myDisplay.saveGame();
            }
        });
        return saveButton;
    }

    private Button createSettingsButton(GameDisplay display) {
        Button settingsButton = new Button("SETTINGS");
        settingsButton.getStyleClass().add("menuButton");
        settingsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myDisplay.openGameSettings();
            }
        });
        return settingsButton;
    }

    private Button createExitButton(GameDisplay display) {
        Button settingsButton = new Button("QUIT");
        settingsButton.getStyleClass().add("menuButton");
        settingsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                display.exitGame();
            }
        });
        return settingsButton;
    }
}
