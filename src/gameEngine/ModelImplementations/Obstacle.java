package gameEngine.ModelImplementations;

/**
 * an example obstacle object that can be placed on the map
 * we can provide additional features such as changestate(), wither() methods for the designer to choose from
 */
public class Obstacle extends GameObject{

    private static final int OBSTACLE_HEIGHT = 1;
    private static final int OBSTACLE_WIDTH = 1;

    public Obstacle(int id, String path){
        super(id, path);
        this.size = new Size(OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
        setWalkable(false);
    }

    @Override
    public boolean checkIsFightable() {
        return false;
    }
}
