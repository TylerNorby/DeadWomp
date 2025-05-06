class GameManager {
    Player[] players;
    int day;
    int totalDays;
    iView view;


    public GameManager()
    {
        view = new TextView(new GameBoard()); //only textview for now

        int playerCount = view.inputPlayerCount();

        players = new Player[playerCount];
        String[] playerNames = view.inputNames(playerCount);

        for (int i = 0; i < playerNames.length; ++i)
        {
            players[i] = new Player(playerNames[i], this);
        }

        switch(playerCount)
        {
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
            case 8: 
                break;
        }

        //set up according to playerCount
    }

    /**
     * Returns whether start location is connected to destination using LocationManager class
     * @param start
     * @param dest
     * @return
     */
    public boolean validateMove(String start, String dest)
    {
        return true;
    }

    /**
     * Validates whether player meets criteria for taking a role, i.e. in same location, sufficient rank, role not already taken.
     * @param role
     * @param location
     * @param onCard
     * @param rank
     * @return
     */
    public boolean validateTakeRole(String role, String location, boolean onCard, int rank)
    {
        return true;
    }

    /**
     * Validates whether player can upgrade to given rank using credits or dollars
     * @param player
     * @param rank
     * @return
     */
    public boolean validateUpgrade(Player player, int rank, boolean useCredits)
    {
        return true;
    }

    /**
     * Validates whether player can act, then sends info to bank for payouts 
     * @param player
     * @param rollAmount
     * @return
     */
    public boolean validateAct(Player player, int rollAmount)
    {
        return true;
    }

    /**
     * Validates whether player can rehearse
     * @param player
     * @return
     */
    public boolean validateRehearse()
    {
        return true;
    }

    /**
     * Reset shot counters, move players to trailer, redistribute scene cards
     */
    public void dayWrap()
    {
    }

    /**
     * Calculate player score and send to view interface to display 
     * @return
     */
    public void calculateScore()
    {
    }

    public static void main(String args[])
    {
        GameManager manager = new GameManager();
    }
}
