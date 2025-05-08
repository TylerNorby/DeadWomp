
/**
 * MovieSet object is a type of location that contains scenes and various other data points
 *
 * @author Ashley Spassov, Tyler Norby
 * @version 1.0
 */
public class MovieSet extends Location {

    Scene scene;
    Role[] extras;

    public MovieSet(String name, Scene scene, Role[] extras) {
        super(name);
    }

    /**
     * Determins if a players acting roll was successful
     *
     * @param playerRoll
     * @return boolean telling if player met the threshhold to successfully act
     */
    public boolean actingSuccess(int playerRoll) {
        return playerRoll >= scene.budget;
    }

    /**
     * Validates that a desired role is available to be taken by a player
     *
     * @param role
     * @param rank
     * @return boolean stating if the role is valid or not
     */
    public boolean validateRole(String role, int rank) {
        Role extraRole = roleCheck(extras, rank, role);
        if (extraRole != null) {
            return !extraRole.inUse;
        }
        Role sceneRole = roleCheck(scene.roles, rank, role);
        if (sceneRole != null) {
            return !sceneRole.inUse;
        }
        return false;
    }

    /**
     * Helper class for validate roll, determines
     *
     * @param roleList[]
     * @param rank
     * @param role
     *
     * @return returns a valid role on the given scene or movie set
     */
    private Role roleCheck(Role[] roleList, int rank, String role) {
        for (int i = 0; i < scene.roles.length; i++) {
            if (roleList[i].getName().equals(role) && rank >= roleList[i].getRank()) {
                return roleList[i];
            }
        }
        return null;
    }
}
