package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.networking.producer.NetWorkProducer;
import nl.rug.aoop.stock.StockList;
import nl.rug.aoop.trader.TraderList;
import nl.rug.aoop.trader.TradingClient;
import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.Random;

/**
 * Main class for the Trading Application.
 */
@Slf4j
public class MainTradingClient {
    private static final Random RANDOM = new Random();

    /**
     * The main method for the Trader Application.
     * @param args containing the command line arguments.
     */
    public static void main(String[] args) {
        try {
            TraderList traderList = loadTraders();
            StockList stockList = loadStocks();
            InetSocketAddress address = new InetSocketAddress("localhost", 6200);

            int randomIndexTrader = RANDOM.nextInt(traderList.getTraderList().size());
            TradingClient tradingBot = new TradingClient(traderList.getTraderList().get(randomIndexTrader), address);
            tradingBot.getTrader().setClientID(tradingBot.getClient().getClientHandlerId());
            NetWorkProducer netProd = new NetWorkProducer(tradingBot.getClient());
            Thread.sleep(1000);

            sendCommands(stockList, tradingBot, netProd);
        } catch (IOException e) {
            log.error("Failed to create a trading bot");
        } catch (InterruptedException e) {
            log.error("Couldn't sleep");
        }
    }

    private static void sendCommands(StockList stockList, TradingClient tradingBot, NetWorkProducer netProd)
            throws InterruptedException {
        while(true) {
            StrategyRandom strategyRandom = new StrategyRandom();
            Message tempMsg = strategyRandom.
                    createRandomOrderMessage(stockList, tradingBot.getClient().getClientHandlerId());
            tradingBot.getTrader().placeOrder(tempMsg, stockList, netProd);
            Thread.sleep(4000);
        }
    }

    private static TraderList loadTraders() throws IOException {
        YamlLoader yaml = new YamlLoader(Path.of("C:\\Cosmin\\University\\RUG\\Year 2\\Semester 1\\1a\\" +
                "Advaced Object Oriented Programming\\2023_Team_057\\stocks\\data\\traders.yaml"));
        return yaml.load(TraderList.class);
    }

    private static StockList loadStocks() throws IOException {
        YamlLoader yaml = new YamlLoader(Path.of("C:\\Cosmin\\University\\RUG\\Year 2\\Semester 1\\1a\\" +
                "Advaced Object Oriented Programming\\2023_Team_057\\stocks\\data\\stocks.yaml"));
        return yaml.load(StockList.class);
    }
}