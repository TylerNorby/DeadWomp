import java.util.ArrayList;
import java.util.List;

public class GraphicView implements iView{
    GameBoard gameBoard;

    public GraphicView(GameBoard gameBoard)
    {
        this.gameBoard = gameBoard;
    }

    public void displayBoard()
    {

    }

    @Override
    public int inputPlayerCount() {
        // TODO 
        throw new UnsupportedOperationException("Unimplemented method 'inputPlayerCount'");
    }

    @Override
    public String[] inputNames(int playerCount) {
        // TODO 
        throw new UnsupportedOperationException("Unimplemented method 'inputNames'");
    }

    @Override
    public Action inputAction(ArrayList<Action> validActions) {
        // TODO 
        throw new UnsupportedOperationException("Unimplemented method 'inputAction'");
    }

    @Override
    public String inputLocation(String[] validLocations) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inputLocation'");
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
    public void displayBoard(GameBoard gameBoard, Player activePlayer, Player[] activePlayers) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayBoard'");
    }

    @Override
    public void displayLocation(GameBoard gameBoard, String location) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayLocation'");
    }

    @Override
    public void displayAct(boolean success, int money, int credits) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayAct'");
    }

    @Override
    public void displaySceneWrap(ArrayList<Player> offCardPlayers, ArrayList<Player> onCardPlayers, int[] onCardPayouts,
            int[] offCardPayouts) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displaySceneWrap'");
    }

    @Override
    public int inputUpgrade(Player player, int[] moneyCost, int[] creditCost) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inputRank'");
    }
}
