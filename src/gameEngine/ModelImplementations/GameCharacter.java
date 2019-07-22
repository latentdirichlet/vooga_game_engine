package gameEngine.ModelImplementations;

import gameEngine.ModelInterfaces.Fightable;
import gameEngine.ModelInterfaces.GameObjectInterface;
import gameEngine.ModelInterfaces.Moveable;
import playerEnvironment.controller.*;

import java.util.*;

/**
 * represents the main character that the user controls in the game world
 */
public class GameCharacter extends Actor implements Moveable, Fightable {

    private Inventory inventory = new Inventory();
    private Map<String, Animation> animationMap = new HashMap<>();
    private Map<String, Sprite> defaultMap = new HashMap<>();
    private Game game;

    private final Coordinate DEFAULT_ENEMY_COORDINATE = new Coordinate(
            CombatManager.DEFAULT_COMBAT_COR2.getX() - CombatManager.ATTACK_DISTANCE,
            CombatManager.DEFAULT_COMBAT_COR2.getY());

    public GameCharacter(int id, String path){
        super(id, path);
        setupDefaultAnimationMaps();
        setUpTestInventory();
        setDefaultSprite();
        setupDefaultCombatAnimationMaps();
        setWalkable(true);
    }

    @Override
    protected void addToInventory(GameObject g) {
        inventory.addItem((Item)g);
    }

    //-------------------Test-----------------------

    private void setDefaultSprite(){
        this.defaultSprite = defaultMap.get("south");
    }

    public void setUpTestInventory(){
        inventory.addItem(new Item(0, "fish", "/CellGraphics/Items/generic-rpg-fish01.png"));
        inventory.addItem(new Item(1, "flower", "/CellGraphics/Items/generic-rpg-flower01.png"));
        inventory.addItem(new Item(1, "flower", "/CellGraphics/Items/generic-rpg-flower01.png"));
        inventory.addItem(new Item(1, "flower", "/CellGraphics/Items/generic-rpg-flower01.png"));
        inventory.addItem(new Item(1, "flower", "/CellGraphics/Items/generic-rpg-flower01.png"));
        inventory.addItem(new Item(1, "flower", "/CellGraphics/Items/generic-rpg-flower01.png"));
        inventory.addItem(new Item(1, "flower", "/CellGraphics/Items/generic-rpg-flower01.png"));
        inventory.addItem(new Item(1, "flower", "/CellGraphics/Items/generic-rpg-flower01.png"));
//        inventory.addItem(new Item(1, "flower", "/CellGraphics/Objects/generic-rpg-flower01.png"));
//        inventory.addItem(new Item(1, "flower", "/CellGraphics/Objects/generic-rpg-flower01.png"));
//        inventory.addItem(new Item(1, "flower", "/CellGraphics/Objects/generic-rpg-flower01.png"));
//        inventory.addItem(new Item(1, "flower", "/CellGraphics/Objects/generic-rpg-flower01.png"));
//        inventory.addItem(new Item(1, "flower", "/CellGraphics/Objects/generic-rpg-flower01.png"));
    }

    //-------------------Animation---------------------------------


    private void setupDefaultAnimationMaps(){
        List<String> directions = new ArrayList<>();
        directions.add("north");
        directions.add("south");
        directions.add("east");
        directions.add("west");
        String base = "/characters/1_";
        String ext = ".png";
        List<String> paths = new ArrayList<>();
        for(String d: directions){
            paths.add(base+d+ext);
        }
        List<SpriteSheet> ssList = new ArrayList<>();
        for(String p: paths){
            ssList.add(new SpriteSheet(p, 1, 4, 1));
        }
        animationMapHelper(0, 4, WALK_TIME, "north", ssList.get(0));
        animationMapHelper(0, 4, WALK_TIME, "south", ssList.get(1));
        animationMapHelper(0, 4, WALK_TIME, "east", ssList.get(2));
        animationMapHelper(0, 4, WALK_TIME, "west", ssList.get(3));
        for(int i = 0; i < 4; i++){
            defaultMap.put(directions.get(i), ssList.get(i).getSprite(0,1));
        }
    }

    private void setWalkAnimation(String input){
        this.animationManager.setObjectAnimation(this.id, animationMap.get(input));
        this.animationManager.activateAnimation(this.getID());
    }

    private void setDefaultSprite(String input){
        this.animationManager.setObjectDefault(this.id, defaultMap.get(input));
    }


    public void setGame(Game game){
        this.game=game;
    }

    public Game getGame() {
        return game;
    }

    //--------------------Combat-------------------------

    public void initiateCombat(){
        combatManager.setIdleAnimation(this.getID(), animationMap.get("idle"));
        combatManager.setDefaultCoordinate(this.getID(), CombatManager.DEFAULT_COMBAT_COR1);
    }

    /**
     * attack in the current direction
     */
    public void attack(Monster monster){
        double damage = stats.getAttackpoints();
        monster.decreaseHealth(damage);
        combatManager.playActionAnimation(this.getID(), getAttackAnimation());
        combatManager.playActionPath(this.getID(), getAttackPath());
    }

    // possible critical hits
    public void heavyAttack(Monster monster){
        double damage = stats.getAttackpoints() * 1.5;
        monster.decreaseHealth(damage);
        combatManager.playActionAnimation(this.getID(), getHeavyAttackAnimation());
        combatManager.playActionPath(this.getID(), getAttackPath());

    }

    public Animation getAttackAnimation(){
        Animation attackAn = Animation.combineAnimation(animationMap.get("moveright"), animationMap.get("moveright"));
        attackAn = Animation.combineAnimation(attackAn, animationMap.get("lightattack"));
        attackAn = Animation.combineAnimation(attackAn, animationMap.get("moveleft"));
        attackAn = Animation.combineAnimation(attackAn, animationMap.get("moveleft"));
        return attackAn;
    }

    public Animation getHeavyAttackAnimation(){
        Animation attackAn = Animation.combineAnimation(animationMap.get("moveright"), animationMap.get("moveright"));
        attackAn = Animation.combineAnimation(attackAn, animationMap.get("heavyattack"));
        attackAn = Animation.combineAnimation(attackAn, animationMap.get("moveleft"));
        attackAn = Animation.combineAnimation(attackAn, animationMap.get("moveleft"));
        return attackAn;
    }

    public Path getAttackPath(){
        Path attackPath = new Path(CombatManager.DEFAULT_COMBAT_COR1, DEFAULT_ENEMY_COORDINATE, LONG_TIME);
        attackPath.extendPath(DEFAULT_ENEMY_COORDINATE, SHORT_TIME);
        attackPath.extendPath(CombatManager.DEFAULT_COMBAT_COR1, LONG_TIME);
        return attackPath;
    }

    private void setupDefaultCombatAnimationMaps(){
        String combatSpritePath = "/combat/adventurer.png";
        SpriteSheet combatSheet = new SpriteSheet(combatSpritePath, 16, 13, 3);
        animationMapHelper(0, 10, LONG_TIME, "idle", combatSheet);
        animationMapHelper(2, 10, SHORT_TIME, "lightattack", combatSheet);
        animationMapHelper(3, 10, SHORT_TIME, "heavyattack", combatSheet);
        animationMapHelper(4, 10, SHORT_TIME, "specialattack", combatSheet);
        animationMapHelper(6, 4, SHORT_TIME, "damage", combatSheet);
        animationMapHelper(7, 6, LONG_TIME, "death", combatSheet);
        animationMapHelper(1, 8, SHORT_TIME, "moveright", combatSheet);
        animationMapHelper(9, 8, SHORT_TIME, "moveleft", combatSheet);
    }

    private void animationMapHelper(int row, int tillColumn, int actionFrame, String name, SpriteSheet combatSheet){
        List<Sprite> tmp = new ArrayList<>();
        for(int i = 0; i < tillColumn; i++){
            tmp.add(combatSheet.getSprite(row, i));
        }
        Animation an = new Animation(tmp, actionFrame);
        animationMap.put(name, an);
    }

    //-------------------Movement-------------------------

    @Override
    public void forwardEast() {
        super.forwardEast();
        setWalkAnimation("east");
    }

    @Override
    public void forwardNorth() {
        super.forwardNorth();
        setWalkAnimation("north");
    }

    @Override
    public void forwardSouth() {
        super.forwardSouth();
        setWalkAnimation("south");
    }

    @Override
    public void forwardWest() {
        super.forwardWest();
        setWalkAnimation("west");
    }

    @Override
    public void turnNorth() {
        super.turnNorth();
        setDefaultSprite("north");
    }

    @Override
    public void turnSouth() {
        super.turnSouth();
        setDefaultSprite("south");
    }

    @Override
    public void turnWest() {
        super.turnWest();
        setDefaultSprite("west");
    }

    @Override
    public void turnEast() {
        super.turnEast();
        setDefaultSprite("east");
    }

    @Override
    public boolean checkInRange(GameObjectInterface otherObject) {
        return false;
    }

    /**
     * attack in the current direction
     */
//    public void attack(Monster monster) {
//        double damage = stats.getAttackpoints();
//        monster.decreaseHealth(damage);
//    }

    //----------------inventory-----------------

    public Map<Item, Integer> getItemMap(){
        return this.inventory.getItemMap();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void addItem(Item item){
        inventory.addItem(item);
    }

    public void dropItem(Item item){
        inventory.dropItem(item);
    }

    public void useItem(int id){
        if(this.inventory.containsItem(id)){
            conditionManager.notifyItemUsage(this, id);
        }
        inventory.dropItem(id);
    }

    @Override
    public void decreaseHealth(double amount) {
        this.stats.decreaseHP(amount);
    }

    public void increaseHealth(double amount) {
        this.stats.increaseHP(amount);
    }

    @Override
    public double getHealth() {
        return this.stats.getHealth();
    }

    @Override
    public boolean checkIsFightable() {
        return true;
    }

    public void defend(){
        stats.increaseArmor();
    }

    public Map<String, Animation> getAnimationMap() {
        return animationMap;
    }

    public boolean isAnimating(){
        return animationManager.isAnimating(this.getID());
    }

    public static void main(String[] args){
        GameCharacter gc = new GameCharacter(0, "");
        Animation attack = gc.getAttackAnimation();
    }

}
