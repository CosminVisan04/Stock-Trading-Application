package nl.rug.aoop.run;

import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.message.Message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TestOrdersHandler {

    private OrdersHandler ordersHandler;
    private CommandHandler mockCommandHandler;

    @BeforeEach
    public void setUp() {
        mockCommandHandler = Mockito.mock(CommandHandler.class);
        ordersHandler = new OrdersHandler(mockCommandHandler);
    }

    @Test
    public void testOrdersHandlerConstructor(){
        assertNotNull(ordersHandler);
    }

    @Test
    public void testHandleMessage() {
        String stockSymbol = "AAPL";
        double quantity = 10.0;
        double price = 150.5;
        int clientId = 123;
        String traderId = "T001";

        String messageBody = String.format("%s %.2f %.2f %d %s", stockSymbol, quantity, price, clientId, traderId);
        Message msg = new Message("BUY", messageBody);
        String serializedMessage = msg.toJson();

        when(mockCommandHandler.isCommand("BUY")).thenReturn(true);

        ordersHandler.handleMessage(serializedMessage);

        ArgumentCaptor<Map<String, Object>> paramsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockCommandHandler).handleCmd(eq("BUY"), paramsCaptor.capture());

        Map<String, Object> capturedParams = paramsCaptor.getValue();
        assertEquals(stockSymbol, capturedParams.get("stockSymbol"));
        assertEquals(quantity, capturedParams.get("stockQuantity"));
        assertEquals(price, capturedParams.get("stockPrice"));
        assertEquals(traderId, capturedParams.get("traderId"));
        assertEquals(clientId, capturedParams.get("clientId"));
    }
}
