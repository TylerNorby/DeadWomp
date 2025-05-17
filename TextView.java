
import java.util.ArrayList;
import java.util.List;

public class TextView implements iView {

    private GameBoard gameBoard;

    public TextView(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

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
        System.out.print("\nPlease select an action (enter to skip): ");

        String input = System.console().readLine();
        try {
            int actionID = Integer.valueOf(input) - 1;
            if (actionID < 0 || actionID >= validActions.size())
            {
                System.out.println("\nInvalid action, please try again.\n");
                return inputAction(validActions);
            }
            return validActions.get(actionID);
        } catch (NumberFormatException e) {
            if (input.trim().equals(""))
            {
                return Action.Nothing;
            }
            System.out.print("\nInvalid action, please try again.\n");
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
                    if (players[i].getRole() == null)
                    {
                        System.out.print(" (Idle)\n");
                    }
                    else
                    {
                        System.out.print(" (Acting)\n");
                    }
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
        System.out.println("\nAvailable locations: ");
        for (String location : validLocations) {
            System.out.println(location);
        }
        System.out.print("Input: ");
        String input = System.console().readLine();

        int i = 0;
        while (i < validLocations.length && !validLocations[i].equals(input)) {
            ++i;
        }
        if (i == validLocations.length) {
            System.out.println("Not valid location, please try again.");
            return inputLocation(validLocations);
        } else {
            return input;
        }

    }

    public void displayAvailableRoles(List<Part> roles) {
        System.out.println("\nAvailable Roles:"); 
        if (roles == null || roles.isEmpty()) { 
            System.out.println("  (None)");
            return;
        }
        for (int i = 0; i < roles.size(); i++) {
            Part role = roles.get(i);
          
            String type = role.onCard() ? "On Card" : "Extra";
            System.out.println("  " + (i + 1) + ". " + role.getName() + " (Rank " + role.getRank() + ", " + type + ")");
        }
    }

   
    public String inputRoleChoice(List<Part> availableRoles) { 
        if (availableRoles == null || availableRoles.isEmpty()) {
       
            System.out.println("No roles to choose from.");
            return null; 
        }

        int choice = -1;
        while (true) {
            System.out.print("Enter the number of the role you want to take (or 0 to cancel): "); 
            String input = System.console().readLine();

            if (input == null) { 
                System.out.println("Error reading input.");
                return null; 
            }

            try {
                choice = Integer.parseInt(input.trim()); 

                if (choice == 0) {
                    return null; 
                }

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
}
