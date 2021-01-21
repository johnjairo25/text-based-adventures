package game.commands;

import game.TextAdventuresGame;
import game.CommandImpl.*;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandBuilder {

    private static final String COMMAND_INSTRUCTIONS = String.format(
            "Available commands:\n%s\n%s\n%s\n%s\n%s\n%s",
            "go [direction]",
            "(take|pickup) [item]",
            "(use|open with|kill with|attack with) [item]",
            "drop [item]",
            "inventory",
            "directions");

    private static final String INVALID_COMMAND_MESSAGE = "I did not understand that. If you need help, try the directions command";
    private static final String COMMAND_REGEX = "(?<command>\\S+( with)?)(\\s+(?<body>.+))?";

    protected static final String GO_COMMAND = "go";

    protected static final String PICKUP_COMMAND = "take";
    protected static final Set<String> PICKUP_COMMAND_SET;

    protected static final String DROP_COMMAND = "drop";

    protected static final String INVENTORY_COMMAND = "inventory";

    protected static final String DIRECTION_COMMAND = "directions";

    protected static final String USE_COMMAND = "use";
    protected static final Set<String> USE_COMMAND_SET;

    static {
        PICKUP_COMMAND_SET = new HashSet<>();
        PICKUP_COMMAND_SET.add("pickup");
        PICKUP_COMMAND_SET.add("take");

        USE_COMMAND_SET = new HashSet<>();
        USE_COMMAND_SET.add("use");
        USE_COMMAND_SET.add("attack with");
        USE_COMMAND_SET.add("kill with");
        USE_COMMAND_SET.add("open with");
    }

    public CommandBuilder() {
    }

    public Command buildCommand(String commandText, TextAdventuresGame game) {
        CommandParts parts = getCommandAndBody(commandText);
        if (parts == null) {
            return new NullCommand(INVALID_COMMAND_MESSAGE);
        }

        if (GO_COMMAND.equalsIgnoreCase(parts.command)) {
            return new GoCommand(game, parts.body);
        } else if (PICKUP_COMMAND_SET.contains(parts.command)) {
            return new PickupCommand(game, parts.body);
        } else if (DROP_COMMAND.equalsIgnoreCase(parts.command)) {
            return new DropCommand(game, parts.body);
        } else if (INVENTORY_COMMAND.equalsIgnoreCase(parts.command)) {
            return new InventoryCommand(game);
        } else if (DIRECTION_COMMAND.equalsIgnoreCase(parts.command)) {
            return new DirectionCommand(game, GO_COMMAND, USE_COMMAND, PICKUP_COMMAND);
        } else if (USE_COMMAND_SET.contains(parts.command)) {
            return new UseCommand(game, parts.body);
        }
        return new NullCommand(INVALID_COMMAND_MESSAGE);
    }

    public String getCommandInstructions() {
        return COMMAND_INSTRUCTIONS;
    }

    protected static CommandParts getCommandAndBody(String commandText) {
        Pattern pattern = Pattern.compile(COMMAND_REGEX);
        Matcher matcher = pattern.matcher(commandText.trim());

        if (!matcher.matches() || matcher.groupCount() < 1) {
            return null;
        }

        String command = normalize(matcher.group("command"));
        String body = matcher.groupCount() == 1 ? "" : normalize(matcher.group("body"));

        return new CommandParts(command, body);
    }

    private static String normalize(String input) {
        return input == null ? "" : input.trim()
                .toLowerCase()
                .replaceAll("\\s+", " ")
                .replaceAll("[^ \\w]", "");
    }

    protected static class CommandParts {
        protected final String command;
        protected final String body;

        public CommandParts(String command, String body) {
            this.command = command;
            this.body = body;
        }

    }

}
