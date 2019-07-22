package authoringEnvironment.backend.gameBuilder;

import gameEngine.ModelImplementations.*;
import playerEnvironment.controller.AnimationManager;

/**
 * @author: Shenghong Zhao
 * This is a simple extension from gameEngine/GameCharacter with some simple add-ons
 */
public class GameCharacterBuilder extends GameCharacter{
    public static final String DEFAULT_IMG = "/characters/1.png";
    public static final int DEFAULT_X = 0;
    public static final int DEFAULT_Y = 0;


    public GameCharacterBuilder(int id, String path) {
        super(id, path);
    }

}
