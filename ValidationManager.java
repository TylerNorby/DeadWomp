
import java.util.ArrayList;

class ValidationManager {

    private GameBoard gameBoard;

    public ValidationManager(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
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
     * Roll dice, calculate whether player succeeds, return if successful
     * @param player
     * @return
     */
    public boolean validateAct(Player player)
    {
        return ((MovieSet) gameBoard.getLocation(player.getLocation())).actingSuccess(player.rollDice());
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
        } else {
            return false;
        }
    }

    /**
     * Get list of possible actions given player can take
     *
     * @param player
     * @return
     */
    public ArrayList<Action> getPossibleActions(Player player) {
        ArrayList<Action> possibleActions = new ArrayList<Action>();
        possibleActions.add(Action.View);
        if (player.role == null) {
            possibleActions.add(Action.Move);
            if (gameBoard.getLocation(player.getLocation()) instanceof MovieSet) {
                possibleActions.add(Action.TakeRole);
            }
            if (player.getLocation().equals("office")) {
                possibleActions.add(Action.Upgrade);
            }
        } else {
            possibleActions.add(Action.Act);
            if (player.getChips() < 6) {
                possibleActions.add(Action.Rehearse);
            }
        }
        return possibleActions;
    }
}
