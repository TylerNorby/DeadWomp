import java.util.List;

public interface iView extends iInput{
    public void displayBoard(GameBoard gameBoard, Player activePlayer, Player[] players);
    public void displayAvailableRoles(List<Part> roles);
    public void displayLocation(GameBoard gameBoard, String locationName);
}
