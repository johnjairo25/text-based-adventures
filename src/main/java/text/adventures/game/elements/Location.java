package text.adventures.game.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Location {

    public static final String WIN_GAME_MSG = "Congratulations! You won!";
    public static final String LOSE_GAME_MSG = "Try again! You lost!";

    public static final String ALREADY_WON = "You already won. Start a new game.";
    public static final String ALREADY_LOST = "You already won. Start a new game.";

    public enum LocationType {
        STANDARD, WINNING, LOSING
    }

    private String name;
    private String message;
    private Map<String, String> directionToLocationMap;
    private List<Equipable> equipables;
    private List<Blocker> blockers;
    private LocationType type;

    public boolean canMoveTo(String direction) {
        return blockers == null || blockers.stream()
                .noneMatch(b -> b.blocksDirection(direction));
    }

    public String locationTo(String direction) {
        return directionToLocationMap.get(direction);
    }

    public Optional<Equipable> takeEquipable(String equipable) {
        Optional<Equipable> equipableOptional = equipables.stream()
                .filter(e -> e.getName().equalsIgnoreCase(equipable))
                .findFirst();
        equipableOptional.ifPresent(eq -> equipables.remove(eq));
        return equipableOptional;
    }

    public String getBlockersMessageTo(String direction) {

        return blockers == null ? "" : blockers.stream()
                .filter(b -> b.blocksDirection(direction))
                .map(Blocker::getDescription)
                .collect(Collectors.joining(", "));
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        switch (type) {
            case WINNING:
                return WIN_GAME_MSG;
            case LOSING:
                return LOSE_GAME_MSG;
            default:
                return getNormalMessage();
        }
    }

    public boolean isEndingLocation() {
        return type == LocationType.WINNING || type == LocationType.LOSING;
    }

    private String getNormalMessage() {
        String items = equipables.stream()
                .map(Equipable::getName)
                .collect(Collectors.joining(", "));

        return items.isEmpty()
                ? String.format("Location: %s.\nMessage: %s", name, message).replace("..", ".")
                : String.format("Location: %s.\nMessage: %s.\nItems: %s", name, message, items).replace("..", ".");
    }

    public void addEquipable(Equipable equipable) {
        equipables.add(equipable);
    }

    public boolean hasEquipable(Equipable equipable) {
        return equipables.contains(equipable);
    }

    public String getInstructions(String goCommandText,
                                  String useCommandText,
                                  String pickupCommandText,
                                  List<String> equipables) {

        String movementString = directionToLocationMap.entrySet().stream()
                .map(entry -> {
                    if (canMoveTo(entry.getKey())) {
                        return String.format("%s %s : Go to %s.", goCommandText, entry.getKey(), entry.getValue());
                    } else {
                        String blockerMessage = getBlockersMessageTo(entry.getKey()).trim();
                        return String.format("%s %s : Blocked by %s.", goCommandText, entry.getKey(), blockerMessage);
                    }
                }).collect(Collectors.joining("\n"));

        String useString = equipables.stream()
                .filter(this::canUseEquipable)
                .map(e -> String.format("%s %s : Use the equipable to unlock more options.", useCommandText, e))
                .collect(Collectors.joining("\n"));

        String pickupString = this.equipables == null ? "" :
                this.equipables.stream()
                        .map(e -> String.format("%s %s : Take the item to use it later.", pickupCommandText, e.getName()))
                        .collect(Collectors.joining("\n"));

        return String.format("%s\n%s\n%s", movementString, useString, pickupString).trim().replace("\n\n", "\n");
    }

    public boolean canUseEquipable(String equipable) {

        return blockers != null && blockers.stream().anyMatch(b -> b.unblocksWith(equipable));
    }

    public String useEquipable(String equipable) {
        List<String> availableDirections = getAvailableDirections();

        blockers.stream()
                .filter(it -> it.unblocksWith(equipable))
                .forEach(Blocker::disable);

        List<String> newlyAvailableDirections = getAvailableDirections();

        String d = newlyAvailableDirections.stream()
                .filter(it -> !availableDirections.contains(it))
                .collect(Collectors.joining(", "));

        return String.format("You used the %s and now you can go %s.", equipable, d);
    }

    private List<String> getAvailableDirections() {

        return directionToLocationMap.keySet().stream()
                .filter(d -> blockers.stream().noneMatch(b -> b.blocksDirection(d)))
                .collect(Collectors.toList());
    }

    public boolean isFinalLocation() {
        return type == LocationType.LOSING || type == LocationType.WINNING;
    }

    public String getFinalMessage() {
        return type == LocationType.WINNING ? ALREADY_WON : ALREADY_LOST;
    }

    public static final class Builder {
        private String name;
        private String message;
        private Map<String, String> directionToLocationMap;
        private List<Equipable> equipables;
        private List<Blocker> blockers;
        private LocationType type;

        private Builder() {
        }

        public static Builder aLocation() {
            return new Builder();
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withDirectionToLocationMap(Map<String, String> directionToLocationMap) {
            this.directionToLocationMap = directionToLocationMap;
            return this;
        }

        public Builder withEquipables(List<Equipable> equipables) {
            this.equipables = equipables;
            return this;
        }

        public Builder withBlockers(List<Blocker> blockers) {
            this.blockers = blockers;
            return this;
        }

        public Builder withType(LocationType type) {
            this.type = type;
            return this;
        }

        public Location build() {
            Location location = new Location();
            location.equipables = this.equipables == null ? new ArrayList<>() : this.equipables;
            location.name = this.name;
            location.blockers = this.blockers;
            location.message = this.message;
            location.directionToLocationMap = this.directionToLocationMap;
            location.type = type != null ? type : LocationType.STANDARD;
            return location;
        }
    }
}
