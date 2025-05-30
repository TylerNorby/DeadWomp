import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class GraphicView implements iView {
    GameBoard gameBoard;
    private JFrame mainFrame;

    public GraphicView(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void displayBoard() {
        if (mainFrame == null) {
            mainFrame = new JFrame("DeadWood Game Board");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(800, 600);
            mainFrame.add(new JLabel("Welcome to DeadWood!"));
            mainFrame.setLocationRelativeTo(null); // Center the window
            mainFrame.setVisible(true);
        } else {
            mainFrame.setVisible(true);
            mainFrame.toFront();
        }
    }

    // This method is complete
    @Override
    public int inputPlayerCount() {
        while (true) {
            String input = JOptionPane.showInputDialog(mainFrame, "Enter number of players (2-8):", "Player Count",
                    JOptionPane.QUESTION_MESSAGE);
            if (input == null)
                throw new UnsupportedOperationException("Cancelled by user");
            try {
                int count = Integer.parseInt(input);
                if (count >= 2 && count <= 8)
                    return count;
            } catch (NumberFormatException ignored) {
            }
            JOptionPane.showMessageDialog(mainFrame, "Please enter a valid number between 2 and 8.", "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // This method is complete
    @Override
    public String[] inputNames(int playerCount) {
        String[] names = new String[playerCount];

        for (int i = 0; i < playerCount; ++i) {
            String input = JOptionPane.showInputDialog(mainFrame, "Enter player " + (i + 1) + "'s name:",
                    "Player names",
                    JOptionPane.QUESTION_MESSAGE);
            if (input == null)
                throw new UnsupportedOperationException("Cancelled by user");
            if (input.trim().isEmpty()) { // Prevents empty names
                JOptionPane.showMessageDialog(mainFrame, "Name cannot be empty. Please enter a valid name.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                i--; // decrement i to retry this player
                continue;
            }
            names[i] = input;
        }
        return names;
    }

    // This method is complete
    @Override
    public Action inputAction(ArrayList<Action> validActions) {
        Object[] options = validActions.toArray();
        int choice = JOptionPane.showOptionDialog(
                mainFrame,
                "Choose an action:",
                "Action Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (choice >= 0)
            return validActions.get(choice);
        throw new UnsupportedOperationException("Action selection cancelled");
    }

    // Implemntation not complete, review validLocations array
    @Override
    public String inputLocation(String[] validLocations) {
        String input = (String) JOptionPane.showInputDialog(mainFrame, "Choose a location:", "Location Dropdown",
                JOptionPane.QUESTION_MESSAGE, null, validLocations, validLocations[0]);
        return input; // null if cancelled
    }

    @Override
    public void displayRoles(List<Part> availableRoles, List<Part> unavailableRoles) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayAvailableRoles'");
    }

    @Override
    public String inputRoleChoice(List<Part> availableRoles) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inputRoleChoice'");
    }

    @Override
    public void displayBoard(int day, int totalDays, GameBoard gameBoard, Player activePlayer, Player[] players) {
        JFrame frame = new JFrame("DeadWood Game Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new JLabel("Welcome to DeadWood!"));
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
        System.out.println("Day: " + day + "/" + totalDays);
        Location[] locations = gameBoard.getLocations();
        String[] playerLocations = new String[players.length];
        int indent = 40;

        for (int i = 0; i < playerLocations.length; ++i) {
            playerLocations[i] = players[i].getLocation();
        }

        for (Location location : locations) {
            int lineLen = 0;
            System.out.print(location.getName().substring(0, 1).toUpperCase()
                    + location.getName().substring(1, location.getName().length()));
            lineLen += location.getName().length();
            if (location instanceof MovieSet) {
                MovieSet movieSet = (MovieSet) location;
                if (movieSet.getCard() == null) {
                    String line = "     (Scene finished)";
                    lineLen += line.length();
                    System.out.print(line);
                } else {
                    String line = " ($" + movieSet.getCard().getBudget() + ",000,000) (" + movieSet.getShotCounter()
                            + "/" + movieSet.getShots() + ")";
                    lineLen += line.length();
                    System.out.print(line);
                }
            }
            System.out.print(": ");
            for (int i = lineLen; i < indent; ++i) {
                System.out.print(" ");
            }
            System.out.print(" --->     ");
            String[] neighbors = location.getConnections();

            int i;
            for (i = 0; i < neighbors.length - 1; ++i) {
                System.out.print(neighbors[i].substring(0, 1).toUpperCase()
                        + neighbors[i].substring(1, neighbors[i].length()) + ", ");
            }
            System.out.print(neighbors[i].substring(0, 1).toUpperCase()
                    + neighbors[i].substring(1, neighbors[i].length()) + "\n");

            for (i = 0; i < playerLocations.length; ++i) {
                if (playerLocations[i].equals(location.getName())) {
                    if (players[i] == activePlayer) {
                        System.out.print("--> [" + players[i].getName() + "]");
                    } else {
                        System.out.print("     " + players[i].getName());
                    }
                    System.out.print(" (" + players[i].getRank() + ")");

                    String role = players[i].getRole();
                    if (role == null) {
                        System.out.print(" (No Role): ");
                    } else {
                        System.out.print(" (Actor: " + role + "): ");
                    }
                    System.out.print("Dollars: " + players[i].getMoney() + ", Credits: " + players[i].getCredits()
                            + ", Practice Chips: " + players[i].getChips() + "\n");
                }
            }
        }
    }

    @Override
    public void displayLocation(GameBoard gameBoard, String locationName) {
        Location location = gameBoard.getLocation(locationName);
        System.out.println("\n" + location.getName().substring(0, 1).toUpperCase()
                + location.getName().substring(1, location.getName().length()) + ": ");
        if (location instanceof MovieSet) {
            MovieSet movieSet = (MovieSet) location;
            Card card = movieSet.getCard();
            if (card == null) {
                System.out.println("    (Scene Finish)");
            } else {
                System.out.println("    Scene #" + card.getSceneNum() + ": \"" + card.getName() + "\"");
                System.out.println("    Shots: " + movieSet.getShotCounter() + "/" + movieSet.getShots());
                System.out.println("    Budget: $" + card.getBudget() + ",000,000");
                System.out.println("    Description: " + card.getDescription());
                System.out.println("    Parts:");
                Part[] roles = card.getRoles();

                for (int i = 0; i < roles.length; ++i) {
                    System.out.println("        \"" + roles[i].getName() + "\" (" + roles[i].getRank() + "):");
                    System.out.println("            \"" + roles[i].getLine() + "\"");
                    if (roles[i].inUse()) {
                        System.out.println("            Taken");
                    } else {
                        System.out.println("            Free");
                    }
                }
                System.out.println("    Extras:");
                Part[] extras = ((MovieSet) location).getExtras();

                for (int i = 0; i < extras.length; ++i) {
                    System.out.println("        \"" + extras[i].getName() + "\" (" + extras[i].getRank() + "):");
                    System.out.println("            \"" + extras[i].getLine() + "\"");
                    if (extras[i].inUse()) {
                        System.out.println("            Taken");
                    } else {
                        System.out.println("            Free");
                    }
                }
            }
        } else if (location instanceof CastingOffice) {
            System.out.println("    Upgrades: ");
            CastingOffice castingOffice = (CastingOffice) location;
            int[] moneyCost = castingOffice.getMoneyCost();
            int[] creditCost = castingOffice.getCreditCost();

            for (int i = 0; i < moneyCost.length; ++i) {
                System.out.println(
                        "        Rank " + (i + 2) + ": " + moneyCost[i] + " dollars / " + creditCost[i] + " credits");
            }
        }
        System.out.println("Press enter to continue.");
        System.console().readLine();
    }


    //method is complete
    @Override
    public void displayAct(boolean success, int money, int credits) {
        StringBuilder message = new StringBuilder();
        message.append("Acting:\n");
        if (success) {
            message.append("    Success!\n");
        } else {
            message.append("    Failure.\n");
        }
        if (money != 0) {
            message.append("    ").append(money).append(" dollars.\n");
        }
        if (credits != 0) {
            message.append("    ").append(credits).append(" credits.\n");
        }
        message.append("\nPress OK to continue.");
        JOptionPane.showMessageDialog(mainFrame, message.toString(), "Act Result", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void displaySceneWrap(ArrayList<Player> offCardPlayers, ArrayList<Player> onCardPlayers, int[] onCardPayouts,
            int[] offCardPayouts) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displaySceneWrap'");
    }

    @Override
    public int[] inputUpgrade(Player player, int[] moneyCost, int[] creditCost) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inputRank'");
    }


    // This method is complete
    @Override
    public void displayScores(Player[] players, int[] playerScores) {
        int len = players.length;
        StringBuilder message = new StringBuilder();
        message.append("\nFinal Scores:\n\n");
        for (int i = 0; i < len; ++i) {
            message.append(players[i].getName()).append(": ").append(playerScores[i]).append("\n");
        }
        message.append("\nPress OK to exit.\n");
        JOptionPane.showMessageDialog(mainFrame, message.toString(), "Final Scores", JOptionPane.INFORMATION_MESSAGE);
    }
}
