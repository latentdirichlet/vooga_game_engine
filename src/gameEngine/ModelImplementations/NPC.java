package gameEngine.ModelImplementations;

import playerEnvironment.controller.AnimationManager;

import java.net.URI;
import java.util.Random;

public class NPC extends Actor {

    protected static final int RANGE_HEIGHT = 2;
    protected static final int RANGE_WIDTH = 2;

    protected static final int DIRECTION_CHANGE = 10;
    protected Random DIR_RANDOM_SEED;

    protected Position ULMovingRange;
    protected Size movingRange;

    public NPC(int id, String path) {
        super(id, path);
        this.movingRange = new Size(RANGE_WIDTH, RANGE_HEIGHT);
        this.ULMovingRange = this.position;
        DIR_RANDOM_SEED = new Random();
    }

    protected void addToInventory(GameObject g) {
        return;
    }

    public void setRange(Size r){
        movingRange = r;
    }

    public void setULMovingRange(Position p){
        ULMovingRange = p;
    }

    private boolean inMovingRange(int newX, int newY){
        return (newX >= ULMovingRange.getX() && newX<ULMovingRange.getX()+movingRange.getHeight()
            && newY >= ULMovingRange.getY() && newY<ULMovingRange.getY()+movingRange.getWidth());
    }

    public void turnRandomly(){
        int r = DIR_RANDOM_SEED.nextInt(DIRECTION_CHANGE);
        switch(r){
            case 0:
                getDirection().setDirection("north");
                break;
            case 1:
                getDirection().setDirection("west");
                break;
            case 2:
                getDirection().setDirection("east");
                break;
            case 3:
                getDirection().setDirection("south");
                break;
        }
    }

    public boolean checkIsFightable(){
        return false;
    }

//    @Override
//    public void forward(GameMap map) {
//        int newX = this.position.getX();
//        int newY = this.position.getY();
//        if (getDirection().getXdir() != 0) {
//            newX += getDirection().getXdir();
//        } else {
//            newY += getDirection().getYdir();
//        }
//        if (map.checkInBound(newX, newY) && map.isAvailable(newX, newY) && inMovingRange(newX, newY)) {
//            this.position.updateXLocation(newX);
//            this.position.updateYLocation(newY);
//        }
//
//    }
}