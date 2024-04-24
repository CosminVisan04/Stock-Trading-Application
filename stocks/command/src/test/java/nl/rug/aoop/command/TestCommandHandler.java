package nl.rug.aoop.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TestCommandHandler {
    private CommandHandler commandHandler;
    private Command mockCommand;

    @BeforeEach
    public void setUp() {
        commandHandler = new CommandHandler();
        mockCommand = mock(Command.class);
        commandHandler.newCommand("testCommand", mockCommand);
    }

    @Test
    void testCommandHandlerConstructor() {
        assertNotNull(commandHandler);
    }

    @Test
    public void testNewCommand() {
        assertEquals(mockCommand, commandHandler.getCmdMap().get("testCommand"));
    }

    @Test
    public void testHandleCmdWithValidCommand() {
        Map<String, Object> parameters = new HashMap<>();
        commandHandler.handleCmd("testCommand", parameters);

        verify(mockCommand).execute(parameters);
    }

    @Test
    public void testHandleCmdWithInvalidCommand() {
        commandHandler.handleCmd("invalidCommand", null);

        verify(mockCommand, never()).execute(anyMap());
    }
}
