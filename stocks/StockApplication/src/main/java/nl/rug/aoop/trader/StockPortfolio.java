package nl.rug.aoop.trader;

import lombok.Getter;
import lombok.Setter;
import java.util.Iterator;
import java.util.List;

/**
 * The class used to store the stockPortfolio of a client.
 */
@Getter
@Setter
public class StockPortfolio {
    private List<AssetQuantity> assetQuantities;

    private AssetQuantity stockSymbolExists(String stockSymbol) {
        for (AssetQuantity assetQuantity : assetQuantities) {
            if (assetQuantity.getStockSymbol().equals(stockSymbol)) {
                return assetQuantity;
            }
        }
        return null;
    }

    /**
     * Get the quantity of a stock by its symbol.
     * @param stockSymbol the symbol of the stock.
     * @return the quantity of the stock with the given symbol.
     */
    public double findQuantityByStockSymbol(String stockSymbol) {
        AssetQuantity assetQuantity = stockSymbolExists(stockSymbol);
        if(assetQuantity != null){
            return assetQuantity.getQuantityShares();
        }
        return -1;
    }

    /**
     * Remove the quantity of a stock from a trader's portfolio.
     * @param stockSymbol the symbol of the stock to be removed.
     */
    public void removeStockQuantity(String stockSymbol) {
        Iterator<AssetQuantity> iterator = assetQuantities.iterator();
        while (iterator.hasNext()) {
            AssetQuantity assetQuantity = iterator.next();
            if (assetQuantity.getStockSymbol().equals(stockSymbol)) {
                iterator.remove();
                break;
            }
        }
    }

    /**
     * Add a new quantity of a stock to the trader's portfolio.
     * @param stockSymbol the stock symbol to which quantity to increase.
     * @param plusAmount the amount to be added.
     */
    public void addStockQuantity(String stockSymbol, double plusAmount) {
        AssetQuantity assetQuantity = stockSymbolExists(stockSymbol);
        if(assetQuantity != null){
            assetQuantity.setQuantityShares(assetQuantity.getQuantityShares() + plusAmount);
            return;
        }
        AssetQuantity newAsset = new AssetQuantity();
        newAsset.setStockSymbol(stockSymbol);
        newAsset.setQuantityShares(plusAmount);
        assetQuantities.add(newAsset);
    }
}
