public class Part {
    String name;
    String description;
    int rank;
    int playerID;
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
        playerID = -1; //index into player array, -1 is empty
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
    int getPlayerID()
    {
        return playerID;
    }
    Boolean onCard()
    {
        return onCard;
    }
}
