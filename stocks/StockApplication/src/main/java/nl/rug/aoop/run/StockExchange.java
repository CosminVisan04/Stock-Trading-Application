package nl.rug.aoop.run;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.messagequeue.queue.ThreadSafeMessageQueue;
import nl.rug.aoop.model.StockDataModel;
import nl.rug.aoop.model.StockExchangeDataModel;
import nl.rug.aoop.model.TraderDataModel;
import nl.rug.aoop.stock.StockList;
import nl.rug.aoop.trader.TraderList;

/**
 * The class of a StockExchange.
 */
@Getter
@Setter
public class StockExchange implements StockExchangeDataModel {
    private StockList stockList;
    private TraderList traderList;
    private ThreadSafeMessageQueue buyQueue;
    private ThreadSafeMessageQueue sellQueue;

    /**
     * The constructor of a Stock Exchange.
     * @param stockList the stockList used in the Stock Exchange.
     * @param traderList the traderList used in the Stock Exchange.
     * @param buyQueue the buyQueue used in the Stock Exchange.
     * @param sellQueue the sellQueue used in the Stock Exchange.
     */
    public StockExchange(StockList stockList, TraderList traderList, ThreadSafeMessageQueue buyQueue,
                         ThreadSafeMessageQueue sellQueue) {
        this.stockList = stockList;
        this.traderList = traderList;
        this.buyQueue = buyQueue;
        this.sellQueue = sellQueue;
    }

    @Override
    public int getNumberOfStocks() {
        return stockList.getStockList().size();
    }

    @Override
    public int getNumberOfTraders() {
        return traderList.getTraderList().size();
    }

    @Override
    public StockDataModel getStockByIndex(int index) {
        if (index < 0 || index >= stockList.getStockList().size()) {
            return null;
        }
        return stockList.getStockList().get(index);
    }

    @Override
    public TraderDataModel getTraderByIndex(int index) {
        if (index < 0 || index >= traderList.getTraderList().size()) {
            return null;
        }
        return traderList.getTraderList().get(index);
    }
}
