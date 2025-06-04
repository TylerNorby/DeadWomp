import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class GraphicView extends JFrame implements iView{
    private GameBoard gameBoard;
    private JFrame mainFrame;
    private JLayeredPane board;
    private JPanel actionPanel;
    private JPanel gameState;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private ImageIcon[][] playerIcons;
    private ImageIcon[] dice;
    String currentAction; //current button input
    Thread inputThread;

    /**
     * Listener for button inputs
     */
    private class Listener implements ActionListener
    {
        private GraphicView parent;
        public void actionPerformed(ActionEvent e)
        {
            currentAction = ((JButton) e.getSource()).getText();
            parent.actionPerformed();
        }
        Listener (GraphicView parent)
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

    private void generatePlayerIcons()
    {
        playerIcons = new ImageIcon[8][6];
        String[] colors = new String[] {"b", "c", "g", "y", "o", "r", "v", "p"};
        for (int i = 0; i < 8; ++i)
        {
            for (int j = 0; j < 6; ++j)
            {
                playerIcons[i][j] = new ImageIcon("Dice/" + colors[i] + (j+1) + ".png");
            }
        }
    }

    private void generateDice()
    {
        dice = new ImageIcon[6];
        for (int i = 0; i < 6; ++i)
        {
            dice[i] = new ImageIcon("Dice/w" + i + ".png");
        }
    }
    
    public GraphicView(){
        super("Deadwood");
        Listener listener = new Listener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new JLayeredPane(); //board components 
        add(board);
        JLabel boardIcon = new JLabel(new ImageIcon("board.jpg"));
        boardIcon.setSize(1200,900);
        board.add(boardIcon, 0);
        board.setLayout(new FlowLayout());

        leftPanel = new JPanel(new GridLayout(8, 1)); //player panel
        TitledBorder playerTitle = BorderFactory.createTitledBorder("Players");
        playerTitle.setTitleJustification(TitledBorder.CENTER);
        playerTitle.setTitleFont(new Font("Times New Roman", Font.PLAIN, 15));
        leftPanel.setBorder(playerTitle);

        rightPanel = new JPanel(new GridLayout(2, 1)); //right panel
        gameState = new JPanel();
        rightPanel.add(gameState);

        actionPanel = new JPanel(new GridLayout(0, 1)); //action panel
        TitledBorder actionTitle = BorderFactory.createTitledBorder("Actions");
        actionTitle.setTitleJustification(TitledBorder.CENTER);
        actionTitle.setTitleFont(new Font("Times New Roman", Font.PLAIN, 15));
        actionPanel.setBorder(actionTitle);
        Action[] actions = Action.class.getEnumConstants();

        for (Action action: actions)
        {
            if (action != action.View)
            {
                String name = action.toString();
                if (name == "TakeRole")
                {
                    name = "Take Role";
                }
                JButton button = new JButton(name);
                button.setFont(new Font("Times New Roman", Font.PLAIN, 20));
                button.addActionListener(listener);
                actionPanel.add(button);
            }
        }
        rightPanel.add(actionPanel);

        generatePlayerIcons();
        generateDice();
        
        setLayout(new BorderLayout());
        getContentPane().add(board, BorderLayout.CENTER);
        getContentPane().add(rightPanel, BorderLayout.EAST);
        leftPanel.setPreferredSize(new Dimension(200,900));
        rightPanel.setPreferredSize(new Dimension(200,900));
        getContentPane().add(leftPanel, BorderLayout.WEST);
        setSize(1920, 1080);
        pack();
        setVisible(true);
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
            if (component instanceof JButton)
            {
                JButton button = (JButton) component;
                String name = button.getText();
                if (name == "Take Role")
                {
                    name = "TakeRole";
                }
                if (validActions.contains(Action.valueOf(name)))
                {
                    button.setEnabled(true);
                }
                else
                {
                    button.setEnabled(false);
                }
            }
        }
        rightPanel.remove(1); //swap out bottom panel
        rightPanel.add(actionPanel);
        rightPanel.validate();
        rightPanel.repaint();
        pack();

        synchronized (this) //wait until button is pressed
        {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (currentAction == "Take Role")
        {
            currentAction = "TakeRole";
        }
        return Action.valueOf(currentAction);
    }

    public String inputLocation(String[] validLocations) {
        JPanel locationPanel = new JPanel(new GridLayout(0, 1));
        TitledBorder locationBorder = BorderFactory.createTitledBorder("Available Locations");
        locationBorder.setTitleJustification(TitledBorder.CENTER);
        locationBorder.setTitleFont(new Font("Times New Roman", Font.PLAIN, 15));
        locationPanel.setBorder(locationBorder);

        Listener listener = new Listener(this);

        for (String string : validLocations)
        {
            String name = string.substring(0, 1).toUpperCase() + string.substring(1, string.length());
            JButton button = new JButton(name);
            button.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            button.addActionListener(listener);
            locationPanel.add(button);
        }
        JButton button = new JButton("Cancel");
        button.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        button.addActionListener(listener);
        locationPanel.add(button);
        rightPanel.remove(1);
        rightPanel.add(locationPanel);
        rightPanel.validate();
        rightPanel.repaint();
        pack();

        synchronized(this)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (currentAction.equals("Cancel"))
        {
            return null;
        }
        else
        {
            if (currentAction.equals("Trailer") || currentAction.equals("Office"))
            {
                currentAction = currentAction.substring(0, 1).toLowerCase() + currentAction.substring(1, currentAction.length());
            }
            return currentAction; 
        }
    }

    public void displayRoles(List<Part> availableRoles, List<Part> unavailableRoles) {
    }

    public String inputRoleChoice(List<Part> availableRoles) {
        JPanel rolePanel = new JPanel(new GridLayout(0, 1));
        TitledBorder border = BorderFactory.createTitledBorder("Available Roles");
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitleFont(new Font("Times New Roman", Font.PLAIN, 15));
        rolePanel.setBorder(border);
        Listener listener = new Listener(this);

        for (Part part: availableRoles)
        {
            String name = part.getName();
            JButton button = new JButton(name);
            button.setPreferredSize(new Dimension(50, 50));
            button.setFont(new Font("Times New Roman", Font.PLAIN, 16));
            button.addActionListener(listener);
            rolePanel.add(button);
        }
        JButton button = new JButton("Cancel");
        button.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        button.addActionListener(listener);
        rolePanel.add(button);
        rightPanel.remove(actionPanel); //swap out bottom panel
        rightPanel.add(rolePanel);
        rightPanel.validate();
        rightPanel.repaint();
        pack();

        synchronized(this)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (currentAction == "Cancel")
        {
            return "q";
        }
        else
        {
            return currentAction;
        }
    }

    public void displayBoard(int day, int totalDays, GameBoard gameBoard, Player activePlayer, Player[] players) {
        leftPanel.removeAll();
        for (int i = 0; i < players.length; ++i)
        {
            final int FONT_SIZE = 15;
            JPanel playerPanel = new JPanel(new BorderLayout());
            JPanel player = new JPanel(new GridLayout(4, 1));
            TitledBorder border = BorderFactory.createTitledBorder(players[i].getName());
            border.setTitleJustification(TitledBorder.CENTER);
            border.setTitleFont(new Font("Times New Roman", Font.PLAIN, 17));
            playerPanel.setBorder(border);

            String roleText = players[i].getRole();
            if (roleText == null)
            {
                roleText = "No Role.";
            }
            JLabel role = new JLabel(roleText);
            role.setFont(new Font("Times New Roman", Font.PLAIN, FONT_SIZE));
            role.setPreferredSize(new Dimension(100,15));
            player.add(role);

            JLabel dollars = new JLabel(players[i].getMoney() + " Dollars");
            dollars.setFont(new Font("Times New Roman", Font.PLAIN, FONT_SIZE));
            player.add(dollars);

            JLabel credits = new JLabel(players[i].getCredits() + " Credits");
            credits.setFont(new Font("Times New Roman", Font.PLAIN, FONT_SIZE));
            player.add(credits);

            JLabel chips = new JLabel(players[i].getChips() + " Chips");
            chips.setFont(new Font("Times New Roman", Font.PLAIN, FONT_SIZE));
            player.add(chips);

            //credits.setBorder(new EmptyBorder(0, 10, 0, 25));
            JLabel playerIcon = new JLabel(playerIcons[i][players[i].getRank()-1]);
            playerIcon.setPreferredSize(new Dimension(60, 45));
            playerPanel.add(playerIcon, BorderLayout.WEST);
            playerPanel.add(player, BorderLayout.CENTER);
            leftPanel.add(playerPanel);
        }

        leftPanel.validate();
        leftPanel.repaint();
        pack();
        //update game state display, update board, update player display
    }

    public void displayLocation(GameBoard gameBoard, String locationName) {
    }


    //method is complete
    @Override
    public void displayAct(boolean success, int roll, int money, int credits) {
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
