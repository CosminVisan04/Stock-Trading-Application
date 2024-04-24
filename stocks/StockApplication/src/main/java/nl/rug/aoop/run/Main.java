package nl.rug.aoop.run;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.initialization.SimpleViewFactory;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.queue.ThreadSafeMessageQueue;
import nl.rug.aoop.networking.server.MessageHandle;
import nl.rug.aoop.networking.server.ClientHandler;
import nl.rug.aoop.networking.server.Server;
import nl.rug.aoop.orders.BuyMqPutCmd;
import nl.rug.aoop.orders.ExecuteCmd;
import nl.rug.aoop.orders.SellMqPutCmd;
import nl.rug.aoop.stock.Stock;
import nl.rug.aoop.stock.StockList;
import nl.rug.aoop.trader.Trader;
import nl.rug.aoop.trader.TraderList;
import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Path;

import static org.awaitility.Awaitility.await;

/**
 * The main class for the stock application.
 */
@Slf4j
public class Main {
    private static Server server;

    /**
     * The main method of the Stock Application.
     * @param args containing the command line arguments.
     * @throws IOException in case of an IO Exception.
     */
    public static void main(String[] args) throws IOException {
        TraderList traderList = loadYAMLTraderList();
        StockList stockList = loadYAMLStockList();

        StockExchange dataStockExchange = generateStockExchange(stockList, traderList);

        CommandHandler commandHandler = new CommandHandler();
        MessageHandle messageHandler = new MessageHandle(commandHandler);

        startServer(messageHandler);
        await().until(() -> server.isRunning());
        startPeriodicBroadcast(dataStockExchange);

        BuyMqPutCmd buyMqPutCmd = new BuyMqPutCmd(dataStockExchange.getBuyQueue(), dataStockExchange, server);
        ExecuteCmd executeCmd = new ExecuteCmd(dataStockExchange);
        SellMqPutCmd sellMqPutCmd = new SellMqPutCmd(dataStockExchange.getSellQueue(), dataStockExchange, server);
        commandHandler.newCommand("SELL", sellMqPutCmd);
        commandHandler.newCommand("BUY", buyMqPutCmd);
        commandHandler.newCommand("EXEC", executeCmd);

        OrdersHandler orderHandler = new OrdersHandler(commandHandler);
        SimpleViewFactory view = new SimpleViewFactory();
        view.createView(dataStockExchange);
        while (true) {
            while (dataStockExchange.getBuyQueue().getSize() != 0) {
                Message message = dataStockExchange.getBuyQueue().dequeue();
                orderHandler.handleMessage(message.toJson());
            }
        }
    }

    private static StockExchange generateStockExchange(StockList stockList, TraderList traderList) {
        ThreadSafeMessageQueue buyQueue = new ThreadSafeMessageQueue();
        ThreadSafeMessageQueue sellQueue = new ThreadSafeMessageQueue();
        return new StockExchange(stockList, traderList, buyQueue, sellQueue);
    }

    private static StockList loadYAMLStockList() throws IOException {
        YamlLoader yaml = new YamlLoader(Path.of("C:\\Cosmin\\University\\RUG\\Year 2\\Semester 1\\1a\\" +
                "Advaced Object Oriented Programming\\2023_Team_057\\stocks\\data\\stocks.yaml"));
        return yaml.load(StockList.class);
    }

    private static TraderList loadYAMLTraderList() throws IOException {
        YamlLoader yaml = new YamlLoader(Path.of("C:\\Cosmin\\University\\RUG\\Year 2\\Semester 1\\1a\\" +
                "Advaced Object Oriented Programming\\2023_Team_057\\stocks\\data\\traders.yaml"));
        return yaml.load(TraderList.class);
    }

    private static void startServer(MessageHandle messageHandler) {
        Thread serverThread = new Thread(() -> {
            try {
                server = new Server(6200, messageHandler);
                server.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
    }

    private static void startPeriodicBroadcast(StockExchange stockExchange) {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    sendStocksInformation(stockExchange);
                    sendTraderInformation(stockExchange);
                    sendTradingHistory(stockExchange);
                } catch (InterruptedException e) {
                    log.error("Could not send periodic info messages!");
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    private static void sendTraderInformation(StockExchange stockExchange) {
        for (ClientHandler clientHandler : server.getClientHandlerList().values()) {
            String periodicMessage = "";
            Trader tempTrader = stockExchange.getTraderList().findByClientId(clientHandler.getId());
            if (tempTrader != null) {
                periodicMessage = periodicMessage + tempTrader.getName() + " " + tempTrader.getId() + " "
                        + tempTrader.getFunds() + " " + tempTrader.getOwnedStocks();

                clientHandler.sendBack(periodicMessage);
            }
        }
    }

    private static void sendStocksInformation(StockExchange model) {
        for (Stock stock : model.getStockList().getStockList()) {
            server.sendPeriodicalMessages(stock.getSymbol() + " " + stock.getPrice());
        }
    }

    private static void sendTradingHistory(StockExchange stockExchange){
        for (ClientHandler clientHandler : server.getClientHandlerList().values()) {
            String history = "";
            Trader tempTrader = stockExchange.getTraderList().findByClientId(clientHandler.getId());
            if (tempTrader != null) {
                for(String transaction : tempTrader.getTransactionHistory()){
                    history = history + transaction + "\n";
                }
                clientHandler.sendBack(history);
            }
        }
    }
}