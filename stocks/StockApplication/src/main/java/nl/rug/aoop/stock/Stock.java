package nl.rug.aoop.stock;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.model.StockDataModel;

/**
 * The class used for a stock.
 */
@Getter
@Setter
public class Stock implements StockDataModel {

    private String symbol;
    private String name;
    private long sharesOutstanding;
    private double price;

    public double getMarketCap() {
        return sharesOutstanding * price;
    }
}

