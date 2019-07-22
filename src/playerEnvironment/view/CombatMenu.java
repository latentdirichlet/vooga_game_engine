package playerEnvironment.view;

import gameEngine.CombatMoveType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * @author Rohit Das (rvd5)
 * @author Peter Ciporin (pbc9)
 *
 * This class is for the in-game menu that is displayed during a combat scene, consisting of a textbox and button menu
 */

public class CombatMenu extends HBox {
    private static final double BUTTON_MENU_PADDING = 10;
    private static final String COMBAT_TEXT = "You are now in combat!";

    private TextBox myTextBox;
    private GridPane myButtonMenu;
    private GameDisplay myGameDisplay;

    public CombatMenu(StackPane parent, GameDisplay gameDisplay) {
        super();
        myGameDisplay = gameDisplay;
        this.maxWidthProperty().bind(parent.widthProperty());

        myTextBox = new TextBox(parent);
        myTextBox.setupText(COMBAT_TEXT);
        myButtonMenu = new GridPane();

        myButtonMenu.prefWidthProperty().bind(parent.widthProperty().divide(2));
        this.maxHeightProperty().bind(myTextBox.heightProperty());

        setupButtonMenu();
        myButtonMenu.setPadding(new Insets(BUTTON_MENU_PADDING,BUTTON_MENU_PADDING,BUTTON_MENU_PADDING,BUTTON_MENU_PADDING));
        getChildren().addAll(myTextBox, myButtonMenu);
        StackPane.setAlignment(myTextBox, Pos.BOTTOM_CENTER);
    }

    private void setupButtonMenu() {
        Button attackButton = createButton("ATTACK", myButtonMenu);
        attackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myGameDisplay.getMyGame().handlePlayerCombatMove(CombatMoveType.ATTACK);
            }
        });

        Button inventoryButton = createButton("INVENTORY", myButtonMenu);
        inventoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myGameDisplay.openInventory(false);
            }
        });

        Button defendButton = createButton("DEFEND", myButtonMenu);
        defendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myGameDisplay.getMyGame().handlePlayerCombatMove(CombatMoveType.DEFEND);
            }
        });

        Button fleeButton = createButton("FLEE", myButtonMenu);
        fleeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myGameDisplay.getMyGame().handlePlayerCombatMove(CombatMoveType.FLEE);
                myGameDisplay.endCombat();
            }
        });

        myButtonMenu.add(attackButton,0,0);
        myButtonMenu.add(inventoryButton,1,0);
        myButtonMenu.add(defendButton,0,1);
        myButtonMenu.add(fleeButton,1,1);
    }

    /**
     *
     * @param text button label
     * @param parent pane within which button will reside
     * @return
     */
    private Button createButton(String text, GridPane parent) {
        Button button = new Button(text);
        button.getStyleClass().add("combatMenuButton");
        button.prefWidthProperty().bind(parent.widthProperty().divide(2));
        button.prefHeightProperty().bind(parent.heightProperty());
        return button;
    }
}
