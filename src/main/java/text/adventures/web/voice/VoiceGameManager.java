package text.adventures.web.voice;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import text.adventures.game.TextAdventuresGame;
import text.adventures.game.builder.GameBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class VoiceGameManager {

    private static final String AVAILABLE_OPTIONS = "Available moves: ";

    private final GameBuilder gameBuilder;
    private final Cache<String, TextAdventuresGame> keyToGameMap;
    private final Cache<String, UserSelectionOptions> keyToOptionsMap;

    public VoiceGameManager(GameBuilder gameBuilder) {
        this.gameBuilder = gameBuilder;
        this.keyToGameMap = Caffeine.newBuilder()
                .expireAfterWrite(20, TimeUnit.MINUTES)
                .maximumSize(1_000)
                .build();
        this.keyToOptionsMap = Caffeine.newBuilder()
                .expireAfterWrite(20, TimeUnit.MINUTES)
                .maximumSize(1_000)
                .build();
    }

    public String startGame(String gameKey) {

        TextAdventuresGame game = gameBuilder.build();
        keyToGameMap.put(gameKey, game);

        String directions = game.applyCommand("directions");

        UserSelectionOptions options = UserSelectionOptions.buildOptions(directions);
        keyToOptionsMap.put(gameKey, options);

        return String.format("%s.\n%s.", game.initialMessage(), options.niceVoiceOptions());
    }

    public CommandResult applyCommand(String gameKey, int selectedOption) {

        validateGameKey(gameKey);
        TextAdventuresGame game = keyToGameMap.get(gameKey, s -> gameBuilder.build());
        UserSelectionOptions previousOptions = keyToOptionsMap.get(gameKey, s -> null);
        validatePreviousOptionExists(gameKey, previousOptions);

        UserOption chosenOption = previousOptions.getOption(selectedOption);

        String commandResult = game.applyCommand(chosenOption.commandText);

        if (game.hasGameEnded()) {

            return new CommandResult(commandResult, true);
        } else {
            String directions = game.applyCommand("directions");

            UserSelectionOptions nextOptions = UserSelectionOptions.buildOptions(directions);
            keyToOptionsMap.put(gameKey, nextOptions);

            String finalResult = String.format("%s.\n%s\n%s.", commandResult, AVAILABLE_OPTIONS, nextOptions.niceVoiceOptions())
                    .replace("..", ".");
            return new CommandResult(finalResult, false);
        }
    }

    private void validateGameKey(String gameKey) {
        if (gameKey == null || gameKey.isEmpty()) {
            throw new IllegalArgumentException("The gameKey must not be null");
        }
    }

    private void validatePreviousOptionExists(String gameKey, UserSelectionOptions previousOptions) {

        if (previousOptions == null) {
            throw new GameNotStartedException(
                    String.format("There's no game started for the key [%s]. The request cannot be processed", gameKey));
        }
    }

    public static class CommandResult {
        private final String result;
        private final boolean ended;

        public CommandResult(String result, boolean ended) {
            this.result = result;
            this.ended = ended;
        }

        public String getResult() {
            return result;
        }

        public boolean isEnded() {
            return ended;
        }
    }

    protected static class GameNotStartedException extends RuntimeException {
        public GameNotStartedException(String message) {
            super(message);
        }
    }

    protected static class UserSelectionOptions {

        private static final UserOption DEFAULT_OPTION = new UserOption(9, "invalid");
        private final List<UserOption> options;

        private UserSelectionOptions(List<UserOption> options) {
            this.options = options;
        }

        public String niceVoiceOptions() {

            return options.stream()
                    .map(it -> String.format("Press %d : %s.", it.option, it.commandText))
                    .collect(Collectors.joining("\n"));
        }

        public UserOption getOption(int option) {

            Optional<UserOption> optionItem = options.stream()
                    .filter(it -> it.option == option)
                    .findFirst();
            return optionItem.orElse(DEFAULT_OPTION);
        }

        static UserSelectionOptions buildOptions(String directions) {

            List<UserOption> options = new ArrayList<>();
            List<String> commands = Arrays.stream(directions.split("\n"))
                    .map(it -> it.split(":")[0].trim())
                    .collect(Collectors.toList());

            for (int i = 0; i < commands.size(); i++) {

                UserOption item = new UserOption(i + 1, commands.get(i));
                options.add(item);
            }

            return new UserSelectionOptions(options);
        }
    }

    static class UserOption {

        private final int option;
        private final String commandText;

        public UserOption(int option, String commandText) {
            this.option = option;
            this.commandText = commandText;
        }

    }

}
