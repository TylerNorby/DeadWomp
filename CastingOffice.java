
/**
 * This static class contains the upgrade information related to the Casting office location in the game of deadwood. It also returns the
 * cost for a desired rank
 *
 * @author Ashley Spassov, Tyler Norby
 * @version 1.0
 */
public class CastingOffice extends Location {

    private static final int[] RANK_COSTS_MONEY = {0, 4, 10, 18, 28, 40};
    private static final int[] RANK_COSTS_CREDITS = {0, 5, 10, 15, 20, 25};

    public CastingOffice(String name) {
        super(name);
    }

    /**
     * Manages player upgrades, uses player data to check that upgrade is valid,
     * and then upgrades to desired rank
     *
     * @param rank
     * @return the cost of the desired rank in money
     */
    public static int getMoneyCost(int rank) {
        if (rank >= 2 && rank <= 6) {
            return RANK_COSTS_MONEY[rank - 1]; // Adjust for 0-based index
        }
        return -1;
    }

    /**
     * Manages player upgrades, uses player data to check that upgrade is valid,
     * and then upgrades to desired rank
     *
     * @param rank
     * @return the cost of the desired rank in credits
     */
    public static int getCreditCost(int rank) {
        if (rank >= 2 && rank <= 6) {
            return RANK_COSTS_CREDITS[rank - 1]; // Adjust for 0-based index
        }
        return -1;
    }

}
