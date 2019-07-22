package authoringEnvironment.frontend.Menu.Components.Conditions;

import authoringEnvironment.utils.ConditionPackage;
import authoringEnvironment.frontend.Menu.Components.ParameterField;
import gameEngine.ModelImplementations.Events.ConditionType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class NewCondition implements ParameterField.ParameterFieldListener, ConditionObjectHolder.ConditionObjectHolderListener {
    public static final String OBJECTS_ONLY = "Right-click drag objects here.";
    public static final String CELLS_ONLY = "Right-click drag cells here.";
    public static final String PROPERTIES_ONLY = "Right-click drag properties here.";


    private VBox root;

    private VBox content;

    private ArrayList<ConditionObjectHolder> conditionObjectsHolders;
    private ArrayList<ArrayList<ConditionPackage>> conditionObjects;

    private ConditionType conditionType;

    private int conditionIndex;

    private ParameterField conditionTypePF;

    public NewCondition(int conditionIndex) {
        this.conditionIndex = conditionIndex;

        this.conditionObjects = new ArrayList<>();
        conditionObjects.add(new ArrayList<>());
        conditionObjects.add(new ArrayList<>());

        this.conditionObjectsHolders = new ArrayList<>();
        this.root = new VBox();
        this.content = new VBox(10);

        content.setPadding(new Insets(20, 0, 20, 0));
        root.setStyle("-fx-background-color: #202124");
        root.setPadding(new Insets(0, 20, 0, 20));

        var types = new ArrayList<String>();
        types.addAll(PredefinedConditionCreator.stringToType.keySet());

        conditionTypePF = new ParameterField("type", types, this);
        root.getChildren().addAll(conditionTypePF.getFieldBox(), content);
    }

    @Override
    public void onParameterSet(String parameter, Object value, Integer index) {
        if(parameter.equals("type")) {
            conditionObjects.forEach(e -> e.clear());


            content.getChildren().clear();
            conditionObjectsHolders.clear();

            var type = (String) value;
            this.conditionType = PredefinedConditionCreator.stringToType.get(type);

            var label = new Label();
            label.setTextFill(Color.WHITE);


            if(type.equals(ConditionType.COLLISION.getDescription())) {
                initDualHolders(OBJECTS_ONLY, OBJECTS_ONLY, "collides with");
            }
            else if(type.equals(ConditionType.LOCATION.getDescription())) {
                initDualHolders(OBJECTS_ONLY, CELLS_ONLY, "shares location with");
            } else if(type.equals(ConditionType.PROPERTIES.getDescription())) {
                var temp1 = new ConditionObjectHolder(conditionIndex, 0, PROPERTIES_ONLY, this);
                conditionObjectsHolders.add(temp1);
                content.setAlignment(Pos.CENTER);
                content.getChildren().addAll(
                        temp1.getRoot()
                );
            } else if(type.equals(ConditionType.WALKOFF.getDescription())) {
                conditionObjects.get(0).add(new ConditionPackage(ConditionPackage.Type.CHARACTER));
                conditionObjects.get(1).add(new ConditionPackage(ConditionPackage.Type.PROPERTY, "", 0));
                var pf = new ParameterField("map", 0, this);
                pf.setIndex(-1);
                content.getChildren().add(pf.getFieldBox());
            } else if(type.equals(ConditionType.ITEMUSAGE.getDescription())) {
                conditionObjects.get(0).add(new ConditionPackage(ConditionPackage.Type.PROPERTY, "", 0));
                var pf = new ParameterField("itemID", 0, this);
                pf.setIndex(-2);
                content.getChildren().add(pf.getFieldBox());
            } else if(type.equals(ConditionType.DIY.getDescription())) {
                initDualHolders(OBJECTS_ONLY, CELLS_ONLY, null);
                var pf = new ParameterField("groovy", "", this);
                pf.setIndex(-3);
                conditionObjects.get(1).add(new ConditionPackage(ConditionPackage.Type.PROPERTY, "", ""));
                content.getChildren().add(pf.getFieldBox());
            }
        } else if(index==-1) {
            conditionObjects.get(1).get(0).value = value;
        } else if(index==-2) {
            conditionObjects.get(0).get(0).value = value;
        } else if(index==-3) {
            conditionObjects.get(1).get( conditionObjects.get(1).size()-1 ).value = value;
        } else {
            conditionObjects.get(0).get(index).value = value;
        }
    }

    public void initDualHolders(String one, String two, String labelText) {
        var temp1 = new ConditionObjectHolder(conditionIndex, 0, one, this);
        var temp2 = new ConditionObjectHolder(conditionIndex, 1, two, this);
        conditionObjectsHolders.add(temp1);
        conditionObjectsHolders.add(temp2);

        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(temp1.getRoot(), temp2.getRoot());
        if(labelText!=null) {
            Label label = new Label();
            label.setTextFill(Color.WHITE);
            label.setText(labelText);

            content.getChildren().add(1, label);
        }
    }

    public void addConditionObject(int index, Node node) {
        if(node==null) return;

        var nodeProperties = node.getProperties();

        var x = (int)nodeProperties.getOrDefault("x", -1);
        var y = (int)nodeProperties.getOrDefault("y", -1);
        var map = (int)nodeProperties.getOrDefault("map", -1);
        var nodeType = (String)nodeProperties.get("type");

        switch (conditionType) {
            case COLLISION:
                if(nodeType.equals("cell")) {
                    conditionObjectsHolders.get(index).addConditionObject(String.format("Object(%d, %d) on Map %d", x, y, map));
                    conditionObjects.get(index).add(new ConditionPackage(ConditionPackage.Type.OBJECT, x, y, map));
                } else if(nodeType.equals("character")) {
                    conditionObjectsHolders.get(index).addConditionObject(String.format("Character", x, y, map));
                    conditionObjects.get(index).add(new ConditionPackage(ConditionPackage.Type.CHARACTER));
                }
                break;
            case DIY:
                if(nodeType.equals("cell")) {
                    if (index == 0) {
                        conditionObjectsHolders.get(index).addConditionObject(String.format("Object(%d, %d) on Map %d", x, y, map));
                        conditionObjects.get(0).add(new ConditionPackage(ConditionPackage.Type.OBJECT, x, y, map));
                    } else if (index == 1) {
                        conditionObjectsHolders.get(index).addConditionObject(String.format("Cell(%d, %d) on Map %d", x, y, map));
                        conditionObjects.get(1).add(conditionObjects.size()-2, new ConditionPackage(ConditionPackage.Type.CELL, x, y, map));
                    }
                } else if(nodeType.equals("character")) {
                    if (index==0) {
                        conditionObjectsHolders.get(index).addConditionObject(String.format("Character", x, y, map));
                        conditionObjects.get(index).add(new ConditionPackage(ConditionPackage.Type.CHARACTER));
                    }
                }
                break;
            case LOCATION:
                if(nodeType.equals("cell")) {
                    if (index == 0) {
                        conditionObjectsHolders.get(index).addConditionObject(String.format("Object(%d, %d) on Map %d", x, y, map));
                        conditionObjects.get(0).add(new ConditionPackage(ConditionPackage.Type.OBJECT, x, y, map));
                    } else if (index == 1) {
                        conditionObjectsHolders.get(index).addConditionObject(String.format("Cell(%d, %d) on Map %d", x, y, map));
                        conditionObjects.get(1).add(new ConditionPackage(ConditionPackage.Type.CELL, x, y, map));
                    }
                } else if(nodeType.equals("character")) {
                    if (index==0) {
                        conditionObjectsHolders.get(index).addConditionObject(String.format("Character", x, y, map));
                        conditionObjects.get(index).add(new ConditionPackage(ConditionPackage.Type.CHARACTER));
                    }
                }
                break;
            case PROPERTIES:
                if(nodeType.equals("property")) {
                    var propertyName = (String)nodeProperties.get("propertyName");
                    var propertyValue = node.getProperties().get("propertyValue");
                    var isCell = (boolean)nodeProperties.get("properties");

                    if(isCell) return;

                    var string = String.format("Object(%d, %d, %d)", x, y, map) + " " + propertyName;

                    conditionObjectsHolders.get(index).addConditionObject(string);

                    conditionObjects.get(0).add(new ConditionPackage(ConditionPackage.Type.OBJECT, x, y, map));
                    conditionObjects.get(1).add(new ConditionPackage(ConditionPackage.Type.PROPERTY, propertyName, propertyValue));

                    var setValue = new ParameterField("equals", propertyValue, this);
                    setValue.setIndex(conditionObjects.get(0).size()-1);
                    setValue.getFieldBox().setPadding(new Insets(0, 0, 10, 0));
                    setValue.getFieldBox().maxWidthProperty().bind(conditionObjectsHolders.get(index).getRoot().widthProperty());
                    conditionObjectsHolders.get(index).addNode(setValue.getFieldBox());
                }
                break;
        }
    }

    public void highlightConditionObjectHolder(int index, boolean onOff) {
        conditionObjectsHolders.get(index).highlight(onOff);
    }

    public VBox getRoot() {
        return root;
    }

    @Override
    public void delete(int index, int conditionIndex) {
        if(conditionType==ConditionType.PROPERTIES) {
            conditionObjects.get(0).remove(conditionIndex);
            conditionObjects.get(1).remove(conditionIndex);
            return;
        }

        conditionObjects.get(index).remove(conditionIndex);
    }

    public ArrayList<ArrayList<ConditionPackage>> getConditionObjects() {
        return conditionObjects;
    }

    public ConditionType getCondititionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
        conditionTypePF.getComboBox().setValue(conditionType.getDescription());
    }
}
