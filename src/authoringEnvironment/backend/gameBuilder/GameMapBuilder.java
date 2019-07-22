package authoringEnvironment.backend.gameBuilder;

import authoringEnvironment.backend.gameDynamics.EventMaker;
import authoringEnvironment.frontend.Menu.Components.PalettePane;
import gameEngine.ModelImplementations.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static authoringEnvironment.backend.MainModel.CONDITION_RESOURCE;

/**
 * @author Shenghong Zhao
 * This is an extension from gameEngine/GameMap
 */
public class GameMapBuilder extends GameMap {
    public static final int DEFAULT_SIZEX = 20;
    public static final int DEFAULT_SIZEY = 20;
    public static final String DEFAULT_CELLTYPE = "CellGraphics/Blocks/grass.jpg";
    private IDGen gen;

    //Constructor takes in map index
    public GameMapBuilder(int sizeX, int sizeY, int id) {
        super(sizeX, sizeY, id);
    }

    //We use a central id controller in the gameMap
    public void setGen(IDGen gen) {
        this.gen = gen;
    }

    //set the cell on a certain index according to img path, type, the game its on and scale
    public void setCellObject(int x, int y, String bg, PalettePane.Type type, Game game, double scale, EventMaker em){
        GameObject obj = null;
        switch (type) {
            case OBJECTS:
                obj = new Obstacle(gen.next(), bg);
                break;
            case NPCS:
                obj = new NPC(gen.next(), bg);
                ((Actor)obj).initialize(
                        game.getAnimationManager(),
                        game.getConditionManager(),
                        game.getPathManager(),
                        game.getCombatManager()
                );
                break;
            case PASSWAYS:
                obj = new Passway(gen.next(), bg);
                break;
            case MONSTERS:
                obj = new Monster(gen.next(), bg);
                ((Actor)obj).initialize(
                        game.getAnimationManager(),
                        game.getConditionManager(),
                        game.getPathManager(),
                        game.getCombatManager()
                );
                em.makeMonster(obj);
                break;
            case ITEMS:
                obj = new Item(gen.next(), bg);
        }
        addGameObject(obj);
        obj.setPosition(new Position(x, y, getId()));
        obj.setMap(this);
        obj.setScale(scale);
    }


    public List<HashMap<String, Object>> getCellProperties (int x, int y)throws IllegalAccessException{
        ArrayList<HashMap<String, Object>> toReturn = new ArrayList<>();

        Cell theCell = this.getCellGrid()[y][x];
        var bgPropertiesMap = getObjectProperties(theCell, 0);
        toReturn.add(bgPropertiesMap);

        GameObject go = this.getObject(x,y);
        var objectPropertiesMap = getObjectProperties(go,0);
        toReturn.add(objectPropertiesMap);

        return toReturn;
    }

    public void eraseCell(int x, int y){
        GameObject object = getObject(x, y);
        int id = object !=null ? object.getID() : -1;
        if (id!= -1)
            removeGameObject(id);
        else
            setCellBG(x,y,"");
    }

    public void setCellProperties(int x, int y, String propertyName, Object value) throws IllegalAccessException{
        Cell theCell = this.getCellGrid()[y][x];
        setObjectProperties(theCell, 0, propertyName, value);
        GameObject go = this.getObject(x,y);
        setObjectProperties(go, 0, propertyName, value);
    }

    public HashMap<String, Object> getObjectProperties(Object object, int depth) throws IllegalAccessException{
        HashMap<String, Object> toReturn = new HashMap<>();
        if(depth > 3 || object == null) return toReturn;
        ArrayList<Field> allFields = getAllFields(object, depth, 0);

        for(Field i:allFields){
            if (i.toGenericString().contains("Hash") || i.toGenericString().contains("static") || i.toGenericString().contains("final"))
                continue;
            i.setAccessible(true);
            Class c = i.getType();
            if(c.equals(Double.TYPE)||c.equals(Integer.TYPE)||c.equals(Boolean.TYPE))
            {
                toReturn.put(i.getName(),i.get(object));
            }
            else if(i.isAccessible()&&!c.isPrimitive()&&!c.getSimpleName().equals("String")){
                toReturn.putAll(getObjectProperties(i.get(object),depth+1));
            }
        }

        if(object instanceof Cell) {
            // Blacklist BG properties to display on frontend
            toReturn.remove("myRow");
            toReturn.remove("myColumn");
            toReturn.remove("capacityIncrement");
        } else if(object instanceof SimpleObject) {
            toReturn.remove("x");
            toReturn.remove("y");
            toReturn.remove("xStart");
            toReturn.remove("yStart");
            toReturn.remove("xEnd");
            toReturn.remove("yEnd");
            toReturn.remove("sizeFactor");
        }

        return toReturn;
    }


    public void setObjectProperties(Object object, int depth,String name, Object value) throws IllegalAccessException{
        System.out.println(value);
        System.out.println(name);
        if(depth > 3|| object == null) return;
        ArrayList<Field> allFields = getAllFields(object,depth,0);

        for(Field i:allFields){
            if (i.toGenericString().contains("Hash")|| i.toGenericString().contains("static") || i.toGenericString().contains("final"))
                continue;
            i.setAccessible(true);
            Class c = i.getType();
            if(i.getName().equals(name))
            {
                i.set(object, value);
                return;
            }
            else if(i.isAccessible()&&!c.isPrimitive()&&!c.getSimpleName().equals("String")){
                setObjectProperties(i.get(object),depth+1, name, value);
            }
        }
    }

    private ArrayList<Field> getAllFields(Object object,int depth, int start) {
        ArrayList<Field> allFields = new ArrayList<>();
        if (depth == start) {
            Field[] f = object.getClass().getSuperclass().getDeclaredFields();
            for (Field i : f) {
                allFields.add(i);
            }
        }
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field i : fields)
            allFields.add(i);
        return allFields;
    }


}
