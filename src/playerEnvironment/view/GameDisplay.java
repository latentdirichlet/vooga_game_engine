package playerEnvironment.view;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import gameEngine.ModelImplementations.Game;
import gameEngine.ModelImplementations.Item;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import playerEnvironment.controller.Coordinate;
import playerEnvironment.controller.GameInputManager;
import playerEnvironment.controller.Sprite;
import playerEnvironment.controller.TextBoxHelper;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import static playerEnvironment.view.MapGrid.CELL_SIZE;
import static playerEnvironment.view.StartDisplay.DATA_FILE_EXTENSION;

/**
 * @author Rohit Das (rvd5)
 * @author Peter Ciporin (pbc9)
 *
 * This class serves as the primary display rendered to the game window in the player environment
 * As a subclass of Display, this class has a reference to the Game object corresponding to the loaded game file
 */
public class GameDisplay extends Display {

    private static final String textboxImagePath = "/dialog.png";
    private static final String EXIT_TEXT = "Are you sure you want to exit?";
    private static final String SAVE_TEXT = "Are you sure you want to save?";
    private static final int MAP_LABEL_PADDING = 15;
    private static final int MAP_LABEL_SIZE = 150;

    private HashMap<String, Image> myInventoryImages = new HashMap<>();
    private HashMap<String, Image> cellSpriteMap = new HashMap<>();
    private StackPane rootPane;

    private Node openMenuItem = null;
    private StackPane currentRoot;

    // gameArea
    private MapGrid mapGrid;
    private Pane objectPane;
    private StackPane gameArea;

    // camera
    private Game myGame;
    private Camera myCamera;

    // input
    private GameInputManager gameInputManager;

    // textbox
    private ImageView textbox;
    private Pane textboxPane;
    private TextBoxHelper textBoxHelper;
    private boolean textBoxMode = false;

    // combat
    private CombatPane combatPane;
    private boolean combatMode = false;

    // menu
    private boolean menuVisible = false;
    private boolean mapOpen = false;
    private GameMenu myGameMenu;

    // stats
    private GameStats gameStats;

    // settings
    private GameSettings gameSettings;

    // inventory
    private InventoryDisplay myInventory;
    private boolean inventoryOpen = false;

    private double width;
    private double height;
    private boolean gameStopped;

    public GameDisplay(Parent root, double width, double height, Game game) {
        super(root, width, height);

        this.gameStopped = false;
        this.width = width;
        this.height = height;


        myGame = game;
        setupGameArea();
        setupCamera();
        setupRootPane();
        myInventory = new InventoryDisplay();
        gameStats = new GameStats(myGame.getRenderStats());
        gameSettings = new GameSettings();
        myInventoryImages = new HashMap<>();
        cellSpriteMap = new HashMap<>();
        updateCurrentRoot(rootPane);

    }

    // -------------------setups--------------------------

    /**
     * gameArea consists of the game map (grid of cells) along with the gameObjects rendered on it
     */
    private void setupGameArea(){
        gameArea = new StackPane();
        mapGrid = new MapGrid(myGame.getCellMap(), myGame.getScaleMap());
        objectPane = new Pane();
        objectPane.setPrefSize(mapGrid.getWidth(), mapGrid.getHeight());

        if( mapGrid.getGridWidth()*CELL_SIZE < width && mapGrid.getGridHeight()*CELL_SIZE < height ) {
            var widthProp = mapGrid.widthProperty().divide(2).multiply(-1).add(width/2);
            var heightProp = mapGrid.heightProperty().divide(2).multiply(-1).add(height/2);
            mapGrid.translateXProperty().bind(widthProp);
            mapGrid.translateYProperty().bind(heightProp);
            objectPane.translateXProperty().bind(widthProp);
            objectPane.translateYProperty().bind(heightProp);
        }

        gameArea.getChildren().add(0, mapGrid);
        gameArea.getChildren().add(1, objectPane);
    }

    /**
     * camera wraps gameArea and defines the section of the game map visible to the user at any given time (always centered on main game character)
     */
    private void setupCamera(){
        myCamera = new Camera();
        myCamera.setContent(gameArea);
        myCamera.setMyMapGrid(mapGrid);
        myCamera.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        myCamera.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    /**
     * render new game map
     */
    public void switchMap(){
        setupGameArea();
        setupCamera();
        setupRootPane();
        updateCurrentRoot(rootPane);
        renderSprites(myGame.getRenderMap());
    }

    /**
     * open/close main in-game menu
     */
    public void toggleGameMenu(){
        if(!menuVisible) {
            menuVisible = true;
            myGameMenu = new GameMenu(this);
            StackPane.setAlignment(myGameMenu, Pos.TOP_RIGHT);
            rootPane.getChildren().add(1, myGameMenu);
        } else {
            menuVisible = false;
            close();
            rootPane.getChildren().remove(myGameMenu);
        }
    }

    /**
     * initialize base StackPane with bottom-level camera
     */
    private void setupRootPane(){
        rootPane = new StackPane();
        rootPane.getChildren().add(0, myCamera);
    }

    //--------------------render----------------------

    /**
     * called at every iteration of the game loop
     */
    public void render() {
        if(combatMode){
            renderCombat(myGame.getCombatRenderMap());
            return;
        }
        if(inventoryOpen){
            //renderInventory(myGame.getInventoryMap());
            return;
        }
        renderSprites(myGame.getRenderMap());
    }

    /**
     *
     * @param itemMap hashmap containing item-quantity entries
     */
    private void renderInventory(Map<Item, Integer> itemMap) {
        myInventory.clear();
        for(Map.Entry<Item, Integer> entry : itemMap.entrySet()) {
            Item item = entry.getKey();
            String imagePath = item.getImagePath();
            Image itemImage;
            if (myInventoryImages.containsKey(imagePath)) {
                itemImage = myInventoryImages.get(imagePath);
            }
            else {
                itemImage = new Image(this.getClass().getResource(imagePath).toExternalForm(), CELL_SIZE, CELL_SIZE, true, false);
                myInventoryImages.put(imagePath, itemImage);
            }
            ItemDisplay inventoryItem = new ItemDisplay(item, itemImage, entry.getValue());
            inventoryItem.getItemButton().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    myGame.useItem(item.getID());
                    openInventory(true);
                }
            });
            myInventory.addItem(inventoryItem);
        }
    }

    /**
     *
     * @param spriteMap hashmap containing sprites for game objects and their positions
     */
    private void renderSprites(Map<Coordinate, Sprite> spriteMap) {
        objectPane.getChildren().clear();
        List<Map.Entry<Coordinate, Sprite>> tmp = sortRenderMap(spriteMap);


        for(Map.Entry<Coordinate, Sprite> entry : tmp) {
            ImageView sprite = getSpriteFromMap(entry);

            var width = CELL_SIZE  * entry.getValue().getSizeFactor();
            var height = CELL_SIZE * entry.getValue().getSizeFactor();

            sprite.setFitWidth(width);
            sprite.setFitHeight(height);
            sprite.setPreserveRatio(true);

            var container = new StackPane(sprite);
            container.setLayoutX(entry.getKey().getX() * CELL_SIZE);
            container.setLayoutY(entry.getKey().getY() * CELL_SIZE);
            container.setPrefSize(width, height);

            objectPane.getChildren().add(container);
        }
    }

    public Game getMyGame() {
        return this.myGame;
    }

    /**
     *
     * @param playerCor player position at given step of the game loop, used to focus camera on
     */
    public void centerCamera(Coordinate playerCor) {
        myCamera.centerPlayerInCamera(playerCor.getX(), playerCor.getY());
    }


    //--------------------textbox-------------------------

    /**
     * This public method is called from DisplayController to render a textbox to the screen
     * It makes use of a TextBoxHelper to utilize several text boxes for long messages
     *
     * @param message the text message to be rendered in the textbox
     */
    public void enterTextBoxMode(String message){
        textBoxMode = true;
        textBoxHelper = new TextBoxHelper();
        textBoxHelper.receiveText(message);
        createTextBox(textBoxHelper.nextText());
        myGame.pauseGame();
    }

    /**
     * Sets up the text box view
     */
    private void setupTextBox(){
        textbox = new ImageView(textboxImagePath);
        textbox.setFitWidth(this.getWidth());
        textbox.setPreserveRatio(true);
        textbox.setSmooth(false);

        textboxPane = new StackPane();
        textboxPane.getChildren().add(0, textbox);
        StackPane.setAlignment(textbox, Pos.BOTTOM_CENTER);
        textboxPane.setMaxHeight(textbox.getFitHeight());
        StackPane.setAlignment(textboxPane, Pos.BOTTOM_CENTER);
    }

    /**
     * Adds a chunk of text to a Label and renders it on the textbox
     * @param input the chunk of text to be displayed
     */
    private void setupText(String input){
        Label label = new Label(input);
        label.setFont(Font.font("Courier", FontWeight.NORMAL, 30));
        label.setStyle("-fx-font-family: 'monospaced';");
        label.setWrapText(true);
        textboxPane.getChildren().add(1, label);
        label.setPadding(new Insets(60, 75, 50, 75));
        StackPane.setAlignment(label, Pos.TOP_LEFT);
    }

    public boolean isTextBoxMode() {
        return textBoxMode;
    }

    /**
     * Called by GameInputManager when the appropriate key is pressed to render new textbox for the next chunk of text in a long message
     */
    public void nextTextBox(){
        if(textBoxHelper.hasNext()){
            createTextBox(textBoxHelper.nextText());
        }
        else{
            removeTextBox();
            textBoxMode = false;
            myGame.resumeGame();
        }
    }

    private void createTextBox(String input){
        removeTextBox();
        setupTextBox();
        setupText(input);
        rootPane.getChildren().add(textboxPane);
    }

    private void removeTextBox(){
        if(textboxPane!=null)
            rootPane.getChildren().remove(textboxPane);
    }

    // -------------------game menu--------------------------

    /**
     * Called from the GameMenu to open a view of the entire current map
     * Note that this view shows map cells only (not game objects)
     */
    public void openMap() {
        close();
        mapOpen = true;
        StackPane map = new StackPane();
        MapGrid grid = new MapGrid(myGame.getCellMap(), myGame.getScaleMap());

        StackPane labelPane = new StackPane();
        labelPane.setMaxSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);
        ImageView labelBg = new ImageView(textboxImagePath);
        labelBg.setFitHeight(MAP_LABEL_SIZE);
        labelBg.setFitWidth(MAP_LABEL_SIZE);
        labelBg.setPreserveRatio(true);
        labelBg.setSmooth(true);
        Label text = new Label("GAME MAP");
        text.setPadding(new Insets(MAP_LABEL_PADDING,0,MAP_LABEL_PADDING,0));
        labelPane.getChildren().addAll(labelBg, text);

        map.setAlignment(labelPane, Pos.TOP_CENTER);

        map.getChildren().addAll(grid, labelPane);
        updateCurrentRoot(map);
    }

    /**
     * This method closes whatever view is currently being displayed on top of the menu
     * Note that it does NOT close the menu, only whatever view had been previously launched from the menu
     */
    public void close() {
        if (mapOpen) {
            updateCurrentRoot(rootPane);
            mapOpen = false;
        }
        if (openMenuItem!=null) {
            currentRoot.getChildren().remove(openMenuItem);
        }
        inventoryOpen = false;
        openMenuItem = null;
    }

    /**
     * Called from the GameMenu to open a view of a player's inventory
     * @param shouldRerender a boolean that should be set to true when the inventory is already open but should be re-rendered anyway (i.e. when an item is used)
     */
    public void openInventory(boolean shouldRerender) {
        close();
        if (!inventoryOpen || shouldRerender) {
            openMenuItem = myInventory;
            inventoryOpen = true;
            renderInventory(myGame.getInventoryMap());
            currentRoot.getChildren().add(myInventory);
        }

    }

    /**
     * Called from the GameMenu to open a view of the player's game statistics (health, AP, etc.)
     */
    public void openGameStats() {
        close();
        openMenuItem = gameStats;
        gameStats.updateStats(myGame.getRenderStats());
        currentRoot.getChildren().add(gameStats);
    }

    /**
     * Called from the GameMenu to open a view from which the player can adjust various game settings (volume, brightness, etc.)
     */
    public void openGameSettings() {
        close();
        openMenuItem = gameSettings;
        currentRoot.getChildren().add(gameSettings);
    }

    public boolean isGamePaused() {
        return menuVisible;
    }

    /**
     * Called from the GameMenu when the user requests to exit the game.  Shows an alert dialog first to confirm this action was intentional.
     */
    public void exitGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, EXIT_TEXT, ButtonType.CANCEL, ButtonType.YES);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            gameStopped = true;
        }
    }

    /**
     * Called from the GameMenu when the user requests to save the game.  Shows an alert dialog first to confirm this action was intentional.
     */
    public void saveGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, SAVE_TEXT, ButtonType.CANCEL, ButtonType.YES);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {

        }
        saveCurrentGame(serializeGame());
    }

    /**
     * This method (and its two helpers below) is used to serialize the active Game object into an XML file for later re-loading
     */
    private boolean saveCurrentGame (String xstream)
    {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Please choose the directory to save ur game settings");
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

    public String serializeGame(){
        XStream mySerializer =  new XStream(new DomDriver());
        String xmlString = mySerializer.toXML(myGame);
        return xmlString;
    }

    private void createAndWrite(File selectedFile, String xstream) throws FileNotFoundException {
        PrintWriter cout= new PrintWriter(selectedFile);
        cout.println(xstream);
        cout.close();
    }

    public boolean isGameStopped() {
        return gameStopped;
    }


    //-------------------combat-------------------------

    /**
     * Sets the root of the GameDisplay to be a CombatPane
     * Called from DisplayControl when combat mode is triggered
     */
    public void initiateCombatDisplay(){
        setupCombatPane();
        combatMode = true;
        updateCurrentRoot(combatPane);
    }

    private void setupCombatPane(){
        combatPane = new CombatPane(this);
    }

    /**
     * Resets the root of the GameDisplay back to the gameArea
     * Called from DisplayControl when combat mode is exited
     */
    public void endCombat(){
        updateCurrentRoot(rootPane);
        combatMode = false;
    }

    public boolean isCombatMode() {
        return combatMode;
    }

    /**
     * Only during combat mode, this method is called instead of renderSprites in GameDisplay's render method
     * It renders the appropriate sprites to the combat pane, updating each character's health and exiting combat
     * automatically if either's health hits 0
     * @param spriteMap
     */
    public void renderCombat(Map<Coordinate, Sprite> spriteMap){
        combatPane.getPane().getChildren().clear();
        List<Map.Entry<Coordinate, Sprite>> renderList = sortCombatMap(spriteMap);
        for(Map.Entry<Coordinate, Sprite> entry : renderList) {
            ImageView sprite = getSpriteFromMap(entry);
            sprite.setX(entry.getKey().getX() * this.getWidth());
            sprite.setY(entry.getKey().getY() * this.getHeight());
            combatPane.getPane().getChildren().add(sprite);
        }
        if(myGame.getPlayerHealth()<=0 || myGame.getMonsterHealth()<=0) {
            endCombat();
        }
        combatPane.updateHealth(myGame.getPlayerHealth(), 150, myGame.getMonsterHealth(), 150);
    }

    /**
     * Used by renderSprites and renderCombat to render the appropriate Sprite for an animated character at a given frame
     *
     * @param entry a mapping of a Sprite object to its Coordinate on the map
     * @return an ImageView of the Sprite in question to be rendered to the player environment UI
     */
    private ImageView getSpriteFromMap(Map.Entry<Coordinate, Sprite> entry){
        Image image;
        if(cellSpriteMap.containsKey(entry.getValue().getImagePath())) {
            image = cellSpriteMap.get(entry.getValue().getImagePath());
        } else {
            image = new Image(this.getClass().getResource(entry.getValue().getImagePath()).toExternalForm(),
                    CELL_SIZE * entry.getValue().getSheetCols() * entry.getValue().getSizeFactor(),
                    CELL_SIZE * entry.getValue().getSheetRows() * entry.getValue().getSizeFactor(),
                    true, false);
            cellSpriteMap.put(entry.getValue().getImagePath(), image);
        }
        ImageView sprite = new ImageView(image);

        double startX = entry.getValue().getxStart() * image.getWidth();
        double width = entry.getValue().getxEnd() * image.getWidth() - startX;
        double startY = entry.getValue().getyStart() * image.getHeight();
        double height = entry.getValue().getyEnd() * image.getHeight() - startY;
        sprite.setViewport(new Rectangle2D(startX, startY, width, height));
        return sprite;
    }

    private void updateCurrentRoot(StackPane newRoot) {
        this.setRoot(newRoot);
        this.currentRoot = newRoot;
    }

    /**
     * The following 2 helper methods sort hashmaps to ensure that they are iterated in the same order every time
     * In doing so, they improve performance by preventing "flickering" issues that occurred when maps
     * were iterated through in a random order
     *
     * @param renderMap the map to be sorted
     * @return a sorted List generated from renderMap
     */

    private List<Map.Entry<Coordinate, Sprite>> sortCombatMap(Map<Coordinate, Sprite> renderMap){
        List<Map.Entry<Coordinate, Sprite>> res = new ArrayList<>();
        for(Map.Entry<Coordinate, Sprite> entry: renderMap.entrySet()){
            res.add(entry);
        }
        Collections.sort(res, new Comparator<Map.Entry<Coordinate, Sprite>>() {
            @Override
            public int compare(Map.Entry<Coordinate, Sprite> o1, Map.Entry<Coordinate, Sprite> o2) {
                return ((Integer)o1.getValue().getPriority()).compareTo(((Integer)o2.getValue().getPriority()));
            }
        });
        return res;
    }

    private List<Map.Entry<Coordinate, Sprite>> sortRenderMap(Map<Coordinate, Sprite> renderMap){
        List<Map.Entry<Coordinate, Sprite>> res = new ArrayList<>();
        for(Map.Entry<Coordinate, Sprite> entry: renderMap.entrySet()){
            res.add(entry);
        }
        Collections.sort(res, new Comparator<Map.Entry<Coordinate, Sprite>>() {
            @Override
            public int compare(Map.Entry<Coordinate, Sprite> o1, Map.Entry<Coordinate, Sprite> o2) {
                return ((Double)o1.getKey().getY()).compareTo((Double)o2.getKey().getY());
            }
        });
        return res;
    }

}
