package playerEnvironment.view;

import gameEngine.ModelImplementations.Game;
import gameEngine.ModelImplementations.GameSession;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import playerEnvironment.controller.DisplayControl;

import java.io.File;

import static playerEnvironment.view.MainGame.PLAYER_SCREEN_HEIGHT;
import static playerEnvironment.view.MainGame.PLAYER_SCREEN_WIDTH;

/**
 * @author Peter Ciporin (pbc9)
 * @author Rohit Das (rvd5)
 *
 * This subclass of Display renders a splashscreen from which a user can load a game file
 * Once a valid file has been loaded, makeStartBtn renders a Start Button to the screen
 * Once clicked, the file is passed to GameSession in the backend and deserialized into a Game object (from gameEngine.ModelImplementations)
 * Finally, a GameDisplay instance is created (later accessed by MainGame), and the main game loop is started inside MainGame
 */
public class StartDisplay extends Display {
    public static final String DATA_FILE_EXTENSION = "*.xml";
    public final String CSS = getClass().getResource("/playerEnv.css").toExternalForm();

    private FileChooser myChooser;
    private boolean startOnDisplay=false;
    private File selectedFile;
    private VBox box;
    private GameDisplay loadedGameDisplay;
    private MainGame mainGame;


    public StartDisplay(MainGame mainGame, Parent root, double width, double height) {
        super(root, width, height);
        root.setStyle("-fx-background-color: gray");
        this.mainGame = mainGame;
        myChooser = makeChooser(DATA_FILE_EXTENSION);
        selectedFile = null;
        loadedGameDisplay = null;

        box = new VBox();
        box.setAlignment(Pos.CENTER);
        ((BorderPane) root).setCenter(box);
        box.setSpacing(10);

        Label lbl = new Label("VOOGALICIOUS");
        lbl.setTextAlignment(TextAlignment.CENTER);
        box.getChildren().add(lbl);

    }

    private FileChooser makeChooser(String extensionAccepted) {
        var result = new FileChooser();
        result.setTitle("Open Game File");
        result.setInitialDirectory(new File(System.getProperty("user.dir")));
        result.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Text Files", extensionAccepted));
        return result;
    }

    private void makeStartBtn() {
        Button btn1 = new Button();
        btn1.setText("PLAY");
        btn1.setAlignment(Pos.CENTER);
        btn1.getStyleClass().add("menuButton");

        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startGame(selectedFile);
            }
        });

        box.getChildren().add(btn1);
        startOnDisplay=true;
    }

    /**
     * This method is called once the start button is clicked
     * It passes the serialized file to GameSession where it is then deserialized into a Game object
     * Finally, it generates a GameDisplay instance for the loaded game and starts the game loop in MainGame
     *
     * @param file the serialized gameEngine.ModelImplementations.Game file (with an XML extension)
     */
    public void startGame(File file) {
        GameSession session = new GameSession(file);
        Game game = session.getGame();
        GameDisplay selectedGame = new GameDisplay(new BorderPane(), PLAYER_SCREEN_WIDTH, PLAYER_SCREEN_HEIGHT, game);
        DisplayControl control = new DisplayControl(selectedGame);
        game.setDisplayControl(control);
        loadedGameDisplay = selectedGame;
        loadedGameDisplay.getStylesheets().add(CSS);
        mainGame.startGameLoop();
    }

    public void handleFileChooser() {
        Stage fileStage = new Stage();
        selectedFile = myChooser.showOpenDialog(fileStage);
        if (selectedFile != null && !startOnDisplay) {
            makeStartBtn();

        }
    }

    /**
     * Called from MainGame to access the GameDisplay once launched
     * @return the GameDisplay handling the current game
     */
    public GameDisplay getLoadedGameDisplay() {
        return this.loadedGameDisplay;
    }

    @Override
    public Game getMyGame() {
        return null;
    }
}
