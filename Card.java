import java.util.HashMap;

public class Card{
    String name;
    int sceneNum;
    int budget;
    String description;
    Part[] roles;
    boolean flipped;

    HashMap<String, Part> roleMap;

    public Card(String name, int sceneNum, int budget, String description, Part[] roles)
    {
        this.name = name;
        this.sceneNum = sceneNum;
        this.budget = budget;
        this.description = description;
        this.roles = roles;
        flipped = false;
        roleMap = new HashMap<String, Part>();

        for (Part role: roles)
        {
            this.roleMap.put(role.getName(), role);
        }
    }

    public String getName() 
    {
        return name;
    }
    public int getSceneNum()
    {
        return sceneNum;
    }
    public String getDescription() 
    {
        return description;
    }
    public int getBudget() 
    {
        return budget;
    }
    public Part[] getRoles()
    {
        return roles;
    }
    public Part getRole(String role){
        return roleMap.get(role);
    }

    public boolean isFlipped()
    {
        return flipped;
    }

    public void flip()
    {
        flipped = true;
    }
}
