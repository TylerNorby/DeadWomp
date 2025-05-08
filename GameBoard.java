
import java.util.HashMap;

public class GameBoard {

    private HashMap<String, Location> locations;

    // Whenever we add castingOffice to this location array we need to make sure its of Casting office type
    public GameBoard() {
        locations = new HashMap<String, Location>();
    }

    public Location getLocation(String location) {
        return null;
    }

    public boolean validateConnection(String start, String dest) {
        return true;
    }

    public void setScene(String location, Scene scene) {

    }

}
