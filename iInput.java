import java.util.ArrayList;
public interface iInput 
{
    public int inputPlayerCount();
    public String[] inputNames(int playerCount);
    public Action inputAction(ArrayList<Action> validActions); 
}