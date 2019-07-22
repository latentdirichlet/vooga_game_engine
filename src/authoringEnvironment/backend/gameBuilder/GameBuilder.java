package authoringEnvironment.backend.gameBuilder;
import gameEngine.ModelImplementations.Game;
import gameEngine.ModelImplementations.GameCharacter;
import gameEngine.ModelImplementations.GameWorld;
import gameEngine.ModelInterfaces.GameInterface;
import gameEngine.ModelInterfaces.LevelInterface;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author: Shenghong Zhao
 * This is a simple extension from gameEngine/Game with some simple add-ons
 */
public class GameBuilder extends Game {
    public GameBuilder(GameCharacter gc) {
        super();
        setGameCharacter(gc);
    }
//    public GameBuilder(GameCharacter p, GameWorld w) {
//        super(p, w);
//    }


    public GameCharacter getCharacter(){
        return (GameCharacterBuilder) gameCharacter;
    }

    public GameWorldBuilder getWold(){
        return (GameWorldBuilder) world;
    }

    public void setCharacter(GameCharacterBuilder c){
        gameCharacter = c;
    }

    public void setWorld(GameWorldBuilder w){
        world = w;
    }



}
