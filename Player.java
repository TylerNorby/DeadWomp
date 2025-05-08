
import java.util.Random;

public class Player {
    String name;
    String location;
    String role;
    int rank;
    int money;
    int credits;
    int practiceChips;

    public Player(String name, int rank, int credits) {
        this.name = name;

        this.rank = rank;
        this.money = 0;
        this.credits = credits;
    }

    /**
     * Prompt player to choose action for current turn.
     */
    public void chooseAction() {
        String action = "";
        switch (action) {
            case "move":
                break;
            case "act":
                break;
            case "rehearse":
                break;
            case "upgrade":
                break;
        }
    }

    /**
     * Returns random number from 1-6.
     *
     * @return
     */
    private int rollDie() {
        return new Random().nextInt(1, 7);
    }

    private boolean move() {
        return true;
    }

    private boolean act() {
        return true;
    }

    private boolean rehearse() {
        return true;
    }

    private boolean upgrade(int rank, boolean useCredits) {
        return true;
    }

    public int getMoney(){
        return money;
    }

    public int getCredits(){
        return credits;
    }

    public int getRank(){
        return rank;
    }

    public String getLocation(){
        return location;
    }

    public String getRole(){
        return role;
    }

}
