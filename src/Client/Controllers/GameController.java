package Client.Controllers;

import Shared.GameSession;

public interface GameController {
    public void initializeGame(GameSession gameSession);

    public void updateGame(GameSession gameSession);
}
