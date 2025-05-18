import java.util.ArrayList;
import java.util.List;

/**
 * Handles the broad gameplay elements of the game of Deadwood, including
 * managing turns, players, and validating player actions.
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
    Bank bank;

    public GameManager() {
        gameBoard = new GameBoard();
        validation = new ValidationManager(gameBoard);
        view = new TextView(); //only textview for now
        bank = new Bank();

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
    public void setupDay() {
        gameBoard.nextDay();
    }

    /**
     * Main game loop
     */
    public void playGame() {
        while (day < totalDays) {
            setupDay();
            while (gameBoard.getScenes() > 0) {
                for (Player player : players) {
                    ArrayList<Action> possibleActions = validation.getPossibleActions(player);
                    takeTurn(player, possibleActions);
                }
            }
        }
    }
    
    /**
     * Iterate through players, if same location, then check if on/off card, add to corresponding array, call bank payout method
     * @param setLocation
     */
    private void scenePayout (String location)
    {
        ArrayList<Player> onCardPlayers = new ArrayList<Player>();
        ArrayList<Player> extraPlayers = new ArrayList<Player>();
        MovieSet currentSet = (MovieSet) gameBoard.getLocation(location);
        
        for (Player player : players)
        {
            if (player.getLocation() == location)
            {
                boolean onCard = currentSet.getRole(player.getRole()).onCard();
                if (onCard)
                {
                    onCardPlayers.add(player);
                }
                else
                {
                    extraPlayers.add(player);
                }
            }
        }
        bank.sceneWrapPayouts(currentSet.getCard().getBudget(), onCardPlayers, extraPlayers);
    }

    private void takeTurn(Player player, ArrayList<Action> possibleActions) {
        view.displayBoard(gameBoard, player, players);
        Action playerAction = view.inputAction(possibleActions);
        String[] validLocations;
        Location currentLocation = gameBoard.getLocation(player.getLocation());
        switch (playerAction) {
            case Move:
                validLocations = gameBoard.getLocation(player.getLocation()).getConnections();
                String destination = view.inputLocation(validLocations);
                if (destination == null) {
                    takeTurn(player, possibleActions);
                } else {
                    player.setLocation(destination);
                    view.displayLocation(gameBoard, destination);
                }
                possibleActions = validation.getPossibleActions(player);
                possibleActions.remove(Action.Move);
                takeTurn(player, possibleActions);
                break;
            case Act:
                currentLocation = gameBoard.getLocation(player.getLocation());
                MovieSet currentMovieSet = (MovieSet) currentLocation;
                int rollAmount = player.rollDice();
                Part playerRole = currentMovieSet.getRole(player.getRole());
                boolean success = validation.validateAct(player);

                if (success)
                {
                    currentMovieSet.removeShot();
                    bank.turnPayout(player, validation.validateAct(player), playerRole.onCard());

                    if (currentMovieSet.getShotCounter() == 0)
                    {
                        scenePayout(player.getLocation());
                    }
                }
                bank.turnPayout(player, success, playerRole.onCard()); 
                break;

            case Rehearse:
                int currentChips = player.getChips();
                if (currentChips < 6) {
                    player.setChips(currentChips + 1);
                }
                break;
            case Upgrade:
                //display upgrade list, get input
                break;
            case TakeRole:
                if (currentLocation instanceof MovieSet) {
                    currentMovieSet = (MovieSet) currentLocation;
                    List<Part> availableRoles = new ArrayList<>();

                    // Add available on-card roles
                    for (Part role : currentMovieSet.getCard().getRoles()) {
                        if (!role.inUse() && player.getRank() >= role.getRank()) {
                            availableRoles.add(role);
                        }
                    }

                    // Add available extra roles
                    for (Part extra : currentMovieSet.getExtras()) {
                        if (!extra.inUse() && player.getRank() >= extra.getRank()) {
                            availableRoles.add(extra);
                        }
                    }

                    view.displayAvailableRoles(availableRoles);
                    String chosenRoleName = view.inputRoleChoice(availableRoles);

                    if (chosenRoleName != null) {
                        // Find the chosen role object
                        Part chosenRole = null;
                        for (Part role : availableRoles) {
                            if (role.getName().equals(chosenRoleName)) {
                                chosenRole = role;
                                break;
                            }
                        }

                        if (chosenRole != null && validation.validateTakeRole(chosenRoleName, player)) {
                            player.setRole(chosenRoleName);
                            chosenRole.inUse = true; // Assuming 'inUse' is public or has a setter
                            System.out.println(player.getName() + " successfully took the role of " + chosenRoleName);
                        } else {
                            System.out.println("Invalid role choice or you do not meet the requirements for this role.");
                            // Re-display possible actions and take turn if role selection failed
                            takeTurn(player, possibleActions);
                        }
                    } else {
                        // Player cancelled role selection, return to action selection
                        takeTurn(player, possibleActions);
                    }
                }
                break;

            case View:
                Location[] locations = gameBoard.getLocations();
                validLocations = new String[locations.length];
                for (int i = 0; i < validLocations.length; ++i) {
                    validLocations[i] = locations[i].getName();
                }
                String location = view.inputLocation(validLocations);
                if (location != null) {
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
