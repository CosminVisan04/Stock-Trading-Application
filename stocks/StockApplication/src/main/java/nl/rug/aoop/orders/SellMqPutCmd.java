package nl.rug.aoop.orders;

import nl.rug.aoop.run.StockExchange;
import nl.rug.aoop.command.Command;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.queue.ThreadSafeMessageQueue;
import nl.rug.aoop.networking.server.Server;
import nl.rug.aoop.trader.Trader;

import java.util.Map;

/**
 * SellMqPutCmd is a command class for putting sell orders into the sell order queue.
 */
public class SellMqPutCmd implements Command {

    private ThreadSafeMessageQueue queue;
    private StockExchange model;
    private Server server;

    /**
     * This is the constructor for the SellMqPutCmd command.
     * @param queue the queue where the sell orders are put.
     * @param stockExchange the class of the data for the stockExchange.
     * @param server used in order to send messages back to the connected clients.
     */
    public SellMqPutCmd(ThreadSafeMessageQueue queue, StockExchange stockExchange, Server server) {
        this.queue = queue;
        this.model = stockExchange;
        this.server = server;
    }

    @Override
    public void execute(Map<String, Object> params) {
        if (params.containsKey("body")) {
            String body = (String) params.get("body");
            Message message = new Message("EXEC", body);
            String[] tokens = message.getBody().split(" ");

            String stockSymbol = tokens[0];
            double quantity = Double.parseDouble(tokens[1]);
            int clientId = Integer.parseInt(tokens[3]);
            String traderId = tokens[4];

            Trader trader = model.getTraderList().findByTraderId(traderId);
            if (trader == null) {
                return;
            }

            validateAndSetClientID(trader, clientId);

            double availableSellQuantity = calculateAvailableSellQuantity(trader, stockSymbol);

            if (quantity <= availableSellQuantity) {
                placeSellOrder(trader, message);
            } else {
                server.getClientHandlerById(trader.getClientID())
                        .sendBack("Not enough shares for a new sell order!");
            }
        }
    }

    private void validateAndSetClientID(Trader trader, int clientId) {
        if (trader.getClientID() == -1) {
            trader.setClientID(clientId);
        } else if (clientId != trader.getClientID()) {
            server.getClientHandlerById(clientId).sendBack("STOP");
            throw new IllegalStateException("Couldn't match the client Id");
        }
    }

    private double calculateAvailableSellQuantity(Trader trader, String stockSymbol) {
        double sellQuantity = trader.getStockPortfolio().findQuantityByStockSymbol(stockSymbol);
        int size = model.getSellQueue().getSize();

        while (size != 0) {
            Message mockMessage = model.getSellQueue().dequeue();

            String[] mockTokens = mockMessage.getBody().split(" ");
            String mockSymbol = mockTokens[0];
            double mockQuantity = Double.parseDouble(mockTokens[1]);
            String mockTraderId = mockTokens[4];

            if (mockTraderId.equals(trader.getId()) && mockSymbol.equals(stockSymbol)) {
                sellQuantity -= mockQuantity;
            }
            size--;
            model.getSellQueue().enqueue(mockMessage);
        }
        return sellQuantity;
    }

    private void placeSellOrder(Trader trader, Message message) {
        queue.enqueue(message);
        server.getClientHandlerById(trader.getClientID())
                .sendBack("Sell order has been placed.");
    }
}
