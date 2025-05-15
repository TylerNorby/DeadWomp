import java.util.ArrayList;
import java.util.HashMap;

public class Card{
    String name;
    int sceneNum;
    int budget;
    String description;

    ArrayList<Part> roleList;
    HashMap<String, Part> roleMap;

    public Card(String name, int sceneNum, int budget, String description, Part[] roles)
    {
        this.name = name;
        this.sceneNum = sceneNum;
        this.budget = budget;
        this.description = description;

        for (Part role: roles)
        {
            this.roleMap.put(role.getName(), role);
        }
    }

    public String getName() 
    {
        return name;
    }

    public String getDescription() 
    {
        return description;
    }

    public int getBudget() 
    {
        return budget;
    }

    public ArrayList<Part> getRoles()
    {
        return roleList;
    }

    public Part getRole(String role){
        return roleMap.get(role);
    }
}
