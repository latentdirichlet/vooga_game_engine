package gameEngine.ModelImplementations;

import gameEngine.ModelInterfaces.Fightable;
import playerEnvironment.controller.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Monster extends Actor implements Fightable {

    private Map<String, Animation> animationMap = new HashMap<>();

    private final Coordinate DEFAULT_ENEMY_COORDINATE = new Coordinate(
            CombatManager.DEFAULT_COMBAT_COR1.getX() + CombatManager.ATTACK_DISTANCE,
            CombatManager.DEFAULT_COMBAT_COR1.getY());

    public Monster(int id, String path) {
        super(id, path);
        setupDefaultCombatAnimationMaps();
        setupDefaultSprite();
    }

    @Override
    protected void addToInventory(GameObject g) {
        return;
    }

    private void setupDefaultSprite(){
        String monsterSpritePath = "/characters/2.png";
        this.defaultSprite = Sprite.imageToSprite(monsterSpritePath, scale);
    }

    public void initiateCombat(){
        combatManager.setIdleAnimation(this.getID(), animationMap.get("idle"));
        combatManager.setDefaultCoordinate(this.getID(), CombatManager.DEFAULT_COMBAT_COR2);
    }


    public Animation getAttackAnimation(){
        Animation attackAn = Animation.combineAnimation(animationMap.get("moveleft"), animationMap.get("moveleft"));
        attackAn = Animation.combineAnimation(attackAn, animationMap.get("lightattack"));
        attackAn = Animation.combineAnimation(attackAn, animationMap.get("moveright"));
        attackAn = Animation.combineAnimation(attackAn, animationMap.get("moveright"));
        return attackAn;
    }

    public Path getAttackPath(){
        Path attackPath = new Path(CombatManager.DEFAULT_COMBAT_COR2, DEFAULT_ENEMY_COORDINATE, 60);
        attackPath.extendPath(DEFAULT_ENEMY_COORDINATE, 30);
        attackPath.extendPath(CombatManager.DEFAULT_COMBAT_COR2, 60);
        return attackPath;
    }

    private void setupDefaultCombatAnimationMaps(){
        String combatSpritePath = "/combat/minotaur.png";
        SpriteSheet combatSheet = new SpriteSheet(combatSpritePath, 20, 9, 4.5);
        animationMapHelper(10, 5, LONG_TIME, "idle", combatSheet);
        animationMapHelper(13, 9, SHORT_TIME, "heavyattack", combatSheet);
        animationMapHelper(14, 5, 30, "lightattack", combatSheet);
        animationMapHelper(16, 9, SHORT_TIME, "specialattack", combatSheet);
        animationMapHelper(18, 3, SHORT_TIME, "damage", combatSheet);
        animationMapHelper(19, 6, LONG_TIME, "death", combatSheet);
        animationMapHelper(2, 5, 30, "moveright", combatSheet);
        animationMapHelper(12, 5, 30, "moveleft", combatSheet);
    }

    private void animationMapHelper(int row, int tillColumn, int actionFrame, String name, SpriteSheet combatSheet){
        List<Sprite> tmp = new ArrayList<>();
        for(int i = 0; i < tillColumn; i++){
            tmp.add(combatSheet.getSprite(row, i));
        }
        Animation an = new Animation(tmp, actionFrame);
        animationMap.put(name, an);
    }

    @Override
    public void decreaseHealth(double amount) {
        this.stats.decreaseHP(amount);
        combatManager.playActionAnimation(this.getID(), animationMap.get("damage"));
    }

    @Override
    public double getHealth() {
        return this.stats.getHealth();
    }

    @Override
    public boolean checkIsFightable() {
        return true;
    }

    public void attack(GameCharacter character){
        Animation attackAn = getAttackAnimation();
        combatManager.playActionAnimation(this.getID(), attackAn);
        Path attackPath = getAttackPath();
        combatManager.playActionPath(this.getID(), attackPath);
        double damage = getAttackValue();
        damage = damage - character.getStats().getArmor() > 0 ? damage - character.getStats().getArmor():0;
        character.decreaseHealth(damage);

    }


}
