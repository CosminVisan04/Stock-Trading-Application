package nl.rug.aoop.orders;

import nl.rug.aoop.trader.Trader;
import nl.rug.aoop.run.StockExchange;
import nl.rug.aoop.command.Command;
import nl.rug.aoop.messagequeue.message.Message;
import java.util.Map;

/**
 * Command used to execute an order (buy / sell) with a matching (buy / sell) order from the stock exchange.
 */
public class ExecuteCmd implements Command {
    private StockExchange stockExchange;

    /**
     * The constructor of the execute command.
     * @param stockExchange the class of the data for the stockExchange.
     */
    public ExecuteCmd(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    @Override
    public void execute(Map<String, Object> params) {
        String stockSymbol = (String) params.get("stockSymbol");
        double quantity = (double) params.get("stockQuantity");
        double price = (double) params.get("stockPrice");
        String traderId = (String) params.get("traderId");
        int clientIdOld = (int) params.get("clientId");

        double minPrice = getMinPrice(stockSymbol, stockExchange.getSellQueue().getSize(), -1);

        if (minPrice != -1) {
            int okBuy = tryMatchOrders(stockSymbol, quantity, price, traderId, clientIdOld, minPrice);

            if (okBuy == 0) {
                generateNewOrder(stockSymbol, quantity, price, traderId, clientIdOld);
            }
        } else {
            generateNewOrder(stockSymbol, quantity, price, traderId, clientIdOld);
        }
    }

    private int tryMatchOrders(String stockSymbol, double tempReminder, double price, String traderId,
                               int clientIdOld, double minPrice) {
        int size = stockExchange.getSellQueue().getSize(), okBuy = 0;
        double reminderToBuy = tempReminder;
        while (size-- > 0) {
            Message message = stockExchange.getSellQueue().dequeue();
            String[] tokens = message.getBody().split(" ");
            String sellStock = tokens[0];
            double sellQuantity = Double.parseDouble(tokens[1]);
            double sellPrice = Double.parseDouble(tokens[2]);
            String sellTraderId = tokens[4];
            if (minPrice == sellPrice && stockSymbol.equals(sellStock) && minPrice <= price) {
                stockExchange.getStockList().findBySymbolStock(stockSymbol).setPrice(sellPrice);
                if (tempReminder > sellQuantity) {
                    reminderToBuy -= sellQuantity;
                    partialBuyOrder(price, traderId, sellStock, sellQuantity, sellPrice, sellTraderId);
                } else if (tempReminder == sellQuantity) {
                    perfectMatchOrder(stockSymbol, price, traderId, sellStock, sellQuantity, sellPrice, sellTraderId);
                    okBuy = 1;
                    break;
                } else {
                    okBuy = prepareSeller(stockSymbol, tempReminder, price, traderId, sellPrice, message);
                    break;
                }
            }
            if (reminderToBuy != tempReminder) {
                generateNewOrder(stockSymbol, reminderToBuy, price, traderId, clientIdOld);
            }
        }
        return okBuy;
    }

    private double getMinPrice(String stockSymbol, int size, double minPrice) {
        while (size != 0) {
            Message tempMsg = stockExchange.getSellQueue().dequeue();
            String[] tokens = tempMsg.getBody().split(" ");
            String stockSymbolSell = tokens[0];
            double priceSell = Double.parseDouble(tokens[2]);
            int clientIdSell = Integer.parseInt(tokens[3]);
            String traderIdSell = tokens[4];
            stockExchange.getTraderList().findByTraderId(traderIdSell).setClientID(clientIdSell);
            if (stockSymbolSell.equals(stockSymbol) && minPrice == -1) {
                minPrice = priceSell;
            } else if (priceSell < minPrice) {
                minPrice = priceSell;
            }
            size--;
            stockExchange.getSellQueue().enqueue(tempMsg);
        }
        return minPrice;
    }

    private int prepareSeller(String stockSymbol, double quantity, double price, String traderId,
                              double sellQuantity, Message message) {
        String delimitator = " ";
        String[] tokens = message.getBody().split(delimitator);
        double priceSell = Double.parseDouble(tokens[2]);
        int clientIdSell = Integer.parseInt(tokens[3]);
        String traderIdSell = tokens[4];

        int okBuy;
        sellQuantity = sellQuantity - quantity;
        partialSellOrder(stockSymbol, quantity, price, traderId, priceSell, traderIdSell);
        okBuy = 1;

        Message tempOrder = new Message("EXEC", stockSymbol + delimitator + sellQuantity +
                delimitator + priceSell + delimitator + clientIdSell + delimitator + traderIdSell);
        stockExchange.getSellQueue().enqueue(tempOrder);
        return okBuy;
    }

    private void partialSellOrder(String stockSymbol, double quantity, double price, String traderId,
                                  double sellPrice, String sellTraderId) {
        Trader buyTrader = stockExchange.getTraderList().findByTraderId(traderId);
        Trader sellTrader = stockExchange.getTraderList().findByTraderId(sellTraderId);
        sellTrader.getStockPortfolio().addStockQuantity(stockSymbol, -quantity);
        sellTrader.addFunds(quantity * sellPrice);
        buyTrader.addFunds((price - sellPrice) * quantity);
        buyTrader.getStockPortfolio().addStockQuantity(stockSymbol, quantity);
        putInHistory("Complete Buy", stockSymbol, quantity, sellPrice, buyTrader);
        putInHistory("Partially Sold", stockSymbol, quantity, sellPrice, sellTrader);
    }

    private void perfectMatchOrder(String stockSymbol, double price, String traderId, String sellStock,
                                   double sellQuantity, double sellPrice, String sellTraderId) {
        if (sellQuantity == stockExchange.getTraderList().findByTraderId(sellTraderId).getStockPortfolio().
                findQuantityByStockSymbol(sellStock)) {
            stockExchange.getTraderList().findByTraderId(sellTraderId).getStockPortfolio().
                    removeStockQuantity(sellStock);
        } else {
            stockExchange.getTraderList().findByTraderId(sellTraderId).getStockPortfolio().
                    addStockQuantity(stockSymbol, -sellQuantity);
        }
        Trader buyTrader = stockExchange.getTraderList().findByTraderId(traderId);
        Trader sellTrader = stockExchange.getTraderList().findByTraderId(sellTraderId);
        buyTrader.addFunds((price - sellPrice) * sellQuantity);
        buyTrader.getStockPortfolio().addStockQuantity(stockSymbol, sellQuantity);
        sellTrader.addFunds(sellPrice * sellQuantity);
        putInHistory("Complete Buy", stockSymbol, sellQuantity, sellPrice, buyTrader);
        putInHistory("Complete Sell", stockSymbol, sellQuantity, sellPrice, sellTrader);
    }

    private void partialBuyOrder(double price, String traderId, String sellStock, double sellQuantity,
                                 double sellPrice, String sellTraderId) {
        if (sellQuantity == stockExchange.getTraderList().findByTraderId(sellTraderId).
                getStockPortfolio().findQuantityByStockSymbol(sellStock)) {
            stockExchange.getTraderList().findByTraderId(sellTraderId).getStockPortfolio().
                    removeStockQuantity(sellStock);
        } else {
            stockExchange.getTraderList().findByTraderId(sellTraderId).getStockPortfolio().
                    addStockQuantity(sellStock, -sellQuantity);
        }
        Trader buyTrader = stockExchange.getTraderList().findByTraderId(traderId);
        Trader sellTrader = stockExchange.getTraderList().findByTraderId(sellTraderId);
        buyTrader.addFunds((price - sellPrice) * sellQuantity);
        buyTrader.getStockPortfolio().addStockQuantity(sellStock, sellQuantity);
        sellTrader.addFunds(sellPrice * sellQuantity);
        putInHistory("Partially Buy", sellStock, sellQuantity, sellPrice, buyTrader);
        putInHistory("Complete Sell", sellStock, sellQuantity, sellPrice, sellTrader);
    }

    private void generateNewOrder(String stockSymbol, double quantity, double price,
                                  String traderId, int clientIdOld) {
        String delimitator = " ";
        Message newOrder = new Message("EXEC", stockSymbol + delimitator +
                quantity + delimitator + price + delimitator + clientIdOld + delimitator + traderId);
        stockExchange.getBuyQueue().enqueue(newOrder);
    }

    private void putInHistory(String action, String symbol, double quantity, double price, Trader trader) {
        String logMessage = action + " " + quantity + " shares of " + symbol + " at the price of: " + price;
        trader.getTransactionHistory().add(logMessage);
    }
}