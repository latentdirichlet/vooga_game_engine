package playerEnvironment.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import playerEnvironment.controller.GameInputManager;
import playerEnvironment.controller.UserInputManager;
import javafx.application.Application;

import java.io.File;
import java.util.List;

/**
 * @author Peter Ciporin (pbc9)
 * @author Rohit Das (rvd5)
 *
 * This class is the starting point of the playerEnvironment, holding the logic that runs the actual game loop
 * Initially it renders a StartDisplay, along with the toolbar used to load in a file
 * The step method is arguably the most important, being called at each frame of the game loop
 */
public class MainGame extends Application {

    public static final int PLAYER_SCREEN_WIDTH = 800;
    public static final int PLAYER_SCREEN_HEIGHT = 600;
    public static final int FRAMES_PER_SECOND = 30;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    String fileSeparator = System.getProperty("file.separator");
    public final String CSS = getClass().getResource("/playerEnv.css").toExternalForm();

    private UserInputManager myUserInputManager;
    private Stage playerStage;
    private StartDisplay myStartDisplay;
    private GameDisplay myGameDisplay;
    private Display myCurrentDisplay;
    private BorderPane borderPane;
    private Timeline myAnimation;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage myStage) {

        this.playerStage = myStage;

        borderPane = new BorderPane();
        this.myStartDisplay = new StartDisplay(this, borderPane, PLAYER_SCREEN_WIDTH, PLAYER_SCREEN_HEIGHT);
        myCurrentDisplay = myStartDisplay;
        myCurrentDisplay.getStylesheets().add(CSS);

        this.myUserInputManager = new UserInputManager(myCurrentDisplay);

        borderPane.prefHeightProperty().bind(myCurrentDisplay.heightProperty());
        borderPane.prefWidthProperty().bind(myCurrentDisplay.widthProperty());

        createMenus(borderPane);

        myStage.setTitle("VOOGALICIOUS");
        myStage.setScene(this.myCurrentDisplay);
        myStage.show();

        List<String> parameters;
        parameters = getParameters().getRaw();
        if(parameters.size()>0)
            myStartDisplay.startGame(new File(parameters.get(0)));
    }

    /**
     * Creates the toolbar rendered on top of the StartDisplay
     *
     * @param borderPane the BorderPane whose top section should be populated with the newly created toolbar
     */
    private void createMenus(BorderPane borderPane) {

        MenuBar menuBar = new MenuBar();

        // --- Menu File
        Menu menuFile = new Menu("File");

        MenuItem load = new MenuItem("Load Game File");
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myStartDisplay.handleFileChooser();
            }
        });
        menuFile.getItems().addAll(load);

        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");

        // --- Menu View
        Menu menuView = new Menu("View");

        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);

        borderPane.setTop(menuBar);
    }

    GameInputManager gameInputManager;

    /**
     * StartDisplay calls this method to begin the game loop as soon as the start button is pressed
     * It is here that the animation is both defined and started, the GameDisplay is loaded to the application,
     * and the gameInputManager is instructed to begin listening to user input
     */
    public void startGameLoop() {
        this.myGameDisplay = myStartDisplay.getLoadedGameDisplay();
        myCurrentDisplay = myGameDisplay;
        myCurrentDisplay.getStylesheets().add(CSS);
        playerStage.setScene(myCurrentDisplay);
        myGameDisplay.render();

        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.getKeyFrames().add(frame);
        myAnimation.play();

        gameInputManager = new GameInputManager(myGameDisplay, myGameDisplay.getMyGame());
        myCurrentDisplay.setOnKeyPressed(e -> gameInputManager.handleKeyPressed(e.getCode()));
        myCurrentDisplay.setOnKeyReleased(e -> gameInputManager.handleKeyReleased(e.getCode()));
    }

    /**
     * Called at each frame of the game loop
     * Makes calls to GameDisplay to render the updated data and center the camera on the player's current position
     * Also calls upon the gameInputManager to process any active keys
     *
     * @param elapsedTime the amount of time that has passed since the last frame (not currently used)
     */
    public void step(double elapsedTime) {
        //myGameDisplay.render();
        //System.out.println("Iteration");
        if (myGameDisplay.isGameStopped()) {
            reloadStartDisplay();
        }
        myGameDisplay.render();
        myGameDisplay.centerCamera(myGameDisplay.getMyGame().getMCCoordinate());
        gameInputManager.processActiveKeys();
    }

    private void reloadStartDisplay() {
        myAnimation.stop();
        start(playerStage);
    }

}
