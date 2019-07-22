package gameEngine.ModelImplementations;

import gameEngine.ModelImplementations.Events.Condition;
import gameEngine.ModelImplementations.Events.PropertyCondition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Position implements Comparable {
    private SimpleIntegerProperty x = new SimpleIntegerProperty();
    private SimpleIntegerProperty y = new SimpleIntegerProperty();
    private SimpleIntegerProperty map = new SimpleIntegerProperty();

    public Position(int pX, int pY, int map){
        x.set(pX);
        y.set(pY);
        this.map.set(map);
    }

    public void addPropertyListener(Condition c){
        System.out.println("adding property listener");
        x.addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        ((PropertyCondition)c).notifyChanged();
                    }
                }
        );
        y.addListener( new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                ((PropertyCondition)c).notifyChanged();
            }
        });
    }

    @Override
    public int compareTo(Object o) {
        Position otherPosition = (Position)o;
        if(otherPosition.getX() == x.getValue() && otherPosition.getY() == y.getValue()){
            return 1;
        };
        return 0;
    }

    public boolean equals(Object o){
        if(!(o instanceof Position)){
            return false;
        }
        Position oPos = (Position)o;
        return oPos.getX() == this.getX() && oPos.getY() == this.getY() && this.getMap() == oPos.getMap();
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 37 * result + x.getValue();
        result = 37 * result + y.getValue();
        return result;
    }

    public int getX(){
        return x.getValue();
    }

    public int getY(){
        return y.getValue();
    }

    public int getMap() {
        return map.get();
    }

    public void updateXLocation(int newX){
        x.set(newX);
    }

    public void updateYLocation(int newY){
        y.set(newY);
    }

    public void updateLocation(int newX, int newY, int map){
        updateXLocation(newX);
        updateYLocation(newY);
        this.map.set(map);
    }

    @Override
    public String toString() {
        return "(" + x.getValue() + "," + y.getValue() + ")";
    }
}
