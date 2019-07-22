package authoringEnvironment.frontend.Menu.Components.Events;

import authoringEnvironment.utils.EventPackage;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dc273
 */
public class EventsPane implements NewEventsPane.NewEventsPaneListener {
    public interface EventsPaneListener {
        void openNewEvent();
        void closeNewEvent();
        void editEvent(EventPackage event);
    }

    private Pane root;

    private VBox events;

    private Button newEventButton;

    private EventsPaneListener listener;

    private List<EventPackage> eventsPackages;

    public EventsPane(int w, int h, EventsPaneListener listener) {
        this.listener = listener;
        this.root = new VBox();
        this.eventsPackages = new ArrayList<>();
        root.setStyle("-fx-background-color: #202124");

        events = new VBox();

        newEventButton = new Button("New Event +");
        newEventButton.getStyleClass().add("media-controls");
        newEventButton.setTextFill(Color.WHITE);
        newEventButton.setOnMouseClicked(event -> listener.openNewEvent());

        var rightAlign = new HBox();
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        rightAlign.getChildren().addAll(region1, newEventButton);

        root.getChildren().addAll(events, rightAlign);
    }

    /**
     * New event created, add representation
     * @param event
     */
    @Override
    public void newEvent(EventPackage event) {
        var label = new Label("Event " + eventsPackages.size());
        label.setTextFill(Color.WHITE);

        var region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        var eventRow = new HBox(label, region);

        events.getChildren().add(eventRow);

        label.setOnMouseClicked(e -> editEvent(event, eventRow));

        eventsPackages.add(event);
        listener.closeNewEvent();
    }

    /**
     * Edit event
     * @param event
     * @param toDelete
     */
    private void editEvent(EventPackage event, Node toDelete) {
        deleteEvent( eventsPackages.indexOf(event), toDelete );
        listener.editEvent(event);
    }

    /**
     * Remove event
     * @param index
     * @param toDelete
     */
    private void deleteEvent(int index, Node toDelete) {
        events.getChildren().remove(toDelete);
        eventsPackages.set(index, null);
    }

    /**
     * Remove editing event
     */
    @Override
    public void cancelNewEvent() {
        listener.closeNewEvent();
    }

    public List<EventPackage> getEvents() {
        return eventsPackages;
    }

    public void disableButton(boolean disabled) {
        newEventButton.setDisable(disabled);
    }

    public Pane getRoot() {
        return root;
    }
}
