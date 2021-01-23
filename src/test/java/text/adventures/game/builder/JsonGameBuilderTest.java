package text.adventures.game.builder;

import text.adventures.game.TextAdventuresGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import text.adventures.web.application.WebApplication;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JsonGameBuilderTest {

    private JsonGameBuilder  gameBuilder;

    @BeforeEach
    public void init() {
        InputStream is = readFromResources();
        this.gameBuilder = new JsonGameBuilder(is);
    }

    @Test
    public void testIt() {
        TextAdventuresGame game = gameBuilder.build();
        assertWinning(game);
    }

    private void assertWinning(TextAdventuresGame game) {
        assertThat(game.applyCommand("go south"), containsStringIgnoringCase("not go"));
        assertThat(game.applyCommand("go north"), containsStringIgnoringCase("north"));
        assertThat(game.applyCommand("go east"), containsStringIgnoringCase("east"));
        assertThat(game.applyCommand("take iron key"), containsStringIgnoringCase("iron key"));
        assertThat(game.applyCommand("go west"), containsStringIgnoringCase("north"));
        assertThat(game.applyCommand("go west"), containsStringIgnoringCase("north west"));
        assertThat(game.applyCommand("take infinity sword"), containsStringIgnoringCase("infinity sword"));
        assertThat(game.applyCommand("go east"), containsStringIgnoringCase("north"));
        assertThat(game.applyCommand("go south"), containsStringIgnoringCase("initial"));
        assertThat(game.applyCommand("use iron key"), containsStringIgnoringCase("south"));
        assertThat(game.applyCommand("go south"), containsStringIgnoringCase("south"));
        assertThat(game.applyCommand("go south"), containsStringIgnoringCase("tiger"));
        assertThat(game.applyCommand("go east"), containsStringIgnoringCase("not go"));
        assertThat(game.applyCommand("use infinity sword"), containsStringIgnoringCase("east"));
        assertThat(game.applyCommand("go east"), containsStringIgnoringCase("congratulations"));
    }

    private InputStream readFromResources() {
        return WebApplication.class
                .getClassLoader()
                .getResourceAsStream("basicGame.json");
    }

}
