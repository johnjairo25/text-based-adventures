package game.commands;

import game.CommandImpl.NullCommand;
import org.junit.jupiter.api.Test;
import game.commands.CommandBuilder.CommandParts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static game.commands.CommandBuilder.getCommandAndBody;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommandBuilderTest {

    @Test
    public void testEmptyCommandIsInvalid() {
        CommandBuilder commandBuilder = new CommandBuilder();

        Command command = commandBuilder.buildCommand("", null);

        assertThat(command, instanceOf(NullCommand.class));
        NullCommand nullCommand = (NullCommand) command;
        assertThat(nullCommand.execute(), is(CommandBuilder.INVALID_COMMAND_MESSAGE));
    }

    @Test
    public void testInvalidCommandIsInvalid() {
        CommandBuilder commandBuilder = new CommandBuilder();

        Command command = commandBuilder.buildCommand("invalid command", null);

        assertThat(command, instanceOf(NullCommand.class));
        NullCommand nullCommand = (NullCommand) command;
        assertThat(nullCommand.execute(), is(CommandBuilder.INVALID_COMMAND_MESSAGE));
    }

    @Test
    public void goToCommand() {
        CommandParts parts = getCommandAndBody("go south east");

        assertNotNull(parts);
        assertEquals("go", parts.command);
        assertEquals("south east", parts.body);
    }

    @Test
    public void goToCommandCleaning() {
        CommandParts parts = getCommandAndBody("GO north   Pole!  \n");

        assertNotNull(parts);
        assertEquals("go", parts.command);
        assertEquals("north pole", parts.body);
    }

    @Test
    public void inventory() {
        CommandParts parts = getCommandAndBody("inventory");

        assertNotNull(parts);
        assertEquals("inventory", parts.command);
        assertEquals("", parts.body);
    }

    @Test
    public void use() {
        CommandParts parts = getCommandAndBody("use axe");
        assertNotNull(parts);
        assertEquals("use", parts.command);

        parts = getCommandAndBody("attack with axe");
        assertNotNull(parts);
        assertEquals("attack with", parts.command);

        parts = getCommandAndBody("kill with axe");
        assertNotNull(parts);
        assertEquals("kill with", parts.command);

        parts = getCommandAndBody("open with axe");
        assertNotNull(parts);
        assertEquals("open with", parts.command);

        parts = getCommandAndBody("OPEN with axe");
        assertNotNull(parts);
        assertEquals("open with", parts.command);
    }



}
