package nl.rug.aoop.networking;

import nl.rug.aoop.networking.server.ClientHandler;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class TestClientHandler {

    private ClientHandler clientHandler;

    @BeforeEach
    public void setUp() {
        clientHandler = mock(ClientHandler.class);
    }

    @Test
    public void testClientHandlerConstructor(){
        assertNotNull(clientHandler);
    }

    @Test
    public void testSendBack() {
        String testString = "Test Message";
        clientHandler.sendBack(testString);
        verify(clientHandler).sendBack("Test Message");
    }
}
