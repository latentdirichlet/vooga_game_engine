package gameEngine.ModelInterfaces;


import java.util.Map;

import gameEngine.CombatMoveType;
import gameEngine.ModelImplementations.CombatEndState;
import gameEngine.ModelImplementations.Events.ConditionManager;
import gameEngine.ModelImplementations.GameCharacter;
import gameEngine.ModelImplementations.GameMap;
import gameEngine.ModelImplementations.Position;
import javafx.scene.input.KeyCode;
import playerEnvironment.controller.AnimationManager;
import playerEnvironment.controller.Coordinate;
import playerEnvironment.controller.FrontEndInterface;
import playerEnvironment.controller.Sprite;

public interface GameInterface {
        //-------------------set methods----------------------

        void setGameCharacter(GameCharacter gameCharacter);


        void updateCurrentMap(GameMap currentMap);

        /**
         * handle user input
         * @param code
         */
        void handleKeyPressed(KeyCode code);

        GameMap getCurrentMap();

        GameCharacter getGameCharacter();

        void handleMovementKeys(String input);

        void update();

        AnimationManager getAnimationManager();

        ConditionManager getConditionManager();

        Map<Position, Sprite> getSpritesMap();

        Coordinate getMCCoordinate();

        Map<Coordinate, Sprite> getRenderMap();

        /**
         * This function will trigger a combat scene with the two game objects that collided. This method is called by CombatEvents.
         * @param o1
         * @param o2
         */
        void triggerCombat(GameObjectInterface o1, GameObjectInterface o2);

        void createTextBox(String message);

        void endCombat(CombatEndState endState);

        void handlePlayerCombatMove(CombatMoveType move);

        void useItem(int id);

}
