package playerEnvironment.controller;

import gameEngine.ModelImplementations.Game;
import gameEngine.ModelImplementations.GameSession;
import javafx.scene.input.KeyCode;
import playerEnvironment.view.Display;

/**
 * Contains methods specifically for handling user input (i.e. mouse and keyboard)
 */
public class UserInputManager implements Manager {

    private Display myCurrentDisplay;

    public UserInputManager(Display currentDisplay) {
        this.myCurrentDisplay = currentDisplay;
    }

    public UserInputManager() {

    }

    public void handleKeyPressed(KeyCode code, Game game){
        if (game != null) {
            game.handleKeyPressed(code);
        }
    }

}
