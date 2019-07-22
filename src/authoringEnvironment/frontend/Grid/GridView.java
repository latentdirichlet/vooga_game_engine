package authoringEnvironment.frontend.Grid;

import authoringEnvironment.utils.ConditionPackage;
import authoringEnvironment.frontend.Grid.Components.Map;
import authoringEnvironment.frontend.Grid.Components.PannableCanvas.NodeGestures;
import authoringEnvironment.frontend.Grid.Components.PannableCanvas.PannableCanvas;
import authoringEnvironment.frontend.Grid.Components.PannableCanvas.SceneGestures;
import authoringEnvironment.frontend.MainView;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author dc273
 */
public class GridView {
    public interface GridViewListener {
        void characterPositionSet(int map, int x, int y);
    }

    public static final int CELL_SIZE = 50;

    private Pane root;
    private PannableCanvas canvas;

    private NodeGestures nodeGestures;
    private SceneGestures sceneGestures;

    private HashMap<Integer, Map> maps;

    private MainView mv;
    private GridViewListener listener;

    private ImageView character;
    private int characterMap = -1;

    private HashMap<Line, List<ConditionPackage>> linesToLink;

    public GridView(MainView mv) {
        this.linesToLink = new HashMap<>();
        String fileSeparator = System.getProperty("file.separator");
        var characterImage = new Image(this.getClass().getResource("/characters/1.png").toExternalForm(),
                CELL_SIZE, CELL_SIZE, true, false);
        this.character = new ImageView(characterImage);
        this.mv = mv;
        this.listener = mv;
        this.root = new Pane();
        this.maps = new HashMap<>();
        root.setStyle("-fx-background-color: #404040;");

        canvas = new PannableCanvas();
        nodeGestures = new NodeGestures(canvas);
        sceneGestures = new SceneGestures(canvas);


        character.getProperties().put("type", "character");

        root.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
        root.addEventFilter( MouseEvent.MOUSE_PRESSED, event -> {
            for(var line: linesToLink.keySet())
                line.setStyle("");
            selectedLine = null;

            sceneGestures.getOnMousePressedEventHandler().handle(event);
        });
        root.addEventFilter( MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        root.setClip(new Rectangle(2000, 2000));
        root.getChildren().add(canvas);
    }

    public void deleteMap(int index) {
        canvas.getChildren().remove(maps.get(index).getRoot());
        maps.remove(index);

        if(characterMap==index) {
            this.characterMap=-1;

            for(Integer key: maps.keySet()) {
                if(maps.get(key)!=null) {
                    moveCharacter(key, 0 , 0);
                    break;
                }
            }
        }
    }


    private void moveCharacter(int map, int x, int y) {
        maps.get(map).moveCharacter(x, y);
        this.characterMap = map;

        listener.characterPositionSet(map, x, y);
    }

    private boolean draggingCharacter = false;
    private int mapIterator = 0;

    public void addMap(int w, int h, Map.MapListener listener, String defaultBG) {
        final int mapIndex = mapIterator;
        mapIterator++;

        var map = new Map(mapIndex, w, h, CELL_SIZE, listener, character);
        maps.put(mapIndex, map);
        canvas.getChildren().add(map.getRoot());

        addEventFilters(map);

        if(canvas.getChildren().size() > 1) {
            map.getRoot().setTranslateX(canvas.getChildren().get(mapIndex-1).getBoundsInParent().getMaxX() + 50);
            map.getRoot().setTranslateY(canvas.getChildren().get(mapIndex-1).getBoundsInParent().getMinY() );
        } else {
            map.getRoot().setTranslateX(50);
            map.getRoot().setTranslateY(50);
        }

        if(defaultBG!=null) {
            for(int i=0; i<w; i++) {
                for(int j=0; j<h; j++) {
                    setCellBackground(mapIndex, i, j, defaultBG, 1.0);
                    mv.getListener().setCellBG(mapIndex, i, j, defaultBG, 1.0);
                }
            }
        }

        focusCameraOn(mapIndex);

        if(characterMap==-1)
            moveCharacter(mapIndex, 0 ,0);

    }

    private void addEventFilters(Map map) {
        map.getRoot().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            var intersectedNode = event.getPickResult().getIntersectedNode();

            // Map being dragged
            if(intersectedNode instanceof Text) {
                nodeGestures.getOnMousePressedEventHandler().handle(event);
                maps.values().forEach(e -> e.setAcceptCellClicks(false));
            }
            else if(intersectedNode instanceof ImageView && event.isPrimaryButtonDown()) {
                this.draggingCharacter = true;
                maps.values().forEach(e -> e.setAcceptCellClicks(false));
            }
            else
                nodeGestures.setMoveable(false);
        });


        map.getRoot().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            var intersectedNode = event.getPickResult().getIntersectedNode();

            if(intersectedNode==null) return;

            if(draggingCharacter && intersectedNode.getProperties()!=null && event.isPrimaryButtonDown()) {
                var x = (int)intersectedNode.getProperties().getOrDefault("x", -1);
                var y = (int)intersectedNode.getProperties().getOrDefault("y", -1);
                var mapIndex = (int) intersectedNode.getProperties().getOrDefault("map", -1);

                if(x==-1 || y==-1 || mapIndex==-1) return;

                moveCharacter(mapIndex, x, y);
            }

            if(intersectedNode instanceof Text) {
                nodeGestures.getOnMouseDraggedEventHandler().handle(event);
            }
        });

        map.getRoot().addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            nodeGestures.setMoveable(true);
            maps.values().forEach(e -> e.setAcceptCellClicks(true));
            this.draggingCharacter = false;

        });
    }

    private HashMap<String, Image> imageCache = new HashMap<>();

    public void focusCameraOn(int index) {
        sceneGestures.setTranslateXY(
                -maps.get(index).getRoot().getBoundsInParent().getMinX() + 50,
                -maps.get(index).getRoot().getBoundsInParent().getMinY() + 50
        );
    }

    public void setCellBackground(int mapIndex, int x, int y, String imageName, double scale) {
        Image img;

        if(imageCache.containsKey(imageName))
            img = imageCache.get(imageName);
        else {
            img = new Image(this.getClass().getResource(imageName).toExternalForm());
            imageCache.put(imageName, img);
        }

        var image = new ImageView(img);

        image.setFitWidth(scale*CELL_SIZE );
        image.setFitHeight(scale*CELL_SIZE);

        maps.get(mapIndex).setCellBackground(x, y, image);
    }

    public void setCellObject(int mapIndex, int x, int y, String imageName, double scale) {
        var img = new Image(this.getClass().getResource(imageName).toExternalForm(),
                scale*CELL_SIZE, scale*CELL_SIZE, true, false);
        var image = new ImageView(img);

        maps.get(mapIndex).setCellObject(x, y, image, scale);
    }

    private Integer lastSelectedX;
    private Integer lastSelectedY;
    private Integer lastSelectedMapIndex;

    public void selectCellObject(int mapIndex, int x, int y, boolean deselectPrevious) {
        if(deselectPrevious)
            deselectCells();

        if(maps.get(mapIndex)==null) return;

        maps.get(mapIndex).selectCell(x, y);

        lastSelectedX = x;
        lastSelectedY = y;
        lastSelectedMapIndex = mapIndex;
    }



    public void deselectCells(Integer map, Integer x, Integer y) {
        if(x!=null && y!=null && maps.get(map)!=null)
            maps.get(map).deselectCell(x, y);
    }

    public void deselectCells() {
        deselectCells(lastSelectedMapIndex, lastSelectedX, lastSelectedY);
    }

    public void selectLastSelected() {
        if(lastSelectedX!=null && lastSelectedY!=null)
            selectCellObject(lastSelectedMapIndex, lastSelectedX, lastSelectedY, true);
    }

    public SceneGestures getSceneGestures() {
        return sceneGestures;
    }

    public void eraseCell(int mapIndex, int x, int y) {
        maps.get(mapIndex).eraseCell(x, y);
    }

    public Pane getRoot() {
        return root;
    }

    private Line selectedLine;

    public void addMapLink(Node start, Node end) {
        var startMap = (int)start.getProperties().get("map");
        var endMap = (int)end.getProperties().get("map");
        var startX = (int)start.getProperties().get("x");
        var startY = (int)start.getProperties().get("y");
        var endX = (int)end.getProperties().get("x");
        var endY = (int)end.getProperties().get("y");

        deselectCells(startMap, startX, startY);
        deselectCells(endMap, endX, endY);

        Bounds bound1 = maps.get(startMap).getRoot().sceneToLocal(start.localToScene(start.getBoundsInLocal()));
        Bounds bound2 = maps.get(endMap).getRoot().sceneToLocal(end.localToScene(end.getBoundsInLocal()));

        var line = new Line();

        line.startXProperty().bind( maps.get(startMap).getRoot().translateXProperty().add(bound1.getMinX()).add(CELL_SIZE/2) );
        line.startYProperty().bind( maps.get(startMap).getRoot().translateYProperty().add(bound1.getMinY()).add(CELL_SIZE/2) );
        line.endXProperty().bind( maps.get(endMap).getRoot().translateXProperty().add(bound2.getMinX()).add(CELL_SIZE/2) );
        line.endYProperty().bind( maps.get(endMap).getRoot().translateYProperty().add(bound2.getMinY()).add(CELL_SIZE/2) );
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(5);

        line.setOnMouseClicked(e -> {
            line.setStyle("-fx-effect: innershadow(gaussian, #039ed3, 2, 1.0, 0, 0);");
            this.selectedLine = line;
        });

        var value = new ArrayList<ConditionPackage>();
        value.add(new ConditionPackage(ConditionPackage.Type.CELL, startX, startY, startMap));
        value.add(new ConditionPackage(ConditionPackage.Type.CELL, endX, endY, endMap));

        Line toRemove = null;
        for(var entry: linesToLink.entrySet()) {
            for(var co: entry.getValue()) {
                if( (co.x==startX && co.y==startY && co.map==startMap) || (co.x==endX && co.y==endY && co.map==endMap) ) {
                    canvas.getChildren().remove(entry.getKey());
                    toRemove = entry.getKey();
                }
            }
        }

        if(toRemove!=null)
            linesToLink.remove(toRemove);

        linesToLink.put(line, value);

        canvas.getChildren().add(line);
    }

    public void deletePressed() {
        if(selectedLine!=null) {
            canvas.getChildren().remove(selectedLine);
            linesToLink.remove(selectedLine);
            selectedLine = null;
        }
    }

    public List<List<ConditionPackage>> getLinks() {
        return new ArrayList<>(linesToLink.values());
    }
}
