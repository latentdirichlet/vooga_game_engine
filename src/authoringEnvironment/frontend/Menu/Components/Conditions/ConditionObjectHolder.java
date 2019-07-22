package authoringEnvironment.frontend.Menu.Components.Conditions;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * @author dc273
 */
public class ConditionObjectHolder {
    public interface ConditionObjectHolderListener {
        void delete(int index, int conditionIndex);
    }

    public static final Insets MARGINS = new Insets(10, 0, 10, 0);

    private Label intro;

    private VBox root;

    private int index;

    private ConditionObjectHolderListener listener;

    private int conditionIndex;

    private VBox lastContainer;

    public ConditionObjectHolder(int conditionIndex, int index, String introText, ConditionObjectHolderListener listener) {
        this.conditionIndex = conditionIndex;
        this.index = index;
        this.listener = listener;

        root = new VBox();
        root.setStyle("-fx-background-color: #303030;");
        root.setAlignment(Pos.CENTER);

        intro = new Label();
        intro.setPadding(MARGINS);
        intro.setTextFill(Color.WHITE);
        intro.setMouseTransparent(true);
        intro.setText(introText);

        root.getProperties().put("conditionHolder", index);
        root.getProperties().put("conditionIndex", conditionIndex);
        root.getChildren().addAll(intro);
    }

    /**
     * Condition object representation
     * @param name
     */
    public void addConditionObject(String name) {
        var COroot = new VBox();

        var region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        var object = new Label(name);
        object.setTextFill(Color.WHITE);
        object.setPadding(MARGINS);

        var button = new Button("x");
        button.getStyleClass().add("media-controls");
        button.setTextFill(Color.WHITE);

        if(root.getChildren().get(0)==intro) root.getChildren().remove(0);

        var container = new HBox();
        container.getChildren().addAll(object, region, button);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(0, 10, 0, 10));
        container.getProperties().put("conditionHolder", index);
        container.getProperties().put("conditionIndex", conditionIndex);

        object.setMouseTransparent(true);
        region.setMouseTransparent(true);

        COroot.getChildren().add(container);

        button.setOnMouseClicked(event -> {
            listener.delete(index, root.getChildren().indexOf(COroot));
            root.getChildren().remove(COroot);
            if(root.getChildren().size()==0) root.getChildren().add(intro);

        });

        root.getChildren().add(COroot);
        lastContainer = COroot;
    }

    public void addNode(Node node) {
        lastContainer.getChildren().add(node);
    }


    public void highlight(boolean onoff) {
        if(onoff) root.setStyle("-fx-background-color: #303030; -fx-effect: innershadow(gaussian, #039ed3, 4, 1.0, 0, 0);");
        else root.setStyle("-fx-background-color: #303030;");
    }

    public VBox getRoot() {
        return root;
    }
}
