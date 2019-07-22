package authoringEnvironment.frontend.Menu;

import authoringEnvironment.frontend.MainView;
import authoringEnvironment.frontend.Menu.Components.CharacterPane;
import authoringEnvironment.frontend.Menu.Components.Events.EventsPane;
import authoringEnvironment.frontend.Menu.Components.Events.NewEventsPane;
import authoringEnvironment.frontend.Menu.Components.MapPane;
import authoringEnvironment.frontend.Menu.Components.PalettePane;
import authoringEnvironment.frontend.Menu.Components.ToolBar.ToolBar;
import authoringEnvironment.frontend.Menu.Components.ToolBar.ToolBarPanes.EraserPane;
import authoringEnvironment.frontend.Menu.Components.ToolBar.ToolBarPanes.HandPane;
import authoringEnvironment.frontend.Menu.Components.ToolBar.ToolBarPanes.PaintPane;
import authoringEnvironment.frontend.Menu.Components.ToolBar.ToolBarPanes.SelectPane;
import authoringEnvironment.utils.EventPackage;
import javafx.geometry.Insets;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.HashMap;

/**
 * @author dc273
 */
public class MenuView implements EventsPane.EventsPaneListener {
    private MainView listener;
    private int width;
    private int height;
    private VBox leftRoot;
    private VBox rightRoot;

    private TitledPane activeToolPane;
    private Pane currentActivePane;

    private ToolBar tb;
    private HashMap<ToolBar.ToolBarButtons, Pane> toolPanes;
    private SelectPane select;
    private HandPane hand;
    private PaintPane paint;
    private EraserPane eraser;

    private PalettePane palettePane;
    private MapPane mapPane;
    private CharacterPane characterPane;

    private EventsPane eventsPane;
    private TitledPane newEventsPaneTP;
    private NewEventsPane newEventsPane;

    public MenuView(int width, MainView listener, Stage stage) {
        this.listener = listener;
        this.width = width;
        this.height = (int)stage.getHeight();

        initializeRight();
        initializeLeft();
    }

    /**
     * Initialize panes that go on the right menu pane
     */
    private void initializeRight() {
        this.rightRoot = new VBox(5);
        rightRoot.setStyle("-fx-background-color: #303030");

        activeToolPane = new TitledPane();
        activeToolPane.setPadding(new Insets(5, 5, 0, 5));

        eventsPane = new EventsPane(width, height, this);
        var eventsTP = new TitledPane();
        eventsTP.setContent(eventsPane.getRoot());
        eventsTP.setText("Events");
        eventsTP.setPadding(new Insets(0, 5, 0, 5));

        populateToolPanes(listener);

        rightRoot.getChildren().addAll(
                new Rectangle(width, 1, Color.TRANSPARENT),
                activeToolPane,
                eventsTP
        );

        rightRoot.setPrefWidth(width);
    }

    /**
     * Initialize left menu panes
     */
    private void initializeLeft() {
        leftRoot = new VBox(10);
        leftRoot.setStyle("-fx-background-color: #303030");

        tb = new ToolBar(listener);
        palettePane = new PalettePane(width, height, listener);
        mapPane = new MapPane(width, height, listener);
        characterPane = new CharacterPane(listener);

        var paletteTP = new TitledPane();
        paletteTP.setText("Palette");
        paletteTP.setContent(palettePane.getRoot());
        paletteTP.setPadding(new Insets(0, 5, 0, 5));

        var mapTP = new TitledPane();
        mapTP.setText("Maps");
        mapTP.setContent(mapPane.getRoot());
        mapTP.setPadding(new Insets(0, 5, 0, 5));

        var characterTP = new TitledPane();
        characterTP.setText("Character");
        characterTP.setContent(characterPane.getRoot());
        characterTP.setPadding(new Insets(0, 5, 0, 5));

        leftRoot.getChildren().addAll(
                new Rectangle(width, 1, Color.TRANSPARENT),
                tb.getRoot(),
                paletteTP,
                mapTP,
                characterTP
        );

        leftRoot.setPrefWidth(width);
    }

    /**
     * Construct tool panes and put in map
     * @param listener
     */
    private void populateToolPanes(MainView listener) {
        toolPanes = new HashMap<>();

        select = new SelectPane(listener);
        toolPanes.put(ToolBar.ToolBarButtons.SELECT, select.getRoot());

        hand = new HandPane();
        toolPanes.put(ToolBar.ToolBarButtons.HAND, hand.getRoot());

        paint = new PaintPane(listener);
        toolPanes.put(ToolBar.ToolBarButtons.PAINT, paint.getRoot());

        eraser = new EraserPane();
        toolPanes.put(ToolBar.ToolBarButtons.ERASER, eraser.getRoot());
    }

    /**
     * Set the active tool
     * @param type
     */
    public void setActiveTool(ToolBar.ToolBarButtons type) {
        activeToolPane.setText("Active Tool: " + type.getName());
        currentActivePane = toolPanes.get(type);
        activeToolPane.setContent(currentActivePane);
    }

    /**
     * New Event pane
     */
    @Override
    public void openNewEvent() {
        newEventsPaneTP = new TitledPane();
        newEventsPane = new NewEventsPane(eventsPane);
        newEventsPaneTP.setContent(newEventsPane.getRoot());
        newEventsPaneTP.setText("New Event");
        newEventsPaneTP.setPadding(new Insets(0, 5, 0, 5));

        rightRoot.getChildren().add(newEventsPaneTP);

        eventsPane.disableButton(true);
    }

    /**
     * Dismiss new event pane
     */
    @Override
    public void closeNewEvent() {
        rightRoot.getChildren().remove(newEventsPaneTP);
        eventsPane.disableButton(false);
    }

    /**
     * Event is being edited
     * @param event
     */
    @Override
    public void editEvent(EventPackage event) {
        closeNewEvent();

        newEventsPaneTP = new TitledPane();
        newEventsPane = new NewEventsPane(eventsPane, event);
        newEventsPaneTP.setContent(newEventsPane.getRoot());
        newEventsPaneTP.setText("Edit Event");
        newEventsPaneTP.setPadding(new Insets(0, 5, 0, 5));

        rightRoot.getChildren().add(newEventsPaneTP);

        eventsPane.disableButton(true);
    }

    public NewEventsPane getNewEventsPane() { return newEventsPane; }

    public PaintPane getPaint() {
        return paint;
    }

    public ToolBar getToolBar() {
        return tb;
    }

    public PalettePane getPalettePane() {
        return palettePane;
    }

    public SelectPane getSelectPane() {
        return select;
    }

    public MapPane getMapPane() {
        return mapPane;
    }

    public VBox getRightRoot() {
        return rightRoot;
    }

    public VBox getLeftRoot() {
        return leftRoot;
    }

    public EventsPane getEventsPane() {
        return eventsPane;
    }

    public CharacterPane getCharacterPane() {
        return characterPane;
    }
}
