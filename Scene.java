public class Scene {
    String name;
    String description;
    int budget;
    int shots;
    Role[] roles;

    public Scene(String name, String description, int shots, Role[] roles, int budget)
    {
        this.name = name;
        this.description = description;
        this.shots = shots;
        this.roles = roles;
        this.budget = budget;
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
}
