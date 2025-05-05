public class Role {
    String name;
    String description;
    int rank;
    Boolean inUse;
    Boolean onCard;

    public Role(String name, String description, int rank, Boolean onCard)
    {
        this.name = name;
        this.description = description;
        this.rank = rank;
        this.onCard = onCard;
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
    Boolean isTaken()
    {
        return inUse;
    }
    Boolean onCard()
    {
        return onCard;
    }
}
