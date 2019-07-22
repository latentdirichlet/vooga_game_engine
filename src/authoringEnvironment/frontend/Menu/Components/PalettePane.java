package authoringEnvironment.frontend.Menu.Components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.nio.file.Paths;

/**
 * @author dc273
 */
public class PalettePane {
    public interface BlocksPaletteListener {
        void blockSelected(String name);
        void blockTypeSelected(Type type);
    }

    /**
     * Types of images to select
     * Path defines where to get images
     * Label is displayed on button
     */
    public enum Type {
        BLOCKS("resources/CellGraphics/Blocks", "Blocks"),
        OBJECTS("resources/CellGraphics/Objects", "Objects"),
        PASSWAYS("resources/CellGraphics/Passways", "Passways"),
        NPCS("resources/CellGraphics/NPCs", "NPCs"),
        MONSTERS("resources/CellGraphics/Monsters", "Monsters"),
        ITEMS("resources/CellGraphics/Items", "Items");

        private String filename;
        private String label;
        private int lastSelected;

        Type(String filename, String label) {
            this.filename = filename;
            this.label = label;
            this.lastSelected = 0;
        }

        public String getFilename() { return filename; }

        public String getLabel() { return label; }

        public int getLastSelected() { return lastSelected; }

        public void setLastSelected(int lastSelected) { this.lastSelected = lastSelected; }
    }

    public static int BLOCK_SIZE = 50;
    public static int BLOCK_PADDING = 3;
    public static Insets BLOCK_MARGINS = new Insets(7, 7, 7, 7);
    public static Insets BLOCK_NO_MARGINS = new Insets(0, 0, 0, 0);

    private Pane root;
    private FlowPane selector;

    private BorderPane[] blocks;
    private String[] images;

    private BlocksPaletteListener listener;
    private Type currentType;

    private Button[] typeButtons;

    private boolean blocksOnly;

    public PalettePane(int width, int height, BlocksPaletteListener listener) {
        this.blocksOnly = false;
        this.root = new VBox(8);
        this.listener = listener;
        this.currentType = Type.BLOCKS;
        this.typeButtons = new Button[Type.values().length];

        root.setStyle("-fx-background-color: #202124;");

        selector = new FlowPane();
        selector.setHgap(BLOCK_PADDING);
        selector.setVgap(BLOCK_PADDING);
        selector.setAlignment(Pos.TOP_CENTER);
        selector.setPrefWidth(width);
        selector.setPrefHeight(height/3);

        ScrollPane selectorContainer = new ScrollPane();
        selectorContainer.setFitToWidth(true);
        selectorContainer.setStyle("-fx-background-color: transparent;");
        selectorContainer.setContent(selector);
        selectorContainer.setPannable(true);

        populateSelector(currentType);
        listener.blockSelected(images[0]);

        var typeSelector = initializeTypeSelect();

        root.getChildren().addAll(typeSelector, selectorContainer);
    }

    /**
     * Constructor to accept blocks only
     * @param width
     * @param height
     * @param listener
     * @param blocksOnly
     */
    public PalettePane(int width, int height, BlocksPaletteListener listener, boolean blocksOnly) {
        this(width, height, listener);
        this.blocksOnly = true;
        root.getChildren().remove(0);
    }

    /**
     * Return FlowPane with buttons of each Type
     * @return
     */
    private FlowPane initializeTypeSelect() {
        var typeSelector = new FlowPane();

        int i = 0;

        for (var type: Type.values()) {
            var button = new Button(type.getLabel());
            typeSelector.getChildren().addAll(button);
            button.setOnMouseClicked(event -> selectType(type));
            button.getStyleClass().add("media-controls");
            button.setTextFill(Color.WHITE);

            if(i==0) button.getStyleClass().add("selected");
            typeButtons[i] = button;
            i++;
        }

        return typeSelector;
    }

    /**
     * Method called when Type button is clocked
     * Replaces selector with blocks of that Type
     * @param type
     */
    private void selectType(Type type) {
        currentType = type;
        listener.blockTypeSelected(type);
        populateSelector(type);
        selectLastBlock();

        if(currentType.getLastSelected()!=-1)
            listener.blockSelected(images[currentType.getLastSelected()]);


        for(Button button: typeButtons)
            button.getStyleClass().remove("selected");

        typeButtons[type.ordinal()].getStyleClass().add("selected");
    }

    /**
     * Populates selector by Type, gets file path through enum
     * @param type
     */
    private void populateSelector(Type type) {
        selector.getChildren().clear();

        var folder = Paths.get(type.getFilename()).toFile();
        blocks = new BorderPane[folder.listFiles().length];
        images = new String[folder.listFiles().length];

        int i = 0;
        for (final File fileEntry : folder.listFiles()) {


            final int placeHolderI = i;

            var imageName = fileEntry.getPath().replace("resources", "").replace("\\","/");

            if(imageName.contains(".DS_Store")) continue;

            var img = new Image(getClass().getResource(imageName).toExternalForm(), BLOCK_SIZE, BLOCK_SIZE, true, false);
            var image = new ImageView(img);

            BorderPane imageViewWrapper = new BorderPane(image);

            imageViewWrapper.setPadding(BLOCK_MARGINS);
            imageViewWrapper.setOnMouseClicked(event -> blockSelected(placeHolderI, type));

            blocks[i] = imageViewWrapper;
            images[i] = imageName;

            selector.getChildren().add(imageViewWrapper);

            i++;
        }
    }

    /**
     * Alert listener which image was selected
     * @param x
     * @param type
     */
    private void blockSelected(int x, Type type) {
        clearBlockSelection();

        type.setLastSelected(x);
        selectLastBlock();

        if(blocksOnly) {
            for(var temp: Type.values()) {
                temp.setLastSelected(0);
            }
        }

        listener.blockSelected(images[x]);
    }

    /**
     * Dehighlight selected block
     */
    public void clearBlockSelection() {
        var last = currentType.getLastSelected();
        if(last==-1) return;
        blocks[last].getStyleClass().remove("selected-block");
        blocks[last].setPadding(BLOCK_MARGINS);
    }

    /**
     * Highlight selected block
     */
    public void selectLastBlock() {
        var last = currentType.getLastSelected();
        if(last==-1) return;
        blocks[last].getStyleClass().add("selected-block");
        blocks[last].setPadding(BLOCK_NO_MARGINS);
    }

    public Pane getRoot() {
        return root;
    }
}
