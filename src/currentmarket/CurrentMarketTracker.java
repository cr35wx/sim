package currentmarket;

import price.Price;
import exceptions.InvalidPriceOperation;
import price.PriceFactory;

/**
 * A singleton that receives updates from the ProductBooks, and sends the information on to the CurrentMarketPublisher.
 */
public final class CurrentMarketTracker {
    private static CurrentMarketTracker instance;

    private CurrentMarketTracker() {}

    public static CurrentMarketTracker getInstance() {
        if (instance == null) {
            instance = new CurrentMarketTracker();
        }

        return instance;
    }

    /**
     * This method is called by the ProductBook when a change occurs to the market.
     * @param symbol The stock symbol/id (e.g. "TSLA", "AAPL").
     * @param buyPrice The current buy price of the stock.
     * @param buyVolume The current buy volume of the stock.
     * @param sellPrice The current sell price of the stock.
     * @param sellVolume The current sell volume of the stock.
     * @throws InvalidPriceOperation If a created price is null or not a number.
     */
    public void updateMarket(String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume)
            throws InvalidPriceOperation {

        Price marketWidth;
        if (buyPrice == null) {
            buyPrice = PriceFactory.makePrice(0);
            marketWidth = PriceFactory.makePrice(0);
        } else if (sellPrice == null) {
            sellPrice = PriceFactory.makePrice(0);
            marketWidth = PriceFactory.makePrice(0);
        } else {
            marketWidth = sellPrice.subtract(buyPrice);
        }

        CurrentMarketSide buySide = new CurrentMarketSide(buyPrice, buyVolume);
        CurrentMarketSide sellSide = new CurrentMarketSide(sellPrice, sellVolume);

        System.out.println("*********** Current Market ***********");
        System.out.println("* " + symbol + "   " + buySide + " - " + sellSide + " [" + marketWidth + "]");
        System.out.println("**************************************");

        CurrentMarketPublisher.getInstance().acceptCurrentMarket(symbol, buySide, sellSide);
    }

}
