import java.util.ArrayList;
import java.util.HashMap;

/**
 * MovieSet object is a type of location that contains scenes and various other data points
 *
 * @author Ashley Spassov, Tyler Norby
 * @version 1.0
 */
public class MovieSet extends Location {

    Scene scene;
    ArrayList<Role> extras;
    HashMap<String, Role> extraMap;

    public MovieSet(String name, Location[] connections, Scene scene, ArrayList<Role> extras) {
        super(name, connections);
        this.extras = extras;
        this.scene = scene;
    }

    /**
     * Determines if a players acting roll was successful
     *
     * @param playerRoll
     * @return boolean telling if player met the threshhold to successfully act
     */
    public boolean actingSuccess(int playerRoll) {
        return playerRoll >= scene.budget;
    }

    /* 
    Checks if input role is extra or on SceneCard, then compares rank.
    **/
    public boolean validateRole(String name, int rank) {
        Role role = extraMap.get(name);
        if (role == null)
        {
            role = scene.getRole(name);
            return role != null && role.getRank() <= rank && !role.isTaken();
        }
        else
        {
            return role.getRank() <= rank && !role.isTaken();
        }
    }
    
    public boolean isExtra(String name)
    {
        Role role = extraMap.get(name); 
        return role != null;
    }

    public ArrayList<Role> getExtras()
    {
        return extras;
    }
}
