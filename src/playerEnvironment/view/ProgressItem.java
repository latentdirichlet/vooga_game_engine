package playerEnvironment.view;

import javafx.scene.control.ProgressBar;

/**
 * @author Peter Ciporin (pbc9)
 * @author Rohit Das (rvd5)
 *
 * This subclass of StatItem is intended for statistics with a concrete maximum possible value, where users
 * might want to see their current progress toward this max.
 *
 * A ProgressItem first renders the title Label (inherited from its parent) inside an HBox
 * It renders a progress bar next to this label representing how close the current value is to its max possible value
 */
public class ProgressItem extends StatItem {

    private ProgressBar progressBar;
    private double maxValue;

    public ProgressItem(String statName, double initial, double max) {
        super(statName, initial);
        this.maxValue = max;
        progressBar = new ProgressBar();
        updateProgressBar();

        getChildren().addAll(title, progressBar);
    }

    /**
     * ProgressItem's implementation of updateStat simply updates the current value and then resets the state of the progress bar
     *
     * @param current the new value of the statistic to which it should be updated
     */
    @Override
    public void updateStat(double current) {
        updateCurrentValue(current);
        updateProgressBar();
    }

    private void updateProgressBar() {
        progressBar.setProgress(getCurrentValue() / maxValue);
    }

}
