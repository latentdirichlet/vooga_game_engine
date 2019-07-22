package gameEngine.ModelImplementations;


public class Passway extends GameObject {
    private static final int PASSWAY_HEIGHT = 1;
    private static final int PASSWAY_WIDTH = 1;

    public Passway(int id, String path){
        super(id, path);
        this.size = new Size(PASSWAY_WIDTH, PASSWAY_HEIGHT);
        setWalkable(true);
    }

    @Override
    public boolean checkIsFightable() {
        return false;
    }
}
