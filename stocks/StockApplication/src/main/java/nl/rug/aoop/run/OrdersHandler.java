package nl.rug.aoop.run;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.message.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * The class of an order handler to deal with the possible orders.
 */
@Slf4j
public class OrdersHandler {
    private CommandHandler commandHandler;

    /**
     * The constructor of the order handler.
     * @param commandHandler the command handler to use.
     */
    public OrdersHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    /**
     * Method to handle a possible order.
     * @param message the given message to be handled.
     */
    public void handleMessage(String message) {
        Message tempMsg = Message.fromJson(message);
        
        String header = tempMsg.getHeader();
        String[] tokens = tempMsg.getBody().split(" ");
        String stockSymbol = tokens[0];
        double quantity = Double.parseDouble(tokens[1]);
        double price = Double.parseDouble(tokens[2]);
        int clientId = Integer.parseInt(tokens[3]);
        String traderId = tokens[4];

        Map<String, Object> params = mapParams(stockSymbol, quantity, price, traderId, clientId);

        if (commandHandler.isCommand(header)) {
            commandHandler.handleCmd(header, params);
        }
    }

    private static Map<String, Object> mapParams(String stockSymbol, double quantity,
                                                 double price, String traderId, int clientId) {
        Map<String, Object> params = new HashMap<>();
        params.put("stockSymbol", stockSymbol);
        params.put("stockQuantity", quantity);
        params.put("stockPrice", price);
        params.put("traderId", traderId);
        params.put("clientId", clientId);
        return params;
    }
}
