package gameEngine.ModelImplementations;

import java.net.URI;

public class SimpleObject extends GameObject {
    private static final int NPC_HEIGHT = 1;
    private static final int NPC_WIDTH = 1;

    public SimpleObject(int id, String path) {
        super(id, path);
        this.size = new Size(NPC_WIDTH, NPC_HEIGHT);
    }

    @Override
    public boolean checkIsFightable() {
        return false;
    }
}
