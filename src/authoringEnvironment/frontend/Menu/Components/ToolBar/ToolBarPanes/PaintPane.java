package authoringEnvironment.frontend.Menu.Components.ToolBar.ToolBarPanes;

import authoringEnvironment.frontend.MainView;
import authoringEnvironment.frontend.Menu.Components.PalettePane;
import authoringEnvironment.frontend.Menu.Components.ParameterField;
import gameEngine.ModelImplementations.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.codehaus.groovy.tools.shell.Main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author dc273
 */
public class PaintPane implements ParameterField.ParameterFieldListener {
    public interface PaintListener {
        void scaleSet(double x);
    }

    private Pane root;

    private MainView listener;

    private PalettePane.Type currentType;
    private HashMap<String, Object> currentParameters;

    private HashMap<PalettePane.Type, ArrayList<ParameterField>> parameters;

    public PaintPane(MainView listener) {
        this.root = new VBox();
        root.setStyle("-fx-background-color: #202124");
        this.listener = listener;
        this.parameters = new HashMap<>();
        this.currentParameters = new HashMap<>();
    }

    /**
     * Must be called after inital map is set in backend
     */
    public void update() {
        for (var value: PalettePane.Type.values()) {
            parameters.put(value, new ArrayList<>());
            HashMap<String, Object> properties;

            switch (value) {
                case BLOCKS:
                    properties = listener.getListener().getPlaceholderProperties(new Cell(-1, -1, -1, ""));
                    break;
                case PASSWAYS:
                    properties = listener.getListener().getPlaceholderProperties(new Passway(-1, ""));
                    break;
                case NPCS:
                    properties = listener.getListener().getPlaceholderProperties(new NPC(-1, ""));
                    break;
                case MONSTERS:
                    properties = listener.getListener().getPlaceholderProperties(new Monster(-1, ""));
                    break;
                default:
                    properties = listener.getListener().getPlaceholderProperties(new SimpleObject(-1, null));
                    break;
            }

            currentParameters = properties;

            for(var entry: properties.entrySet()) {
                parameters.get(value).add(new ParameterField(entry.getKey(), entry.getValue(), this));
            }
        }


        paletteTypeSet(PalettePane.Type.BLOCKS);
    }

    /**
     * Change properties to reflect change in palette type
     * @param type
     */
    public void paletteTypeSet(PalettePane.Type type) {
        this.currentType = type;

        currentParameters.clear();
        root.getChildren().clear();
        parameters.get(type).forEach(e -> {
            root.getChildren().add(e.getFieldBox());
            if(e.getParameter().equals("scale")) e.updateValue(1.0);
        });

        listener.scaleSet(1.0);
    }

    public Pane getRoot() {
        return root;
    }

    /**
     * ParameterField listener for sclae
     * @param parameter
     * @param value
     * @param index
     */
    @Override
    public void onParameterSet(String parameter, Object value, Integer index) {
        if(parameter.equals("scale")) {
            Double cast = (Double) value;
            if(cast>1.0) {
                cast = (double) cast.intValue();

                final Double temp = cast;
                parameters.get(currentType).forEach(e -> {
                    if(e.getParameter().equals("scale")) e.updateValue(temp);
                });
            }
            listener.scaleSet(cast);
        }
        currentParameters.put(parameter, value);

    }

    public HashMap<String, Object> getProperties() {
        return currentParameters;
    }

}

