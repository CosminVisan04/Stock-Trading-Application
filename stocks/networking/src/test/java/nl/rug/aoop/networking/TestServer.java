package nl.rug.aoop.networking;

import nl.rug.aoop.networking.client.MessageHandler;
import nl.rug.aoop.networking.server.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.io.IOException;

class TestServer {
    private Server server;
    private MessageHandler mockMessageHandler;

    @BeforeEach
    public void setUp(){
        mockMessageHandler = mock(MessageHandler.class);
        try {
            server = new Server(0, mockMessageHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new Thread(server).start();
        await().until(() -> server.isRunning());
    }

    @AfterEach
    public void finish(){
        server.terminate();
    }

    @Test
    public void testServerConstructor(){
        assertNotNull(server);
    }

    @Test
    public void testServerRunning(){
        assertTrue(server.isRunning());
    }

    @Test
    void testServerTerminate() {
        server.terminate();
        assertFalse(server.isRunning());
    }
}