package playerEnvironment.controller;

import gameEngine.ModelImplementations.Game;
import gameEngine.ModelImplementations.GameSession;
import javafx.scene.input.KeyCode;
import playerEnvironment.view.GameDisplay;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author dc273
 */
public class GameInputManager implements Manager {

    private GameDisplay display;
    private Game game;
    private Map<KeyCode, String> controlMap = new HashMap<>();

    public GameInputManager(GameDisplay dp, Game game){
        this.display = dp;
        this.game = game;
        this.activeKeys = new HashSet<>();
        setupTestControlMap();
    }

    private void setupTestControlMap(){
        controlMap.put(KeyCode.SPACE, "interact");
//        controlMap.put(KeyCode.T, "test");
        controlMap.put(KeyCode.C, "combat");
        controlMap.put(KeyCode.M, "gameMenu");
        controlMap.put(KeyCode.B, "close");
        controlMap.put(KeyCode.P, "attack");
    }

    private Set<KeyCode> activeKeys;

    public void handleKeyPressed(KeyCode code){
        activeKeys.add(code);
    }

    public void handleKeyReleased(KeyCode code) {
        activeKeys.remove(code);
    }

    public void processActiveKeys() {
        for (KeyCode activeKey : activeKeys) {
            String input = controlMap.containsKey(activeKey) ? controlMap.get(activeKey):"null";
            if(input == null){
                handleInBackend(activeKey);
                return;
            }
            if(display.isTextBoxMode()) {
                handleTextBoxKeyPressed(activeKey, input);
            }
            handleNormalKeyPressed(activeKey, input);
        }
    }

    private void handleTextBoxKeyPressed(KeyCode code, String input){
        switch(input){
            case("interact"):
                this.display.nextTextBox();
                break;
        }
    }

    private void handleNormalKeyPressed(KeyCode code, String input){
        switch(input){
            case("test"):
                this.game.createTextBox("I started a new job. My boss said: 'Hi, my name is Rebecca, " +
                        "but people call me Becky.' I said: 'My name is Kyle, but people call me Dick.'" +
                        "She said: 'How do you get dick from kyle?'" +
                        "I replied: 'You just ask nicely'");
                break;
//            case("combat"):
//                this.game.triggerCombat();
            case("gameMenu"):
                this.display.toggleGameMenu();
                activeKeys.remove(code);
                break;
            case("close"):
                this.display.close();
                break;
            default:
                handleInBackend(code);
                break;
        }
    }

    private void handleInBackend(KeyCode code) {
        if (!display.isGamePaused()) {
            game.handleKeyPressed(code);
        }
    }
}
