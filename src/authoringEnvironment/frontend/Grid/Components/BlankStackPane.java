package authoringEnvironment.frontend.Grid.Components;

// Used to identify cells (using instanceof BlankStackPane) with mouse events
// Class acts as tag, better than using getProperties() because don't have to check for null properties

import javafx.scene.layout.StackPane;

/**
 * @author dc273
 *
 * Used to identify cells (using instanceof BlankStackPane) with mouse events
 * Class acts as tag, better than using getProperties() because don't have to check for null properties
 */
public class BlankStackPane extends StackPane {
    public BlankStackPane() {
        super();
    }
}
