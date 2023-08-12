package Client.Controllers;

import Shared.GameSession;

import java.io.IOException;

public interface GameController {
    public void initializeGame(GameSession gameSession);

    public void updateGame(GameSession gameSession) throws IOException;
}
