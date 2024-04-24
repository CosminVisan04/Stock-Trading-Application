package nl.rug.aoop.stock;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * The class used to store a list of stocks.
 */
@Getter
@Setter
@Slf4j
public class StockList {
    private List<Stock> stockList;

    /**
     * Method used to find a specific Stock by its symbol.
     * @param symbol the stock symbol.
     * @return the stock with the given symbol.
     */
    public Stock findBySymbolStock(String symbol) {
        for (Stock stock : stockList) {
            if (stock.getSymbol().equals(symbol)) {
                return stock;
            }
        }
        log.error("No stock found by the given symbol: " + symbol);
        return null;
    }

    /**
     * Method to get the price of a stock by its symbol.
     * @param symbol the stock symbol.
     * @return the price of the stock with the given symbol.
     */
    public double findPriceByStockSymbol(String symbol) {
        Stock tempStock = findBySymbolStock(symbol);
        if (tempStock != null) {
            return tempStock.getPrice();
        }
        return -1;
    }
}
