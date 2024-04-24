package nl.rug.aoop.trader;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.client.Client;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * A class that maps a trader with a client.
 */
@Getter
@Setter
@Slf4j
public class TradingClient {

    private Trader trader;
    private Client client;

    /**
     * For every new trader, a network client is created.
     * @param trader the trader for which the client is created.
     * @param address the address where to connect the client.
     * @throws IOException exceptions with connecting the client.
     */
    public TradingClient(Trader trader, InetSocketAddress address) throws IOException {
        this.trader = trader;
        this.client = new Client(address, (message, responder) -> {
            log.info("Client " + trader.getId() + " got: " + message);
        });
    }
}
