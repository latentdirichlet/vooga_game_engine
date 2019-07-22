package gameEngine.ModelImplementations.Statistics;

import gameEngine.ModelImplementations.Events.Condition;
import gameEngine.ModelImplementations.Events.PropertyCondition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * @author aks64
 * stats class for keeping tack of health
 */
public class Health {
    private SimpleDoubleProperty healthPoints = new SimpleDoubleProperty();
    private SimpleDoubleProperty maxHealth = new SimpleDoubleProperty();

    /**
     * constructor that will set separate values for maxhealths and healthpoints
     * @param hp
     * @param max
     */
    public Health(double hp, double max){
        healthPoints.set(hp);
        maxHealth.set(max);
    }
    /**
     * constructor that will set same values for maxhealths and healthpoints
     * @param hp
     */
    public Health(double hp){
        healthPoints.set(hp);
        maxHealth.set(hp);
    }

    public void addObserver(Condition c){
        healthPoints.addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        ((PropertyCondition)c).notifyChanged();
                    }
                }
        );
        maxHealth.addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        ((PropertyCondition)c).notifyChanged();
                    }
                }
        );
    }

    public double getDamage(double hp){
        if(healthPoints.getValue() -hp<=0){
            healthPoints.set(0);
        }else{
            healthPoints.set(healthPoints.getValue() - hp);
        }
        return healthPoints.getValue();
    }

    public double addHealth(double hp){
        if(healthPoints.getValue() + hp > maxHealth.getValue()){
            healthPoints.set(maxHealth.getValue());
        }
        else{
            healthPoints.set(healthPoints.getValue() + hp);
        }
        return healthPoints.getValue();
    }

    public double increaseMaxHealth(double hp){
        maxHealth.set(maxHealth.getValue() + hp);;
        return maxHealth.getValue();
    }

    public double getHealthPoints() {
        return healthPoints.getValue();
    }

    public double getMaxHealth() {
        return maxHealth.getValue();
    }

    public void setHealthPoints(double hp) {
        healthPoints.set(hp);
    }
}
