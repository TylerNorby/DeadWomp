
public class MovieSet extends Location {

    Scene scene;
    Role[] extras;

    public MovieSet(String name, Scene scene, Role[] extras) {
        super(name);
    }

   
    public boolean actingSuccess(int playerRoll){
        return playerRoll >= scene.budget;
    }

    // Checks if input roll is extra or on SceneCard, also compares Rank. Crappy implementation this was done at midnight
    public boolean validateRole(String role, int rank) {
        Role extraRole = roleCheck(extras, rank, role);
        if(extraRole != null){
            return !extraRole.inUse;
        }
        Role sceneRole = roleCheck(scene.roles, rank, role);
        if(sceneRole != null){
            return !sceneRole.inUse;
        }
        return false;
    }


    // Remove inUse from here, add it to checks above
    private Role roleCheck(Role[] roleList, int rank, String role) {
        for (int i = 0; i < scene.roles.length; i++) {
            if (roleList[i].getName().equals(role) && rank >= roleList[i].getRank()) {
                return roleList[i];
            }
        }
        return null;
    }
}
