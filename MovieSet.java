import java.util.ArrayList;
import java.util.HashMap;

/**
 * MovieSet object is a type of location that contains scenes and various other data points
 *
 * @author Ashley Spassov, Tyler Norby
 * @version 1.0
 */
public class MovieSet extends Location {

    Part[] extras;
    HashMap<String, Part> extraMap;
    Card[] cards;
    int shots;
    int cardNum;

    public MovieSet(String name, String[] connections, int shots, Part[] extras) {
        super(name, connections);
        this.shots = shots;
        this.extras = extras;
        this.cards = new Card[4];
    }

    /**
     * Determines if a players acting roll was successful
     *
     * @param playerRoll
     * @return boolean telling if player met the threshhold to successfully act
     */
    public boolean actingSuccess(int playerRoll) {
        return playerRoll >= cards[cardNum].budget;
    }

    /* 
    Checks if input role is extra or on SceneCard, then compares rank.
    **/
    public boolean validateRole(String name, int rank) {
        Part role = extraMap.get(name);
        if (role == null)
        {
            role = cards[cardNum].getRole(name);
            return role != null && role.getRank() <= rank && role.getPlayerID() == -1;
        }
        else
        {
            return role.getRank() <= rank && role.getPlayerID() == -1;
        }
    }
    
    public boolean isExtra(String name)
    {
        Part role = extraMap.get(name); 
        return role != null;
    }

    public Part[] getExtras()
    {
        return extras;
    }

    public Card getCard()
    {
        return cards[cardNum];
    }
}
