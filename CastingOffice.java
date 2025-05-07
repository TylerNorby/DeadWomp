
   // Class Completed
   
    public class CastingOffice extends Location {

        private static final int[] RANK_COSTS_MONEY = {0,4,10,18,28,40}; 
        private static final int[] RANK_COSTS_CREDITS = {0, 5, 10, 15, 20, 25}; 
    
        public CastingOffice(String name) {
            super(name);
        }
    
        public static int getMoneyCost(int rank) {
            if (rank >= 2 && rank <= 6) {
                return RANK_COSTS_MONEY[rank - 1]; // Adjust for 0-based index
            }
            return -1; 
        }
    
        public static int getCreditCost(int rank) {
            if (rank >= 2 && rank <= 6) {
                return RANK_COSTS_CREDITS[rank - 1]; // Adjust for 0-based index
            }
            return -1; 
        }
    
    }
