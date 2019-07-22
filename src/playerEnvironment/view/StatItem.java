package playerEnvironment.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * @author Peter Ciporin (pbc9)
 * @author Rohit Das (rvd5)
 *
 * This abstract class holds information about a particular game statistic
 * It assumes that every statistic will have a name (represented by the String statName)
 * as well as a current value (represented by the double current)
 */
public abstract class StatItem extends HBox {

    private static final int SPACING = 10;

    protected Label title;
    private double currentValue;

    public StatItem(String statName, double current) {
        super(SPACING);
        title = new Label(statName + ": ");
        updateCurrentValue(current);
    }

    protected void updateCurrentValue(double current) {
        this.currentValue = current;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    /**
     * This method, once implemented by subclasses, will be used by consumers to update the StatItem to its appropriate state
     * @param current the new value of the statistic to which it should be updated
     */
    public abstract void updateStat(double current);

}
