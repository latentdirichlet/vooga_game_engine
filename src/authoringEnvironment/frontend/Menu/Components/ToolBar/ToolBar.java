package authoringEnvironment.frontend.Menu.Components.ToolBar;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * @author dc273
 */
public class ToolBar {
    public interface ToolBarListener {
        void toolSelected(ToolBarButtons value);
    }

    public static final int BUTTON_SIZE = 20;

    /**
     * Buttons and their file locations
     */
    public enum ToolBarButtons {
        SELECT("/ToolBarIcons/pointer.png", "Select", "V"),
        HAND("/ToolBarIcons/hand.png", "Hand", "H"),
        ZOOM("/ToolBarIcons/zoom.png", "Zoom", "Control"),
        PAINT("/ToolBarIcons/paint.png", "Paint", "F"),
        ERASER("/ToolBarIcons/eraser.png", "Eraser", "R");

        private String iconFileName;
        private String name;
        private String shortcut;

        ToolBarButtons(String iconFileName, String name, String shortcut) {
            this.iconFileName = iconFileName;
            this.name = name;
            this.shortcut = shortcut;
        }

        public String getIconFileName() {
            return iconFileName;
        }

        public String getName() { return name; }

        public String getShortcut() { return shortcut; }
    }


    private HBox root;
    private Button[] buttons;

    private ToolBarListener listener;

    public ToolBar(ToolBarListener listener) {
        this.listener = listener;

        root = new HBox();

        var buttonBox = new HBox();
        var buttonTypes = ToolBarButtons.values();
        buttons = new Button[buttonTypes.length];

        for(int i=0; i<buttonTypes.length; i++) {
            var button = makeMediaControlButton(buttonTypes[i].getIconFileName(), buttonTypes[i]);

            var buttonType = buttonTypes[i];
            Tooltip tt = new Tooltip(String.format("%s (%s)", buttonType.getName(), buttonType.getShortcut()));
            tt.setShowDelay(Duration.millis(150));
            tt.setHideDelay(Duration.ZERO);

            button.setTooltip(tt);

            buttons[i] = button;

            if(i==0)
                button.getStyleClass().add("selected");

            buttonBox.getChildren().add(button);
        }
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().add(buttonBox);
        root.setPadding(new Insets(0, 10, 0, 10));
    }

    /**
     * Create button based on file name and set listener
     *
     * @param icon
     * @param listenerValue
     * @return button
     */
    private Button makeMediaControlButton(String icon, ToolBarButtons listenerValue) {
        Button newButton = new Button();

        Image image = new Image(this.getClass().getResource(icon).toExternalForm());

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(BUTTON_SIZE);
        imageView.setFitHeight(BUTTON_SIZE);

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(1);
        imageView.setEffect(colorAdjust);

        newButton.setGraphic(imageView);
        newButton.getStyleClass().add("media-controls");
        newButton.setOnAction(e -> selectTool(listenerValue));

        return newButton;
    }

    /**
     * Alert listener and reflect change in frontend
     * @param value
     */
    public void selectTool(ToolBarButtons value) {
        for(Button button: buttons)
            button.getStyleClass().remove("selected");

        buttons[value.ordinal()].getStyleClass().add("selected");

        listener.toolSelected(value);
    }

    public HBox getRoot() {
        return root;
    }
}
