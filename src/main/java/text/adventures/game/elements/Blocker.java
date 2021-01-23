package text.adventures.game.elements;

import java.util.List;

public class Blocker {

    private String description;
    private List<String> blockedDirections;
    private String unblocksWithEquipable;
    private boolean disabled;

    public String getDescription() {
        return description;
    }

    public boolean blocksDirection(String direction) {
        return !disabled && blockedDirections.contains(direction);
    }

    public boolean unblocksWith(String equipable) {
        return !disabled && unblocksWithEquipable.equalsIgnoreCase(equipable);
    }

    public void disable() {
        disabled = true;
    }

    public static final class Builder {
        private String description;
        private List<String> blockedDirections;
        private String unblocksWithEquipable;

        private Builder() {
        }

        public static Builder aBlocker() {
            return new Builder();
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withBlockedDirections(List<String> blockedDirections) {
            this.blockedDirections = blockedDirections;
            return this;
        }

        public Builder withUnblocksWithEquipable(String unblocksWithEquipable) {
            this.unblocksWithEquipable = unblocksWithEquipable;
            return this;
        }

        public Blocker build() {
            Blocker blocker = new Blocker();
            blocker.unblocksWithEquipable = this.unblocksWithEquipable;
            blocker.blockedDirections = this.blockedDirections;
            blocker.description = this.description;
            return blocker;
        }
    }
}
