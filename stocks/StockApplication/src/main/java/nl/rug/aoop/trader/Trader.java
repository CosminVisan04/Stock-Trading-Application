package nl.rug.aoop.trader;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.model.TraderDataModel;
import nl.rug.aoop.networking.producer.NetWorkProducer;
import nl.rug.aoop.stock.StockList;

import java.util.ArrayList;
import java.util.List;

/**
 * The class of the client.
 */
@Getter
@Setter
@Slf4j
public class Trader implements TraderDataModel {
    private String id;
    private String name;
    private double funds;
    private StockPortfolio stockPortfolio;
    private int clientID = -1;
    private List<String> transactionHistory = new ArrayList<String>();

    @Override
    public List<String> getOwnedStocks() {
        List<String> stocksOwned = new ArrayList<>();
        for (AssetQuantity assetQuantity : stockPortfolio.getAssetQuantities()) {
            stocksOwned.add(assetQuantity.getStockSymbol() + ": " + assetQuantity.getQuantityShares());
        }
        return stocksOwned;
    }

    /**
     * Method to add funds to a trader.
     * @param amount the amount to be added.
     */
    public void addFunds(double amount) {
        funds += amount;
    }

    /**
     * Method to place a new order.
     * @param message the message which is a possible order.
     * @param possibleStockList the stock list from which the order can select a stock.
     * @param producer the producer used in order to send the order (message) over the network.
     */
    public void placeOrder(Message message, StockList possibleStockList, NetWorkProducer producer) {
        String[] tokens = message.getBody().split(" ");

        if (tokens.length != 4) {
            log.error("Incorrect order type.");
            return;
        }

        String stockSymbol = tokens[0];
        Double quantity = parseDouble(tokens[1], "Not a valid number in quantity.");
        Double price = parseDouble(tokens[2], "Not a valid number in price.");

        if (quantity == null || price == null) {
            return;
        }

        if (isStockPresent(stockSymbol, possibleStockList)) {
            Message tempMsg = new Message(message.getHeader(), message.getBody() + " " + id);
            producer.networkProduce(tempMsg);
        }
    }

    private Double parseDouble(String value, String errorMessage) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            log.error(errorMessage + " " + e);
            return null;
        }
    }

    private boolean isStockPresent(String stockSymbol, StockList possibleStockList) {
        return possibleStockList.getStockList().stream().anyMatch(stock -> stock.getSymbol().equals(stockSymbol));
    }
}