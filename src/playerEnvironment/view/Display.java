package playerEnvironment.view;

import gameEngine.ModelImplementations.Game;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import playerEnvironment.controller.UserInputManager;

/**
 * @author Rohit Das (rvd5)
 * @author Peter Ciporin (pbc9)
 *
 * Top-level abstract superclass for different kinds of displays that are rendered to the game window in the player environment
 * All Displays have a UserInputManager to handle keyboard and mouse input during gameplay
 */
public abstract class Display extends Scene {

    private UserInputManager userInputManager;

    public Display(Parent root, double width, double height) {
        super(root, width, height, Color.BLACK);
        setFill(Color.BLACK);
        this.userInputManager = new UserInputManager();
        this.setOnKeyPressed(e -> userInputManager.handleKeyPressed(e.getCode(), getMyGame()));
    }

    /**
     *
     * @return must have Game object associated with it
     */
    public abstract Game getMyGame();

}
