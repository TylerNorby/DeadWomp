public class Part {
    String name;
    String description;
    int rank;
    Boolean inUse;
    Boolean onCard;

    /**
     * Object representing a part on the board 
     * @param name
     * @param description
     * @param rank
     * @param onCard
     */
    public Part(String name, String description, int rank, Boolean onCard)
    {
        this.name = name;
        this.description = description;
        this.rank = rank;
        this.onCard = onCard;
        inUse = false; 
}

    String getName()
    {
        return name;
    }
    String getDescription()
    {
        return description;
    }
    int getRank()
    {
        return rank;
    }
    boolean inUse()
    {
        return inUse;
    }
    Boolean onCard()
    {
        return onCard;
    }
}
