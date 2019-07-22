package authoringEnvironment.frontend.Menu.Components.ToolBar.ToolBarPanes;

import javafx.scene.layout.Pane;

/**
 * @author dc273
 */
public class EraserPane {
    private Pane root;

    public EraserPane() {
        this.root = new Pane();
        root.setStyle("-fx-background-color: #202124");
    }

    public Pane getRoot() {
        return root;
    }
}

