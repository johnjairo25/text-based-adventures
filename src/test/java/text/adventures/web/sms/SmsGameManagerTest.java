package text.adventures.web.sms;

import text.adventures.game.builder.StaticGameBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SmsGameManagerTest {

    private SmsGameManager gameManager;

    @BeforeEach
    public void init() {
        gameManager = new SmsGameManager(new StaticGameBuilder());
    }

    // empty text.adventures.game key should return an exception
    @Test
    public void emptyGameKeyShouldReturnException() {
        assertThrows(IllegalArgumentException.class, () -> gameManager.applyCommand("", "go north"));
    }

    @Test
    public void nullGameKeyShouldReturnException() {
        assertThrows(IllegalArgumentException.class, () -> gameManager.applyCommand(null, "go north"));
    }

    // null command text should return an exception
    @Test
    public void nullCommandShouldReturnException() {
        assertThrows(IllegalArgumentException.class, () -> gameManager.applyCommand("one", null));
    }

    @Test
    public void emptyCommandShouldBeProcessed() {
        assertNotNull(gameManager.applyCommand("one", ""));
    }

    @Test
    public void canHandleOneGame() {
        assertThat(gameManager.applyCommand("one", "start text.adventures.game"), containsStringIgnoringCase("Welcome"));
        assertThat(gameManager.applyCommand("one", "go south"), containsStringIgnoringCase("not go"));
        assertThat(gameManager.applyCommand("one", "go north"), containsStringIgnoringCase("north"));
        assertThat(gameManager.applyCommand("one", "go east"), containsStringIgnoringCase("east"));
        assertThat(gameManager.applyCommand("one", "take " + StaticGameBuilder.KEY_NAME), containsStringIgnoringCase(StaticGameBuilder.KEY_NAME));
        assertThat(gameManager.applyCommand("one", "go west"), containsStringIgnoringCase("north"));
        assertThat(gameManager.applyCommand("one", "go west"), containsStringIgnoringCase("northwest"));
        assertThat(gameManager.applyCommand("one", "take " + StaticGameBuilder.SWORD_NAME), containsStringIgnoringCase(StaticGameBuilder.SWORD_NAME));
        assertThat(gameManager.applyCommand("one", "go east"), containsStringIgnoringCase("north"));
        assertThat(gameManager.applyCommand("one", "go south"), containsStringIgnoringCase("initial"));
        assertThat(gameManager.applyCommand("one", "use " + StaticGameBuilder.KEY_NAME), containsStringIgnoringCase("south"));
        assertThat(gameManager.applyCommand("one", "go south"), containsStringIgnoringCase("south"));
        assertThat(gameManager.applyCommand("one", "go south"), containsStringIgnoringCase("southName"));
        assertThat(gameManager.applyCommand("one", "go east"), containsStringIgnoringCase("not go"));
        assertThat(gameManager.applyCommand("one", "use " + StaticGameBuilder.SWORD_NAME), containsStringIgnoringCase("east"));
        assertThat(gameManager.applyCommand("one", "go east"), containsStringIgnoringCase("congratulations"));
    }

    @Test
    public void canHandleTwoGamesAtTheSameTime() {
        assertThat(gameManager.applyCommand("one", "start text.adventures.game"), containsStringIgnoringCase("Welcome"));
        assertThat(gameManager.applyCommand("two", "start text.adventures.game"), containsStringIgnoringCase("Welcome"));

        assertThat(gameManager.applyCommand("one", "go south"), containsStringIgnoringCase("not go"));
        assertThat(gameManager.applyCommand("two", "go south"), containsStringIgnoringCase("not go"));

        assertThat(gameManager.applyCommand("one", "go north"), containsStringIgnoringCase("north"));
        assertThat(gameManager.applyCommand("two", "go north"), containsStringIgnoringCase("north"));

        assertThat(gameManager.applyCommand("one", "go east"), containsStringIgnoringCase("east"));
        assertThat(gameManager.applyCommand("two", "go east"), containsStringIgnoringCase("east"));

        assertThat(gameManager.applyCommand("one", "take " + StaticGameBuilder.KEY_NAME), containsStringIgnoringCase(StaticGameBuilder.KEY_NAME));
        assertThat(gameManager.applyCommand("two", "take " + StaticGameBuilder.KEY_NAME), containsStringIgnoringCase(StaticGameBuilder.KEY_NAME));

        assertThat(gameManager.applyCommand("one", "go west"), containsStringIgnoringCase("north"));
        assertThat(gameManager.applyCommand("two", "go west"), containsStringIgnoringCase("north"));

        assertThat(gameManager.applyCommand("one", "go west"), containsStringIgnoringCase("northwest"));
        assertThat(gameManager.applyCommand("two", "go west"), containsStringIgnoringCase("northwest"));

        assertThat(gameManager.applyCommand("one", "take " + StaticGameBuilder.SWORD_NAME), containsStringIgnoringCase(StaticGameBuilder.SWORD_NAME));
        assertThat(gameManager.applyCommand("two", "take " + StaticGameBuilder.SWORD_NAME), containsStringIgnoringCase(StaticGameBuilder.SWORD_NAME));

        assertThat(gameManager.applyCommand("one", "go east"), containsStringIgnoringCase("north"));
        assertThat(gameManager.applyCommand("two", "go east"), containsStringIgnoringCase("north"));

        assertThat(gameManager.applyCommand("one", "go south"), containsStringIgnoringCase("initial"));
        assertThat(gameManager.applyCommand("two", "go south"), containsStringIgnoringCase("initial"));

        assertThat(gameManager.applyCommand("one", "use " + StaticGameBuilder.KEY_NAME), containsStringIgnoringCase("south"));
        assertThat(gameManager.applyCommand("two", "use " + StaticGameBuilder.KEY_NAME), containsStringIgnoringCase("south"));

        assertThat(gameManager.applyCommand("one", "go south"), containsStringIgnoringCase("south"));
        assertThat(gameManager.applyCommand("two", "go south"), containsStringIgnoringCase("south"));

        assertThat(gameManager.applyCommand("one", "go south"), containsStringIgnoringCase("southName"));
        assertThat(gameManager.applyCommand("two", "go south"), containsStringIgnoringCase("southName"));

        assertThat(gameManager.applyCommand("one", "go east"), containsStringIgnoringCase("not go"));
        assertThat(gameManager.applyCommand("two", "go east"), containsStringIgnoringCase("not go"));

        assertThat(gameManager.applyCommand("one", "use " + StaticGameBuilder.SWORD_NAME), containsStringIgnoringCase("east"));
        assertThat(gameManager.applyCommand("two", "use " + StaticGameBuilder.SWORD_NAME), containsStringIgnoringCase("east"));

        assertThat(gameManager.applyCommand("one", "go east"), containsStringIgnoringCase("congratulations"));
        assertThat(gameManager.applyCommand("two", "go east"), containsStringIgnoringCase("congratulations"));
    }

}
