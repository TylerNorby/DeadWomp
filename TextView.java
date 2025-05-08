import java.util.ArrayList;

public class TextView implements iView{
    private GameBoard gameBoard;

    public TextView(GameBoard gameBoard)
    {
        this.gameBoard = gameBoard;
    }

    public int inputPlayerCount()
    {
        System.out.print("\nEnter number of players (3-8): ");
        String input = System.console().readLine();
        int playerCount;

        try
        {
           playerCount = Integer.valueOf(input);
        }
        catch(NumberFormatException e)
        {
            System.out.println("Input not number, please try again. ");
            playerCount = inputPlayerCount();
        }

        if (playerCount >= 3 && playerCount <= 8)
        {
            return playerCount;
        }
        else
        {
            System.out.println("Invalid number of players, please try again. ");
            return inputPlayerCount();
        }
    }

    public String[] inputNames (int playerCount)
    {
        String[] names = new String[playerCount];

        for (int i = 0; i < playerCount; ++i)
        {
            System.out.println("Enter player " + (i+1) + "'s name: ");
            names[i] = System.console().readLine();
        }
        System.out.println();
        return names;
    }

    public Action inputAction(ArrayList<Action> validActions)
    {
        System.out.println("Available actions: ");

        for(Action a: validActions)
        {
            System.out.println(a);
        }
        System.out.print("Please enter an action: ");

        try
        {
            return Action.valueOf(System.console().readLine());
        }
        catch(IllegalArgumentException e)
        {
            System.out.print("\nInvalid action, please try again.\n");
            return inputAction(validActions);
        }
    }

    /**
     * Display each location, show players in locations, highlight active player
     */
    public void displayBoard(GameBoard gameBoard)
    {
    }
}