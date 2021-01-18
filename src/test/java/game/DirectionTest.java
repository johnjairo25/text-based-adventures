package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectionTest extends MapFixture {

    @Test
    public void movementDirections() {
        game.applyCommand("go north"); // go to basic location

        String directions = game.applyCommand("directions");

        assertTrue(directions.contains("go south"),
                String.format("Actual directions: \n %s", directions));
        assertTrue(directions.contains("go east"),
                String.format("Actual directions: \n %s", directions));
        assertTrue(directions.contains("go west"),
                String.format("Actual directions: \n %s", directions));
    }

    @Test
    public void movementDirectionsWithBlocker() {
        String directions = game.applyCommand("directions");

        assertTrue(directions.contains("go north"),
                String.format("Actual directions: \n %s", directions));
        assertTrue(directions.contains("go south : Blocked"),
                String.format("Actual directions: \n %s", directions));
        assertTrue(directions.contains(southBlocker.getDescription()),
                String.format("Actual directions: \n %s", directions));
    }

    @Test
    public void movementDirectionsUnblocked() {
        initialPickupKeyInitial();
        game.applyCommand("use " + keyName);

        String directions = game.applyCommand("directions");

        assertTrue(directions.contains("go north"),
                String.format("Actual directions: \n %s", directions));
        assertTrue(directions.contains("go south"),
                String.format("Actual directions: \n %s", directions));
        assertFalse(directions.contains("Blocked"),
                String.format("Actual directions: \n %s", directions));
    }

    @Test
    public void useCommandShouldBeShown() {
        initialPickupKeyInitial();

        String directions = game.applyCommand("directions");

        assertTrue(directions.contains("go north"),
                String.format("Actual directions: \n %s", directions));
        assertTrue(directions.contains("go south"),
                String.format("Actual directions: \n %s", directions));
        assertTrue(directions.contains(southBlocker.getDescription()),
                String.format("Actual directions: \n %s", directions));

        assertTrue(directions.contains("use " + keyName),
                String.format("Actual directions: \n %s", directions));
    }

    @Test
    public void useCommandShouldStopBeingShownWhenUsed() {
        initialPickupKeyInitial();
        useKey();

        String directions = game.applyCommand("directions");

        assertFalse(directions.contains("use"),
                String.format("Use command should not be shown, actual directions:\n%s", directions));
    }

    @Test
    public void pickupCommandShouldBeShown() {
        initialKey();

        String directions = game.applyCommand("directions");

        assertTrue(directions.contains("take " + keyName));
    }

    @Test
    public void pickupCommandShouldNotBeShownAfterPickup() {
        initialKey();
        pickupKey();

        String directions = game.applyCommand("directions");

        assertFalse(directions.contains("take " + keyName));
    }

}
