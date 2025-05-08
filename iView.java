public interface iView extends iInput{
    public void displayBoard(GameBoard gameBoard, Player activePlayer, Player[] players);
    public String inputLocation(String[] validLocations);
}
