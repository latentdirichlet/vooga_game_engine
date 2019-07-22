package gameEngine.ModelImplementations;

import gameEngine.ModelImplementations.Events.ConditionManager;
import gameEngine.ModelImplementations.Statistics.Statistics;
import gameEngine.ModelInterfaces.Moveable;
import playerEnvironment.controller.*;

import java.util.Map;

public abstract class Actor extends GameObject implements Moveable {

    protected static final int ACTOR_HEIGHT = 1;
    protected static final int ACTOR_WIDTH = 1;

    protected Direction dir = new Direction(0, 1);
    protected Statistics stats = new Statistics();
    protected Size size = new Size(ACTOR_HEIGHT, ACTOR_WIDTH);
    protected AnimationManager animationManager;
    protected ConditionManager conditionManager;
    protected PathManager pathManager;
    protected CombatManager combatManager;

    protected final int SHORT_TIME = 30;
    protected final int LONG_TIME = 60;
    protected final int WALK_TIME = 15;

    public Actor(int id, String path) {
        super(id, path);
        setWalkable(false);
    }

    //------------initialization----------------

    public void initialize(AnimationManager am, ConditionManager cm, PathManager pm, CombatManager combat){
        this.animationManager = am;
        this.conditionManager = cm;
        this.pathManager = pm;
        this.combatManager = combat;
    }

    //-------------Direction--------------
    public Direction getDirection(){
        return dir;
    }

    public void setDirection(Direction d){
        dir = d;
    }

    public void setDirection(String input){
        this.dir.setDirection(input);
    }

    //--------------Movement-------------------

    public void setWalkPath(Position start, Position end){
        pathManager.setObjectPath(this.id, new Path(start, end, this.WALK_TIME));
        pathManager.activatePath(this.getID());
    }

    //--------------Movement-------------------

    @Override
    public void move(int x, int y) {
        if(checkPosition(x, y)){

            map.restoreTraversible(this.getPosition(), scale);
            Position newp = new Position(x, y, map.getId());
            setWalkPath(this.position, newp);
            map.setTraversible(newp, scale, true);

            //changed this so that atLocation events are triggered correctly
            position.updateLocation(x, y, map.getId());
//            this.position = new Position(x, y);
        }
        else if(!map.checkInBound(x, y)) {
            conditionManager.notifyWalkOffMap(this.getMap().getId());
        }
        else if(map.hasObject(x, y, map.getId())){
            GameObject g = map.getObject(x, y);
            conditionManager.notifyCollision(this, g);
            if(map.getObject(x, y).isCollectible()){
                map.removeGameObject(g);
                addToInventory(g);
                map.restoreTraversible(g.getPosition(), scale);
            }
        }
    }

    protected abstract void addToInventory(GameObject g);

    public void teleport(int x, int y, int map) {
        position.updateLocation(x, y, map);
    }

    @Override
    public void forwardNorth() {
        int newX = this.position.getX();
        int newY = this.position.getY()-1;
        move(newX, newY);
    }

    @Override
    public void forwardSouth() {
        int newX = this.position.getX();
        int newY = this.position.getY()+1;
        move(newX, newY);
    }

    @Override
    public void forwardEast() {
        int newX = this.position.getX()+1;
        int newY = this.position.getY();
        move(newX, newY);
    }

    @Override
    public void forwardWest() {
        int newX = this.position.getX()-1;
        int newY = this.position.getY();
        move(newX, newY);
    }

    @Override
    public void turnNorth() {
        this.dir.north();
    }

    @Override
    public void turnSouth() {
        this.dir.south();
    }

    @Override
    public void turnWest() {
        this.dir.west();
    }

    @Override
    public void turnEast() {
        this.dir.east();
    }

    protected boolean checkPosition(int x, int y){
        // && !map.hasObject(x, y, map.getId())
        if(map.checkInBound(x, y) && map.isAvailable(x, y)){
            return true;
        }
        return false;
    }

    //--------------Update---------------------

    @Override
    public void update() {
        if(this.stats.getHealth() <= 0){
            die();
        }
        notifyObservers();
    }

    private void die(){
        System.out.println("died: " + this.position);
        this.map.removeGameObject(this);
    }

    // -------------Statistics------------------

    public Statistics getStats(){
        return stats;
    }

    public double getAttackValue(){
        return this.stats.getAttackpoints();
    }

    // --------------Utility----------------------

    /**
     * the next position in the direction of the main character
     * @return position
     */
    protected Position nextPosition(){
        int newX = this.position.getX();
        int newY = this.position.getY();
        if(dir.getXdir() != 0){
            newX += dir.getXdir();
        }
        else{
            newY += dir.getYdir();
        }
        return checkPosition(newX, newY) ? new Position(newX, newY, map.getId()) : this.position;
    }


}
