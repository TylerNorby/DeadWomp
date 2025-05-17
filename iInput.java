import java.util.ArrayList;
import java.util.List;
public interface iInput 
{
    public int inputPlayerCount();
    public String[] inputNames(int playerCount);
    public Action inputAction(ArrayList<Action> validActions); 
    public String inputLocation(String[] validLocations);
    public String inputRoleChoice(List<Part> availableRoles);
}