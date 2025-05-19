
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * This bank class handles the methods related to player banking for the game of
 * deadwood
 *
 * @author Ashley Spassov, Tyler Norby
 * @version 1.0
 */
public class Bank {

    iView view;
    public Bank(iView view)
    {
        this.view = view;
    }
    /**
     * Calculate payout for list of players with certain movie budget
     * @param movieBudget
     * @param players
     */
    public void sceneWrapPayouts(int budget, ArrayList<Player> onCardPlayers, ArrayList<Player> extraPlayers)
    {
        // Roll dice equal to the budget
        int[] diceRolls = new int[budget];
        int[] onCardPayouts = new int[onCardPlayers.size()];
        int[] offCardPayouts = new int[extraPlayers.size()];

        onCardPlayers.sort(Comparator.comparingInt(Player::getRank).reversed()); //sort descending rank order for payout

        Random random = new Random();
        for (int i = 0; i < budget; i++) {
            diceRolls[i] = random.nextInt(1,7);
        }
        Arrays.sort(diceRolls);

        // On card player bonus
        int j = 0;
        for (int i = 0; i < onCardPayouts.length; ++i)
        {
            onCardPayouts[i] = 0;
        }
        for (int i = 0; i < offCardPayouts.length; ++i)
        {
            offCardPayouts[i] = 0;
        }
        if (onCardPlayers.size() > 0)
        {
            for (int i = 0; i < diceRolls.length; ++i) {
                if (j == onCardPlayers.size())
                {
                    j = 0;
                }
                onCardPayouts[j] += diceRolls[i];
                onCardPlayers.get(j).addMoney(diceRolls[i]);
                ++j;
            }
        }
        for (int i = 0; i < extraPlayers.size(); ++i)
        {
            offCardPayouts[i] += extraPlayers.get(i).getRank();
            extraPlayers.get(i).addMoney(extraPlayers.get(i).getRank());
        }
        view.displaySceneWrap(onCardPlayers, extraPlayers, onCardPayouts, offCardPayouts);
    }

    /**
     * Payout for end of turn
     * @param player
     * @param success
     * @param onCard
     */
    public void turnPayout(Player player, boolean success, boolean onCard) {
        int credits = 0;
        int money = 0;
        if (success)
        {
            if (onCard)
            {
                credits = 2;
            }
            else
            {
                credits = 1;
                money = 1; 
            }
        }
        else
        {
            if (!onCard)
            {
                money = 1; 
            }
        }
        view.displayAct(success, money, credits);
        player.addCredits(credits);
        player.addMoney(money);
    }
}
