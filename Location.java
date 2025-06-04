public class Location {
    String name;
    String[] connections;
    int[] area;

    public Location(String name, int[] area, String[] connections)
    {
        this.name = name;
        this.connections = connections;
        this.area = area;
    }
    public int[] getArea()
    {
        return area;
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
