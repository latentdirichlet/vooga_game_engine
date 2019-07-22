package authoringEnvironment.frontend.Grid.Components;

import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * @author dc273
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

    public void setBackground(ImageView background) {
        bg.getChildren().set(0, background);
    }

    public void setObject(ImageView ob, double scale) {
        if(scale>1.0)
            objectContainer.setPrefSize(scale*size, scale*size);

        ((StackPane)object.getChildren().get(0)).getChildren().set(0, ob);

    }

    public void erase() {
        var obj = (ImageView)((StackPane)object.getChildren().get(0)).getChildren().get(0);
        var background = (ImageView)bg.getChildren().get(0);

        if(obj.getImage()!=null) {
            obj.setImage(null);
            objectContainer.setPrefSize(size, size);
        }
        else if(background.getImage()!=null) {
            background.setImage(null);
        }
    }

    public void selectCell() {
//        bg.setStyle("-fx-effect: innershadow(gaussian, #039ed3, 4, 1.0, 0, 0);");
        object.setStyle("-fx-effect: innershadow(gaussian, #039ed3, 4, 1.0, 0, 0);");
    }

    public void deselectCell() {
//        bg.setStyle("");
        object.setStyle("");
    }

    public Pane getBg() {
        return bg;
    }

    public Pane getObject() {
        return object;
    }
}
