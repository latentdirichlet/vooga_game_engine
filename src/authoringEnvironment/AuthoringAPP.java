package authoringEnvironment;

import authoringEnvironment.backend.MainModel;
import authoringEnvironment.frontend.MainView;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * @author dc273
 */
public class AuthoringAPP extends Application {
    public static final int APP_SCREEN_WIDTH = 1400;
    public static final int APP_SCREEN_HEIGHT = 800;

    private Scene scene;
    private Pane root;
    private Line currentLine ;
    private Node pickWhipStart;

    private MainModel model;
    private MainView view;

    @Override
    public void start(Stage primaryStage){
        primaryStage.setHeight(APP_SCREEN_HEIGHT);
        primaryStage.setWidth(APP_SCREEN_WIDTH);

        model = new MainModel();
        view = new MainView(model, primaryStage);

        root = new Pane(view.getRoot());
        root.setPrefSize(APP_SCREEN_WIDTH, APP_SCREEN_HEIGHT);

        scene = new Scene(root, APP_SCREEN_WIDTH, APP_SCREEN_HEIGHT, Color.WHITE);
        String css = getClass().getResource("/main.css").toExternalForm();

        scene.getStylesheets().add(css);
        scene.addEventFilter(MouseDragEvent.DRAG_DETECTED, event -> scene.startFullDrag());
        scene.setOnKeyPressed(e -> view.keyPressed(e.getCode()));
        scene.setOnKeyReleased(event -> view.keyReleased(event.getCode()));

        primaryStage.setScene(scene);
        primaryStage.show();

        addEventFilters();
    }

    private void addEventFilters() {
        root.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if(e.isSecondaryButtonDown()) {
                currentLine = new Line(e.getX(), e.getY(), e.getX(), e.getY());

                currentLine.setStroke(Color.MEDIUMBLUE);
                currentLine.setStrokeWidth(3);
                currentLine.setMouseTransparent(true);

                root.getChildren().add(currentLine);

                pickWhipStart = e.getPickResult().getIntersectedNode();
                view.pickWhipStarted(pickWhipStart);
            }
        });

        root.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            if(e.isSecondaryButtonDown()) {
                currentLine.setEndX(e.getX());
                currentLine.setEndY(e.getY());

                view.pickWhipDown(pickWhipStart, e.getPickResult().getIntersectedNode());
            }
        });

        root.addEventFilter(MouseEvent.MOUSE_RELEASED, e-> {
            if(pickWhipStart!=null) {
                root.getChildren().remove(currentLine);
                view.pickWhipCompleted(pickWhipStart, e.getPickResult().getIntersectedNode());
                pickWhipStart = null;
            }
        });
    }

    public static void main(String[] args){
        launch(args);
    }
}
