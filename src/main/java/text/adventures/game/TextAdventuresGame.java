package text.adventures.game;

import text.adventures.game.commands.Command;
import text.adventures.game.commands.CommandBuilder;
import text.adventures.game.elements.Equipable;
import text.adventures.game.elements.Location;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TextAdventuresGame {

    protected static final String WELCOME_MSG = "Welcome to the text-based adventures text.adventures.game";

    protected static final String NO_NEXT_LOCATION_MSG = "You cannot go that way!";

    protected static final String PICKUP_MSG = "You picked up the item %s.";
    protected static final String NOT_PICKUP_MSG = "You cannot pickup %s.";

    protected static final String DROP_MSG = "You dropped the element(s): %s.";
    protected static final String NOT_DROP_MSG = "You don't have the element %s to drop.";

    protected static final String NOT_USE_MSG = "You don't have the %s in your inventory.";
    protected static final String NOT_USE_IN_LOCATION_MSG = "You cannot use this %s in the current location.";

    private Location currentLocation;
    private Set<Location> locations;
    private Map<String, Location> nameToLocationMap;
    private List<Equipable> equipables;

    private final CommandBuilder commandBuilder;


    private TextAdventuresGame(CommandBuilder commandBuilder) {
        this.commandBuilder = commandBuilder;
    }

    public String getInitialMessageWithInstructions() {
        return String.format("%s\n%s\n%s",
                WELCOME_MSG,
                commandBuilder.getCommandInstructions(),
                currentLocation.getMessage());
    }

    public String initialMessage() {
        return String.format("%s.\n%s", WELCOME_MSG, currentLocation.getMessage());
    }

    public String applyCommand(String commandText) {
        if (currentLocation.isFinalLocation()) {
            return currentLocation.getFinalMessage();
        }
        Command command = commandBuilder.buildCommand(commandText, this);
        return command.execute();
    }

    public boolean hasGameEnded() {
        return currentLocation.isEndingLocation();
    }

    protected String goToDirection(String direction) {

        if (!currentLocation.canMoveTo(direction)) {
            return String.format("%s Blocked by %s", NO_NEXT_LOCATION_MSG, currentLocation.getBlockersMessageTo(direction));
        }

        String nextLocationName = currentLocation.locationTo(direction);
        if (nextLocationName == null) {
            return NO_NEXT_LOCATION_MSG;
        }

        if (nameToLocationMap == null) {
            buildNameToLocationMap();
        }

        currentLocation = nameToLocationMap.getOrDefault(nextLocationName, currentLocation);
        return currentLocation.getMessage();
    }

    protected String pickupEquipable(String equipable) {
        Optional<Equipable> equipableOptional = currentLocation.takeEquipable(equipable);
        if (equipableOptional.isPresent()) {
            Equipable actualEquipable = equipableOptional.get();
            equipables.add(actualEquipable);
            return String.format(PICKUP_MSG, actualEquipable.getName());
        }
        return String.format(NOT_PICKUP_MSG, equipable);
    }

    protected String dropEquipable(String equipable) {
        Optional<Equipable> optionalEquipable = equipables.stream()
                .filter(e -> e.getName().equalsIgnoreCase(equipable))
                .findFirst();

        if (optionalEquipable.isPresent()) {

            Equipable actualEquipable = optionalEquipable.get();
            equipables.remove(actualEquipable);
            currentLocation.addEquipable(actualEquipable);
            return String.format(DROP_MSG, actualEquipable.getName());
        }

        return String.format(NOT_DROP_MSG, equipable);
    }

    protected String getInventory() {

        return equipables.stream()
                .map(e -> String.format("%s : %s.", e.getName(), e.getDescription()))
                .collect(Collectors.joining("\n", "You are carrying:\n", "\n"));
    }

    protected String getInstructions(String goCommandText,
                                     String useCommandText,
                                     String pickupCommandText) {
        List<String> equipableStrings = equipables.stream()
                .map(Equipable::getName)
                .collect(Collectors.toList());

        return currentLocation.getInstructions(goCommandText, useCommandText, pickupCommandText, equipableStrings);
    }

    protected List<Equipable> getEquipables() {
        return equipables;
    }

    protected Location getCurrentLocation() {
        return currentLocation;
    }

    private void buildNameToLocationMap() {
        nameToLocationMap = locations.stream()
                .collect(Collectors.toMap(Location::getName, Function.identity()));
    }

    protected String useEquipable(String equipable) {
        boolean notEquipped = equipables.stream()
                .noneMatch(eq -> eq.getName().equalsIgnoreCase(equipable));
        if (notEquipped) {
            return String.format(NOT_USE_MSG, equipable);
        }
        if (!currentLocation.canUseEquipable(equipable)) {
            return String.format(NOT_USE_IN_LOCATION_MSG, equipable);
        }
        return currentLocation.useEquipable(equipable);
    }

    public static final class Builder {
        private Location currentLocation;
        private Set<Location> locations;

        private Builder() {
        }

        public static Builder aTextAdventuresGame() {
            return new Builder();
        }

        public Builder withCurrentLocation(Location currentLocation) {
            this.currentLocation = currentLocation;
            return this;
        }

        public Builder withLocations(Set<Location> locations) {
            this.locations = locations;
            return this;
        }

        public TextAdventuresGame build() {
            TextAdventuresGame textAdventuresGame = new TextAdventuresGame(new CommandBuilder());
            textAdventuresGame.locations = this.locations;
            textAdventuresGame.currentLocation = this.currentLocation;
            textAdventuresGame.equipables = new ArrayList<>();
            return textAdventuresGame;
        }
    }
}
