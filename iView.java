import java.util.ArrayList;
import java.util.List;

public interface iView extends iInput {
    public void displayBoard(int day, int totalDays, GameBoard gameBoard, Player activePlayer, Player[] players);
    public void displayRoles(List<Part> availableRoles, List<Part> unavailableRoles);
    public void displayLocation(GameBoard gameBoard, String locationName);
    public void displayAct(boolean success, int money, int credits);
    public void displaySceneWrap(ArrayList<Player> offCardPlayers, ArrayList<Player> onCardPlayers, int[] onCardPayouts, int[] offCardPayouts);
    public void displayScores(Player[] players, int[] playerScores);
}
