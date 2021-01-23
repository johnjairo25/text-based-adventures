package game.builder;

import game.TextAdventuresGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public class StaticGameBuilderTest {

    private StaticGameBuilder gameBuilder;

    @BeforeEach
    public void init() {
        this.gameBuilder = new StaticGameBuilder();
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
        assertThat(game.applyCommand("take " + StaticGameBuilder.KEY_NAME), containsStringIgnoringCase(StaticGameBuilder.KEY_NAME));
        assertThat(game.applyCommand("go west"), containsStringIgnoringCase("north"));
        assertThat(game.applyCommand("go west"), containsStringIgnoringCase("northwest"));
        assertThat(game.applyCommand("take " + StaticGameBuilder.SWORD_NAME), containsStringIgnoringCase(StaticGameBuilder.SWORD_NAME));
        assertThat(game.applyCommand("go east"), containsStringIgnoringCase("north"));
        assertThat(game.applyCommand("go south"), containsStringIgnoringCase("initial"));
        assertThat(game.applyCommand("use " + StaticGameBuilder.KEY_NAME), containsStringIgnoringCase("south"));
        assertThat(game.applyCommand("go south"), containsStringIgnoringCase("south"));
        assertThat(game.applyCommand("go south"), containsStringIgnoringCase("southName"));
        assertThat(game.applyCommand("go east"), containsStringIgnoringCase("not go"));
        assertThat(game.applyCommand("use " + StaticGameBuilder.SWORD_NAME), containsStringIgnoringCase("east"));
        assertThat(game.applyCommand("go east"), containsStringIgnoringCase("congratulations"));
    }

}
