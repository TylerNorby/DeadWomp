public class Location {
    String name;
    String[] connections;

    public Location(String name, String[] connections)
    {
        this.name = name;
        this.connections = connections;
    }
    public String[] getConnections()
    {
        return connections;
    }
    public boolean isConnected(String location)
    {
        int i = 0;
        while (i < connections.length && connections[i] != location)
        {
            ++i;
        }
        return connections[i] == location;
    }
    public String getName()
    {
        return name;
    }
    
}
