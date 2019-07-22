package gameEngine.ModelImplementations;

import gameEngine.ModelInterfaces.ItemInterface;

/**
 * Items are collectible game objects that are added to the inventory when they are collided with automatically. Items can then be used and can be assigned a particular use with the creation of an ItemUsageCondition and action.
 * @mpz5
 */
public class Item extends GameObject {
    protected String name;

    public Item(int id, String ip){
        this(id, "Item " + id, ip);
    }
    public Item(int id, String n, String ip){
        super(id,ip);
        this.name = n;
        setCollectible();
    }

    public String getName() {
        return name;
    }

    public void setName(String newName){
        name = newName;
    }

    @Override
    public String toString() {
        return "Item " + this.getID() + ": " + this.name;
    }

    @Override
    public boolean checkIsFightable() {
        return false;
    }
}
