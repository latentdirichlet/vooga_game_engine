package gameEngine.ModelImplementations;

public class TeleportPoint extends GameObject{

    private static final int TELE_WIDTH = 1;
    private static final int TELE_HEIGHT = 1;

    private int destinationMapID;
    private Position destinationPosition;

    public TeleportPoint(int id, String path){
        super(id, path);
        this.size = new Size(TELE_WIDTH, TELE_HEIGHT);
        setWalkable(true);
        setCoveredCells(true);
    }

    public void setDestination(int mapID, Position d){
        destinationMapID = mapID;
        destinationPosition = d;
    }

    @Override
    public boolean checkIsFightable() {
        return false;
    }
}
