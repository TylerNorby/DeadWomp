public class Location {
    String name;
    Location[] connections;

    public Location(String name, Location[] locations)
    {
        this.name = name;
    }
    public Location[] getConnections()
    {
        return connections;
    }
    public boolean isConnected(String location)
    {
        int i = 0;
        while (i < connections.length && connections[i].getName() != location)
        {
            ++i;
        }
        return connections[i].getName() == location;
    }
    public String getName()
    {
        return name;
    }
    
}
