package nl.rug.aoop.trader;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.networking.producer.NetWorkProducer;
import nl.rug.aoop.stock.Stock;
import nl.rug.aoop.stock.StockList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TestTrader {

    private Trader trader;
    private StockList mockStockList;
    private Message mockMessage;
    private NetWorkProducer mockProducer;
    private StockPortfolio mockPortfolio;

    @BeforeEach
    public void setUp() {
        trader = new Trader();
        mockStockList = Mockito.mock(StockList.class);
        mockMessage = Mockito.mock(Message.class);
        mockProducer = Mockito.mock(NetWorkProducer.class);
        mockPortfolio = Mockito.mock(StockPortfolio.class);

        trader.setStockPortfolio(mockPortfolio);
    }

    @Test
    public void testTraderConstructor(){
        assertNotNull(trader);
    }

    @Test
    public void testAddFunds() {
        trader.setFunds(1000.0);
        trader.addFunds(500.0);

        assertEquals(1500.0, trader.getFunds());
    }

    @Test
    public void testPlaceOrderWithInvalidStock() {
        when(mockStockList.getStockList()).thenReturn(Arrays.asList());
        when(mockMessage.getBody()).thenReturn("GOOGL 10 1200.5 1");

        trader.placeOrder(mockMessage, mockStockList, mockProducer);

        verify(mockProducer, never()).networkProduce(any());
    }

}
