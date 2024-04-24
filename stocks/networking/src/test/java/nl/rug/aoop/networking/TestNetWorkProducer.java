package nl.rug.aoop.networking;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.producer.NetWorkProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TestNetWorkProducer {
    private NetWorkProducer netWorkProducer;
    private Client mockClient;
    private Message mockMessage;

    @BeforeEach
    public void setUp() {
        mockClient = mock(Client.class);
        netWorkProducer = new NetWorkProducer(mockClient);
        mockMessage = mock(Message.class);
    }

    @Test
    public void testServerConstructor(){
        assertNotNull(netWorkProducer);
    }

    @Test
    public void testNetworkProduce() {
        String jsonMessage = "{'header':'test'}";
        when(mockMessage.toJson()).thenReturn(jsonMessage);
        netWorkProducer.networkProduce(mockMessage);
        verify(mockClient).sendString("{'header':'test'}");
    }
}
