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
    Card card;
    int shots;
    int shotCounter;
    int cardNum;

    public MovieSet(String name, String[] connections, int shots, Part[] extras) {
        super(name, connections);
        this.shots = shots;
        this.extras = extras;
    }

    /**
     * Determines if a players acting roll was successful
     *
     * @param playerRoll
     * @return boolean telling if player met the threshhold to successfully act
     */
    public boolean actingSuccess(int playerRoll) {
        return playerRoll >= card.getBudget();
    }

    /* 
    Checks if input role is extra or on SceneCard, then compares rank.
    **/
    public boolean validateRole(String name, int rank) {
        Part role = extraMap.get(name);
        if (role == null)
        {
            role = card.getRole(name);
            return role != null && role.getRank() <= rank && role.inUse();
        }
        else
        {
            return role.getRank() <= rank && role.inUse();
        }
    }
    public boolean isExtra(String name)
    {
        Part role = extraMap.get(name); 
        return role != null;
    }
    public int getShots()
    {
        return shots;
    }
    public int getShotCounter()
    {
        return shotCounter;
    }
    public void removeShot()
    {
        --shotCounter;
    }
    public Part[] getExtras()
    {
        return extras;
    }
    public Card getCard()
    {
        return card;
    }
    public void setCard(Card card)
    {
        this.card = card;
    }
}
