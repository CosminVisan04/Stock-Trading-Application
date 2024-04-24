package nl.rug.aoop;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.stock.Stock;
import nl.rug.aoop.stock.StockList;

import java.util.List;
import java.util.Random;

/**
 * The class for the random strategy.
 */
public class StrategyRandom {
    private static final Random RANDOM = new Random();

    /**
     * The constructor for the strategy.
     * @param stockList the existing stock list.
     * @param clientHandlerId the clientHandlerId of the client linked to the trader.
     * @return the order message.
     */
    public Message createRandomOrderMessage(StockList stockList, int clientHandlerId) {
        String action = RANDOM.nextBoolean() ? "BUY" : "SELL";
        List<Stock> availableStocks = stockList.getStockList();
        String selectedStock = availableStocks.get(RANDOM.nextInt(availableStocks.size())).getSymbol();
        int randomAmount = RANDOM.nextInt(100) + 1;
        String order = "";
        order = order + selectedStock + " " + randomAmount + " " + calculatePrice(action, selectedStock, stockList) +
                " " + clientHandlerId;
        return new Message(action, order);
    }

    private double calculatePrice(String action, String stock, StockList stockList) {
        double currentPrice = stockList.findPriceByStockSymbol(stock);
        double priceFluctuation = RANDOM.nextDouble() * 3;
        return "BUY".equals(action) ? currentPrice + priceFluctuation : currentPrice - priceFluctuation;
    }
}
