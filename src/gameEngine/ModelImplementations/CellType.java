package gameEngine.ModelImplementations;

public enum CellType {
    GRASS (true, "/CellGraphics/Blocks/grass.jpg"),
    LAVA (false, "/CellGraphics/Blocks/lava.jpg"),
    MUD (true, "/CellGraphics/Blocks/stones.jpg"),
    RIVER (false, "/CellGraphics/Blocks/water.jpg"),
    MARBLE (true, "/CellGraphics/Blocks/rock.jpg"),
    BLACKSTONE (true, "/CellGraphics/Blocks/brown_stone.jpg");

    private boolean walkable;
    private String path;

    CellType(boolean w, String p){
        walkable = w;
        path = p;
    }

    public boolean isWalkable(){
        return walkable;
    }

    public String getPath(){
        return path;
    }

    }
