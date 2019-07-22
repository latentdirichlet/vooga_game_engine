package gameEngine.ModelImplementations.Events.Actions;

import java.util.List;
/**
 * @author: Shenghong Zhao
 * An Enumeration of all possible action types
 */
public enum ActionType {
    MOVENORTH_DIRECTION("Move North", List.of(), List.of()),
    MOVESOUTH_DIRECTION("Move South", List.of(), List.of()),
    MOVEWEST_DIRECTION("Move West", List.of(), List.of()),
    MOVEEAST_DIRECTION("Move East", List.of(), List.of()),
    ARMOR_INCREASE("Increase Armor", List.of("amount"), List.of()),
    ATTACK_INCREASE("Increase Attack", List.of("amount"), List.of()),
//    ATTACK_DECREASE("Decrease Attack", List.of("amount"), List.of()),
//    ATTACK_SET("Set Attack", List.of("amount"), List.of()),
    EXPERIENCE_INCREASE("Increase Experience", List.of("amount"), List.of()),
//    EXPERIENCE_DECREASE("Decrease Experience", List.of("amount"), List.of()),
//    EXPERIENCE_SET("Set Experience", List.of("amount"), List.of()),
    HEALTH_INCREASE("Increase Health", List.of("amount"), List.of()),
    HEALTH_DECREASE("Decrease Health", List.of("amount"), List.of()),
    HEALTH_SET("Set Health", List.of("amount"), List.of()),
    MAXHP_INCREASE("Increase Max HP", List.of("amount"), List.of()),
    REVERSE_DIRECTION("Reverse Direction", List.of(), List.of()),
    CREATE_TEXTBOX("Dialog", List.of(), List.of("text")),
    DIY_ACTORACTION("Custom Actor Action", List.of(), List.of("groovy")),
    DIY_PLAYERACTION("Custom Player Action", List.of(), List.of("grooy")),
    LOCATION("Move to a New Position", List.of("x", "y", "map"), List.of()),
    COMBAT("Create a Combat Scene", List.of(), List.of());

    private String description;
    private List<String> intArgumentNames;
    private List<String> stringArgumentNames;

    private ActionType(String description, List intArgumentNames, List stringArgumentNames) {
        this.description = description;
        this.intArgumentNames = intArgumentNames;
        this.stringArgumentNames = stringArgumentNames;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getIntArgumentNames() {
        return intArgumentNames;
    }

    public List<String> getStringArgumentNames() {
        return stringArgumentNames;
    }
}
