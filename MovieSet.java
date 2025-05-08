import java.util.ArrayList;
import java.util.HashMap;

public class MovieSet extends Location {

    Scene scene;
    ArrayList<Role> extras;
    HashMap<String, Role> extraMap;

    public MovieSet(String name, Scene scene, ArrayList<Role> extras) {
        super(name);
        this.extras = extras;
        this.scene = scene;
    }

   
    public boolean actingSuccess(int playerRoll){
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
