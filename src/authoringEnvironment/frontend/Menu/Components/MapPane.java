package authoringEnvironment.frontend.Menu.Components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import static authoringEnvironment.backend.gameBuilder.GameMapBuilder.DEFAULT_SIZEX;
import static authoringEnvironment.backend.gameBuilder.GameMapBuilder.DEFAULT_SIZEY;

/**
 * @author dc273
 */
public class MapPane implements ParameterField.ParameterFieldListener, PalettePane.BlocksPaletteListener {
    public interface MapPaneListener {
        void newMap(int width, int height, String defaultBG);
        void deleteMap(int index);
        void focusMap(int index);
    }

    private Pane root;

    private VBox maps;

    private int width;
    private int height;

    private MapPaneListener listener;

    private String defaultBG;

    public MapPane(int w, int h, MapPaneListener listener) {
        this.listener = listener;
        this.root = new VBox(10);
        root.setStyle("-fx-background-color: #202124");

        this.maps = new VBox(10);

        Button newMap = new Button("New Map +");
        newMap.getStyleClass().add("media-controls");
        newMap.setTextFill(Color.WHITE);
        newMap.setOnMouseClicked(event -> newMap());

        var rightAlignContainer = new HBox();
        rightAlignContainer.setAlignment(Pos.CENTER);
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        rightAlignContainer.getChildren().addAll(region1, newMap);

        root.getChildren().addAll(maps, rightAlignContainer);

        width = DEFAULT_SIZEX;
        height = DEFAULT_SIZEY;

    }

    /**
     * Creates a new map by prompting user with dialog
     */
    public void newMap() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("New Map");
        dialog.setHeaderText("Enter Dimensions:");

        // Set the button types.
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        var pickerLabel = new Label("default background");
        pickerLabel.setPadding(new Insets(10, 15, 10, 15));
        var picker = new PalettePane(280, 800, this, true);


        var box = new VBox();

        width = DEFAULT_SIZEX;
        height = DEFAULT_SIZEY;

        box.getChildren().addAll(
                new ParameterField("width", DEFAULT_SIZEX, this).getFieldBox(),
                new ParameterField("height", DEFAULT_SIZEY, this).getFieldBox(),
                pickerLabel,
                picker.getRoot()
        );
        box.setPrefWidth(300);
        box.setStyle("-fx-background-color: #202124;");

        dialog.getDialogPane().setContent(box);

        DialogPane dialogPane = dialog.getDialogPane();
        String css = getClass().getResource("/main.css").toExternalForm();
        dialogPane.getStylesheets().add(css);

        ((Stage) dialog.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);

        dialog.show();
        defaultBG = null;


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                listener.newMap(width, height, defaultBG);
                createMapRow();
            }
            return null;
        });
    }

    private int mapIndex = 0;

    /**
     * Create "Map 0" and delete button
     */
    private void createMapRow() {
        final int index = mapIndex;

        var region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        var object = new Label("Map " + index);
        object.setTextFill(Color.WHITE);

        var button = new Button("x");
        button.getStyleClass().add("media-controls");
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: transparent;");

        var container = new HBox();
        container.getChildren().addAll(object, region, button);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(0, 10, 0, 10));

        maps.getChildren().add(container);

        object.setOnMouseClicked(event -> listener.focusMap(index));

        button.setOnMouseClicked(event -> {
            maps.getChildren().remove(container);
            listener.deleteMap(index);
        });

        mapIndex++;
    }

    public Pane getRoot() {
        return root;
    }

    /**
     * ParameterField listeners
     * @param parameter
     * @param value
     * @param index
     */
    @Override
    public void onParameterSet(String parameter, Object value, Integer index) {
        if(parameter.equals("width")) this.width = (Integer) value;
        else if(parameter.equals("height")) this.height = (Integer) value;
    }

    @Override
    public void blockSelected(String name) {
        defaultBG = name;
    }

    @Override
    public void blockTypeSelected(PalettePane.Type type) {
    }
}
