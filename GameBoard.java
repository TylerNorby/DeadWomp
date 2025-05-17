
/**
 * Handles elements of gameboard related side of Deadwood, i.e. the board itself, and locations around the map
 *
 * @author Ashley Spassov, Tyler Norby
 * @version 1.0
 */
import java.util.HashMap;
import java.util.Random;

public class GameBoard {

    private HashMap<String, Location> locationMap;
    private Location[] locations;
    private Card[] cards;
    private int cardIndex;
    private int scenes;

    public GameBoard() {
        locationMap = new HashMap<String, Location>();
        ParseXML parser = new ParseXML("board.xml", "cards.xml");
        locations = parser.parseBoard();

        cards = parser.parseCards();
        cardIndex = 0;
        scenes = 0;
        for (Location location : locations)
        {
            if (location instanceof MovieSet)
            {
                ++scenes;
            }
        }

        //shuffle cards 
        for (int i = 0; i < cards.length; ++i)
        {
            int index = new Random().nextInt(cards.length);
            Card swap = cards[i];
            cards[i] = cards[index];
            cards[index] = swap;
        }
        
        //add locations in array to hashmap for faster access
        for (int i = 0; i < locations.length; ++i)
        {
            locationMap.put(locations[i].getName(), locations[i]);
        }

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

    public int getScenes()
    {
        return scenes;
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
     * assigns next scene cards to all locations, resets shot counters
     *
     * @param location
     * @param scene
     */
    public void nextDay() {
        for (Location location : locations)
        {
            if (location instanceof MovieSet)
            {
                ((MovieSet) (location)).setCard(cards[cardIndex++]);
            }
        }
    }

}
