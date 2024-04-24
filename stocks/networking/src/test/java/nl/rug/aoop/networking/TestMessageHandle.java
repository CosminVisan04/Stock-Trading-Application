package nl.rug.aoop.networking;

import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.networking.client.MessageSendBack;
import nl.rug.aoop.networking.server.MessageHandle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static nl.rug.aoop.messagequeue.message.Message.fromJson;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TestMessageHandle {

    private MessageHandle messageHandle;
    private CommandHandler mockCommandHandler;
    private MessageSendBack mockSendBack;

    @BeforeEach
    public void setUp() {
        mockCommandHandler = mock(CommandHandler.class);
        mockSendBack = mock(MessageSendBack.class);
        messageHandle = new MessageHandle(mockCommandHandler);
    }

    @Test
    public void testMessageHandleConstructor(){
        assertNotNull(messageHandle);
    }

    @Test
    public void testValidCommand(){
        Message message = new Message("MqPut", "test body");
        messageHandle.handleMessage(message.toJson(), mockSendBack);
        verify(mockSendBack, never()).sendBack("The command was received by the server, message added to the queue!");
    }

    @Test
    public void testNonValidCommand(){
        Message message = new Message("Not a command", "test body");
        messageHandle.handleMessage(message.toJson(), mockSendBack);
        verify(mockSendBack).sendBack("Couldn't match the command!");
    }
}
