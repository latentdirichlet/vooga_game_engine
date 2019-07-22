package authoringEnvironment.frontend.Menu.Components.ToolBar.ToolBarPanes;

import authoringEnvironment.frontend.Menu.Components.ParameterField;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.List;

/**
 * @author dc273
 */
public class SelectPane implements ParameterField.ParameterFieldListener {
    public interface SelectListener {
        void parameterSet(int mapIndex, int x, int y, String parameter, Object value);
    }

    private Pane root;

    private Label selectedCellLabel;

    private VBox bgProperties;
    private VBox objectProperties;

    private SelectListener listener;

    private int selectedX;
    private int selectedY;
    private int selectedMap;

    public SelectPane(SelectListener listener) {
        this.root = new VBox(5);
        root.setStyle("-fx-background-color: #202124");

        this.bgProperties = new VBox();
        this.objectProperties = new VBox();
        this.listener = listener;


        var selectedLabel1 = new Label("Selected Cell:");
        selectedLabel1.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        selectedLabel1.setTextFill(Color.WHITE);

        var region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        selectedCellLabel = new Label("None");
        selectedCellLabel.setTextFill(Color.WHITE);

        var selectedContainer = new HBox();
        selectedContainer.getChildren().addAll(selectedLabel1, region, selectedCellLabel);


        var backgroundLabel = new Label("Background:");
        backgroundLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        backgroundLabel.setTextFill(Color.WHITE);

        var objectLabel = new Label("Object:");
        objectLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        objectLabel.setTextFill(Color.WHITE);

        root.getChildren().addAll(selectedContainer, backgroundLabel, bgProperties, objectLabel, objectProperties);
    }

    /**
     * Populate parameters and attach node properties
     * @param mapIndex
     * @param x
     * @param y
     * @param properties
     */
    public void setSelectedCell(int mapIndex, int x, int y, List<HashMap<String, Object>> properties) {
        this.selectedX = x;
        this.selectedY = y;
        this.selectedMap = mapIndex;

        properties.remove("scale");

        bgProperties.getChildren().clear();
        var bgPropertiesMap = properties.get(0);

        int i = 0;
        for(var entry: bgPropertiesMap.entrySet()) {
            var pf = new ParameterField(entry.getKey(), entry.getValue(), this);
            bgProperties.getChildren().addAll(pf.getFieldBox());
            attachNodeProperties(pf.getFieldBox(), true, i, x, y, mapIndex, entry.getKey(), entry.getValue());
            i++;
        }

        i = 0;
        objectProperties.getChildren().clear();
        var objectPropertiesMap = properties.get(1);
        for(var entry: objectPropertiesMap.entrySet()) {
            var pf = new ParameterField(entry.getKey(), entry.getValue(), this);
            objectProperties.getChildren().addAll(pf.getFieldBox());
            attachNodeProperties(pf.getFieldBox(), false, i, x, y, mapIndex, entry.getKey(), entry.getValue());
            i++;
        }


        selectedCellLabel.setText(String.format("(%s, %s)", x, y));
    }

    /**
     * Attach properties needed for right click drag
     * @param node
     * @param properties
     * @param i
     * @param x
     * @param y
     * @param map
     * @param name
     * @param value
     */
    public void attachNodeProperties(Node node, boolean properties, int i, int x, int y, int map, String name, Object value) {
        node.getProperties().put("properties", properties);
        node.getProperties().put("index", i);
        node.getProperties().put("x", x);
        node.getProperties().put("y", y);
        node.getProperties().put("map", map);
        node.getProperties().put("type", "property");
        node.getProperties().put("propertyName", name);
        node.getProperties().put("propertyValue", value);
    }

    /**
     * Highlight property
     * @param properties
     * @param index
     * @param onoff
     */
    public void highlight(boolean properties, int index, boolean onoff) {
        if(properties) {
            if(onoff) bgProperties.getChildren().get(index).setStyle("-fx-background-color: #303030; -fx-effect: innershadow(gaussian, #039ed3, 4, 1.0, 0, 0);");
            else bgProperties.getChildren().get(index).setStyle("");
        } else {
            if(onoff) objectProperties.getChildren().get(index).setStyle("-fx-background-color: #303030; -fx-effect: innershadow(gaussian, #039ed3, 4, 1.0, 0, 0);");
            else objectProperties.getChildren().get(index).setStyle("");
        }
    }

    public Pane getRoot() {
        return root;
    }

    @Override
    public void onParameterSet(String parameter, Object value, Integer index) {
        listener.parameterSet(selectedMap, selectedX, selectedY, parameter, value);
    }
}
