package game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import game.elements.Location;

public class WinAndLoseTest extends MapFixture {

    @Test
    public void winGameMessage() {
        initialToUseSword();

        String result = game.applyCommand("go east");

        Assertions.assertEquals(Location.WIN_GAME_MSG, result);
    }

    @Test
    public void noCommandsAfterWinning() {
        initialToUseSword();
        game.applyCommand("go east");

        String result = game.applyCommand("go west");

        Assertions.assertEquals(Location.ALREADY_WON, result);
    }

    @Test
    public void loseGameMessage() {
        initialToUseSword();

        String result = game.applyCommand("go west");

        Assertions.assertEquals(Location.LOSE_GAME_MSG, result);
    }

    @Test
    public void noCommandsAfterLosing() {
        initialToUseSword();
        game.applyCommand("go east");

        String result = game.applyCommand("go west");

        Assertions.assertEquals(Location.ALREADY_LOST, result);
    }


}
