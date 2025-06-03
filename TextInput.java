import java.util.ArrayList;
import java.util.List;

public class TextInput implements iInput {
    public int inputPlayerCount() {
        System.out.print("\nEnter number of players (2-8): ");
        String input = System.console().readLine();
        int playerCount;

        try {
            playerCount = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            System.out.println("Input not number, please try again. ");
            playerCount = inputPlayerCount();
        }

        if (playerCount >= 2 && playerCount <= 8) {
            return playerCount;
        } else {
            System.out.println("Invalid number of players, please try again. ");
            return inputPlayerCount();
        }
    }

    public String[] inputNames(int playerCount) {
        String[] names = new String[playerCount];

        for (int i = 0; i < playerCount; ++i) {
            System.out.println("Enter player " + (i + 1) + "'s name: ");
            names[i] = System.console().readLine();
        }
        System.out.println();
        return names;
    }

    public Action inputAction(ArrayList<Action> validActions) {
        System.out.println("\nAvailable actions: ");

        for (int i = 0; i < validActions.size(); ++i) {
            Action action = validActions.get(i);
            if (action == Action.TakeRole)
            {
                System.out.println((i + 1) + ": Take Role"); 
            }
            else
            {
                System.out.println((i + 1) + ": " + validActions.get(i));
            }
        }
        System.out.print("\nPlease select an action (Press enter to skip): ");

        String input = System.console().readLine();
        try {
            int actionID = Integer.valueOf(input) - 1;
            if (actionID < 0 || actionID >= validActions.size())
            {
                System.out.println("\nInvalid input: Out of range.\n");
                return inputAction(validActions);
            }
            return validActions.get(actionID);
        } catch (NumberFormatException e) {
            if (input.trim().equals(""))
            {
                return Action.Nothing;
            }
            System.out.print("\nInvalid input: Not a number.\n");
            return inputAction(validActions);
        }
    }
    
    /**
     * Display valid locations, validate & return input
     *
     * @param validLocations
     * @return
     */
    public String inputLocation(String[] validLocations) {
        System.out.println("\nSelect a location (\"q\" to cancel): ");
        for (int i = 0; i < validLocations.length; ++i)
        {
            System.out.println((i + 1) + ": " + validLocations[i].substring(0,1).toUpperCase() + validLocations[i].substring(1,validLocations[i].length()));
        }
        System.out.print("Input: ");
        String input = System.console().readLine();
        try 
        {
           int locationNum = Integer.parseInt(input);
           --locationNum;
           if (locationNum < 0 || locationNum >= validLocations.length)
           {
                System.out.println("\nInvalid input: Out of range.");
                return inputLocation(validLocations);
           }
           else
           {
                return validLocations[locationNum];
           }

        }
        catch (NumberFormatException e)
        {
            if (input.equals("q"))
            {
                return null;
            }
            System.out.println("\nInvalid input: Not a number.");
            return inputLocation(validLocations);
        }

    }

    public String inputRoleChoice(List<Part> availableRoles) { 
        if (availableRoles == null || availableRoles.isEmpty()) {
            System.out.println("\nNo roles available for your rank. (Press Enter to continue):\n");
            System.console().readLine();
            return "q"; 
        }

        int choice = -1;
        while (true) {
            System.out.print("Enter the number of the role you want to take (q to cancel): "); 
            String input = System.console().readLine();

            if (input == null) { 
                System.out.println("Error reading input.");
                return null; 
            }
            else if (input.trim().equals("q"))
            {
                return "q";
            }

            try {
                choice = Integer.parseInt(input.trim()); 
                if (choice >= 1 && choice <= availableRoles.size()) {
                    return availableRoles.get(choice - 1).getName();
                } else {
                    System.out.println("Invalid role number. Please enter a number from the list.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Get array of payment type and destination rank as return value. 0 as payment type means no change in rank.
     */
    public int[] inputUpgrade(boolean[][] availableRanks)
    {
        System.out.println("\nAvailable Ranks:");
        for (int i = 0; i < availableRanks.length; ++i) //Print available ranks, and valid purchase method
        {
            System.out.print("    " + (i+2) + ": ");
            if (availableRanks[i][0])
            {
                System.out.print("(Money) ");
            }
            if (availableRanks[i][1])
            {
                System.out.print("(Credits)");
            }
            if (!availableRanks[i][0] && !availableRanks[i][1])
            {
                System.out.print("(Unavailable)");
            }
            System.out.println();
        }

        System.out.print("\nEnter rank selection (q to cancel): ");
        String input = System.console().readLine();
        
        try
        {
            int rankNum = Integer.parseInt(input);
            if (rankNum < 2 || rankNum > 6)
            {
                System.out.println("\nInvalid input: Rank out of bounds.");
                return inputUpgrade(availableRanks);
            }
            else
            {
                if (availableRanks[rankNum-2][0] || availableRanks[rankNum-2][1])
                {
                    int[] choice = new int[] {enterPaymentType(new boolean[] {availableRanks[rankNum-2][0], availableRanks[rankNum-2][1]}), rankNum};
                    return choice;
                }
                else
                {
                    System.out.println("\nInvalid input: Rank not available.");
                    return inputUpgrade(availableRanks);
                }
            }
        }
        catch (NumberFormatException e)
        {
            if (input.trim().equals("q"))
            {
                return new int[] {0, 0};
            }
            else
            {
                System.out.println("\nInvalid input: Not number.");
                return inputUpgrade(availableRanks);
            }
        }

    }

    /**
     * Helper method for payment type input for rank input. 0 is no payment, 1 is money, 2 is credits.
     * @param paymentTypes valid payment types
     * @return
     */
    private int enterPaymentType(boolean[] paymentTypes)
    {
        System.out.println("\nPayment types: ");
        if (paymentTypes[0])
        {
            System.out.println("    1: Money");
        }
        if (paymentTypes[1])
        {
            System.out.println("    2: Credits");
        }
        System.out.print("\nEnter payment type (q to cancel): ");
        String input = System.console().readLine();

        try
        {
            int type = Integer.parseInt(input);
            if (type < 1 || type > 2)
            {
                System.out.println("\nInvalid selection: Type out of bounds.");
                return enterPaymentType(paymentTypes);
            }
            else
            {
                if (paymentTypes[type-1])
                {
                    return type;
                }
                else
                {
                    System.out.println("\n Invalid selection: Invalid payment type.");
                    return enterPaymentType(paymentTypes);
                }
            }
       }
        catch (NumberFormatException e)
        {
            if (input.trim().equals("q"))
            {
                return 0;
            }
            System.out.println("\nInvalid selection: Not number.");
            return enterPaymentType(paymentTypes);
        }
    }
}
