package playerEnvironment.controller;

import gameEngine.ModelImplementations.Combat;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CombatManager {

    private AnimationManager animationManager = new AnimationManager();
    private PathManager pathManager = new PathManager();
    private Combat combat;
    private int activeID1 = 0;
    private int activeID2 = 1;
    private int totalFrame;
    private int counter;

    public static Coordinate DEFAULT_COMBAT_COR1 = new Coordinate(0.05, 0.5);
    public static Coordinate DEFAULT_COMBAT_COR2 = new Coordinate(0.7, 0.5);
    public static double ATTACK_DISTANCE = 0.02;

    public void defineCombat(Combat combat){
        this.combat = combat;
        this.activeID1 = this.combat.getGameCharacter().getID();
        this.activeID2 = this.combat.getMonster().getID();
    }

    public void setIdleAnimation(int id, Animation animation){
        animationManager.setObjectIdleAnimation(id, animation);
    }

    public void playActionAnimation(int id, Animation animation){
        animationManager.setObjectAnimation(id, animation);
        animationManager.activateAnimation(id);
        counter = 0;
        totalFrame = animation.getTotalFrame();
    }

    public void setDefaultSprite(int id, Sprite sprite){
        animationManager.setObjectDefault(id, sprite);
    }

    public void playActionPath(int id, Path path){
        pathManager.setObjectPath(id, path);
        pathManager.activatePath(id);
    }

    public void setDefaultCoordinate(int id, Coordinate cor){
        pathManager.setObjectCoordinate(id, cor);
    }

    public Map<Coordinate, Sprite> getCombatRenderMap(){
        Map<Coordinate, Sprite> res = new HashMap<>();
        // ID1
        Sprite sp1 = animationManager.getObjectSprite(activeID1);
        setSpritePriority(activeID1, sp1);
        res.put(pathManager.getObjectCoordinate(activeID1), sp1);
        // ID2
        Sprite sp2 = animationManager.getObjectSprite(activeID2);
        setSpritePriority(activeID2, sp2);
        res.put(pathManager.getObjectCoordinate(activeID2), sp2);
        checkAnimationComplete();

        return res;
    }

    private void checkAnimationComplete(){
        if(animationManager.isAnimating(activeID1)){
            counter++;
            if(counter >= totalFrame - 1){
                notifyAnimationComplete();
            }
        }
    }

    public void notifyAnimationComplete(){
        totalFrame = 0;
        counter = 0;
        combat.notifyTurnEnd();
    }

    private void setSpritePriority(int id, Sprite sp){
        if(animationManager.isAnimating(id)){
            sp.setPriority(Sprite.HIGH_PRIORITY);
        }
    }
}
