package text.adventures.web.voice;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import text.adventures.game.builder.StaticGameBuilder;
import text.adventures.web.voice.VoiceGameManager.CommandResult;
import text.adventures.web.voice.VoiceGameManager.GameNotStartedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VoiceGameManagerTest {

    private VoiceGameManager gameManager;

    @BeforeEach
    public void init() {
        StaticGameBuilder gameBuilder = new StaticGameBuilder();
        this.gameManager = new VoiceGameManager(gameBuilder);
    }

    @Test
    public void nullGameKeyShouldReturnException() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameManager.applyCommand(null, 1);
        });
    }

    @Test
    public void emptyGameKeyShouldReturnException() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameManager.applyCommand("", 1);
        });
    }

    @Test
    public void notStartedExceptionIfGameHasNotStarted() {
        assertThrows(GameNotStartedException.class, () -> {
            gameManager.applyCommand("key", 1);
        });
    }

    @Test
    public void canStartGame() {
        String result = gameManager.startGame("key");

        assertThat(result, containsStringIgnoringCase("welcome"));
    }

    @Test
    public void canChooseOptionsAfterStartingGame() {
        gameManager.startGame("key");
        CommandResult result = gameManager.applyCommand("key", 2);

        assertThat(result.getResult(), Matchers.containsStringIgnoringCase("north"));
    }


}
