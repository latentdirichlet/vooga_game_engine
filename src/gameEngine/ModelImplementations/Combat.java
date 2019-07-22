package gameEngine.ModelImplementations;


import gameEngine.CombatMoveType;

/**
 * This class is used/instantiated in the triggerCombat method of the the Game class. It is used to initiate the back end of Combat between a game character and a monster.
 * @author mpz5
 */

public class Combat {
    GameCharacter gc;
    Monster monster;
    Game game;
    boolean active;

    public Combat(GameCharacter pGameCharacter, Monster pMonster, Game pGame){
        gc = pGameCharacter;
        monster = pMonster;
        game = pGame;
        active = true;
    }

    public void makeMove(CombatMoveType move){
        //handle player move
        handlePlayerMove(move);
        checkMonsterDeath();
        //make monster move
    }

    private boolean checkMonsterDeath(){
        if(monster.getHealth() <= 0){
            game.getCurrentMap().removeGameObject(monster);
            game.getCurrentMap().restoreTraversible(monster.getPosition(), monster.getScale());
            game.endCombat(CombatEndState.MONSTERDIES);
            active = false;
            return true;
        }
        return false;
    }

    private boolean checkPlayerDeath(){
        if(gc.getHealth() <= 0){
            game.endCombat(CombatEndState.PLAYERDIES);
            active = false;
            return true;
        }
        return false;
    }

    private void handlePlayerMove(CombatMoveType move){
        if (active) {
            switch(move){
                case ATTACK:
                    gc.attack(monster);
                    return;
                case FLEE:
                    active = false;
                    game.endCombat(CombatEndState.PLAYERFLEES);
                    return;
                case DEFEND:
                    gc.defend();
                    notifyTurnEnd();
                    break;
                case USEITEM:
                    notifyTurnEnd();
                    return;
            }
        }
    }

    public GameCharacter getGameCharacter(){
        return gc;
    }

    public Monster getMonster(){
        return monster;
    }

    public void notifyTurnEnd(){
        monster.attack(gc);
    }

    public double getCharacterHealth(){
        return this.gc.getHealth();
    }

    public double getMonsterHealth(){
        return this.monster.getHealth();
    }

}
