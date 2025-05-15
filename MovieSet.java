import java.util.ArrayList;
import java.util.HashMap;

/**
 * MovieSet object is a type of location that contains scenes and various other data points
 *
 * @author Ashley Spassov, Tyler Norby
 * @version 1.0
 */
public class MovieSet extends Location {

    Role[] extras;
    HashMap<String, Role> extraMap;
    Scene[] scenes = new Scene[4];
    int sceneNum = 0;

    public MovieSet(String name, String[] connections, Role[] extras) {
        super(name, connections);
        this.extras = extras;
        this.scenes = new Scene[3];
    }

    /**
     * Determines if a players acting roll was successful
     *
     * @param playerRoll
     * @return boolean telling if player met the threshhold to successfully act
     */
    public boolean actingSuccess(int playerRoll) {
        return playerRoll >= scenes[sceneNum].budget;
    }

    /* 
    Checks if input role is extra or on SceneCard, then compares rank.
    **/
    public boolean validateRole(String name, int rank) {
        Role role = extraMap.get(name);
        if (role == null)
        {
            role = scenes[sceneNum].getRole(name);
            return role != null && role.getRank() <= rank && role.getPlayerID() == -1;
        }
        else
        {
            return role.getRank() <= rank && role.getPlayerID() == -1;
        }
    }
    
    public boolean isExtra(String name)
    {
        Role role = extraMap.get(name); 
        return role != null;
    }

    public Role[] getExtras()
    {
        return extras;
    }

    public Scene getScene()
    {
        return scenes[sceneNum];
    }
}
