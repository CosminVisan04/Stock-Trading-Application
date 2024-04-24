package nl.rug.aoop.run;

import nl.rug.aoop.messagequeue.queue.ThreadSafeMessageQueue;
import nl.rug.aoop.model.StockDataModel;
import nl.rug.aoop.stock.StockList;
import nl.rug.aoop.trader.Trader;
import nl.rug.aoop.trader.TraderList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TestStockExchange {

    private StockExchange stockExchange;
    private StockList mockStockList;
    private TraderList mockTraderList;
    private ThreadSafeMessageQueue mockBuyQueue;
    private ThreadSafeMessageQueue mockSellQueue;

    @BeforeEach
    public void setUp() {
        mockStockList = Mockito.mock(StockList.class);
        mockTraderList = Mockito.mock(TraderList.class);
        mockBuyQueue = Mockito.mock(ThreadSafeMessageQueue.class);
        mockSellQueue = Mockito.mock(ThreadSafeMessageQueue.class);

        stockExchange = new StockExchange(mockStockList, mockTraderList, mockBuyQueue, mockSellQueue);
    }

    @Test
    public void testStockExchangeConstructor(){
        assertNotNull(stockExchange);
    }

    @Test
    public void testGetNumberOfTraders() {
        when(mockTraderList.getTraderList()).thenReturn(Collections.singletonList(new Trader()));

        assertEquals(1, stockExchange.getNumberOfTraders());
    }

    @Test
    public void testGetTraderByIndex() {
        Trader mockTrader = Mockito.mock(Trader.class);
        when(mockTraderList.getTraderList()).thenReturn(Arrays.asList(mockTrader));

        assertEquals(mockTrader, stockExchange.getTraderByIndex(0));
        assertNull(stockExchange.getTraderByIndex(-1));
        assertNull(stockExchange.getTraderByIndex(1));
    }
}
