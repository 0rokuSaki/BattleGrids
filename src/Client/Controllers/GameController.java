package Client.Controllers;

import Shared.GameSession;

public interface GameController {
    void initializeGame(GameSession gameSession);

    void updateGame(GameSession gameSession);

    void terminateGame(String message);
}
