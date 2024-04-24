package nl.rug.aoop.trader;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The class of a List of traders.
 */
@Getter
@Setter
public class TraderList {
    private List<Trader> traderList;

    /**
     * Find a trader by a given ID.
     * @param id the id of the trader to be found.
     * @return the trader with the given id.
     */
    public Trader findByTraderId(String id) {
        for (Trader trader : traderList) {
            if (trader.getId().equals(id)) {
                return trader;
            }
        }
        return null;
    }

    /**
     * Find a trader by a given client ID.
     * @param id the id of the client id to find a trader.
     * @return the trader with the given ID.
     */
    public Trader findByClientId(int id) {
        for (Trader trader : traderList) {
            if (trader.getClientID() == id) {
                return trader;
            }
        }
        return null;
    }
}
