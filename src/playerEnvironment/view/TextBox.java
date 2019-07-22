package playerEnvironment.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import playerEnvironment.controller.TextBoxHelper;

/**
 *  @author Peter Ciporin (pbc9)
 *  @author Rohit Das (rvd5)
 *
 *  This class contains the view component for rendering a text box to the player environment UI
 *  Passing your desired text to the setupText method handles rendering your text to the box
 *
 *  Note that this class should be used with the TextBoxHelper in playerEnvironment.controller to ensure, for
 *  example, that the text never overflows the box
 */
public class TextBox extends StackPane {
    private ImageView textbox;
    private final String textboxImagePath = "/dialog.png";

    public TextBox(StackPane parent) {
        super();
        setupTextBox(parent);
    }

    private void setupTextBox(StackPane parent){
        textbox = new ImageView(textboxImagePath);
        textbox.setPreserveRatio(true);
        textbox.setSmooth(false);

        textbox.fitWidthProperty().bind(parent.widthProperty().divide(2));

        getChildren().add(0, textbox);
        setPrefSize(textbox.getFitWidth(), textbox.getFitHeight());
        StackPane.setAlignment(textbox, Pos.BOTTOM_CENTER);
        setMaxHeight(textbox.getFitHeight());
    }

    /**
     * This public method renders the specified text to the textbox
     * @param input the text to be rendered to the textbox
     */
    public void setupText(String input){
        Label label = new Label(input);
        label.setFont(Font.font("Courier", FontWeight.NORMAL, 30));
        label.setStyle("-fx-font-family: 'monospaced';");
        label.setWrapText(true);
        getChildren().add(1, label);
        label.setPadding(new Insets(20, 0, 0, 20));
        setAlignment(label, Pos.TOP_LEFT);
    }

}
