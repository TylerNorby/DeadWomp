import java.util.Random;

public class Player {
    String name;
    String location;
    String role;
    int rank;
    int money;
    int credits;
    int practiceChips;

    public Player(String name)
    {
        this.name = name;
    }

    /**
     * Prompt player to choose action for current turn.
     */
    public void chooseAction()
    {

    }
    /**
     * Returns random number from 1-6.
     * @return
     */
    private int rollDie()
    {
        return new Random().nextInt(1,7);
    }

    private boolean move()
    {
        return true;
    }

    private boolean act()
    {
        return true;
    }

    private boolean rehearse()
    {
        return true;
    }

    private boolean upgrade(int rank, boolean useCredits)
    {
        return true;
    }

}
