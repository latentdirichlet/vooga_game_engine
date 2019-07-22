package playerEnvironment.view;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.HashMap;

/**
 * @author David Cheng (dc273)
 * @author Peter Ciporin (pbc9)
 * @author Rohit Das (rvd5)
 *
 * This class holds the information necessary to render a game map on the front end (with no direct dependency on backend logic)
 * Extending a GridPane, a MapGrid holds a 2D array of Cell objects
 * Note that one major assumption is that all maps will be 2-dimensional and be populated instances of Cell (or its subclasses)
 *
 */
public class MapGrid extends GridPane {
    public static final int CELL_SIZE = 60;

    private Cell cells[][];
    private HashMap<String, Image> cellImageMap;
    private HashMap<String, Image> cellObjectMap;
    private double gridWidth;
    private double gridHeight;

    private double[][] scales;

    public MapGrid(String[][] grid, double[][] scales) {
        super();

        this.scales = scales;

        cellImageMap = new HashMap<>();
        cellObjectMap = new HashMap<>();

        gridWidth = grid.length;
        gridHeight = grid[0].length;

        cells = new Cell[(int) gridWidth][(int) gridHeight];

        setStyle("-fx-background-color: #B8B8B8");
        renderGrid(grid);

    }

    /**
     * This private method generates a grid of Cell objects from a 2D array of Strings
     * Each String represents the path to an image that should be loaded for a particular cell
     * This method also ensures that all ImageViews are set to the proper size and scale
     *
     * @param grid a 2D array of Strings, each holding a path to the background image for the Cell at the corresponding coordinates
     */
    private void renderGrid(String[][] grid) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                var cell = new Cell(CELL_SIZE);
                Image image;
                if (cellImageMap.containsKey(grid[row][col])) {
                    image = cellImageMap.get(grid[row][col]);
                }
                else {
                    image = new Image(this.getClass().getResource(grid[row][col]).toExternalForm());
                    cellImageMap.put(grid[row][col], image);
                }

                var scale = scales[row][col];
                var imageView = new ImageView(image);
                imageView.setFitWidth(scale*CELL_SIZE );
                imageView.setFitHeight(scale*CELL_SIZE);
                cell.setBackground(imageView);
                cells[row][col] = cell;
                add(cell.getBg(), col, row);
            }
        }
    }

    public double getGridWidth() {
        return gridWidth;
    }

    public double getGridHeight() {
        return gridHeight;
    }
}
