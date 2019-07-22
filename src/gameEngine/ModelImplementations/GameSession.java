package gameEngine.ModelImplementations;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import gameEngine.ModelImplementations.Events.ConditionManager;
import javafx.scene.input.KeyCode;
import playerEnvironment.controller.AnimationManager;
import playerEnvironment.controller.Coordinate;
import playerEnvironment.controller.DisplayControl;
import playerEnvironment.controller.Sprite;

import java.io.*;
import java.util.Map;

public class GameSession {

    private Game game;

    public GameSession(File gameFile){
        this.game = readGameFile(gameFile);
    }

    public GameSession(){
        this.game = new Game();
    }

    public Game getGame(){
        return game;
    }

//    /**
//     * Handle key pressed by the user
//     * @param code
//     */
//    public void handleKeyPressed(KeyCode code){
//        this.game.handleKeyPressed(code);
//    }
//
    private Game readGameFile(File gameFile){
        try {
            FileReader fr = new FileReader(gameFile);
            StringBuilder sb = new StringBuilder();
            int i;
            while((i=fr.read())!=-1)
                sb.append((char)i);


            //System.out.println(sb);
            XStream deserializer = new XStream(new DomDriver());
            Game theGame = (Game)deserializer.fromXML(sb.toString());
            return (theGame);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
//
//    public Map<Position, Sprite> getSpritesMap(){
//        return this.game.getSpritesMap();
//    }
//
//    public Map<Coordinate, Sprite> getRenderMap(){
//        return this.game.getRenderMap();
//    }
//
//    public Map<Position, String> getObjectsMap(){
////        System.out.println(this.game.getCurrentMap().getObjectPositions());
//        return this.game.getCurrentMap().getObjectPositions();
//    }
//
//    public String[][] getCellMap(){
//        return this.game.getCurrentMap().getGridImage();
//    }
//
//    public Position getMCPosition(){
//        return this.game.getGameCharacter().getPosition();
//    }
//
//    public Coordinate getMCCoordinate(){
//        return this.game.getMCCoordinate();
//    }
//
//    public Map<Item, Integer> getInventoryMap(){
//        return this.game.getGameCharacter().getItemMap();
//    }
//
//    public void useItem(int id){
//        this.game.getGameCharacter().useItem(id);
//        System.out.println(game.getGameCharacter().getHealth());
//    }
//
//    public double getHealth(){
//        return this.game.getGameCharacter().getHealth();
//    }
//
//    public void update(){
//        this.game.update();
//    }
//
//    public static void main(String[] args){
//        GameSession gs = new GameSession();
//        System.out.println(gs.getRenderMap());
//        gs.handleKeyPressed(KeyCode.W);
//        for(int i = 0; i < 35; i++){
//            System.out.println(gs.getRenderMap());
//        }
//        gs.handleKeyPressed(KeyCode.W);
//        for(int i = 0; i < 35; i++){
//            System.out.println(gs.getRenderMap());
//        }
//    }
//
//    public void setDisplayControl(DisplayControl control) {
//        this.control = control;
//    }
}
