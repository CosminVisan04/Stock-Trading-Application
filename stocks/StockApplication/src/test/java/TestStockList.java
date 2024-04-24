package nl.rug.aoop.stock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TestStockList {

    private StockList stockList;
    private Stock mockStock;

    @BeforeEach
    public void setUp() {
        stockList = new StockList();
        mockStock = Mockito.mock(Stock.class);

        when(mockStock.getSymbol()).thenReturn("AAPL");
        when(mockStock.getPrice()).thenReturn(123.5);

        stockList.setStockList(Arrays.asList(mockStock));
    }

    @Test
    public void testStockListConstructor(){
        assertNotNull(stockList);
    }

    @Test
    public void testFindBySymbolStock() {
        Stock foundStock = stockList.findBySymbolStock("AAPL");
        assertEquals(mockStock, foundStock);

        Stock nonExistingStock = stockList.findBySymbolStock("AOOP");
        assertNull(nonExistingStock);
    }

    @Test
    public void testFindPriceByStockSymbol() {
        double price = stockList.findPriceByStockSymbol("AAPL");
        assertEquals(123.5, price);

        double nonExistingPrice = stockList.findPriceByStockSymbol("AOOP");
        assertEquals(-1, nonExistingPrice);
    }
}
