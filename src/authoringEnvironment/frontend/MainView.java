package authoringEnvironment.frontend;

import authoringEnvironment.frontend.Grid.Components.BlankStackPane;
import authoringEnvironment.frontend.Grid.Components.Map;
import authoringEnvironment.frontend.Grid.GridView;
import authoringEnvironment.frontend.Menu.Components.CharacterPane;
import authoringEnvironment.frontend.Menu.Components.MapPane;
import authoringEnvironment.frontend.Menu.Components.PalettePane;
import authoringEnvironment.frontend.Menu.Components.ToolBar.ToolBarPanes.SelectPane;
import authoringEnvironment.frontend.Menu.Components.ToolBar.ToolBarPanes.PaintPane;
import authoringEnvironment.frontend.Menu.Components.ToolBar.ToolBar;
import authoringEnvironment.utils.AuthoringEventListener;
import authoringEnvironment.frontend.Menu.MenuView;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author dc273
 */
public class MainView implements ToolBar.ToolBarListener, PalettePane.BlocksPaletteListener, Map.MapListener,
        PaintPane.PaintListener, SelectPane.SelectListener, MapPane.MapPaneListener,
        CharacterPane.CharacterPaneListener, GridView.GridViewListener {

    private static final int MENU_WIDTH = 280;

    private Node root;
    private MenuBar bar;
    private GridView grid;
    private MenuView menu;

    private String selectedBlock;

    private AuthoringEventListener listener;

    private PalettePane.Type type;
    private ToolBar.ToolBarButtons currentTool;
    private double scale = 1.0;
    private ToolBar.ToolBarButtons last;
    private boolean firstMap = true;
    private Boolean isProperty;
    private Integer index;


    public MainView(AuthoringEventListener mainModel, Stage stage) {
        this.listener = mainModel;
        this.root = new VBox();
        this.type = PalettePane.Type.BLOCKS;

        initializeMenuBar();

        grid = new GridView(this);
        menu = new MenuView(MENU_WIDTH, this, stage);

        var leftScrollPane = insertIntoScrollPane(menu.getLeftRoot());
        var rightScrollPane = insertIntoScrollPane(menu.getRightRoot());

        var content = new HBox();
        content.getChildren().addAll(leftScrollPane, grid.getRoot(), rightScrollPane);

        toolSelected(ToolBar.ToolBarButtons.SELECT);
        menu.getMapPane().newMap();

        ((VBox)root).getChildren().addAll(bar, content);
        ((VBox) root).prefWidthProperty().bind(stage.widthProperty());
        ((VBox) root).prefHeightProperty().bind(stage.heightProperty());
    }

    /**
     * Wraps node in scrollpane
     * @param node
     * @return
     */
    private ScrollPane insertIntoScrollPane(Node node) {
        var scrollPane = new ScrollPane(node);
        scrollPane.setMinWidth(MENU_WIDTH + 2);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: #303030");

        return scrollPane;
    }

    /**
     * Initializes menu bar
     */
    private void initializeMenuBar() {
        bar = new MenuBar();
        bar.setStyle("-fx-background-color: #202124; ");

        Menu menuFile = new Menu("File");
        MenuItem save = new MenuItem("Save");
        save.setOnAction(event -> saveGame());
        menuFile.getItems().add(save);

        MenuItem load = new MenuItem("Load");
        load.setOnAction(event -> loadGame());
        menuFile.getItems().add(load);

        Menu menuRun = new Menu("Run");
        MenuItem runGame = new MenuItem("Run Game");
        runGame.setOnAction(event -> runGame());
        menuRun.getItems().add(runGame);

        bar.getMenus().addAll(menuFile, menuRun);
    }

    /**
     * Runs game by populating events and calling listener
     */
    private void runGame() {
        populateEvents();
        listener.runGame();
    }

    /**
     * Prompts user for dialog and serializes game
     */
    private void saveGame() {
        populateEvents();
        listener.saveGame();
    }

    /**
     * Populates events by getting parameters from appropriate location
     */
    private void populateEvents() {
        grid.getLinks().forEach(e -> listener.createMapLink(e.get(0), e.get(1)));
        menu.getEventsPane().getEvents().forEach(e -> {
            if(e!=null) listener.newEvent(e);
        });
    }

    /**
     * Load's game (doesn't work)
     */
    private void loadGame() {
        listener.loadGame();
        //TODO: root, grid
    }

    /**
     * Listener for when a tool is selected
     * Any actions for when a tool is selected should be coded here
     * @param value
     */
    @Override
    public void toolSelected(ToolBar.ToolBarButtons value) {
        this.last = currentTool;
        this.currentTool = value;
        this.menu.setActiveTool(currentTool);
        this.grid.getSceneGestures().setPannable(false);
        this.grid.getSceneGestures().setZoomable(false);
        this.grid.deselectCells();
        this.menu.getPalettePane().clearBlockSelection();

        switch (value) {
            case SELECT:
                menu.getPalettePane().clearBlockSelection();
                grid.selectLastSelected();
                break;
            case HAND:
                grid.getSceneGestures().setPannable(true);
                break;
            case PAINT:
                menu.getPalettePane().selectLastBlock();
                break;
            case ZOOM:
                grid.getSceneGestures().setZoomable(true);
                break;
        }
    }

    /**
     * Block selected
     * @param name of file
     */
    @Override
    public void blockSelected(String name) {
        this.selectedBlock = name;
        if(menu!=null) menu.getToolBar().selectTool(ToolBar.ToolBarButtons.PAINT);
    }

    /**
     * Block type selected
     * @param type
     */
    @Override
    public void blockTypeSelected(PalettePane.Type type) {
        this.type = type;
        menu.getPaint().paletteTypeSet(type);
    }

    /**
     * Cell is clicked; behavior changes depending on tool selected
     * @param mapIndex
     * @param x
     * @param y
     */
    @Override
    public void cellClicked(int mapIndex, int x, int y) {
        if(currentTool== ToolBar.ToolBarButtons.SELECT) {
            var properties = listener.getCellProperties(mapIndex, x, y);
            grid.selectCellObject(mapIndex, x, y, true);
            menu.getSelectPane().setSelectedCell(mapIndex, x, y, properties);
        }
        else if(currentTool==ToolBar.ToolBarButtons.PAINT) {
            switch (type) {
                case BLOCKS:
                    grid.setCellBackground(mapIndex, x, y, selectedBlock, scale);
                    listener.setCellBG(mapIndex, x, y, selectedBlock, scale);
                    break;
                default:
                    grid.setCellObject(mapIndex, x, y, selectedBlock, scale);
                    listener.setCellObject(mapIndex, x, y, selectedBlock, type, scale);
                    break;
            }

            menu.getPaint().getProperties().entrySet().forEach(e -> listener.setCellProperty(mapIndex, x, y, e.getKey(), e.getValue()));
        } else if(currentTool== ToolBar.ToolBarButtons.ERASER) {
            grid.eraseCell(mapIndex, x, y);
            listener.eraseCell(mapIndex, x, y);
        }
    }

    /**
     * Object scale is set
     * @param x
     */
    @Override
    public void scaleSet(double x) {
        this.scale = x;
    }

    /**
     * Object(x, y, map) parameter is set to value
     * @param mapIndex
     * @param x
     * @param y
     * @param parameter
     * @param value
     */
    @Override
    public void parameterSet(int mapIndex, int x, int y, String parameter, Object value) {
        listener.setCellProperty(mapIndex, x, y, parameter, value);
    }

    /**
     * Key pressed, switch to corresponding tool/behavior
     * @param code
     */
    public void keyPressed(KeyCode code) {
        for(var tool: ToolBar.ToolBarButtons.values()) {
            if(code.getChar().equals(tool.getShortcut()))
                menu.getToolBar().selectTool(tool);
        }

        if(code.equals(KeyCode.BACK_SPACE)) grid.deletePressed();

        if(code.equals(KeyCode.CONTROL)) menu.getToolBar().selectTool(ToolBar.ToolBarButtons.ZOOM);
    }

    /**
     * Key released, switch off toggled behavior
     * @param code
     */
    public void keyReleased(KeyCode code) {
        if(code.equals(KeyCode.CONTROL)) menu.getToolBar().selectTool(last);
    }

    /**
     * New map listener
     * @param width
     * @param height
     * @param defaultBG
     */
    @Override
    public void newMap(int width, int height, String defaultBG) {
        listener.newMap(width, height);
        grid.addMap(width, height, this, defaultBG);

        if(firstMap) {
            menu.getCharacterPane().update();
            menu.getPaint().update();
            firstMap = false;
        }
    }

    /**
     * Delete map in backend and frontend
     * @param index
     */
    @Override
    public void deleteMap(int index) {
        listener.deleteMap(index);
        grid.deleteMap(index);
    }

    /**
     * Focus camera on map
     * @param index
     */
    @Override
    public void focusMap(int index) {
        grid.focusCameraOn(index);
    }

    /**
     * Right click drag started, highlight component
     * @param node
     */
    public void pickWhipStarted(Node node) {
        if(node.getProperties()==null) return;

        var type = node.getProperties().getOrDefault("type", null);

        if(type==null) return;

        if(type.equals("cell")) {
            if(node.getProperties()==null || !node.getProperties().containsKey("map")) return;

            var mapIndex = (int) node.getProperties().get("map");
            var x = (int) node.getProperties().get("x");
            var y = (int) node.getProperties().get("y");

            grid.selectCellObject(mapIndex, x, y, true);
            var properties = listener.getCellProperties(mapIndex, x, y);
            menu.getSelectPane().setSelectedCell(mapIndex, x, y, properties);
        } else if(type.equals("property")) {
            var properties = (boolean)node.getProperties().get("properties");
            var index = (int)node.getProperties().get("index");
            menu.getSelectPane().highlight(properties, index, true);
            this.isProperty = properties;
            this.index = index;
        }

    }

    private Integer lastCOH;
    private Integer lastCI;

    private Integer lastEndX;
    private Integer lastEndY;
    private Integer lastEndMap;

    /**
     * Right click drag in progress
     * @param start
     * @param node
     */
    public void pickWhipDown(Node start, Node node) {
        if(node==null) return;

        if(node.getProperties()==null) return;

        if(start instanceof BlankStackPane && node instanceof BlankStackPane) {
            var startMap = (int)start.getProperties().get("map");
            var endMap = (int)node.getProperties().get("map");
            var endX = (int)node.getProperties().get("x");
            var endY = (int)node.getProperties().get("y");

            if(startMap!=endMap) {
                grid.selectCellObject(endMap, endX, endY, true);

                if(lastEndX!=null && (lastEndX!=endX || lastEndY!=endY)) {
                    grid.deselectCells(lastEndMap, lastEndX, lastEndY);
                    lastEndX = null;
                    lastEndY = null;
                    lastEndMap = null;
                }

                lastEndX = endX;
                lastEndY = endY;
                lastEndMap = endMap;
            }

            return;
        }

        if(lastEndX!=null) {
            grid.deselectCells(lastEndMap, lastEndX, lastEndY);
            lastEndX = null;
            lastEndY = null;
            lastEndMap = null;
        }

        if(node.getProperties().containsKey("conditionHolder")) {
            var conditionIndex = (int)node.getProperties().get("conditionIndex");
            var holderIndex = (int)node.getProperties().get("conditionHolder");

            highlightConditionObjectHolder(conditionIndex, holderIndex, true);
            lastCOH = holderIndex;
            lastCI = conditionIndex;

        } else if(lastCOH!=null) deHighlightCOH();
    }

    /**
     * Right click drag completed
     * @param start
     * @param end
     */
    public void pickWhipCompleted(Node start, Node end) {
        if(start==null) return;
        if(end.getProperties() == null || start.getProperties() == null) return;

        if(start instanceof StackPane && end instanceof StackPane) {
            var startMap = (int)start.getProperties().get("map");
            var endMap = (int)end.getProperties().get("map");

            if(startMap!=endMap)
                grid.addMapLink(start, end);

            return;
        }

        if(lastEndX!=null) {
            grid.deselectCells(lastEndMap, lastEndX, lastEndY);
        }

        if(end.getProperties().containsKey("conditionHolder")) {
            var conditionIndex = (int) end.getProperties().get("conditionIndex");
            var holderIndex = (int) end.getProperties().get("conditionHolder");
            menu.getNewEventsPane().addConditionObject(conditionIndex, holderIndex, start);
        }

        if(isProperty!=null && index!=null) {
            menu.getSelectPane().highlight(isProperty, index, false);
            isProperty = null;
            index = null;
        }

        if(lastCOH!=null) deHighlightCOH();
    }

    /**
     * Dehighlight last Condition object holder
     */
    private void deHighlightCOH() {
        highlightConditionObjectHolder(lastCI, lastCOH, false);
        lastCOH = null;
        lastCI = null;
    }

    /**
     * Highlight condition object holder
     * @param conditionIndex
     * @param holderIndex
     * @param onOff
     */
    private void highlightConditionObjectHolder(int conditionIndex, int holderIndex, boolean onOff) {
        menu.getNewEventsPane().highlightConditionObjectHolder(conditionIndex, holderIndex, onOff);
    }

    /**
     * Listener for character parameter set
     * @param name
     * @param value
     */
    @Override
    public void characterParameterSet(String name, Object value) {
        listener.setCharacterProperty(name, value);
    }

    /**
     * Listener for character position set
     * @param map
     * @param x
     * @param y
     */
    @Override
    public void characterPositionSet(int map, int x, int y) {
        listener.setCharacterPosition(map, x, y);
    }

    /**
     * get root
     * @return
     */
    public Node getRoot() {
        return root;
    }

    /**
     * get listener
     * @return
     */
    public AuthoringEventListener getListener() {
        return listener;
    }
}
