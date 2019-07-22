package authoringEnvironment.utils;

import javafx.scene.input.KeyCode;


/**
 * @author dc273
 *
 * Holds parameters for creating conditions
 * CELL: x, y, map
 * OBJECT: x, y, map
 * PROPERTY: property, value
 * CHARACTER: ~
 * KEYPRESS: KeyCode
 */
public class ConditionPackage {
    public enum Type {
        CELL, OBJECT, PROPERTY, CHARACTER, KEYPRESS;
    }

    public KeyCode keyCode;

    public Integer x;
    public Integer y;
    public Integer map;

    public Type type;

    public String property;
    public Object value;

    public ConditionPackage(Type tyoe, Object value) {
        this.type = tyoe;
        this.value = value;
    }

    public ConditionPackage(Type type, KeyCode keyCode) {
        this.type = type;
        this.keyCode = keyCode;
    }

    /**
     * CHARACTER
     * @param type
     */
    public ConditionPackage(Type type) {
        this.type = type;
    }

    /**
     * CELL/OBJECT Type
     * @param type
     * @param x
     * @param y
     * @param map
     */
    public ConditionPackage(Type type, int x, int y, int map) {
        this.x = x;
        this.y = y;
        this.map = map;
        this.type = type;
    }

    /**
     * PROPERTY Type
     * @param type
     * @param property
     * @param value
     */
    public ConditionPackage(Type type, String property, Object value) {
        this.type = type;
        this.property = property;
        this.value = value;
    }
}
