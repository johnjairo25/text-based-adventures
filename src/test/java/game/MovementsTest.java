package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovementsTest extends MapFixture {

    @Test
    public void startGameShowsInitialMessage() {
        String result = game.startGame();

        assertTrue(result.contains(TextAdventuresGame.WELCOME_MSG));
        assertTrue(result.contains(initial.getMessage()));
    }

    @Test
    public void goInvalidDirection() {
        String result = game.applyCommand("go west");

        assertEquals(TextAdventuresGame.NO_NEXT_LOCATION_MSG, result);
        assertEquals(initial, game.getCurrentLocation());
    }

    @Test
    public void goNorthCorrectly() {
        String result = game.applyCommand("go north");

        assertEquals(north.getMessage(), result);
        assertEquals(north, game.getCurrentLocation());
    }

    @Test
    public void doTwoMovementsCorrectly() {
        game.applyCommand("go north");
        String result = game.applyCommand("go east");

        assertEquals(northEastWithKey.getMessage(), result);
        assertEquals(northEastWithKey, game.getCurrentLocation());
    }

    @Test
    public void goToBlockedDirection() {
        String result = game.applyCommand("go south");

        assertTrue(result.contains(TextAdventuresGame.NO_NEXT_LOCATION_MSG), "Result should contain the NO_NEXT_LOCATION message");
        assertTrue(result.contains(southBlocker.getDescription()), "Result should contain the Blocker description");
    }

    @Test
    public void cannotUseEquipablesNotPickedUp() {
        String result = game.applyCommand("use " + keyName);

        assertTrue(String.format(TextAdventuresGame.NOT_USE_MSG, keyName).equalsIgnoreCase(result));
    }

    @Test
    public void cannotUseEquipableInLocation() {
        initialPickupKey();

        String result = game.applyCommand("use " + keyName);

        assertEquals(String.format(TextAdventuresGame.NOT_USE_IN_LOCATION_MSG, keyName.toLowerCase()), result);
    }

    @Test
    public void unblockAndMoveToDirection() {
        initialPickupKeyInitial();

        String useCommand = game.applyCommand("use " + keyName);
        String moveToBlockedDirection = game.applyCommand("go south");

        assertEquals(String.format("You used the %s and now you can go to the south", keyName.toLowerCase()), useCommand);
        assertEquals(southBlockedByDoor.getMessage(), moveToBlockedDirection);
    }


}
