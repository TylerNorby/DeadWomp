
/**
 * Handles elements of gameboard related side of Deadwood, i.e. the board itself, and locations around the map
 *
 * @author Ashley Spassov, Tyler Norby
 * @version 1.0
 */
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

public class GameBoard {

    private HashMap<String, Location> locationMap;
    private Location[] locations;
    private Scene[] scenes;

    // Whenever we add castingOffice to this location array we need to make sure its of Casting office type
    public GameBoard() {
        locationMap = new HashMap<String, Location>();
        ParseXML parser = new ParseXML("board.xml", "cards.xml");

        locations = parser.parseBoard();
        scenes = parser.parseScenes();
    }

    /**
     * takes the name of desired location object and returns said object from
     * the hashmap
     *
     * @param location
     * @return returns location object with given name
     */
    public Location getLocation(String location) {
        return locationMap.get(location);
    }

    public Location[] getLocations()
    {
        return locations;
    }

    /**
     * Validates that two location names are actually next to each other on the game board
     *
     * @param start
     * @param dest
     * @return boolean value depending on if the two locations are next to each other
     */
    public boolean validateConnection(String start, String dest) {
        return locationMap.get(start).isConnected(dest);
    }

    /**
     * assigns a scene to a relevant location, i.e a movieSet object
     *
     * @param location
     * @param scene
     */
    public void setScene(String location, Scene scene) {

    }

}
