
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * This bank class handles the methods related to player banking for the game of
 * deadwood
 *
 * @author Ashley Spassov, Tyler Norby
 * @version 1.0
 */
public class Bank {

    int rankCredits[];
    int rankMoney[];

    public Bank() {
    }

    /**
     * Manages player upgrades, uses player data to check that upgrade is valid,
     * and then upgrades to desired rank
     *
     * @param player
     * @param rank
     * @return boolean of if the upgrade was successful or not
     */
    public boolean upgrade(Player player, int rank) {
        return true;
    }

    public void calculateSceneWrapPayouts(MovieSet movieSet, Player[] allPlayers) {
        System.out.println("Calculating payouts for scene wrap at " + movieSet.getName());

        Card sceneCard = movieSet.getCard();
        if (sceneCard == null) {
            System.out.println("Error: No scene card on this movie set.");
            return;
        }

        int budget = sceneCard.getBudget();
        System.out.println("Scene budget: $" + budget);

        List<Player> onCardPlayers = new ArrayList<>();
        List<Player> extraPlayers = new ArrayList<>();

        // Identify players at this movie set with roles
        for (Player player : allPlayers) {
            if (player.getLocation().equals(movieSet.getName()) && player.getRole() != null) {
                Part playerRole = movieSet.getRole(player.getRole());
                if (playerRole != null) {
                    if (playerRole.onCard()) {
                        onCardPlayers.add(player);
                    } else {
                        extraPlayers.add(player);
                    }
                }
            }
        }

        if (!onCardPlayers.isEmpty()) {
            onCardPlayers.sort(Comparator.comparingInt(Player::getRank).reversed());

            // Roll dice equal to the budget
            List<Integer> diceRolls = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < budget; i++) {
                diceRolls.add(random.nextInt(1, 7));
            }
            // Sort dice rolls in descending order
            diceRolls.sort(Comparator.comparingInt(Integer::intValue).reversed());

            System.out.println("On-card player payouts (sorted by rank):");
            for (int i = 0; i < onCardPlayers.size(); i++) {
                Player player = onCardPlayers.get(i);
                int payout = 0;
                for (int j = i; j < diceRolls.size(); j += onCardPlayers.size()) {
                    payout += diceRolls.get(j);
                }
                givePayout(player, payout);
                player.setRole(null);
            }
        } else {
            System.out.println("No on-card players to receive payouts.");
        }

        if (!extraPlayers.isEmpty() && !onCardPlayers.isEmpty()) {
            System.out.println("Extra player payouts:");
            for (Player player : extraPlayers) {

                Part playerRole = movieSet.getRole(player.getRole());
                if (playerRole != null) {
                    int payout = playerRole.getRank();
                    givePayout(player, payout);
                }

                player.setRole(null);
            }
        } else if (!extraPlayers.isEmpty() && onCardPlayers.isEmpty()) {
            System.out.println("Extra players do not receive a scene wrap bonus because there were no on-card players.");
            for (Player player : extraPlayers) {
                player.setRole(null);
            }
        }

        System.out.println("Scene wrap payouts complete.");
    }

    public void givePayout(Player player, int amount) {
        player.addMoney(amount);
        System.out.println(player.getName() + " received $" + amount + ".");
    }
}
