
import java.util.ArrayList;

public class GameBoard {

    private ArrayList<Location> locations;

    // Whenever we add castingOffice to this location array we need to make sure its of Casting office type
    public GameBoard() {
        locations = new ArrayList<Location>();
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
