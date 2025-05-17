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
    ValidationManager validation;
    iView view;

    public GameManager() {
        gameBoard = new GameBoard();
        validation = new ValidationManager(gameBoard);
        view = new TextView(); //only textview for now

        int playerCount = view.inputPlayerCount();

        players = new Player[playerCount];
        String[] playerNames = view.inputNames(playerCount);

        //set up according to amount of players (4 as default)
        int startRank = 1;
        int startCredits = 0;
        day = 0;
        totalDays = 4;

        switch (playerCount) {
            case 2:
            case 3:
                totalDays = 3;
                break;
            case 4:
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
     * Reset shot counters, move players to trailer, redistribute scene cards
     */
    public void setupDay() 
    {
        gameBoard.nextDay();
    }

    /**
     * Main game loop
     */
    public void playGame()
    {
        while (day < totalDays)
        {
            setupDay();
            for(Player player : players)
            {
                ArrayList<Action> possibleActions = validation.getPossibleActions(player); 
                takeTurn(player, possibleActions);
            }
        }
    }

    private void takeTurn(Player player, ArrayList<Action> possibleActions) 
    {
        view.displayBoard(gameBoard, player, players);
        Action playerAction = view.inputAction(possibleActions);
        String[] validLocations;
        switch(playerAction)
        {
            case Move:
                validLocations = gameBoard.getLocation(player.getLocation()).getConnections();
                String destination = view.inputLocation(validLocations);
                if (destination == null)
                {
                    takeTurn(player, possibleActions);
                }
                else
                {
                    player.setLocation(destination);
                }
                possibleActions = validation.getPossibleActions(player);
                possibleActions.remove(Action.Move);
                takeTurn(player, possibleActions);
                break;
            case Act:
                validation.validateAct(player, player.rollDice());
                //display outcome, reward
                break;
            case Rehearse:
                //display outcome, reward
                break;
            case Upgrade:
                //display upgrade list, get input
                break;
            case TakeRole:
                break;
            case View:
                Location[] locations = gameBoard.getLocations();
                validLocations = new String[locations.length];
                for (int i = 0; i < validLocations.length; ++i)
                {
                    validLocations[i] = locations[i].getName();
                }
                String location = view.inputLocation(validLocations);
                if (location != null)
                {
                    view.displayLocation(gameBoard, location);
                }
                takeTurn(player, possibleActions);
                break;
            case Nothing:
                break;
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