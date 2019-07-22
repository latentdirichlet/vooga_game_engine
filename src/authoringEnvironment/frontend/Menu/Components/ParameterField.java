package authoringEnvironment.frontend.Menu.Components;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;


/**
 * @author dc273
 */

public class ParameterField {
    private static final Insets TITLE_PADDING = new Insets(5, 0, 0, 0);
    private static final int AUTOPLAY_SPEED_FIELD_WIDTH = 50;


    /**
     * ParameterField event listener
     */
    public interface ParameterFieldListener {
        void onParameterSet(String parameter, Object value, Integer index);
    }

    private String parameter;
    private ParameterFieldListener listener;
    private HBox fieldBox;
    private TextField textField;

    private Integer index;

    public ParameterField(String parameter, Object initialValue, ParameterFieldListener listener) {
        this.parameter = parameter;
        this.listener = listener;

        fieldBox = new HBox();
        fieldBox.setAlignment(Pos.CENTER);

        var parameterLabel = new Label();
        parameterLabel.setText(parameter);
        parameterLabel.setPadding(new Insets(10, 50, 10, 0));
        parameterLabel.setAlignment(Pos.CENTER_LEFT);

        var setBox = new HBox();
        setBox.setAlignment(Pos.CENTER_RIGHT);

        Node field = null;

        if(initialValue instanceof Double)
            field = initializeFieldForDouble(parameter, (Double)initialValue);
        else if(initialValue instanceof Boolean)
            field = initializeFieldForBoolean(parameter, (Boolean)initialValue);
        else if(initialValue instanceof Integer)
            field = initializeFieldForInteger(parameter, (Integer)initialValue);
        else if(initialValue instanceof ArrayList)
            field = initializeFieldForArrayList(parameter, (ArrayList)initialValue);
        else if(initialValue instanceof String)
            field = initializeFieldForString(parameter, (String)initialValue);


        setBox.getChildren().add(field);

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        parameterLabel.setPadding(new Insets(0, 0, 0, 15));
        parameterLabel.setTextFill(Color.WHITE);

        setBox.setPadding(new Insets(0, 15, 0, 0));

        region1.setMouseTransparent(true);
        parameterLabel.setMouseTransparent(true);

        fieldBox.getChildren().addAll(parameterLabel, region1, setBox);
        fieldBox.setPadding(TITLE_PADDING);
    }

    /**
     * Show button with editing dialog
     * @param parameter
     * @param value
     * @return
     */
    private Button initializeFieldForString(String parameter, String value) {
        var editButton = new Button("Edit");
        editButton.getStyleClass().add("media-controls");
        editButton.setTextFill(Color.WHITE);
        editButton.setOnMouseClicked(event -> showEditorDialog(parameter, value));

        return editButton;
    }

    /**
     * Show dialog with TextArea
     * @param parameter
     * @param value
     */
    private void showEditorDialog(String parameter, String value) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Text Editor");
        dialog.setHeaderText("Edit Text");

        // Set the button types.
        ButtonType addButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        var box = new VBox();

        var textArea = new TextArea();

        box.getChildren().addAll(textArea);
        box.setPrefWidth(800);
        box.setStyle("-fx-background-color: #202124;");

        dialog.getDialogPane().setContent(box);

        DialogPane dialogPane = dialog.getDialogPane();
        String css = getClass().getResource("/main.css").toExternalForm();
        dialogPane.getStylesheets().add(css);

        ((Stage) dialog.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);

        dialog.show();

        dialog.setResultConverter(dialogButton -> {
            listener.onParameterSet(parameter, textArea.getText(), index);
            return null;
        });
    }


    /**
     * Show combo box
     */
    private ComboBox comboBox;
    private ComboBox initializeFieldForArrayList(String parameter, ArrayList<String> values) {
        ObservableList<String> options =
                FXCollections.observableArrayList(values);
        comboBox = new ComboBox(options);

        comboBox.getSelectionModel().selectedItemProperty().addListener((vals, oldValue, newValue) -> {
            listener.onParameterSet(parameter, newValue, index);
        });

        comboBox.setMaxWidth(AUTOPLAY_SPEED_FIELD_WIDTH*2.5);

        return comboBox;
    }

    public ComboBox getComboBox() {
        return comboBox;
    }

    /**
     * Initialize the parameter field for an integer input
     * @param parameter
     * @param initialValue
     * @return
     */
    private TextField initializeFieldForInteger(String parameter, Integer initialValue) {
        var textField = new TextField();
        textField.setMaxWidth(AUTOPLAY_SPEED_FIELD_WIDTH);

        textField.setText(Integer.toString(initialValue));

        // Make only number input-able
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*")) {
                newValue = newValue.replaceAll("[^-?\\d]", "");
                textField.setText(newValue);
            }

            if(newValue.equals("-"))
                newValue = "0";

            if(!newValue.equals(""))
                listener.onParameterSet(parameter, Integer.parseInt(newValue), index);
        });

        return textField;
    }

    /**
     * Initialize the parameter field for a double input (needs double text formatter to restrict input)
     * @param parameter
     * @param initialValue
     * @return
     */
    private TextField initializeFieldForDouble(String parameter, Double initialValue) {
        textField = new TextField();
        var textFormatter = setUpDoubleTF();

        textField.setMaxWidth(AUTOPLAY_SPEED_FIELD_WIDTH);
        textField.setTextFormatter(textFormatter);
        textField.setText(Double.toString(initialValue));
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals("") && !newValue.equals("."))
                listener.onParameterSet(parameter, Double.parseDouble(newValue), index);
        });

        return textField;
    }

    /**
     * Return Checkbox
     * @param parameter
     * @param initialValue
     * @return
     */
    private Pane initializeFieldForBoolean(String parameter, Boolean initialValue) {
        var container = new StackPane();
        container.setPrefWidth(AUTOPLAY_SPEED_FIELD_WIDTH);

        container.setAlignment(Pos.CENTER);

        var cb = new CheckBox();
        container.getChildren().addAll(cb);

        cb.setSelected(initialValue);

        cb.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            listener.onParameterSet(parameter, newValue, index);
        }));

        return container;
    }

    /**
     * TextFormatter for double input only
     * @return
     */
    private TextFormatter<Double> setUpDoubleTF() {
        Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");
        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c ;
            } else {
                return null ;
            }
        };
        StringConverter<Double> converter = new StringConverter<>() {
            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0.0 ;
                } else {
                    return Double.valueOf(s);
                }
            }

            @Override
            public String toString(Double d) {
                return d.toString();
            }
        };
        TextFormatter<Double> textFormatter = new TextFormatter<>(converter, 0.0, filter);
        return textFormatter;
    }

    /**
     * Updates value
     * @param value
     */
    public void updateValue(Double value) {
        textField.setText(Double.toString(value));
    }

    public String getParameter() {
        return parameter;
    }

    /**
     * Get root to add to panes
     * @return
     */
    public HBox getFieldBox() {
        return fieldBox;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
