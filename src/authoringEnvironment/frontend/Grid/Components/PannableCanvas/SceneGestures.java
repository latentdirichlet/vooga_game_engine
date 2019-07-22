package authoringEnvironment.frontend.Grid.Components.PannableCanvas;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;


/**
 * @author Roland (https://stackoverflow.com/questions/32220042/pick-and-move-a-node-in-a-pannable-zoomable-pane)
 *
 * modified by dc273
 *
 * Listeners for making the scene's canvas draggable and zoomable
 */
public class SceneGestures {

    private static final double MAX_SCALE = 10.0d;
    private static final double MIN_SCALE = .1d;

    private DragContext sceneDragContext = new DragContext();

    PannableCanvas canvas;

    private boolean zoomable = false;
    private boolean pannable = false;

    public SceneGestures( PannableCanvas canvas) {
        this.canvas = canvas;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    public EventHandler<ScrollEvent> getOnScrollEventHandler() {
        return onScrollEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
//            if(event.isPrimaryButtonDown() || event.isSecondaryButtonDown()) return;
            if(pannable) {
                sceneDragContext.mouseAnchorX = event.getSceneX();
                sceneDragContext.mouseAnchorY = event.getSceneY();

                sceneDragContext.translateAnchorX = canvas.getTranslateX();
                sceneDragContext.translateAnchorY = canvas.getTranslateY();
            }
        }

    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            if(event.isSecondaryButtonDown()) return;
            if(pannable) {
                canvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
                canvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);

                event.consume();
            }

        }
    };

    /**
     * Mouse wheel handler: zoom to pivot point
     */
    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

        @Override
        public void handle(ScrollEvent event) {
            if(zoomable) {
                double delta = 1.05;

                double scale = canvas.getScale(); // currently we only use Y, same value is used for X
                double oldScale = scale;

                if (event.getDeltaY() < 0)
                    scale /= delta;
                else
                    scale *= delta;

                scale = clamp(scale, MIN_SCALE, MAX_SCALE);

                double f = (scale / oldScale) - 1;

                double dx = (event.getSceneX() - (canvas.getBoundsInParent().getWidth() / 2 + canvas.getBoundsInParent().getMinX()));
                double dy = (event.getSceneY() - (canvas.getBoundsInParent().getHeight() / 2 + canvas.getBoundsInParent().getMinY()));

                canvas.setScale(scale);

                // note: pivot value must be untransformed, i. e. without scaling
                canvas.setPivot(f * dx, f * dy);

                event.consume();
            } else {
                canvas.setTranslateX(canvas.getTranslateX() + event.getDeltaX());
                canvas.setTranslateY(canvas.getTranslateY() + event.getDeltaY());

                event.consume();
            }
        }

    };

    public void setZoomable(boolean zoomable) {
        this.zoomable = zoomable;
    }

    public void setPannable(boolean pannable) {
        this.pannable = pannable;
    }

    public static double clamp(double value, double min, double max) {

        if( Double.compare(value, min) < 0)
            return min;

        if( Double.compare(value, max) > 0)
            return max;

        return value;
    }

    public void setTranslateXY(double x, double y) {
        canvas.setScale(1);
        canvas.setTranslateX(x);
        canvas.setTranslateY(y);
    }


}

