package text.adventures.game;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InventoryTest extends MapFixture {

    @Test
    public void cannotTakeNonExistentEquipable() {
        String result = game.applyCommand("take nothing");

        assertThat(result, containsStringIgnoringCase("you cannot pickup"));
        assertThat(result, containsStringIgnoringCase("nothing"));
    }

    @Test
    public void cannotDropNotTakenEquipable() {
        String result = game.applyCommand("drop nothing");

        assertThat(result, containsStringIgnoringCase("You don't have the element"));
        assertThat(result, containsStringIgnoringCase("nothing"));
    }

    @Test
    public void canPickupEquipable() {
        initialKey();

        String result = game.applyCommand("pickup " + keyName);

        assertTrue(result.contains(keyName), "Command result should contain the equipable\n" + result);
        assertTrue(game.getEquipables().contains(keyEquipable), "Equipables list should contain the equipable");
        assertFalse(northEastWithKey.takeEquipable(keyEquipable.getName()).isPresent(), "Location should not have the equipable");
    }

    @Test
    public void canDropEquipable() {
        initialPickupKeyInitial();

        String result = game.applyCommand("drop " + keyName);

        assertTrue(result.contains(keyEquipable.getName()), "Command result should contain the equipable");
        assertFalse(game.getEquipables().contains(keyEquipable), "Equipables list should NOT contain the equipable");
        assertTrue(initial.hasEquipable(keyEquipable), "Initial location should have the equipable");
    }

    @Test
    public void listInventory() {
        initialPickupKey();
        keyPickupSword();

        String inventory = game.applyCommand("inventory");

        assertTrue(inventory.contains(keyEquipable.getName()),
                "Inventory should contain the first name. " + inventory);
        assertTrue(inventory.contains(keyEquipable.getDescription()),
                "Inventory should contain the first description. " + inventory);
        assertTrue(inventory.contains(swordEquipable.getDescription()),
                "Inventory should contain the second name. " + inventory);
        assertTrue(inventory.contains(swordEquipable.getDescription()),
                "Inventory should contain the second description. " + inventory);
    }

    @Test
    public void pickTwoDropOne() {
        initialPickupKey();
        keyPickupSword();
        game.applyCommand("drop " + keyName);

        String inventory = game.applyCommand("inventory");

        assertTrue(inventory.contains(swordEquipable.getDescription()),
                "Inventory should contain the second name. " + inventory);
        assertTrue(inventory.contains(swordEquipable.getDescription()),
                "Inventory should contain the second description. " + inventory);
    }

}
