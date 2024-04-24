package nl.rug.aoop.trader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TestStockPortfolio {

    private StockPortfolio stockPortfolio;
    private AssetQuantity mockAssetQuantity;

    @BeforeEach
    public void setUp() {
        stockPortfolio = new StockPortfolio();
        mockAssetQuantity = Mockito.mock(AssetQuantity.class);

        List<AssetQuantity> assetQuantities = new ArrayList<>();
        assetQuantities.add(mockAssetQuantity);
        stockPortfolio.setAssetQuantities(assetQuantities);
    }

    @Test
    public void testStockPortfolioConstructor(){
        assertNotNull(stockPortfolio);
    }

    @Test
    public void testFindQuantityByStockSymbol() {
        when(mockAssetQuantity.getStockSymbol()).thenReturn("AAPL");
        when(mockAssetQuantity.getQuantityShares()).thenReturn(100.0);

        assertEquals(100.0, stockPortfolio.findQuantityByStockSymbol("AAPL"));
        assertEquals(-1, stockPortfolio.findQuantityByStockSymbol("MSFT"));
    }

    @Test
    public void testRemoveStockQuantity() {
        when(mockAssetQuantity.getStockSymbol()).thenReturn("AAPL");

        stockPortfolio.removeStockQuantity("AAPL");
        verify(mockAssetQuantity, times(1)).getStockSymbol();
    }

    @Test
    public void testAddStockQuantity() {
        when(mockAssetQuantity.getStockSymbol()).thenReturn("AAPL");

        stockPortfolio.addStockQuantity("AAPL", 50.0);
        verify(mockAssetQuantity, times(1)).setQuantityShares(anyDouble());

        stockPortfolio.addStockQuantity("MSFT", 70.0);
        assertEquals(2, stockPortfolio.getAssetQuantities().size());
    }
}
