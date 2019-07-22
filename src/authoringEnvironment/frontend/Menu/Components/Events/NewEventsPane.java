package authoringEnvironment.frontend.Menu.Components.Events;

import authoringEnvironment.utils.ConditionPackage;
import authoringEnvironment.utils.EventPackage;
import authoringEnvironment.frontend.Menu.Components.Action.NewAction;
import authoringEnvironment.frontend.Menu.Components.Conditions.NewCondition;
import gameEngine.ModelImplementations.Events.Actions.ActionType;
import gameEngine.ModelImplementations.Events.ConditionType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static authoringEnvironment.backend.gameDynamics.PredefinedConditionCreator.stringToType;

/**
 * @author dc273
 */
public class NewEventsPane {
    public interface NewEventsPaneListener {
        void newEvent(EventPackage event);
        void cancelNewEvent();
    }

    private Pane root;

    private VBox conditions;
    private VBox actions;

    private ArrayList<NewCondition> newConditions;
    private ArrayList<NewAction> newActions;

    private NewEventsPaneListener listener;

    public NewEventsPane(NewEventsPaneListener listener) {
        this.listener = listener;

        this.newConditions = new ArrayList<>();
        this.newActions = new ArrayList<>();

        this.conditions = new VBox();
        this.actions = new VBox();

        this.root = new VBox(0);
        root.setStyle("-fx-background-color: #202124");

        initNewButtons();
        initPaneButtons();
        newCondition();
        newAction();
    }

    public NewEventsPane(NewEventsPaneListener listener, EventPackage event) {
        this(listener);

        for(int i=0; i<event.conditionTypes.size(); i++) {
            var type = event.conditionTypes.get(i);
            var left = event.left.get(i);
            var right = event.right.get(i);

            if(i!=0) newCondition();

            newConditions.get(i).setConditionType(type);

            repopulateHolders(i, left, right);
        }

        for (int i = 0; i < event.actionTypes.size(); i++) {
            var type = event.actionTypes.get(i);
            var objs = event.objs.get(i);
            var arguments = event.arguments.get(i);

            if(i!=0) newAction();

            newActions.get(i).setType(type, (ArrayList<Object>) arguments);
        }
    }

    /**
     * When editing event, repopulate with parameters already set
     * @param conditionIndex
     * @param left
     * @param right
     */
    private void repopulateHolders(int conditionIndex, List<ConditionPackage> left, List<ConditionPackage> right) {
        var holders = List.of(left, right);

        for (int i = 0; i < holders.size(); i++) {
            var holder = holders.get(i);

            for (int j = 0; j < holder.size(); j++) {
                var conditionPackage = holder.get(conditionIndex);

                var node = new Pane();
                var properties = node.getProperties();
                var map =  new HashMap<>();

                for (Field field : conditionPackage.getClass().getDeclaredFields()) {
                    field.setAccessible(true);

                    Object value = null;
                    try {
                        value = field.get(conditionPackage);
                    } catch(IllegalAccessException ex) {
                        ex.printStackTrace();
                    }

                    if(field.getName().equals("type"))
                        value = value.toString().toLowerCase();


                    if (value != null)
                        map.put(field.getName(), value);
                }

                properties.putAll(map);

                addConditionObject(conditionIndex, i, node);
            }
        }
    }

    /**
     * Initialize new buttons
     */
    private void initNewButtons() {
        Button newConditionButton = new Button("New Condition");
        newConditionButton.getStyleClass().add("media-controls");
        newConditionButton.setTextFill(Color.WHITE);
        newConditionButton.setOnMouseClicked(event -> newCondition());

        Button newActionButton = new Button("New Action");
        newActionButton.getStyleClass().add("media-controls");
        newActionButton.setTextFill(Color.WHITE);
        newActionButton.setOnMouseClicked(event -> newAction());

        var buttonContainer = new HBox();
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        buttonContainer.getChildren().addAll(newConditionButton, region1, newActionButton);

        root.getChildren().addAll(conditions, actions, buttonContainer);
    }

    /**
     * Initialize Delete and Done buttons
     */
    private void initPaneButtons() {
        Button cancelButton = new Button("Delete");
        cancelButton.getStyleClass().add("media-controls");
        cancelButton.setTextFill(Color.WHITE);
        cancelButton.setOnMouseClicked(event -> cancel());

        Button doneButton = new Button("Done");
        doneButton.getStyleClass().add("media-controls");
        doneButton.setTextFill(Color.WHITE);
        doneButton.setOnMouseClicked(event -> done());

        var buttonContainer = new HBox();
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        buttonContainer.getChildren().addAll(cancelButton, region1, doneButton);

        root.getChildren().addAll(buttonContainer);
    }

    private void cancel() {
        listener.cancelNewEvent();
    }

    /**
     * Get parameters and store
     */
    private void done() {
        List<List<ConditionPackage>> left = new ArrayList<>();
        List<List<ConditionPackage>> right = new ArrayList<>();
        ArrayList<ConditionType> conditionTypes = new ArrayList<>();

        newConditions.forEach(newCondition -> {
            left.add(newCondition.getConditionObjects().get(0));
            right.add(newCondition.getConditionObjects().get(1));
            conditionTypes.add(newCondition.getCondititionType());
        });

//        List<ConditionPackage> objs, List<ActionType> types, List<String> stringArgs, List<Integer> intArgs
        ArrayList<ConditionPackage> objs = new ArrayList<>();
        ArrayList<ActionType> actionTypes = new ArrayList<>();
        List<List<Object>> arguments = new ArrayList<>();


        newActions.forEach(newAction -> {
            // TODO: Fix
            objs.add(new ConditionPackage(ConditionPackage.Type.CHARACTER));
            actionTypes.add(newAction.getType());
            arguments.add(newAction.getArguments());

        });


        listener.newEvent(new EventPackage(left, right, conditionTypes, objs, actionTypes, arguments));
    }

    /**
     * Create new NewCondition
     */
    private void newCondition() {
        var newCondition = new NewCondition(newConditions.size());
        var conditionTP = new TitledPane();
        conditionTP.setContent(newCondition.getRoot());

        var deleteButton = new Button("x");
        deleteButton.getStyleClass().add("media-controls");
        deleteButton.setTextFill(Color.WHITE);
        deleteButton.setStyle("-fx-background-color: transparent;");

        var container = new HBox();
        container.setAlignment(Pos.CENTER);
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        var label = new Label("Condition " + (newConditions.size() + 1));
        label.setTextFill(Color.WHITE);

        container.getChildren().addAll(label, region1, deleteButton);
        container.prefWidthProperty().bind(root.widthProperty().subtract(70));

        conditionTP.setGraphic(container);
        conditionTP.setAlignment(Pos.CENTER_RIGHT);
        conditionTP.setPadding(new Insets(0, 0, 0, 10));

        deleteButton.setOnMouseClicked(event -> deleteCondition(conditionTP, newCondition));

        conditions.getChildren().add(conditionTP);
        newConditions.add(newCondition);
    }

    /**
     * Create new NewAction
     */
    private void newAction() {
        var newAction = new NewAction();
        var actionTP = new TitledPane();
        actionTP.setContent(newAction.getRoot());

        var deleteButton = new Button("x");
        deleteButton.getStyleClass().add("media-controls");
        deleteButton.setTextFill(Color.WHITE);
        deleteButton.setStyle("-fx-background-color: transparent;");

        var container = new HBox();
        container.setAlignment(Pos.CENTER);
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        var label = new Label("Action " + Integer.toString(newActions.size() + 1));
        label.setTextFill(Color.WHITE);

        container.getChildren().addAll(label, region1, deleteButton);
        container.prefWidthProperty().bind(root.widthProperty().subtract(70));

        actionTP.setGraphic(container);
        actionTP.setAlignment(Pos.CENTER_RIGHT);
        actionTP.setPadding(new Insets(0, 0, 0, 10));

        deleteButton.setOnMouseClicked(event -> deleteAction(actionTP, newAction));

        actions.getChildren().add(actionTP);
        newActions.add(newAction);
    }

    private void deleteAction(TitledPane tp, NewAction action) {
        newActions.remove(action);
        actions.getChildren().remove(tp);
    }

    private void deleteCondition(TitledPane tp, NewCondition condition) {
        newConditions.remove(condition);
        conditions.getChildren().remove(tp);
    }

    public void addConditionObject(int conditionIndex, int holderIndex, Node node) {
        newConditions.get(conditionIndex).addConditionObject(holderIndex, node);
    }

    public void highlightConditionObjectHolder(int conditionIndex, int holderIndex, boolean onOff) {
        newConditions.get(conditionIndex).highlightConditionObjectHolder(holderIndex, onOff);
    }

    public Pane getRoot() {
        return root;
    }
}
