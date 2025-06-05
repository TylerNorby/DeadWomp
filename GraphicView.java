import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class GraphicView extends JFrame implements iView{
    private GameBoard gameBoard;
    private JFrame mainFrame;
    private JLayeredPane board;
    private JPanel actionPanel;
    private JPanel gameState;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JLabel[][] playerLabels;
    private ImageIcon[] dice;
    private ImageIcon cardBack;
    private ImageIcon shot;
    private HashMap<String, ImageIcon> cards;
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

    private void generatePlayerLabels()
    {
        playerLabels = new JLabel[8][6];
        String[] colors = new String[] {"b", "c", "g", "y", "o", "r", "v", "p"};
        for (int i = 0; i < 8; ++i)
        {
            for (int j = 0; j < 6; ++j)
            {
                playerLabels[i][j] = new JLabel(new ImageIcon("Dice/" + colors[i] + (j+1) + ".png"));
            }
        }
    }

    private void generateDice()
    {
        dice = new ImageIcon[6];
        for (int i = 0; i < 6; ++i)
        {
            dice[i] = new ImageIcon("Dice/w" + (i+1) + ".png");
        }
    }

    private void generateCards()
    {
        cards = new HashMap<String, ImageIcon>();
        for (int i = 1; i < 10; ++i)
        {
            cards.put("0" + i + ".png", new ImageIcon("Cards/0" + i + ".png"));
        }
        for (int i = 10; i < 41; ++i)
        {
            cards.put(i + ".png", new ImageIcon("Cards/" + i + ".png"));
        }
        shot = new ImageIcon("shot.png");
        cardBack = new ImageIcon("cardBack.png");
    }
    
    public GraphicView(){
        super("Deadwood");
        Listener listener = new Listener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new JLayeredPane(); //board components 
        board.setLayout(null);
        board.setPreferredSize(new Dimension(1200,900));
        JLabel boardIcon = new JLabel(new ImageIcon("board.jpg"));
        boardIcon.setSize(1200, 900);
        board.add(boardIcon, 10);
        add(board);

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

        generatePlayerLabels();
        generateDice();
        generateCards();
        
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

    private void displayPlayers(GameBoard gameBoard, Player activePlayer, Player[] players)
    {
        leftPanel.removeAll();
        for (int i = 0; i < players.length; ++i)
        {
            final int FONT_SIZE = 15;
            JPanel playerPanel = new JPanel(new BorderLayout());
            JPanel playerInfo = new JPanel(new GridLayout(4, 1));
            TitledBorder border = BorderFactory.createTitledBorder(players[i].getName());
            border.setTitleJustification(TitledBorder.CENTER);
            border.setTitleFont(new Font("Times New Roman", Font.PLAIN, 17));
            playerPanel.setBorder(border);
            Player player = players[i];

            if (player == activePlayer)
            {
                playerPanel.setBackground(Color.YELLOW);
                playerInfo.setBackground(Color.YELLOW);
            }
            else
            {
                playerPanel.setBackground(null);
            }

            String roleText = player.getRole();
            if (roleText == null)
            {
                roleText = "No Role";
            }
            JLabel role = new JLabel(roleText);
            role.setFont(new Font("Times New Roman", Font.PLAIN, FONT_SIZE));
            role.setPreferredSize(new Dimension(100,15));
            playerInfo.add(role);

            JLabel dollars = new JLabel(players[i].getMoney() + " Dollars");
            dollars.setFont(new Font("Times New Roman", Font.PLAIN, FONT_SIZE));
            playerInfo.add(dollars);

            JLabel credits = new JLabel(players[i].getCredits() + " Credits");
            credits.setFont(new Font("Times New Roman", Font.PLAIN, FONT_SIZE));
            playerInfo.add(credits);

            JLabel chips = new JLabel(players[i].getChips() + " Chips");
            chips.setFont(new Font("Times New Roman", Font.PLAIN, FONT_SIZE));
            playerInfo.add(chips);

            //credits.setBorder(new EmptyBorder(0, 10, 0, 25));
            JLabel playerLabel = playerLabels[i][players[i].getRank()-1];
            ImageIcon img = (ImageIcon) playerLabel.getIcon();
            playerLabel = new JLabel(img);

            playerLabel.setPreferredSize(new Dimension(60, 45));
            playerPanel.add(playerLabel, BorderLayout.WEST);
            playerPanel.add(playerInfo, BorderLayout.CENTER);
            leftPanel.add(playerPanel);
        }
        leftPanel.validate();
        leftPanel.repaint();
    }

    private class PlayerPair
    {
        public Player player;
        public JLabel label;
        public PlayerPair(Player player, JLabel label)
        {
            this.player = player;
            this.label = label;
        }
        public Player getPlayer(){ return player; }
        public JLabel getLabel() { return label; }
    }

    public void displayBoard(int day, int totalDays, GameBoard gameBoard, Player activePlayer, Player[] players) {
        displayPlayers(gameBoard, activePlayer, players);

        //update game state display, update board, update player display
        board.removeAll();
        JLabel boardIcon = new JLabel(new ImageIcon("board.jpg"));
        boardIcon.setSize(1200, 900);
        board.add(boardIcon, Integer.valueOf(0));

        Location[] locations = gameBoard.getLocations();
        for (Location location: locations)
        {
            
            ArrayList<PlayerPair> playerList = new ArrayList<PlayerPair>(); //get all players in location and their respective icons
            for (int i = 0; i < players.length; ++i)
            {
                if (gameBoard.getLocation(players[i].getLocation()) == location)
                {

                    playerList.add(new PlayerPair(players[i], playerLabels[i][players[i].getRank() - 1]));
                }
            }
            int[] area = location.getArea();

            if (location instanceof MovieSet)
            {
                MovieSet movieSet = (MovieSet) location;
                Card card = movieSet.getCard();
                ImageIcon cardIcon;
                JLayeredPane cardPane = new JLayeredPane();
                cardPane.setLayout(null);
                cardPane.setBounds(area[0], area[1], area[2], area[3]);

                if(card != null)
                {
                    if(card.isFlipped()) 
                    {
                        cardIcon = cards.get(card.getImage());
                    }
                    else
                    {
                        cardIcon = cardBack;
                    }
                    JLabel cardLabel = new JLabel(cardIcon);
                    //cardLabel.setBounds(area[0], area[1], 205,115);
                    cardLabel.setSize(205,115);
                    cardPane.add(cardLabel, Integer.valueOf(1));

                    int[][] shots = movieSet.getShots();
                    for (int i = 0; i < movieSet.getShotCounter(); ++i)
                    {
                        JLabel shotLabel = new JLabel(shot);
                        shotLabel.setBounds(shots[i][0], shots[i][1], shots[i][2], shots[i][3]);
                        board.add(shotLabel, Integer.valueOf(3));
                    }
                }

                int offset = 0; //calculate player positions for each player in location
                int playerCount = 0;
                for (PlayerPair playerPair: playerList)
                {
                    Player player = playerPair.getPlayer();
                    JLabel playerLabel = playerPair.getLabel();

                    Part part = movieSet.getRole(player.getRole());
                    if (part != null)
                    {
                        area = part.getArea();
                        if (part.onCard())
                        {
                            playerLabel.setLocation(area[0] - 1, area[1] - 1);
                            cardPane.add(playerLabel, Integer.valueOf(3));
                        }
                        else
                        {
                            playerLabel.setBounds(area[0] + 1, area[1] + 1, 45, 45);
                            board.add(playerLabel, Integer.valueOf(3));
                        }
                    }
                    else
                    {
                        area = location.getArea();
                        if (playerCount < 4)
                        {
                            playerLabel.setBounds(area[0] - 15 + offset, area[1] + 115, 45, 45);
                            offset += 50;
                            ++playerCount;
                        }
                        else
                        {
                            playerLabel.setBounds(area[0] - 15 + (offset - 200), area[1] - 30, 45, 45);
                            offset += 50;
                        }
                        board.add(playerLabel, Integer.valueOf(3));
                    }
                }
                board.add(cardPane, Integer.valueOf(2));
            }
            else
            {
                int offset = 0;
                int playerCount = 0;
                for (PlayerPair playerPair: playerList)
                {
                    JLabel playerLabel = playerPair.getLabel();
                    area = location.getArea();
                    if (playerCount < 4)
                    {
                        playerLabel.setBounds(area[0] + 15 + offset, area[1], 45, 45);
                        offset += 50;
                        ++playerCount;
                    }
                    else
                    {
                        playerLabel.setBounds(area[0] + 15 + offset - 200, area[1] + 45, 45, 45);
                        offset += 50;
                    }
                    board.add(playerLabel, Integer.valueOf(3));
                }
            }
        }
    }

    public void displayLocation(GameBoard gameBoard, String locationName) {
    }


    //method is complete
    public void displayAct(boolean success, int roll, int money, int credits) {
        JPanel actPanel = new JPanel(new GridLayout(0, 1));
        JLabel dice1 = new JLabel(dice[roll-1]);
        JPanel topPanel = new JPanel();
        JLabel top = new JLabel();
        if (success)
        {
            top = new JLabel("Success!");
        }
        else
        {
            top = new JLabel("Failure.");
        }
        top.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        topPanel.add(top);
        TitledBorder topBorder = new TitledBorder("Acting:");
        topBorder.setTitleFont(new Font("Times New Roman", Font.PLAIN, 20));
        topPanel.setBorder(topBorder);

        JPanel bottom = new JPanel(new GridLayout(2,1));
        TitledBorder bottomBorder = new TitledBorder("Recieved:");
        bottomBorder.setTitleFont(new Font("Times New Roman", Font.PLAIN, 20));
        bottom.setBorder(bottomBorder);

        JLabel moneyLabel = new JLabel(money + " dollars.");
        moneyLabel.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        JLabel creditsLabel = new JLabel(credits + " credits.");
        creditsLabel.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        bottom.add(moneyLabel);
        bottom.add(creditsLabel);
        
        actPanel.add(topPanel);
        actPanel.add(dice1);
        actPanel.add(bottom);

        rightPanel.remove(0);
        rightPanel.add(actPanel, 0);
        rightPanel.validate();
    }

    public void displaySceneWrap(ArrayList<Player> offCardPlayers, ArrayList<Player> onCardPlayers, int[] onCardPayouts, int[] offCardPayouts) {
        JPanel wrapPanel = new JPanel();
    }

    public int[] inputUpgrade(boolean[][] availableRanks)
    {
        JPanel upgradePanel = new JPanel(new GridLayout(6, 1));
        TitledBorder upgradeBorder = new TitledBorder("Upgrades");
        upgradeBorder.setTitleFont(new Font("Times New Roman", Font.PLAIN, 15));
        upgradeBorder.setTitleJustification(TitledBorder.CENTER);
        upgradePanel.setBorder(upgradeBorder);
        Font buttonFont = new Font("Times New Roman", Font.PLAIN, 15);

        for (int i = 0; i < availableRanks.length; ++i)
        {
            JPanel rankPanel = new JPanel(new GridLayout(1, 2));

            JButton money = new JButton(i+2 + ": Money");
            money.setFont(buttonFont);
            money.addActionListener(new Listener(this));
            JButton credits = new JButton(i+2 + ": Credits");
            credits.setFont(buttonFont);
            credits.addActionListener(new Listener(this));
            rankPanel.add(money);
            rankPanel.add(credits);

            if (!availableRanks[i][0])
            {
                money.setEnabled(false);
            }
            if (!availableRanks[i][1])
            {
                credits.setEnabled(false);
            }
            upgradePanel.add(rankPanel);
        }
        JButton cancel = new JButton("Cancel");
        cancel.setFont(buttonFont);
        cancel.addActionListener(new Listener(this));
        upgradePanel.add(cancel);
        rightPanel.remove(1);
        rightPanel.add(upgradePanel);
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
            return new int[]{0, 0};
        }
        else
        {
            if (currentAction.charAt(3) == 'M') 
            {
                return new int[]{1, Integer.valueOf(currentAction.charAt(0)) - 48};
            }
            else
            {
                return new int[]{2, Integer.valueOf(currentAction.charAt(0)) - 48};
            }
        }
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
