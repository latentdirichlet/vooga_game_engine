package gameEngine.ModelImplementations.Events;

/**
 * This enum is used by the front end to display the propert conditions.
 * @author mpz5
 */
public enum ConditionType {
    COLLISION("collision"),
    LOCATION("location"),
    PROPERTIES("object properties"),
    ITEMUSAGE("item usage"),
    WALKOFF("walk off map"),
    DIY("DIY"),
    USERINTERACTION("");

    private String description;

    private ConditionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
