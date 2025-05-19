
import java.util.ArrayList;
import java.util.List;

public class TextView implements iView {
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
     * Display each location, show players in locations, highlight active player
     */
    public void displayBoard(int day, int totalDays, GameBoard gameBoard, Player activePlayer, Player[] players) {
        System.out.println("Day: " + day + "/" + totalDays);
        Location[] locations = gameBoard.getLocations();
        String[] playerLocations = new String[players.length];
        int indent = 40;

        for (int i = 0; i < playerLocations.length; ++i)
        {
            playerLocations[i] = players[i].getLocation();
        }

        for (Location location : locations)
        {
            int lineLen = 0;
            System.out.print(location.getName().substring(0,1).toUpperCase() + location.getName().substring(1,location.getName().length()));
            lineLen += location.getName().length();
            if (location instanceof MovieSet)
            {
                MovieSet movieSet = (MovieSet) location;
                if (movieSet.getCard() == null)
                {
                    String line = "     (Scene finished)";
                    lineLen += line.length();
                    System.out.print(line);
                }
                else
                {
                    String line = " ($" + movieSet.getCard().getBudget() + ",000,000) (" + movieSet.getShotCounter() + "/" + movieSet.getShots() + ")";
                    lineLen += line.length();
                    System.out.print(line);
                }
            }
            System.out.print(": ");
            for (int i = lineLen; i < indent; ++i)
            {
                System.out.print(" ");
            }
            System.out.print(" --->     ");
            String[] neighbors = location.getConnections();

            int i;
            for (i = 0; i < neighbors.length - 1; ++i)
            {
                System.out.print(neighbors[i].substring(0,1).toUpperCase() + neighbors[i].substring(1,neighbors[i].length()) + ", ");
            }
            System.out.print(neighbors[i].substring(0,1).toUpperCase() + neighbors[i].substring(1,neighbors[i].length()) + "\n");

            for (i = 0; i < playerLocations.length; ++i)
            {
                if (playerLocations[i].equals(location.getName()))
                {
                    if (players[i] == activePlayer)
                    {
                        System.out.print("--> [" + players[i].getName() + "]");
                    }
                    else
                    {
                        System.out.print("     " + players[i].getName());
                    }
                    System.out.print(" (" + players[i].getRank() + ")");

                    String role = players[i].getRole();
                    if (role == null)
                    {
                        System.out.print(" (No Role): ");
                    }
                    else
                    {
                        System.out.print(" (Actor: " + role + "): ");
                    }
                    System.out.print("Dollars: " + players[i].getMoney() + ", Credits: " + players[i].getCredits() + ", Practice Chips: " + players[i].getChips() + "\n");
                }
            }
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

    public void displayRoles(List<Part> availableRoles, List<Part> unavailableRoles) {
        System.out.println();
        if (!(availableRoles == null || availableRoles.isEmpty())) { 
            System.out.println("\nAvailable Roles:"); 
            for (int i = 0; i < availableRoles.size(); i++) {
                Part role = availableRoles.get(i);
            
                String type = role.onCard() ? "Part" : "Extra";
                System.out.println("    " + (i + 1) + ". " + role.getName() + " (" + role.getRank() + "): " + type);
                System.out.println("        \"" + role.getLine() + "\"");
            }
        }
        System.out.println("Unavailable Roles:");
        for (int i = 0; i < unavailableRoles.size(); i++) {
            Part role = unavailableRoles.get(i);
        
            String type = role.onCard() ? "Part" : "Extra";
            System.out.println("     " + role.getName() + " (" + role.getRank() + "): " + type);
            System.out.println("        \"" + role.getLine() + "\"");
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
     * Display location info and Scene info (if applicable).
     */
    public void displayLocation(GameBoard gameBoard, String locationName) {
        Location location = gameBoard.getLocation(locationName); 
        System.out.println("\n" + location.getName().substring(0,1).toUpperCase() + location.getName().substring(1,location.getName().length()) + ": ");
        if (location instanceof MovieSet)
        {
            MovieSet movieSet = (MovieSet) location;
            Card card = movieSet.getCard();
            if (card == null)
            {
                System.out.println("    (Scene Finish)");
            }
            else
            {
                System.out.println("    Scene #" + card.getSceneNum() + ": \"" + card.getName() + "\"");
                System.out.println("    Shots: " + movieSet.getShotCounter() + "/" + movieSet.getShots());
                System.out.println("    Budget: $" + card.getBudget() + ",000,000");
                System.out.println("    Description: " + card.getDescription());
                System.out.println("    Parts:");
                Part[] roles = card.getRoles();

                for (int i = 0; i < roles.length; ++i)
                {
                    System.out.println("        \"" + roles[i].getName() + "\" (" + roles[i].getRank() + "):");
                    System.out.println("            \"" + roles[i].getLine() + "\"");
                    if (roles[i].inUse())
                    {
                        System.out.println("            Taken");
                    }
                    else
                    {
                        System.out.println("            Free");
                    }
                }
                System.out.println("    Extras:");
                Part[] extras = ((MovieSet) location).getExtras();

                for (int i = 0; i < extras.length; ++i)
                {
                    System.out.println("        \"" + extras[i].getName() + "\" (" + extras[i].getRank() + "):");
                    System.out.println("            \"" + extras[i].getLine() + "\"");
                    if (extras[i].inUse())
                    {
                        System.out.println("            Taken");
                    }
                    else
                    {
                        System.out.println("            Free");
                    }
                }
            }
        }
        else if (location instanceof CastingOffice)
        {
            System.out.println("    Upgrades: ");
            CastingOffice castingOffice = (CastingOffice) location;
            int[] moneyCost = castingOffice.getMoneyCost();
            int[] creditCost = castingOffice.getCreditCost();

            for (int i = 0; i < moneyCost.length; ++i)
            {
                System.out.println("        Rank " + (i+2) + ": " + moneyCost[i] + " dollars / " + creditCost[i] + " credits");
            }
        }
        System.out.println("Press enter to continue.");
        System.console().readLine();
    }

    /**
     * Display acting outcome
     */
    public void displayAct(boolean success, int money, int credits) {
        System.out.println("\nActing: ");
        if (success)
        {
            System.out.println("    Success!");
        }
        else
        {
            System.out.println("    Failure.");
        }
        if (money != 0)
        {
            System.out.println("    " + money + " dollars.");
        }
        if (credits != 0)
        {
            System.out.println("    " + credits + " credits.");
        }
        System.out.println("\nPress enter to continue.");
        System.console().readLine();
    }

    /**
     * Display scene wrap payouts
     */
    public void displaySceneWrap(ArrayList<Player> onCardPlayers, ArrayList<Player> offCardPlayers, int[] onCardPayouts, int[] offCardPayouts)
    {
        System.out.println("\nScene Wrap:");
        System.out.println("\n    Payouts:");
        if (onCardPlayers.size() > 0)
        {
            System.out.println("    Parts:");
            for (int i = 0; i < onCardPlayers.size(); ++i)
            {
                Player player = onCardPlayers.get(i);
                System.out.println("        \"" + player.getName() + "\": " + onCardPayouts[i] + " dollars.");
            }
        }
        if (offCardPlayers.size() > 0)
        {
            System.out.println("    Extras:");
            for (int i = 0; i < offCardPlayers.size(); ++i)
            {
                Player player = offCardPlayers.get(i);
                System.out.println("        \"" + player.getName() + "\": " + offCardPayouts[i] + " dollars.");
            }
        }
        System.out.println("\nPress enter to continue:");
        System.console().readLine();
    }

    /**
     * Get array of payment type and destination rank as return value. 0 as payment type means no change in rank.
     */
    public int[] inputUpgrade(Player player, int[] moneyCost, int[] creditCost) {
        System.out.println("\nRanks:");
        ArrayList<Integer> availableRanks = new ArrayList<Integer>();
        for (int i = 0; i < moneyCost.length; ++i) //Print ranks, add to list if player can afford, array starts at rank 2
        {
            System.out.print("    ");
            boolean enoughMoney = moneyCost[i] <= player.getMoney();
            boolean enoughCredits = creditCost[i] <= player.getCredits();
            boolean rankAvailable = enoughMoney || enoughCredits && player.getRank() + 2 < i + 2; 

            if (rankAvailable)
            {
                availableRanks.add(i);
            }

            System.out.print(i + 2);
            System.out.print(": ");
            System.out.print(moneyCost[i] + " dollars / " + creditCost[i] + " credits\n");
        }

        if (availableRanks.size() > 0)
        {
            System.out.println("\nAvailable Ranks: ");
            for (int i = 0; i < availableRanks.size(); ++i) //list available ranks
            {
                System.out.print((availableRanks.get(i) + 2));
                System.out.print(": " + moneyCost[availableRanks.get(i)] + " dollars / " + creditCost[availableRanks.get(i)] + " credits\n");
            }
            System.out.println("\nCurrency:\nMoney: " + player.getMoney() + " dollars / " + player.getCredits() + " credits.");
            System.out.print("\nEnter rank number to upgrade to (q to cancel):\n");
            String input = System.console().readLine();

            try
            {
                int choice = Integer.parseInt(input);
                int i = 0; 
                while (i < availableRanks.size() && !(availableRanks.get(i) == choice - 2)) //check if selection is an available rank
                {
                    ++i;
                }
                if (i == availableRanks.size())
                {
                    System.out.println("Not valid rank. ");
                    return inputUpgrade(player, moneyCost, creditCost);
                }
                else
                {
                    boolean enoughMoney = moneyCost[i] <= player.getMoney();
                    boolean enoughCredits = creditCost[i] <= player.getCredits();
                    System.out.println("Pay with:");
                    System.out.println("1. Money");
                    System.out.println("2. Credits");
                    System.out.print("Enter choice (q to cancel): ");
                    input = System.console().readLine();

                    try
                    {
                        int payment = Integer.parseInt(input);
                        if (payment == 1)
                        {
                            if (enoughMoney)
                            {
                                System.out.println("\nSuccessfully upgraded rank to: " + choice);
                                System.out.print("\nPress enter to continue:\n");
                                System.console().readLine();
                                return new int[] {1, choice};
                            }
                            else
                            {
                                System.out.println("\nNot enough money.");
                                return inputUpgrade(player, moneyCost, creditCost);
                            }
                        }
                        else if (payment == 2)
                        {
                            if (enoughCredits)
                            {
                                System.out.println("\nSuccessfully upgraded rank to: " + choice);
                                System.out.print("\nPress enter to continue:\n");
                                System.console().readLine();
                                return new int[] {2, choice};
                            }
                            else
                            {
                                System.out.println("\nNot enough credits.");
                                return inputUpgrade(player, moneyCost, creditCost);
                            }
                        }

                    }
                    catch (NumberFormatException e)
                    {
                        if (input.trim().equals("q"))
                        {
                            return inputUpgrade(player, moneyCost, creditCost); 
                        }
                        else
                        {
                            System.out.println("Invalid input: please enter a number.");
                        }
                    }
                }
            }
            catch (NumberFormatException e)
            {
                if (input.trim().equals("q"))
                {
                    return new int[]{0, player.getRank()};
                }
                else
                {
                    System.out.println("Invalid input: please enter a number.");
                    return inputUpgrade(player, moneyCost, creditCost);
                }
            }
        }
        else
        {
            System.out.println("\nNo available ranks.\nPress enter to continue:");
            System.console().readLine();
            return new int[] {0, player.getRank()};
        }
        return new int[] {0, player.getRank()};
    }

    public void displayScores(Player[] players, int[] playerScores) {
        int len = players.length;

        System.out.println("\nFinal Scores: \n");
        for (int i = 0; i < len; ++i)
        {
            System.out.println(players[i].getName() + ": " + playerScores[i]);
        }
        System.out.println("\nEnter to exit:\n");
        System.console().readLine();
    }
}
