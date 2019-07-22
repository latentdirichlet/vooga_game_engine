package authoringEnvironment.frontend.Menu.Components;

import authoringEnvironment.frontend.MainView;
import gameEngine.ModelImplementations.GameCharacter;
import javafx.scene.layout.VBox;
import org.codehaus.groovy.tools.shell.Main;

import java.util.List;

/**
 * @author dc273
 */
public class CharacterPane implements ParameterField.ParameterFieldListener {
    public interface CharacterPaneListener {
        void characterParameterSet(String name, Object value);
    }

    public static final List<String> blackList = List.of(
            "ACTOR_HEIGHT",
            "ACTOR_WIDTH",
            "WALK_TIME",
            "height",
            "width",
            "x",
            "y"
    );

    private VBox root;

    private VBox propertiesBox;

    private MainView listener;

    public CharacterPane(MainView mv) {
        this.listener = mv;
        this.propertiesBox = new VBox();
        this.root = new VBox();

        root.setStyle("-fx-background-color: #202124");
        root.getChildren().add(propertiesBox);
    }

    /**
     * Update must be called after current map is set in backend
     */
    public void update(){
        var properties = listener.getListener().getPlaceholderProperties(new GameCharacter(-1, ""));
        for(var entry: properties.entrySet()) {
            if(blackList.contains(entry.getKey())) continue;

            var pf = new ParameterField(entry.getKey(), entry.getValue(), this);
            propertiesBox.getChildren().add(pf.getFieldBox());
        }
    }

    public VBox getRoot() {
        return root;
    }

    @Override
    public void onParameterSet(String parameter, Object value, Integer index) {
        listener.characterParameterSet(parameter, value);
    }
}
