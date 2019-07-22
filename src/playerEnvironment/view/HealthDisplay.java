package playerEnvironment.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * @author Peter Ciporin (pbc9)
 * @author Rohit Das (rvd5)
 *
 * This view component is used to render health information about players involved in combat
 * A progress bar representing a player's current health (as it compares to the max possible health) is rendered
 * on top of a HP graphic borrowed from Pokemon
 */
public class HealthDisplay extends StackPane {

    private static final String BACKGROUND_PATH = "/health.png";
    private static final int BACKGROUND_SIZE = 200;
    private ProgressBar healthBar;


    public HealthDisplay(String name) {
        super();
        this.setMaxSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);
        this.setPadding(new Insets(10, 10, 10, 10));
        Image i = new Image(this.getClass().getResource(BACKGROUND_PATH).toExternalForm());
        ImageView background = new ImageView(i);
        background.setFitWidth(BACKGROUND_SIZE);
        background.setFitHeight(BACKGROUND_SIZE);
        background.setPreserveRatio(true);
        background.setSmooth(false);
        this.getChildren().add(background);

        initializeHealthPane(name);
    }

    private void initializeHealthPane(String name) {
        Label nameText = new Label(name);
        this.getChildren().add(nameText);
        setAlignment(nameText, Pos.TOP_LEFT);
        nameText.setPadding(new Insets(5, 0, 0, 10));

        healthBar = new ProgressBar();
        this.getChildren().add(healthBar);
        setAlignment(healthBar, Pos.BOTTOM_RIGHT);
        healthBar.setPadding(new Insets(-5, 20, 10, -24));
        healthBar.setMaxHeight(20);
    }

    /**
     * This public method is used to update the state of the progress bar when either the player's current or max health is altered
     * @param currentHealth the (updated) current health value of the player in question
     * @param maxHealth the (updated) maximum possible health value of the player in question
     */
    public void setHealthBar(double currentHealth, double maxHealth) {
        healthBar.setProgress(currentHealth / maxHealth);
    }

}
