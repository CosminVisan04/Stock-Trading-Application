package nl.rug.aoop.orders;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.run.StockExchange;
import nl.rug.aoop.command.Command;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.queue.ThreadSafeMessageQueue;
import nl.rug.aoop.networking.server.Server;
import nl.rug.aoop.trader.Trader;

import java.util.Map;

/**
 * BuyMqPutCmd is a command class for putting buy orders into the buy order queue.
 */
@Slf4j
public class BuyMqPutCmd implements Command {

    private ThreadSafeMessageQueue queue;
    private StockExchange stockExchange;
    private Server server;

    /**
     * This is the constructor for the BuyMqPutCmd command.
     * @param queue the queue where the buy orders are put.
     * @param stockExchange the class of the data for the stockExchange.
     * @param server used in order to send messages back to the connected clients.
     */
    public BuyMqPutCmd(ThreadSafeMessageQueue queue, StockExchange stockExchange, Server server) {
        this.queue = queue;
        this.stockExchange = stockExchange;
        this.server = server;
    }

    @Override
    public void execute(Map<String, Object> params) {
        if (params.containsKey("body")) {
            String body = (String) params.get("body");
            Message message = new Message("EXEC", body);
            String[] tokens = message.getBody().split(" ");

            double quantity = Double.parseDouble(tokens[1]);
            double price = Double.parseDouble(tokens[2]);
            int clientId = Integer.parseInt(tokens[3]);
            String traderId = tokens[4];

            Trader trader = stockExchange.getTraderList().findByTraderId(traderId);
            if (trader == null) {
                return;
            }

            validateAndSetClientID(trader, clientId);

            if (canAffordTrade(trader, quantity, price)) {
                placeOrder(trader, message, quantity, price);
            } else {
                server.getClientHandlerById(trader.getClientID())
                        .sendBack("Not enough funds for a new buy order!");
            }
        }
    }

    private void validateAndSetClientID(Trader trader, int clientId) {
        if (trader.getClientID() == -1) {
            trader.setClientID(clientId);
        } else if (clientId != trader.getClientID()) {
            server.getClientHandlerById(clientId).sendBack("STOP");
            log.error("Couldn't match the client Id");
        }
    }

    private boolean canAffordTrade(Trader trader, double quantity, double price) {
        return price * quantity <= trader.getFunds();
    }

    private void placeOrder(Trader trader, Message message, double quantity, double price) {
        trader.setFunds(trader.getFunds() - price * quantity);
        queue.enqueue(message);
        server.getClientHandlerById(trader.getClientID()).sendBack("Buy order has been placed.");
        pauseForProcessing();
    }

    private void pauseForProcessing() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            log.error("Couldn't sleep the thread" + e);
        }
    }
}