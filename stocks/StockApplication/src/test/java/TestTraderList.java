package nl.rug.aoop.trader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TestTraderList {

    private TraderList traderList;
    private Trader mockTrader1;
    private Trader mockTrader2;

    @BeforeEach
    public void setUp() {
        traderList = new TraderList();
        mockTrader1 = Mockito.mock(Trader.class);
        mockTrader2 = Mockito.mock(Trader.class);

        when(mockTrader1.getId()).thenReturn("id123");
        when(mockTrader1.getClientID()).thenReturn(1001);
        when(mockTrader2.getId()).thenReturn("id456");
        when(mockTrader2.getClientID()).thenReturn(1002);

        traderList.setTraderList(Arrays.asList(mockTrader1, mockTrader2));
    }

    @Test
    public void testTraderListConstructor(){
        assertNotNull(traderList);
    }

    @Test
    public void testFindByTraderIdValid() {
        Trader foundTrader = traderList.findByTraderId("id123");
        assertEquals(mockTrader1, foundTrader);
    }

    @Test
    public void testFindByTraderIdInvalid() {
        Trader notFoundTrader = traderList.findByTraderId("id999");
        assertNull(notFoundTrader);
    }

    @Test
    public void testFindByClientIdValid() {
        Trader foundTrader = traderList.findByClientId(1001);
        assertEquals(mockTrader1, foundTrader);
    }

    @Test
    public void testFindByClientIdInvalid() {
        Trader notFoundTrader = traderList.findByClientId(9999);
        assertNull(notFoundTrader);
    }
}
