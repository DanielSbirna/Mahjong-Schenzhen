package singleton;

public class GameController {

    private static GameController instance;

    private GameController() {}

    public static GameController getInstance() {
        if(instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public void startGame() {
        System.out.println("Game Started");
    }

    public void pauseGame() {
        System.out.println("Game paused");
    }
}

