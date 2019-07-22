package authoringEnvironment.frontend.Menu.Components.Action;

import authoringEnvironment.frontend.Menu.Components.ParameterField;
import gameEngine.ModelImplementations.Events.Actions.ActionType;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dc273
 */
public class NewAction implements ParameterField.ParameterFieldListener {
    private VBox root;

    private VBox parameters;

    private ActionType type;
    private ArrayList<Object> arguments;

    private ParameterField actionTypeChooser;

    public NewAction() {
        this.arguments = new ArrayList<>();
        this.parameters = new VBox();
        this.root = new VBox();
        this.root.setStyle("-fx-background-color: #202124");
        this.root.setPadding(new Insets(0, 20, 20, 20));

        var actionTypeDescriptions = new ArrayList<String>();
        for(var value: ActionType.values())
            actionTypeDescriptions.add(value.getDescription());

        actionTypeChooser = new ParameterField("action", actionTypeDescriptions, this);

        root.getChildren().addAll(actionTypeChooser.getFieldBox(), parameters);
    }

    public VBox getRoot() {
        return root;
    }

    /**
     * Action types and changing contents to reflect parameters needed
     * @param parameter
     * @param value
     * @param index
     */
    @Override
    public void onParameterSet(String parameter, Object value, Integer index) {
        if(parameter.equals("action")) {
            parameters.getChildren().clear();
            arguments.clear();

            var valueString = (String) value;
            ActionType actionType = null;
            for(var x: ActionType.values()) {
                if(x.getDescription().equals(valueString)) {
                    actionType = x;
                    break;
                }
            }

            var intArgumentNames = actionType.getIntArgumentNames();
            var stringArgumentNames = actionType.getStringArgumentNames();

            for(int i=0; i<intArgumentNames.size(); i++) {
                var pf = new ParameterField(intArgumentNames.get(i), 0, this);
                pf.setIndex(i);
                parameters.getChildren().add(pf.getFieldBox());
                arguments.add(0);
            }

            for(int i=0; i<stringArgumentNames.size(); i++ ) {
                var pf = new ParameterField(stringArgumentNames.get(i), "", this);
                pf.setIndex(i+actionType.getIntArgumentNames().size());
                parameters.getChildren().add(pf.getFieldBox());
                arguments.add("");
            }


            this.type = actionType;
        }
        else if(index!=null) {
            arguments.set(index, value);
        }

    }

    public void setType(ActionType type, ArrayList<Object> arguments) {
        this.type = type;
        actionTypeChooser.getComboBox().setValue(type.getDescription());
        this.arguments = arguments;

    }

    public ArrayList<Object> getArguments() {
        return arguments;
    }

    public ActionType getType() {
        return type;
    }

}
