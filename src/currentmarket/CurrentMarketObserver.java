package currentmarket;

/**
 * This interface is implemented by classes that want to be able to register for
 * Current Market updates (currently the User class will implement this).
 */
public interface CurrentMarketObserver {
    /**
     * This method is called by the CurrentMarketPublisher when a CurrentMarket update occurs.
     * @param symbol The stock symbol/id (e.g. "TSLA", "AAPL").
     * @param buySide CurrentMarketSide object representing the buy side of the market.
     * @param sellSide CurrentMarketSide object representing the sell side of the market.
     */
    void updateCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide);
}
