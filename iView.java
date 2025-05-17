import java.util.List;

public interface iView extends iInput{
    public void displayBoard(GameBoard gameBoard, Player activePlayer, Player[] players);
    public String inputLocation(String[] validLocations);
      public void displayAvailableRoles(List<Part> roles);
    public String inputRoleChoice(List<Part> availableRoles);
}
