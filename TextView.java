
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
    public void displayBoard(GameBoard gameBoard, Player activePlayer, Player[] players) {
        System.out.println("Locations: ");
        Location[] locations = gameBoard.getLocations();
        String[] playerLocations = new String[players.length];

        for (int i = 0; i < playerLocations.length; ++i)
        {
            playerLocations[i] = players[i].getLocation();
        }

        for (Location location : locations)
        {
            System.out.print(location.getName() + ":     ->     ");
            String[] neighbors = location.getConnections();

            int i;
            for (i = 0; i < neighbors.length - 1; ++i)
            {
                System.out.print(neighbors[i] + ", ");
            }
            System.out.print(neighbors[i] + "\n");

            for (i = 0; i < playerLocations.length; ++i)
            {
                if (playerLocations[i].equals(location.getName()))
                {
                    if (players[i] == activePlayer)
                    {
                        System.out.print("    [" + players[i].getName() + "]");
                    }
                    else
                    {
                        System.out.print("    " + players[i].getName());
                    }
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
            System.out.println((i + 1) + ": " + validLocations[i]);
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
            return null; 
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
                return null;
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

    public void displayLocation(GameBoard gameBoard, String locationName) {
        Location location = gameBoard.getLocation(locationName); 
        System.out.println("\n" + location.getName() + ": ");
        if (location instanceof MovieSet)
        {
            Card card = ((MovieSet) location).getCard();
            System.out.println("    Scene #" + card.getSceneNum() + ": \"" + card.getName() + "\"");
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
}
