package gameEngine.ModelImplementations;

public class CollectableItem extends GameObject {
    private static final int ITEM_HEIGHT = 1;
    private static final int ITEM_WIDTH = 1;

    private String myType; //"fish", "healthpotion"

    public CollectableItem(int id, String path, String itemType){
        super(id, path);
        this.size = new Size(ITEM_WIDTH, ITEM_HEIGHT);
        this.myType = itemType;
        this.setWalkable(false);
        this.setCoveredCells(true);
    }

    @Override
    public boolean checkIsFightable() {
        return false;
    }
}
