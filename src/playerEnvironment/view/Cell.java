package playerEnvironment.view;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * @author dc273
 * @author Rohit Das (rvd5)
 * @author Peter Ciporin (pbc9)
 *
 * This class is used to represent a cell, the fundamental building block of the game map display
 */
public class Cell {

    /**
     * StackPane Children:
     *      1th - background
     *      2th - object
     */
    private Pane bg;
    private Pane object;

    private StackPane objectContainer;

    private int size;

    public Cell(int size) {
        this.size = size;

        this.bg = new Pane(new ImageView());
        bg.setPrefHeight(size);
        bg.setPrefWidth(size);


        objectContainer = new StackPane(new ImageView());
        objectContainer.setPrefSize(size, size);

        this.object = new Pane();
        object.setPrefHeight(size);
        object.setPrefWidth(size);
        object.getChildren().addAll(objectContainer);

    }

    /**
     *
     * @param background background image for cell
     */
    public void setBackground(ImageView background) {
        bg.getChildren().set(0, background);
    }

    public Pane getBg() {
        return bg;
    }

    public Pane getObject() {
        return object;
    }
}
