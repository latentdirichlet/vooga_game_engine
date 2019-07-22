package gameEngine.ModelImplementations;

import gameEngine.CombatMoveType;
import gameEngine.ModelImplementations.Events.*;
import gameEngine.ModelImplementations.Events.Actions.ActionType;
import gameEngine.ModelImplementations.Statistics.Statistics;
import gameEngine.ModelInterfaces.ConditionFactory;
import gameEngine.ModelInterfaces.GameInterface;
import gameEngine.ModelInterfaces.GameObjectInterface;
import javafx.scene.input.KeyCode;
import javafx.util.Pair;
import playerEnvironment.controller.*;

import java.util.*;

/**
 * @author mpz5 (only for combat related methods) aks64(for debugging and a few methods)
 */

public class Game implements GameInterface {

    protected GameCharacter gameCharacter;
    protected GameWorld world;
    protected GameMap currentMap;
    protected Combat currCombat;

    protected Map<KeyCode, String> controlMap = new HashMap<>();
    protected ConditionManager conditionManager = new ConditionManager();
    protected AnimationManager animationManager = new AnimationManager();
    protected TextBoxHelper textBoxHelper = new TextBoxHelper();
    protected PathManager pathManager = new PathManager();
    protected CombatManager combatManager = new CombatManager();

    protected List<Event> events = new ArrayList<>();

    private IDGen gen = new IDGen();

    private Coordinate currentMCCoordinate;
    private DisplayControl displayControl;

    private boolean paused;

    public Game(){
        System.out.println("new game");
        setupTestControl();
        setupTestGameCharacter();
        setupTestGameMap();
        updateCurrentMap(this.currentMap);
    }

    public void setWorld(GameWorld world) {
        this.world = world;
    }

    public GameWorld getWorld() {
        return world;
    }

    private void setupCombatEvent(Monster monster){
        ConditionFactoryManager cfm = new ConditionFactoryManager("ConditionResources.ConditionResource", conditionManager);
        List<List<Object>> arguments = new ArrayList<>();
        List<Object> gc = new ArrayList<>();
        gc.add(gameCharacter);
        List<Object> monsters = new ArrayList<>();
        monsters.add(monster);
        arguments.add(gc);
        arguments.add(monsters);

        List<Object> objs = new ArrayList<>();
        objs.add(this);
        List<ActionType> types = new ArrayList<>();
        types.add(ActionType.COMBAT);
        List<List<Object>> args = new ArrayList<>();
        Condition c = cfm.createCondition(arguments, ConditionType.COLLISION);
        args.add(List.of(c));

        Event combat = new Event(List.of(c),objs,List.of(ActionType.COMBAT), args);
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public List<Event> getEvents() {
        return events;
    }



    //-------------------testing-----------------------

    private void setupTestControl(){
        controlMap.put(KeyCode.W, "Up");
        controlMap.put(KeyCode.S, "Down");
        controlMap.put(KeyCode.A, "Left");
        controlMap.put(KeyCode.D, "Right");
        controlMap.put(KeyCode.Z, "A");
        controlMap.put(KeyCode.X, "B");
    }

    private void setupTestGameCharacter(){
        System.out.println("setting up fake character?");
        int id = gen.next();
        GameCharacter gc = new GameCharacter(id, "/characters/1.png");
        gc.setPosition(new Position(16, 19, 0));
        gc.initialize(animationManager, conditionManager, pathManager, combatManager);
        this.gameCharacter = gc;
    }

    private void setupTestGameMap(){
        int id = gen.next();
        GameMap gm = new GameMap(30, 30, 0);
        GameObject go1 = new Obstacle(id, "/CellGraphics/Objects/generic-rpg-barrel01.png");
        go1.setPosition(new Position(15, 20, 0));
        gm.addGameObject(go1);
        Monster monster = new Monster(gen.next(), "/combat/minotaur.png");
        monster.setPosition(new Position(16, 20, 0));
        monster.initialize(animationManager, conditionManager, pathManager, combatManager);
        gm.addGameObject(monster);
        setupCombatEvent(monster);
        this.currentMap = gm;
    }

    //-------------------set methods----------------------

    public void setGameCharacter(GameCharacter gameCharacter) {
        this.gameCharacter = gameCharacter;
        this.currentMCCoordinate = new Coordinate(gameCharacter.getPosition().getX(),gameCharacter.getPosition().getY());
    }

    public void setCurrentMap(GameMap currentMap) {
        this.currentMap.removeGameObject(gameCharacter);
        this.currentMap = currentMap;
        currentMap.addGameObject(gameCharacter);
        gameCharacter.setMap(currentMap);
    }

    private void setCurrentAnimation(GameMap currentMap){
//        animationManager.clear();
        for(GameObject go: currentMap.getObjectList()){
            if(go.getDefaultSprite() != null){
                animationManager.setObjectDefault(go.getID(), go.getDefaultSprite());
            }
            else{
                animationManager.setObjectDefault(go.getID(), Sprite.imageToSprite(go.getImagePath(), go.getScale()));
            }
        }
    }

    private void setCurrentPosition(GameMap currentMap){
        pathManager.clear();
        for(GameObject go: currentMap.getObjectList()){
            pathManager.setObjectCoordinate(go.getID(), Coordinate.PosToCor(go.getPosition()));
        }
    }

    public void updateCurrentMap(GameMap currentMap){
        setCurrentMap(currentMap);
        setCurrentAnimation(currentMap);
        setCurrentPosition(currentMap);
    }

    /**
     * handle user input
     * @param code
     */
    public void handleKeyPressed(KeyCode code){
        if(!paused){
            String input = controlMap.get(code);
            if(input!=null){
                handleMovementKeys(input);
                handleControlKeys(input);
            }
        }
    }

    public GameMap getCurrentMap() {
        return currentMap;
    }

    public GameCharacter getGameCharacter() {
        return gameCharacter;
    }

    private void handleControlKeys(String input){
        switch(input){
            case "A":
                return;
            case "B":
                return;
            default:
                return;
        }
    }

    public void handleMovementKeys(String input){
        if(this.animationManager.isAnimating(gameCharacter.getID()) || this.pathManager.isMoving(gameCharacter.getID())){
            return;
        }
        Direction dir = this.gameCharacter.getDirection();
        switch(input){
            case "Up":
                if(dir.getYdir() < 0)
                    gameCharacter.forwardNorth();
                else
                    gameCharacter.turnNorth();
                return;
            case "Down":
                if(dir.getYdir() > 0) {
                    System.out.println("lollollol");
                    gameCharacter.forwardSouth();
                }
                else
                    gameCharacter.turnSouth();
                return;
            case "Left":
                if(dir.getXdir() < 0)
                    gameCharacter.forwardWest();
                else
                    gameCharacter.turnWest();
                return;
            case "Right":
                if(dir.getXdir() > 0)
                    gameCharacter.forwardEast();
                else
                    gameCharacter.turnEast();
                return;
            default:
                return;
        }
    }

    public void update(){
        for(GameObject go: this.currentMap.getObjectList()){
            go.update();
        }
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public ConditionManager getConditionManager() {
        return conditionManager;
    }

    public PathManager getPathManager() {
        return pathManager;
    }

    public CombatManager getCombatManager() {
        return combatManager;
    }

    public Map<Position, Sprite> getSpritesMap(){
        Map<Position, Sprite> sMap = new HashMap<>();
        for(GameObject go: currentMap.getObjectList()){
            sMap.put(go.getPosition(), animationManager.getObjectSprite(go.getID()));
        }
        sMap.put(gameCharacter.getPosition(), animationManager.getObjectSprite(gameCharacter.getID()));
        return sMap;
    }

    public Coordinate getMCCoordinate(){
        return currentMCCoordinate;
    }

    public Map<Coordinate, Sprite> getRenderMap(){
        Map<Coordinate, Sprite> res = new HashMap<>();
        for(GameObject go: currentMap.getObjectList()){
            Coordinate tmpCor = pathManager.getObjectCoordinate(go.getID());
            Sprite tmpSpr = animationManager.getObjectSprite(go.getID());
            if(go.getID() == gameCharacter.getID())
                currentMCCoordinate = tmpCor;
            res.put(tmpCor, tmpSpr);
        }
//        System.out.println(res);
        return res;
    }
    public Map<String, Double> getRenderStats(){
        Map<String, Double> fieldValuesMap = new HashMap<>();
        Statistics stats = gameCharacter.getStats();
        fieldValuesMap.put("Max Health", stats.getMaxHealth());
        fieldValuesMap.put("HP", stats.getHealth());
        fieldValuesMap.put("AP", stats.getAttackpoints());
        fieldValuesMap.put("Armor", stats.getArmor());
        fieldValuesMap.put("XP", stats.getExp());
        System.out.println("Rendering stats: " + stats.toString());
        for(Map.Entry<String, Double> entry : fieldValuesMap.entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        return fieldValuesMap;
    }

    public void notifyMapChange(){

    }

    public String[][] getCellMap(){
        return this.getCurrentMap().getGridImage();
    }

    public double[][] getScaleMap() {
        return this.getCurrentMap().getGridScale();
    }

    public void useItem(int id){
        this.getGameCharacter().useItem(id);
    }

    public void setDisplayControl(DisplayControl dc){
        this.displayControl = dc;
    }
    public void setNewMap(){
        displayControl.displayNewMap();
    }

    //-----------------------textbox--------------------------------

    @Override
    public void createTextBox(String message) {
        displayControl.displayTextBox(message);
    }

    // -----------------------combat---------------------------------

    /**
     * This function will trigger a combat scene with the two game objects that collided. This method is called by CombatEvents.
     * @param o1
     * @param o2
     */
    public void triggerCombat(GameObjectInterface o1, GameObjectInterface o2){
        if(testCombatArgs(o1, o2)){
            createCombat((GameCharacter)o1, (Monster)o2);
        }
        else if(testCombatArgs(o2, o1)){
            createCombat((GameCharacter)o2, (Monster)o1);
        }
    }

    private void createCombat(GameCharacter gc, Monster monster){
        this.pauseGame();
        Combat combat = new Combat(gc, monster, this);
        gc.initiateCombat();
        monster.initiateCombat();
        combatManager.defineCombat(combat);
        displayControl.initiateCombat();
        currCombat = combat;
    }

    /**
     * determine if the arguments are valid for combat. This means that o1 must be the gameCharacter and o2 must be fightable
     * @param o1
     * @param o2
     * @return
     */
    private boolean testCombatArgs(GameObjectInterface o1, GameObjectInterface o2){
        return (o1 == gameCharacter && o2.checkIsFightable());
    }

    public void endCombat(CombatEndState endState){
        currCombat = null;
        if(endState == CombatEndState.PLAYERDIES){
            displayControl.endGame();
        }
        resumeGame();
    }

    @Override
    public void handlePlayerCombatMove(CombatMoveType move) {
        currCombat.makeMove(move);
    }

    public void startTestCombat(){
        this.pauseGame();
        Monster monster = new Monster(gen.next(), "/combat/minotaur.png");
        monster.initialize(animationManager, conditionManager, pathManager, combatManager);
        this.gameCharacter.initiateCombat();
        monster.initiateCombat();
        this.currCombat = new Combat(this.gameCharacter, monster, this);
        combatManager.defineCombat(currCombat);
        this.displayControl.initiateCombat();
    }

    public Map<Coordinate, Sprite> getCombatRenderMap(){
        return combatManager.getCombatRenderMap();
    }

    // ---------------------pause / resume-------------------

    public void pauseGame(){
        paused = true;
    }

    public void resumeGame(){
        paused = false;
    }

    public Map<Item, Integer> getInventoryMap(){
        return this.getGameCharacter().getItemMap();
    }

    public double getPlayerHealth(){
        if(currCombat != null){
            return currCombat.getCharacterHealth();
        }
        return 0;
    }

    public double getMonsterHealth(){
        if(currCombat != null){
            return currCombat.getMonsterHealth();
        }
        return 0;
    }

    public void repaintDisplay(){
        displayControl.displayNewMap();
    }
}
