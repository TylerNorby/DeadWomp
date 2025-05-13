import java.util.ArrayList;

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
    public void displayBoard(GameBoard gameBoard, Player activePlayer, Player[] players) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayBoard'");
    }

    @Override
    public String inputLocation(String[] validLocations) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inputLocation'");
    }
}
