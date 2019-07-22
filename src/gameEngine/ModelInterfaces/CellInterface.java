package gameEngine.ModelInterfaces;

import gameEngine.ModelImplementations.CellType;
import gameEngine.ModelImplementations.Position;
import javafx.scene.image.Image;

import java.net.URL;

/**
 * this interface describes a cell on the game map.
 * It will be hold its row and column and the texture.
 *
 * A texture(CellType) will be dirt/grass/sand, etc.
 * It can be indicated by an int, or something else. (Remains to be discussed).
 */

public interface CellInterface {

    CellType myType = CellType.GRASS;

    /**
     * This function sets the texture of this cell
     *
     * @param path
     */
    void setCellImagePath(String path);

    /**
     * get the image of cell texture for display purpose
     */
    String getCellImage();

    Position getPosition();

    CellType getMyType();

}
