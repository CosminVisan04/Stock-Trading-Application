package nl.rug.aoop.stock;

import nl.rug.aoop.model.StockDataModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class TestStock {

    private Stock stock;
    private StockDataModel stockDataModel;

    @BeforeEach
    public void setUp() {
        stockDataModel = Mockito.mock(StockDataModel.class);
        stock = new Stock();
    }

    @Test
    public void testStockConstructor(){
        assertNotNull(stock);
    }

    @Test
    public void testGetMarketCap() {
        when(stockDataModel.getMarketCap()).thenReturn(10500.0);

        stock.setPrice(10.5);
        stock.setSharesOutstanding(1000);

        double expectedMarketCap = 10.5 * 1000;
        double actualMarketCap = stock.getMarketCap();

        assertEquals(expectedMarketCap, actualMarketCap);
    }
}
