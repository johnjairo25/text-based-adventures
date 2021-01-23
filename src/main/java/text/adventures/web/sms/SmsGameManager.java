package text.adventures.web.sms;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import text.adventures.game.TextAdventuresGame;
import text.adventures.game.builder.GameBuilder;

import java.util.concurrent.TimeUnit;

public class SmsGameManager {

    private static final String START_GAME_COMMAND = "start text.adventures.game";

    private final GameBuilder gameBuilder;
    private final Cache<String, TextAdventuresGame> keyToGameMap;

    public SmsGameManager(GameBuilder gameBuilder) {
        this.gameBuilder = gameBuilder;
        this.keyToGameMap = Caffeine.newBuilder()
                .expireAfterWrite(20, TimeUnit.MINUTES)
                .maximumSize(1_000)
                .build();
    }

    public String applyCommand(String gameKey, String commandText) {
        if (isNullOrEmpty(gameKey)) {
            throw new IllegalArgumentException("gameKey must not be null or empty");
        }
        if (commandText == null) {
            throw new IllegalArgumentException("gameKey must not be null or empty");
        }

        boolean createNewGame = START_GAME_COMMAND.equalsIgnoreCase(commandText.trim())
                || keyToGameMap.getIfPresent(gameKey) == null;

        TextAdventuresGame game = keyToGameMap.get(gameKey, a -> gameBuilder.build());
        keyToGameMap.put(gameKey, game);

        return createNewGame ? game.getInitialMessageWithInstructions() : game.applyCommand(commandText);
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

}
