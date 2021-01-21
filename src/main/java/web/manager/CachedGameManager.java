package web.manager;

import game.TextAdventuresGame;
import game.builder.GameBuilder;

import java.util.HashMap;
import java.util.Map;

public class CachedGameManager {

    private static final String WELCOME_MESSAGE = "Welcome to the Text Based Adventures World.";
    private static final String START_GAME_COMMAND = "start game";

    private final GameBuilder gameBuilder;
    private final Map<String, TextAdventuresGame> keyToGameMap;

    public CachedGameManager(GameBuilder gameBuilder) {
        this.gameBuilder = gameBuilder;
        this.keyToGameMap = new HashMap<>();
    }

    public String applyCommand(String gameKey, String commandText) {
        if (isNullOrEmpty(gameKey)) {
            throw new IllegalArgumentException("gameKey must not be null or empty");
        }
        if (isNullOrEmpty(commandText)) {
            throw new IllegalArgumentException("gameKey must not be null or empty");
        }

        boolean createNewGame = !keyToGameMap.containsKey(gameKey)
                || START_GAME_COMMAND.equalsIgnoreCase(commandText.trim());

        TextAdventuresGame game = createNewGame ? gameBuilder.build() : keyToGameMap.get(gameKey);
        keyToGameMap.put(gameKey, game);

        return createNewGame ?
                String.format("%s\n%s", WELCOME_MESSAGE, game.startGame())
                : game.applyCommand(commandText);
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

}
