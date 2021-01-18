package game;

import game.commands.Command;

public class CommandImpl {

    private CommandImpl() {}

    public static class NullCommand implements Command {

        private final String result;

        public NullCommand(String result) {
            this.result = result;
        }

        @Override
        public String execute() {
            return result;
        }
    }

    public static class GoCommand implements Command {

        private final TextAdventuresGame game;
        private final String direction;

        public GoCommand(TextAdventuresGame game, String direction) {
            this.game = game;
            this.direction = direction;
        }

        @Override
        public String execute() {
            return game.goToDirection(direction);
        }

    }

    public static class PickupCommand implements Command {

        private final TextAdventuresGame game;
        private final String element;

        public PickupCommand(TextAdventuresGame game, String element) {
            this.game = game;
            this.element = element;
        }

        @Override
        public String execute() {
            return game.pickupEquipable(element);
        }

    }

    public static class DropCommand implements Command {
        private final TextAdventuresGame game;
        private final String equipable;

        public DropCommand(TextAdventuresGame game, String equipable) {
            this.game = game;
            this.equipable = equipable;
        }

        @Override
        public String execute() {
            return game.dropEquipable(equipable);
        }

    }

    public static class InventoryCommand implements Command {

        private final TextAdventuresGame game;

        public InventoryCommand(TextAdventuresGame game) {
            this.game = game;
        }

        @Override
        public String execute() {
            return game.getInventory();
        }

    }

    public static class DirectionCommand implements Command {

        private final TextAdventuresGame game;
        private final String goCommandText;
        private final String useCommandText;
        private final String pickupCommandText;

        public DirectionCommand(TextAdventuresGame game,
                                String goCommandText,
                                String useCommandText,
                                String pickupCommandText) {
            this.game = game;
            this.goCommandText = goCommandText;
            this.useCommandText = useCommandText;
            this.pickupCommandText = pickupCommandText;
        }

        @Override
        public String execute() {
            return game.getInstructions(goCommandText, useCommandText, pickupCommandText);
        }

    }

    public static class UseCommand implements Command {

        private final TextAdventuresGame game;
        private final String equipable;

        public UseCommand(TextAdventuresGame game, String equipable) {
            this.game = game;
            this.equipable = equipable;
        }

        @Override
        public String execute() {
            return game.useEquipable(equipable);
        }
    }

}
