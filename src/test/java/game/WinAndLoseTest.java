package game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import game.elements.Location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WinAndLoseTest extends MapFixture {

    @Test
    public void winGameMessage() {
        initialToUseSword();

        String result = game.applyCommand("go east");

        assertEquals(Location.WIN_GAME_MSG, result);
        assertTrue(game.hasGameEnded());
    }

    @Test
    public void noCommandsAfterWinning() {
        initialToUseSword();
        game.applyCommand("go east");

        String result = game.applyCommand("go west");

        assertEquals(Location.ALREADY_WON, result);
        assertTrue(game.hasGameEnded());
    }

    @Test
    public void loseGameMessage() {
        initialToUseSword();

        String result = game.applyCommand("go west");

        assertEquals(Location.LOSE_GAME_MSG, result);
        assertTrue(game.hasGameEnded());
    }

    @Test
    public void noCommandsAfterLosing() {
        initialToUseSword();
        game.applyCommand("go east");

        String result = game.applyCommand("go west");

        assertEquals(Location.ALREADY_LOST, result);
        assertTrue(game.hasGameEnded());
    }


}
