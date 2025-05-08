
import java.util.Random;

public class Player {
    public String name;
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

    public int rollDice()
    {
        return new Random().nextInt(1,7);
    }

    public String getName() {return name;}
    public String getLocation() {return location;}
    public String getRole() {return role;}
    public int getRank() {return rank;}
    public int getMoney() {return money;} 
    public int getCredits() {return credits;}
    public int getChips() {return practiceChips;}
    
    public void setLocation(String location) {this.location = location;}
    public void setRole(String role) {this.role = role;}
    public void setRank(int rank) {this.rank = rank;}
    public void addMoney(int amt){this.money += amt;}
    public void removeMoney(int amt){this.money -= amt;}
    public void setChips(int chips){this.practiceChips = chips;}
}
