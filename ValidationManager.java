
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
    public boolean validateAct(String location, int diceRoll)
    {
        return ((MovieSet) gameBoard.getLocation(location)).actingSuccess(diceRoll);
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
            CastingOffice castingOffice = (CastingOffice) location;
            if (useCredits) {
                return (player.getCredits() >= castingOffice.getCreditCost(desiredRank) && castingOffice.getCreditCost(desiredRank) != -1);
            } else {
                return (player.getMoney() >= castingOffice.getMoneyCost(desiredRank) && castingOffice.getMoneyCost(desiredRank) != -1);
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
        Location location = gameBoard.getLocation(player.getLocation());
        possibleActions.add(Action.View);
        possibleActions.add(Action.Nothing);
        if (player.role == null) 
        {
            possibleActions.add(Action.Move);
            if (location instanceof MovieSet && ((MovieSet) location).getCard() != null)
            {
                possibleActions.add(Action.TakeRole);
            }
            if (player.getLocation().equals("office"))
            {
                possibleActions.add(Action.Upgrade);
            }
        }
        else if (location instanceof MovieSet && ((MovieSet) location).getCard() != null) 
        {
            possibleActions.add(Action.Act);
            if (player.getChips() < 6)
            {
                possibleActions.add(Action.Rehearse);
            }
        }
        return possibleActions;
    }
}
