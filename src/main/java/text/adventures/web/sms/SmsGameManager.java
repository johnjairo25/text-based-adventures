package text.adventures.web.sms;

import text.adventures.game.TextAdventuresGame;
import text.adventures.game.builder.GameBuilder;

import java.util.HashMap;
import java.util.Map;

public class SmsGameManager {

    private static final String START_GAME_COMMAND = "start text.adventures.game";

    private final GameBuilder gameBuilder;
    private final Map<String, TextAdventuresGame> keyToGameMap;

    public SmsGameManager(GameBuilder gameBuilder) {
        this.gameBuilder = gameBuilder;
        this.keyToGameMap = new HashMap<>();
    }

    public String applyCommand(String gameKey, String commandText) {
        if (isNullOrEmpty(gameKey)) {
            throw new IllegalArgumentException("gameKey must not be null or empty");
        }
        if (commandText == null) {
            throw new IllegalArgumentException("gameKey must not be null or empty");
        }

        boolean createNewGame = !keyToGameMap.containsKey(gameKey)
                || START_GAME_COMMAND.equalsIgnoreCase(commandText.trim());

        TextAdventuresGame game = createNewGame ? gameBuilder.build() : keyToGameMap.get(gameKey);
        keyToGameMap.put(gameKey, game);

        return createNewGame ? game.getInitialMessageWithInstructions() : game.applyCommand(commandText);
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

}
