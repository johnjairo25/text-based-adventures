package text.adventures.web.voice;

import text.adventures.game.TextAdventuresGame;
import text.adventures.game.builder.GameBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class VoiceGameManager {

    private static final String AVAILABLE_OPTIONS = "Available moves: ";

    private final GameBuilder gameBuilder;
    private final Map<String, TextAdventuresGame> keyToGameMap;
    private final Map<String, UserSelectionOptions> keyToOptionsMap;

    public VoiceGameManager(GameBuilder gameBuilder) {
        this.gameBuilder = gameBuilder;
        this.keyToGameMap = new HashMap<>();
        this.keyToOptionsMap = new HashMap<>();
    }

    public String startGame(String gameKey) {

        TextAdventuresGame game = gameBuilder.build();
        keyToGameMap.put(gameKey, game);

        String directions = game.applyCommand("directions");

        UserSelectionOptions options = UserSelectionOptions.buildOptions(directions);
        keyToOptionsMap.put(gameKey, options);

        return String.format("%s.\n%s.", game.initialMessage(), options.niceVoiceOptions());
    }

    public String applyCommand(String gameKey, int selectedOption) {

        TextAdventuresGame game = getOrCreateGame(gameKey);
        UserSelectionOptions previousOptions = keyToOptionsMap.get(gameKey);
        validatePreviousOptionExists(gameKey, previousOptions);

        UserOption chosenOption = previousOptions.getOption(selectedOption);

        String commandResult = game.applyCommand(chosenOption.commandText);

        if (game.hasGameEnded()) {

            return commandResult;
        } else {
            String directions = game.applyCommand("directions");

            UserSelectionOptions nextOptions = UserSelectionOptions.buildOptions(directions);
            keyToOptionsMap.put(gameKey, nextOptions);

            return String.format("%s.\n%s\n%s.", commandResult, AVAILABLE_OPTIONS, nextOptions.niceVoiceOptions())
                    .replace("..", ".");
        }
    }

    public boolean hasGameEnded(String gameKey) {

        TextAdventuresGame game = getOrCreateGame(gameKey);
        return game.hasGameEnded();
    }

    private void validatePreviousOptionExists(String gameKey, UserSelectionOptions previousOptions) {

        if (previousOptions == null) {
            throw new GameNotStartedException(
                    String.format("There's no text.adventures.game started for the key [%s]. The request cannot be processed", gameKey));
        }
    }

    private TextAdventuresGame getOrCreateGame(String gameKey) {

        TextAdventuresGame game = this.keyToGameMap.containsKey(gameKey) ?
                this.keyToGameMap.get(gameKey)
                : gameBuilder.build();
        keyToGameMap.put(gameKey, game);

        return game;
    }

    static class GameNotStartedException extends RuntimeException {
        public GameNotStartedException(String message) {
            super(message);
        }
    }

    static class UserSelectionOptions {

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
