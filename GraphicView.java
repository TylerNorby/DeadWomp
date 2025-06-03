import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.function.Function;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class GraphicView extends JFrame implements iView{
    private GameBoard gameBoard;
    private JFrame mainFrame;
    private JLayeredPane board;
    private Panel actionPanel;
    Action currentAction;
    Thread inputThread;

    /**
     * Listener for button inputs
     */
    private class Listener implements ActionListener
    {
        private GraphicView parent;
        public void actionPerformed(ActionEvent e)
        {
            currentAction = Action.valueOf(((JButton) e.getSource()).getText());
            parent.actionPerformed();
        }
        Listener(GraphicView parent)
        {
            this.parent = parent;
        }
    }

    /**
     * Unpause obj thread when button input received.
     */
    public synchronized void actionPerformed()
    {
        this.notify();
    }

    public GraphicView(GameBoard gameBoard) {
        super("Deadwood");
        this.gameBoard = gameBoard;
        Listener listener = new Listener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new JLayeredPane();
        add(board);
        JLabel boardIcon = new JLabel(new ImageIcon("board.jpg"));
        boardIcon.setSize(1600,900);
        board.add(boardIcon, 0);
        board.setLayout(new FlowLayout());
        actionPanel = new Panel(new GridLayout(5, 2)); //actions added at each turn depending on available action

        Action[] actions = Action.class.getEnumConstants();
        for (Action action: actions)
        {
            if (action != action.View)
            {
                JButton button = new JButton(action.toString());
                button.addActionListener(listener);
                actionPanel.add(button);
            }
        }
        

        setLayout(new BorderLayout());
        getContentPane().add(board, BorderLayout.CENTER);
        getContentPane().add(actionPanel, BorderLayout.EAST);
        setSize(1920, 1080);
        pack();
        setVisible(true);
    }

    public void displayBoard() {
    }

    public int inputPlayerCount() {
        while (true) {
            String input = JOptionPane.showInputDialog(this, "Enter number of players (2-8):", "Player Count", JOptionPane.QUESTION_MESSAGE);
            if (input == null)
            {
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
            try {
                int count = Integer.parseInt(input);
                if (count >= 2 && count <= 8)
                {
                    return count;
                }
                else
                {
                    JOptionPane.showMessageDialog((JFrame) this, "Please enter a valid number between 2 and 8.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog((JFrame) this, "Please enter a valid number between 2 and 8.", "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public String[] inputNames(int playerCount) {
        String[] names = new String[playerCount];

        for (int i = 0; i < playerCount; ++i) {
            boolean validInput = false;
            while (!validInput)
            {
                String input = JOptionPane.showInputDialog(this, "Enter Player " + (i + 1) + "'s name:", "Player names", JOptionPane.QUESTION_MESSAGE);
                if (input == null) 
                {
                    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                } 
                else if (input.trim().isEmpty()) 
                {
                    JOptionPane.showMessageDialog(this, "Name cannot be empty. Please enter a valid name.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    names[i] = input;
                    validInput = true;
                }
            }
        }
        return names;
    }

    // This method is complete
    public Action inputAction(ArrayList<Action> validActions) {
        Component[] components = actionPanel.getComponents();
        for (Component component : components)
        {
            JButton button = (JButton) component;
            if (validActions.contains(Action.valueOf(button.getText())))
            {
                button.setEnabled(true);
            }
            else
            {
                button.setEnabled(false);
            }
        }

        synchronized (this) //wait until button is pressed
        {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return currentAction;
    }

    // Implemntation not complete, review validLocations array
    public String inputLocation(String[] validLocations) {
        String input = (String) JOptionPane.showInputDialog(mainFrame, "Choose a location:", "Location Dropdown",
                JOptionPane.QUESTION_MESSAGE, null, validLocations, validLocations[0]);
        return input; // null if cancelled
    }

    public void displayRoles(List<Part> availableRoles, List<Part> unavailableRoles) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayAvailableRoles'");
    }

    public String inputRoleChoice(List<Part> availableRoles) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inputRoleChoice'");
    }

    public void displayBoard(int day, int totalDays, GameBoard gameBoard, Player activePlayer, Player[] players) {
        //update game state display, update board, update player display
    }

    public void displayLocation(GameBoard gameBoard, String locationName) {
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

    public int[] inputUpgrade(boolean[][] availableRanks)
    {
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
