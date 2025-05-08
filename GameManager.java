import java.util.ArrayList;


/**
 * Handles the broad gameplay elements of the game of Deadwood, including managing turns, players, and validating player actions.
 *
 * @author Ashley Spassov, Tyler Norby
 * @version 1.0
 */
class GameManager {

    Player[] players;
    int[] playerScores;
    int day;
    int totalDays;
    GameBoard gameBoard;
    iView view;

    public GameManager() {
        gameBoard = new GameBoard();
        view = new TextView(gameBoard); //only textview for now

        int playerCount = view.inputPlayerCount();

        players = new Player[playerCount];
        String[] playerNames = view.inputNames(playerCount);

        //set up according to amount of players (4 as default)
        int startRank = 1;
        int startCredits = 0;

        switch (playerCount) {
            case 2:
            case 3:
                totalDays = 3;
                break;
            case 5:
                startCredits = 2;
                break;
            case 6:
                startCredits = 2;
                break;
            case 7:
            case 8:
                startRank = 2;
                break;
        }

        for (int i = 0; i < playerNames.length; ++i) {
            players[i] = new Player(playerNames[i], startRank, startCredits);
        }

    }

    /**
     * * Returns true if player meets criteria for taking a role, i.e. in same*
     * location, sufficient rank, role not already taken.** @param role* @param
     * location* @param onCard* @param rank* @return
     */
    public boolean validateTakeRole(String role, Player player) {

        Location location = gameBoard.getLocation(player.getLocation());
        boolean roleValidation = false;

        // If player location is not on a movieSet, will skip over and return false
        if (location instanceof MovieSet) {
            MovieSet movieSet = (MovieSet) location;
            // ensures that desired roll is either on movieSet as extra, or on SceneCard. Also compares player rank w/ that roll.
            roleValidation = (movieSet.validateRole(role, player.getRank()));
        }
        return roleValidation;

    }

    /**
     * Validates whether player can upgrade to given rank using credits or
     * dollars
     *
     * @param player
     * @param rank
     * @return
     */
    public boolean validateUpgrade(Player player, int desiredRank, boolean useCredits) {
        Location location = gameBoard.getLocation(player.getLocation());

        if (location instanceof CastingOffice) {
            if (useCredits) {
                return (player.getCredits() >= CastingOffice.getCreditCost(desiredRank) && CastingOffice.getCreditCost(desiredRank) != -1);
            } else {
                return (player.getCredits() >= CastingOffice.getMoneyCost(desiredRank) && CastingOffice.getMoneyCost(desiredRank) != -1);
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Validates whether player can act, then sends info to bank for payouts
     *
     * @param player
     * @param rollAmount
     * @return
     */
    public boolean validateAct(Player player, int rollAmount) {
            // this will always be a movieSet if everything is implemented correctly
            MovieSet set = (MovieSet) gameBoard.getLocation(player.getLocation());

            if (set.actingSuccess(rollAmount)) {

            }
        return false;
    }

    /**
     * Handles move option 
     * @param player
     * @param start
     * @param dest
     */
    private void Move(Player player)
    {
        Location[] locations = gameBoard.getLocation(player.getLocation()).getConnections();
        String[] validLocations = new String[locations.length];

        for (int i = 0; i < locations.length; ++i)
        {
            validLocations[i] = locations[i].getName();
        }

        String destination = view.inputLocation(validLocations);
        if (destination != null)
        {
            player.setLocation(destination);
        }
    }

    /**
     * Get list of possible actions given player can take
     * @param player
     * @return
     */
    private ArrayList<Action> getPossibleActions(Player player)
    {
        ArrayList<Action> possibleActions = new ArrayList<Action>();
        if(player.role == null)
        {
            possibleActions.add(Action.Move);
            if(gameBoard.getLocation(player.getLocation()) instanceof MovieSet)
            {
                possibleActions.add(Action.TakeRole);
            }
        }
        else
        {
            possibleActions.add(Action.Act);
            if (player.getChips() < 6)
            {
                possibleActions.add(Action.Rehearse);
            }
            if (player.getLocation() == "CastingOffice")
            {
                possibleActions.add(Action.Upgrade);
            }
        }
        return possibleActions;
    }
    /**
     * Reset shot counters, move players to trailer, redistribute scene cards
     */
    public void setupDay() 
    {
        //call method in GameBoard
    }

    /**
     * Main game loop
     */
    public void playGame()
    {
        while (day < totalDays)
        {
            for(Player player : players)
            {
                ArrayList<Action> possibleActions = getPossibleActions(player); 
                Action playerAction = view.inputAction(possibleActions);
                //call view to display board with active player highlighted
                switch(playerAction)
                {
                    case Move:
                        Move(player);
                        possibleActions = getPossibleActions(player);
                        possibleActions.remove(Action.Move);

                        break;
                    case Act:
                        validateAct(player, player.rollDice());
                        //display outcome, reward
                        break;
                    case Rehearse:
                        //display outcome, reward
                        break;
                    case Upgrade:
                        //display upgrade list, get input
                        break;
                    case TakeRole:
                        //display available roles (if any), get input
                        break;
                }
            }

            setupDay();
        }
    }

    /**
     * Calculate player score and send to view interface to display
     *
     * @return
     */
    public void calculateScore() {
        int[] playerScores = new int[players.length];
        for (int i = 0; i < players.length; i++) {
            playerScores[i] = players[i].getMoney() + players[i].getCredits() + (players[i].getRank() * 5);
        }
        //pass playerscores off to view
    }

    public static void main(String args[]) {
        GameManager manager = new GameManager();
        manager.playGame();
    }
}