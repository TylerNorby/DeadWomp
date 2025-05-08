import java.util.ArrayList;
import java.util.HashMap;

public class Scene {
    String name;
    String description;
    int budget;
    int shots;
    int totalShots;

    ArrayList<Role> roleList;
    HashMap<String, Role> roleMap;

    // Class Completed

    public Scene(String name, String description, int shots, Role[] roles, int budget)
    {
        this.name = name;
        this.description = description;
        this.totalShots = shots;
        this.shots = this.totalShots;
        this.budget = budget;

        for (Role role: roles)
        {
            this.roleMap.put(role.getName(), role);
        }
    }

    public int advanceScene()
    {
        --shots;
        return shots;
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

    public int getShots() 
    {
        return shots;
    }

    public int getTotalShots()
    {
        return totalShots;
    }

    public ArrayList<Role> getRoles()
    {
        return roleList;
    }

    public Role getRole(String role){
        return roleMap.get(role);
    }
}
