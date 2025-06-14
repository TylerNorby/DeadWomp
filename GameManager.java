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

        view = new GraphicView(); 
        bank = new Bank(view);

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
    private void setupDay() {
        ++day;
        gameBoard.setupDay();
        for (Player player : players)
        {
            player.setLocation("trailer");
        }
    }

    private void sceneWrap(MovieSet movieSet)
    {
        scenePayout(movieSet.getName());
        movieSet.setCard(null); 
        gameBoard.removeScene();
        for (Player player : players)
        {
            if (player.getLocation().equals(movieSet.getName()))
            {
                player.setRole(null);
                player.setChips(0);
            }
        }
    }

    /**
     * Main game loop
     */
    public void playGame() {
        while (day <= totalDays) {
            setupDay();
            int playerNum = 0;
            int playerCount = players.length;

            while (gameBoard.getScenes() > 0) {
                if (playerNum == playerCount) {
                    playerNum = 0;
                }
                Player player = players[playerNum];
                ArrayList<Action> possibleActions = validation.getPossibleActions(player);
                takeTurn(player, possibleActions);
                ++playerNum;
            }
        }
        calculateScore();
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
            if (player.getLocation().equals(location))
            {
                Part playerPart = currentSet.getRole(player.getRole());
                if (playerPart != null)
                {
                    boolean onCard = playerPart.onCard();
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
        }
        bank.sceneWrapPayouts(currentSet.getCard().getBudget(), onCardPlayers, extraPlayers);
    }

    private void takeTurn(Player player, ArrayList<Action> possibleActions) {
        view.displayBoard(day, totalDays, gameBoard, player, players);
        Action playerAction = view.inputAction(possibleActions);
        String[] validLocations;
        Location currentLocation = gameBoard.getLocation(player.getLocation());
        MovieSet currentMovieSet;

        switch (playerAction) {
            case Move:
                validLocations = gameBoard.getLocation(player.getLocation()).getConnections();
                String destination = view.inputLocation(validLocations);
                if (destination == null) {
                    takeTurn(player, possibleActions);
                } else {
                    player.setLocation(destination);
                    gameBoard.flip(destination);
                    view.displayLocation(gameBoard, destination);
                }
                possibleActions = validation.getPossibleActions(player);
                possibleActions.remove(Action.Move);
                takeTurn(player, possibleActions);
                break;

            case Act:
                currentMovieSet = (MovieSet) gameBoard.getLocation(player.getLocation());
                Part playerRole = currentMovieSet.getRole(player.getRole());
                int roll = player.rollDice();
                boolean success = validation.validateAct(player.getLocation(), roll + player.getChips());
                boolean onCard = playerRole.onCard();

                if (success)
                {
                    currentMovieSet.removeShot();

                    if (currentMovieSet.getShotCounter() == 0)
                    {
                        sceneWrap(currentMovieSet);
                    }
                }
                bank.turnPayout(player, roll, success, onCard);
                break;

            case Rehearse:
                int currentChips = player.getChips();
                if (currentChips < 6) {
                    player.setChips(currentChips + 1);
                }
                break;

            case Upgrade:
                CastingOffice office = (CastingOffice) currentLocation; 
                int[] creditCost = office.getCreditCost();
                int[] moneyCost = office.getMoneyCost();
                boolean[][] availableRanks = bank.getAvailableRanks(player, moneyCost, creditCost);
                int rank[] = view.inputUpgrade(availableRanks); //array of payment type and rank to change to. payment type of "0" means no change in rank

                if (rank[0] == 1)
                {
                    player.removeMoney(office.getMoneyCost(rank[1]));
                    player.setRank(rank[1]);
                }
                else if (rank[0] == 2)
                {
                    player.removeCredits(office.getCreditCost(rank[1]));
                    player.setRank(rank[1]);
                }
                else
                {
                    takeTurn(player, possibleActions);
                }
                break;

            case TakeRole:
                currentMovieSet = (MovieSet) currentLocation;
                List<Part> availableRoles = new ArrayList<>();
                List<Part> unavailableRoles = new ArrayList<>();

                // Add available on-card roles
                for (Part role : currentMovieSet.getCard().getRoles()) {
                    if (!role.inUse() && player.getRank() >= role.getRank()) {
                        availableRoles.add(role);
                    }
                    else
                    {
                        unavailableRoles.add(role);
                    }
                }
                // Add available extra roles
                for (Part extra : currentMovieSet.getExtras()) {
                    if (!extra.inUse() && player.getRank() >= extra.getRank()) {
                        availableRoles.add(extra);
                    }
                    else
                    {
                        unavailableRoles.add(extra);
                    }
                }

                view.displayRoles(availableRoles, unavailableRoles);
                String chosenRoleName = view.inputRoleChoice(availableRoles); //view class and availableRoles ensure only valid options may be returned

                if (chosenRoleName == "q")
                {
                    takeTurn (player, possibleActions); 
                }
                else 
                {
                    player.setRole(chosenRoleName);
                    currentMovieSet.getRole(chosenRoleName).take(); 
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
        view.displayScores(players, playerScores);
    }

    public static void main(String args[]) {
        GameManager manager = new GameManager();
        manager.playGame();
    }
}
