package playerEnvironment.view;

import javafx.scene.control.Label;

/**
 * @author Peter Ciporin (pbc9)
 * @author Rohit Das (rvd5)
 *
 * This subclass of StatItem is intended for statistics where users want to see the current value of the statistic.
 *
 * A NonProgressItem first renders the title Label (inherited from its parent) inside an HBox
 * It renders second label next to the title containing a String representation of this value
 */
public class NonProgressItem extends StatItem {

    private Label valueLabel;

    public NonProgressItem(String statName, double current) {
        super(statName, current);
        updateStat(current);

        getChildren().addAll(title, valueLabel);
    }

    /**
     * NonProgressItem's implementation of updateStat updates the current value and re-generates the label that displays it
     *
     * @param current the new value of the statistic to which it should be updated
     */
    @Override
    public void updateStat(double current) {
        updateCurrentValue(current);
        this.valueLabel = new Label(Double.toString(getCurrentValue()));
    }

}
