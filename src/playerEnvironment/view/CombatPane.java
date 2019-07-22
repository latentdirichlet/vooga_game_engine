package playerEnvironment.view;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * @author Rohit Das (rvd5)
 * @author Peter Ciporin (pbc9)
 *
 * This class represents the standard scene that is displayed when a player engages with a monster in combat
 */

public class CombatPane extends StackPane {

    private String combatBackgroundPath = "/combat/battle_background.jpg";
    private Pane pane = new Pane();
    private HealthDisplay characterHealthDisplay;
    private HealthDisplay monsterHealthDisplay;

    public CombatPane(GameDisplay gameDisplay){
        setupCombatPane(gameDisplay);
    }

    private void setupCombatPane(GameDisplay gameDisplay){
        ImageView combatBackground = new ImageView(combatBackgroundPath);
        combatBackground.fitWidthProperty().bind(this.widthProperty());
        combatBackground.fitHeightProperty().bind(this.heightProperty());
        this.getChildren().add(0, combatBackground);
        this.getChildren().add(1, pane);

        characterHealthDisplay = new HealthDisplay("ME");
        this.getChildren().add(2, characterHealthDisplay);
        StackPane.setAlignment(characterHealthDisplay, Pos.TOP_LEFT);
        monsterHealthDisplay = new HealthDisplay("MONSTER");
        this.getChildren().add(3, monsterHealthDisplay);
        StackPane.setAlignment(monsterHealthDisplay, Pos.TOP_RIGHT);

        CombatMenu combatMenu = new CombatMenu(this, gameDisplay);
        this.getChildren().add(4, combatMenu);
        StackPane.setAlignment(combatMenu, Pos.BOTTOM_CENTER);
    }

    /**
     *
     * @param characterHealth new health of main game character
     * @param characterMaxHealth maximum health of main game character
     * @param monsterHealth new health of monster opponent
     * @param monsterMaxHealth maximum health of monster opponent
     */
    public void updateHealth(double characterHealth, double characterMaxHealth, double monsterHealth, double monsterMaxHealth) {
        characterHealthDisplay.setHealthBar(characterHealth, characterMaxHealth);
        monsterHealthDisplay.setHealthBar(monsterHealth, monsterMaxHealth);
    }

    public Pane getPane() {
        return pane;
    }
}
