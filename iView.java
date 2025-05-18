import java.util.List;

public interface iView extends iInput{
    public void displayBoard(GameBoard gameBoard, Player activePlayer, Player[] players);
    public void displayRoles(List<Part> availableRoles, List<Part> unavailableRoles);
    public void displayLocation(GameBoard gameBoard, String locationName);
}
