package playerEnvironment.view;

import javafx.scene.control.ScrollPane;

/**
 * @author Rohit Das (rvd5)
 * @author Peter Ciporin (pbc9)
 *
 * This class defines which portion of the game map is visible to the user on-screen at any given time.
 */

public class Camera extends ScrollPane {

    private MapGrid myMapGrid;

    public Camera() {
        super();

        myMapGrid = null;
        setStyle("-fx-background-color: black;");
        getStyleClass().add("camera");

    }

    public void setMyMapGrid(MapGrid mapGrid) {
        myMapGrid = mapGrid;
    }

    /**
     *
     * @param xPos horizontal coordinate of grid about which to center camera (in our case, x-position of player)
     * @param yPos vertical coordinate of grid about which to center camera (in our case, y-position of player)
     */
    public void centerPlayerInCamera(double xPos, double yPos) {
        setHvalue(xPos / myMapGrid.getGridWidth());
        setVvalue(yPos / myMapGrid.getGridHeight());
    }



}
