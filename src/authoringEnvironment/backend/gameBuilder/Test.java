package authoringEnvironment.backend.gameBuilder;

import javafx.beans.property.SimpleDoubleProperty;

import java.io.Serializable;

public class Test implements Serializable {
    private SimpleDoubleProperty lol;
    public Test(){
        lol = new SimpleDoubleProperty(4.5);
        lol.set(4.3);
    }
}
