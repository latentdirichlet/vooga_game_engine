package authoringEnvironment.utils;

import authoringEnvironment.frontend.Menu.Components.PalettePane;

import java.util.HashMap;
import java.util.List;

/**
 * @author dc273 sz165
 */

public interface AuthoringEventListener {
    // Sets the background of a cell
    void setCellBG(int mapIndex, int x, int y, String bg, double scale);

    // Sets the object of the cell
    void setCellObject(int mapIndex, int x, int y, String bg, PalettePane.Type type, double scale);

    // Gets properties
    List<HashMap<String, Object>> getCellProperties(int mapIndex, int x, int y);

    // Sets properties
    void setCellProperty(int mapIndex, int x, int y, String propertyName, Object value);

    // Erase cell: first erases objects if exists, then erases cell background
    void eraseCell(int mapIndex, int x, int y);

    // Serializes game and prompts user for file location
    boolean saveGame();

    // Doesn't do anything
    boolean loadGame();

    // Gets properties of object using reflection
    HashMap<String, Object> getPlaceholderProperties(Object obj);

    // Creates new map
    void newMap(int width, int height);

    // Deletes map at index (replaces with null)
    void deleteMap(int index);

    // Constructs new event
    void newEvent(EventPackage event);

    // Set character property reflectively
    void setCharacterProperty(String propertyName, Object value);

    // Set character position
    void setCharacterPosition(int mapIndex, int x, int y);

    // Create conditions + events for map links
    void createMapLink(ConditionPackage from, ConditionPackage to);

    // Runs game
    void runGame();
}

