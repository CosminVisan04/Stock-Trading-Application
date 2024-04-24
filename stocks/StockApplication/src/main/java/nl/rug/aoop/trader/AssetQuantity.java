package nl.rug.aoop.trader;

import lombok.Getter;
import lombok.Setter;

/**
 * The class used to map a stock with the quantity a trader is owning.
 */
@Getter
@Setter
public class AssetQuantity {
    private String stockSymbol;
    private double quantityShares;
}
