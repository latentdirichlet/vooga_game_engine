package authoringEnvironment.frontend.Grid.Components;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dc273
 */
public class Map {
    public interface MapListener {
        void cellClicked(int mapIndex, int x, int y);
    }

    private Cell cells[][];
    private GridPane bgGrid;
    private GridPane objectGrid;

    private Pane blanks[][];

    private VBox root;

    private boolean acceptCellClicks;

    private MapListener listener;

    private ImageView character;
    private int characterX = 0;
    private int characterY = 0;


    public Map(int mapIndex, int width, int height, int cellSize, MapListener listener, ImageView character) {
        this.character = character;
        this.listener = listener;
        this.acceptCellClicks = true;

        cells = new Cell[width][height];
        blanks = new Pane[width][height];

        bgGrid = new GridPane();
        bgGrid.setHgap(0);
        bgGrid.setVgap(0);
        bgGrid.getStyleClass().addAll("objectGrid");
        bgGrid.setGridLinesVisible(true);

        objectGrid = new GridPane();
        objectGrid.setStyle("-fx-background-color: transparent");
        objectGrid.setHgap(0);
        objectGrid.setVgap(0);

        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: transparent");
        grid.setHgap(0);
        grid.setVgap(0);
        grid.getStyleClass().add("gridlines");

        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                final int iPlaceholder = i;
                final int jPlaceholder = j;

                var cell = new Cell(cellSize);
                cells[i][j] = cell;

                var blankCell = new BlankStackPane();
                blankCell.setPrefSize(cellSize, cellSize);
                blankCell.setStyle("-fx-background-color: transparent;");

                blankCell.addEventHandler(MouseEvent.ANY, event -> {
                    var type = event.getEventType();
                    if( (event.isPrimaryButtonDown()&&type.equals(MouseDragEvent.MOUSE_DRAG_ENTERED)) || type.equals(MouseEvent.MOUSE_CLICKED)) {
                        if(acceptCellClicks)
                            listener.cellClicked(mapIndex, iPlaceholder, jPlaceholder);
                    }

                });
                blankCell.getProperties().put("map", mapIndex);
                blankCell.getProperties().put("x", i);
                blankCell.getProperties().put("y", j);
                blankCell.getProperties().put("type", "cell");

                blanks[i][j] = blankCell;

                bgGrid.add(cell.getBg(), i, j);
                objectGrid.add(cell.getObject(), i, j);
                grid.add(blankCell, i, j);
            }
        }

        var grids = new Group(new Rectangle(width*cellSize, height*cellSize, Color.WHITE),
                bgGrid, objectGrid, grid);

        var label = new Label("Map " + Integer.toString(mapIndex));
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        root = new VBox(label, grids);
        root.setSpacing(5);


    }

    public void setCellBackground(int x, int y, ImageView image) {
        cells[x][y].setBackground(image);
    }

    public void setCellObject(int x, int y, ImageView image, double scale) {
        cells[x][y].setObject(image, scale);
    }

    public void selectCell(int x, int y) {
        cells[x][y].selectCell();
        blanks[x][y].setBorder(new Border(new BorderStroke(Color.valueOf("#039ed3"),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));
    }


    public void deselectCell(int x, int y) {
        cells[x][y].deselectCell();
        blanks[x][y].setBorder(null);
    }

    public void eraseCell(int x, int y) {
        cells[x][y].erase();
    }


    public void moveCharacter(int x, int y) {
        removeCharacter();
        blanks[x][y].getChildren().add(character);

        this.characterX = x;
        this.characterY = y;
    }

    public void removeCharacter() {
        blanks[characterX][characterY].getChildren().remove(character);
    }



    public void setAcceptCellClicks(boolean acceptCellClicks) {
        this.acceptCellClicks = acceptCellClicks;
    }

    public VBox getRoot() {
        return root;
    }
}
