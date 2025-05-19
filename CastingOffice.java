
/**
 * This static class contains the upgrade information related to the Casting office location in the game of deadwood. It also returns the
 * cost for a desired rank
 *
 * @author Ashley Spassov, Tyler Norby
 * @version 1.0
 */
public class CastingOffice extends Location {

    private int moneyCost[];
    private int creditCost[];

    public CastingOffice(String name, String[] connections, int[] moneyCost, int[] creditCost) {
        super(name, connections);
        this.moneyCost = moneyCost;
        this.creditCost = creditCost;
    }

    public int[] getMoneyCost()
    {
        return moneyCost;
    }
    
    public int[] getCreditCost()
    {
        return creditCost;
    }
    /**
     * Manages player upgrades, uses player data to check that upgrade is valid,
     * and then upgrades to desired rank
     *
     * @param rank
     * @return the cost of the desired rank in money
     */
    public int getMoneyCost(int rank) {
        if (rank >= 2 && rank <= 6) {
            return moneyCost[rank - 2]; // Adjust for 0-based index
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
    public int getCreditCost(int rank) {
        if (rank >= 2 && rank <= 6) {
            return creditCost[rank - 2]; // Adjust for 0-based index
        }
        return -1;
    }

}
