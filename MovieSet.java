
import java.util.HashMap;

/**
 * MovieSet object is a type of location that contains scenes and various other
 * data points
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
        this.extraMap = new HashMap<>();

        if (extras != null) {
            for (Part extra : extras) {
                this.extraMap.put(extra.getName(), extra);
            }
        }
    }

    public Part getRole(String roleName) {
        Part role = null;
        if (this.card != null) {
            role = this.card.getRole(roleName);
        }

        if (role == null && this.extraMap.containsKey(roleName)) {
            role = this.extraMap.get(roleName);
        }

        return role;
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
        Part targetRole = null;
        if (this.card != null) {
            if (this.card.roleMap != null && this.card.roleMap.containsKey(name)) {
                targetRole = this.card.roleMap.get(name);
            }
        }

        if (targetRole == null) {
            if (this.extraMap != null && this.extraMap.containsKey(name)) {
                targetRole = this.extraMap.get(name);
            }

        }

        if (targetRole == null) {
            return false;
        }

        if (targetRole.inUse()) {
            return false;
        }
        return true;
    }

    public boolean isExtra(String name) {
        Part role = extraMap.get(name);
        return role != null;
    }

    public int getShots() {
        return shots;
    }

    public int getShotCounter() {
        return shotCounter;
    }

    public void removeShot() {
        --shotCounter;
    }

    public void resetShotCounter()
    {
        shotCounter = shots;
    }

    public Part[] getExtras() {
        return extras;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
