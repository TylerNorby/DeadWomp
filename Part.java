public class Part {
    String name;
    String line;
    int rank;
    int[] position;
    int[] area;
    Boolean inUse;
    Boolean onCard;

    /**
     * Object representing a part on the board 
     * @param name
     * @param description
     * @param rank
     * @param onCard
     */
    public Part(String name, int[] area, String line, int rank, Boolean onCard)
    {
        this.name = name;
        this.line = line;
        this.rank = rank;
        this.onCard = onCard;
        this.area = area;
        inUse = false; 
    }

    public String getName()
    {
        return name;
    }
    public int[] getArea()
    {
        return area;
    }
    public String getLine()
    {
        return line;
    }
    public int getRank()
    {
        return rank;
    }
    public boolean inUse()
    {
        return inUse;
    }
    public void take()
    {
        inUse = true;
    }
    public void free()
    {
        inUse = false;
    }
    public boolean onCard()
    {
        return onCard;
    }
}
