package authoringEnvironment.backend;

import authoringEnvironment.backend.gameBuilder.GameCharacterBuilder;
import authoringEnvironment.backend.gameBuilder.GameBuilder;
import authoringEnvironment.backend.gameBuilder.GameMapBuilder;
import authoringEnvironment.backend.gameBuilder.GameWorldBuilder;
import authoringEnvironment.backend.gameDynamics.EventMaker;
import authoringEnvironment.utils.AuthoringEventListener;
import authoringEnvironment.utils.ConditionPackage;
import authoringEnvironment.frontend.Menu.Components.PalettePane;
import authoringEnvironment.utils.EventPackage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import gameEngine.ModelImplementations.*;
import gameEngine.ModelImplementations.Events.Event;
import gameEngine.ModelImplementations.IDGen;
import gameEngine.ModelInterfaces.ConditionInterface;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.HashMap;
import java.util.List;


/**
 * @author dc273 sz165
 */

public class MainModel implements AuthoringEventListener {
    public static final String DATA_FILE_EXTENSION = "*.xml";
    public static final String CONDITION_RESOURCE = "ConditionResources.ConditionResource";

    private Game game;
    private GameCharacterBuilder character;
    private GameMapBuilder currentMap;
    private GameWorldBuilder gameWorld;
    private IDGen gen = new IDGen();

    private EventMaker em;

    public MainModel(){
        character = new GameCharacterBuilder(gen.next(), GameCharacterBuilder.DEFAULT_IMG);

        gameWorld = new GameWorldBuilder();
        game = new GameBuilder(character);
        ((GameBuilder) game).setWorld(gameWorld);

        character.initialize(
                game.getAnimationManager(),
                game.getConditionManager(),
                game.getPathManager(),
                game.getCombatManager()
        );
        character.setGame(game);

        em = new EventMaker(CONDITION_RESOURCE, game.getConditionManager(),this);
    }

    public GameBuilder getGame(){
        return (GameBuilder)game;
    }


    public void updateCurrentMap(){
        game.updateCurrentMap(currentMap);
    }

    @Override
    public void setCellBG(int mapIndex, int x, int y, String bg, double scale) {
        gameWorld.getMapAt(mapIndex).setCellBG(x,y,bg,scale);
    }

    @Override
    public void setCellObject(int mapIndex, int x, int y, String bg, PalettePane.Type type, double scale) {

        gameWorld.getMapAt(mapIndex).setCellObject(x,y, bg, type, game, scale, em);
    }

    @Override
    public List<HashMap<String, Object>> getCellProperties(int mapIndex, int x, int y) {
        try{return gameWorld.getMapAt(mapIndex).getCellProperties(x,y);}
        catch (IllegalAccessException e){
            System.out.println(e.getStackTrace());
        }
        return null;
    }

    @Override
    public HashMap<String, Object> getPlaceholderProperties(Object obj) {
        if(currentMap==null) return new HashMap<>();
        
        try{return currentMap.getObjectProperties(obj,0);}
        catch (IllegalAccessException e){
            System.out.println(e.getStackTrace());
        }
        return null;
    }


    @Override
    public void newMap(int width, int height) {
        GameMapBuilder gmb = new GameMapBuilder(width, height, gameWorld.getNumMaps());
        gmb.setGen(gen);
        gameWorld.addMap(gmb);
    }

    @Override
    public void deleteMap(int index) {
        gameWorld.deleteMap(index);
    }



    @Override
    public void newEvent(EventPackage event) throws IllegalArgumentException{


        List<ConditionInterface> conditions = em.createConditions(event.left, event.right, event.conditionTypes);
        List<Object> objs = em.createActionObjects(event.objs, event.actionTypes, event.arguments);
        Event eve = new Event(conditions, objs, event.actionTypes, event.arguments);

        game.addEvent(eve);

    }

    @Override
    public void setCharacterProperty(String propertyName, Object value) {
        try{
            currentMap.setObjectProperties(game.getGameCharacter(), 4, propertyName, value);
        }
        catch (IllegalAccessException e){
            System.out.println(e.getStackTrace());
        }

    }

    @Override
    public void setCharacterPosition(int mapIndex, int x, int y) {
        currentMap = gameWorld.getMapAt(mapIndex);
        gameWorld.setStartingMap(currentMap);
        gameWorld.setCurrentMap(currentMap);
        game.setCurrentMap(currentMap);

        game.getGameCharacter().setPosition(new Position(x, y, mapIndex));
    }

    @Override
    public void createMapLink(ConditionPackage from, ConditionPackage to) {
        em.createLinkEvent(from, to);
        em.createLinkEvent(to, from);
    }

    @Override
    public void runGame() {
        currentMap = gameWorld.getMapAt(0);
        currentMap.addGameObject(character);
        updateCurrentMap();
        String gameObj = serializeGame();

        try{
            File file = new File("./temp/game.xml");
            createAndWrite(file, gameObj);;
        }
        catch (FileNotFoundException e){
            return;
        }

        // Run a java app in a separate system process
        try {
            Process proc = Runtime.getRuntime().exec(
                    "java -jar ./out/artifacts/Player_jar/voogasalad_voogalicious.jar ./temp/game.xml"
            );
            // Then retreive the process output
            InputStream in = proc.getInputStream();
            InputStream err = proc.getErrorStream();
        } catch(IOException e) {
            e.printStackTrace();
        }

        game.getEvents().clear();
        game.getConditionManager().clearConditionsMap();
        em.clearConditions();
    }


    @Override
    public void setCellProperty(int mapIndex, int x, int y, String propertyName, Object value) {
        try{gameWorld.getMapAt(mapIndex).setCellProperties(x,y,propertyName,value);}
        catch (IllegalAccessException e){
            System.out.println(e.getStackTrace());
        }
    }

    @Override
    public void eraseCell(int mapIndex, int x, int y) {
        gameWorld.getMapAt(mapIndex).eraseCell(x,y);
    }

    @Override
    public boolean saveGame() {
        currentMap = gameWorld.getMapAt(0);
        currentMap.addGameObject(character);
        updateCurrentMap();
        String gameObj = serializeGame();
        saveGame(gameObj);
        return false;
    }

    public String serializeGame(){
        XStream mySerializer =  new XStream(new DomDriver());
        String xmlString = mySerializer.toXML(game);
        return xmlString;
    }

    public boolean saveGame (String xstream)
    {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Please choose the directory to save game");
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("text file ",DATA_FILE_EXTENSION));
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File selectedFile = null;
        while (selectedFile == null){
            selectedFile = chooser.showSaveDialog(null);
        }
        try{
            createAndWrite(selectedFile, xstream);
        }
        catch (FileNotFoundException e){
            return false;
        }
        return true;
    }

    private void createAndWrite(File selectedFile, String xstream) throws FileNotFoundException {
        PrintWriter cout= new PrintWriter(selectedFile);
        cout.println(xstream);
        cout.close();
    }

    public GameWorldBuilder getWorld(){
        return gameWorld;
    }

    @Override
    public boolean loadGame(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Please choose a game file to load");
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("text file ",DATA_FILE_EXTENSION));
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        Stage fileStage = new Stage();
        File selectedFile = chooser.showOpenDialog(fileStage);
        if (selectedFile != null) {

            return true;
        }
        return false;
    }

    public GameCharacterBuilder getCharacter() {
        return character;
    }
}
