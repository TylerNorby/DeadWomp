class GameManager {

    Player[] players;
    int[] playerScores;
    int day;
    int totalDays;
    GameBoard gameBoard; //Add this to the UML pls
    iView view;

    public GameManager() {
        view = new TextView(new GameBoard()); //only textview for now

        int playerCount = view.inputPlayerCount();

        players = new Player[playerCount];
        playerScores = new int[playerCount];
        String[] playerNames = view.inputNames(playerCount);

        for (int i = 0; i < playerNames.length; ++i) {
            players[i] = new Player(playerNames[i], this);
        }

        switch (playerCount) {
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
     * Returns whether start location is connected to destination using and
     * player does not have role
     *
     * @param start
     * @param dest
     * @return
     */
    public boolean validateMove(Player player, String dest) {
        return gameBoard.validateConnection(player.location, dest) && (player.role == null);

    }

    /**
     * * Returns true if player meets criteria for taking a role, i.e. in same*
     * location, sufficient rank, role not already taken.** @param role* @param
     * location* @param onCard* @param rank* @return
     */
    public boolean validateTakeRole(String role, Player player) {

        Location loca = gameBoard.getLocation(player.getLocation());
        boolean roleValidation = false;

        // If player location is not on a movieSet, will skip over and return false
        if (loca instanceof MovieSet) {
            MovieSet movieSet = (MovieSet) loca;
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
        Location loca = gameBoard.getLocation(player.getLocation());
        // If player location is not on a casting office, skips over and returns false
        if (loca instanceof CastingOffice) {
            if (useCredits) {
                return (player.getCredits() >= CastingOffice.getCreditCost(desiredRank) && CastingOffice.getCreditCost(desiredRank) != -1);
            } else {
                return (player.getCredits() >= CastingOffice.getMoneyCost(desiredRank) && CastingOffice.getMoneyCost(desiredRank) != -1);
            }
        }
        return false;
    }

    /**
     * Validates whether player can act, then sends info to bank for payouts
     *
     * @param player
     * @param rollAmount
     * @return
     */
    public boolean validateAct(Player player, int rollAmount) {
        if (player.role != null) {
            // this will always be a movieSet if everything is implemented correctly
            MovieSet loca = (MovieSet) gameBoard.getLocation(player.getLocation());
            if (loca.actingSuccess(rollAmount)) {

            }
        }
        return false;
    }

    /**
     * Validates whether player can rehearse
     *
     * @param player
     * @return
     */
    public boolean validateRehearse(Player player) {
        if (player.getRole() == null || player.getRole().isEmpty()) {
            return false;
        }
        String playerLocationName = player.getLocation();
        Location currentLocation = gameBoard.getLocation(playerLocationName);
        if (currentLocation == null) {
            return false;
        }

        if (!(currentLocation instanceof MovieSet)) {
            return false;
        }

        MovieSet currentMovieSet = (MovieSet) currentLocation;

        Scene currentScene = currentMovieSet.scene;

        if (currentScene == null || currentScene.getShots() <= 0) {
            return false;
        }
        int playerRank = player.getRank();
        int rehearsalMarkers = player.practiceChips;
        int sceneBudget = currentScene.getBudget();

        if (playerRank + rehearsalMarkers >= sceneBudget) {
            return false;
        }

        return true;
    }

    /**
     * Reset shot counters, move players to trailer, redistribute scene cards
     */
    public void setupDay() {
    }

    /**
     * Calculate player score and send to view interface to display
     *
     * @return
     */
    public void calculateScore() {
        for (int i = 0; i < players.length; i++) {
            playerScores[i] = players[i].getMoney() + players[i].getCredits() + (players[i].getRank() * 5);
        }
    }

    public static void main(String args[]) {
        GameManager manager = new GameManager();
    }
}
