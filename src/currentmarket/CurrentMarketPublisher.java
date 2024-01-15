package currentmarket;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is a singleton that uses the observer pattern. It maintains a list of observers, and filters what
 * observers want from the Current Market for what stocks. It also publishes the Current Market updates to the
 * subscribed observers.
 */
public final class CurrentMarketPublisher {
    private static CurrentMarketPublisher instance;

    private CurrentMarketPublisher() {}

    public static CurrentMarketPublisher getInstance() {
        if (instance == null) {
            instance = new CurrentMarketPublisher();
        }

        return instance;
    }


    /** &lt;Stock, List of CurrentMarketObserver implementors watching for that stock&gt; */
    private final HashMap<String, ArrayList<CurrentMarketObserver>> filters = new HashMap<>();

    /**
     * Subscribe a market observer (e.g. User) to a stock by adding them to the filters HashMap.
     * @param symbol Stock symbol/id (e.g. "TSLA", "AAPL").
     * @param cmo The market observer who wants to be subscribed to the stock.
     */
    public void subscribeCurrentMarket(String symbol, CurrentMarketObserver cmo) {
        if (filters.containsKey(symbol)) {
            filters.get(symbol).add(cmo);
        } else {
            filters.put(symbol, new ArrayList<>());
            filters.get(symbol).add(cmo);
        }
    }

    /**
     * Unsubscribe a market observer (e.g. User) to a stock by removing them from the filters HashMap.
     * @param symbol Stock symbol/id (e.g. "TSLA", "AAPL").
     * @param cmo The market observer who wants to be unsubscribed from the stock.
     */
    public void unSubscribeCurrentMarket(String symbol, CurrentMarketObserver cmo) {
        if (filters.containsKey(symbol)) {
            filters.get(symbol).remove(cmo);
        }
    }

    /**
     * Accept a current market update from the ProductBook and pass it on to the market observers.
     * @param symbol Stock symbol/id (e.g. "TSLA", "AAPL").
     * @param buySide CurrentMarketSide object representing the buy side of the market.
     * @param sellSide CurrentMarketSide object representing the sell side of the market.
     */
    public void acceptCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide) {
        if (filters.containsKey(symbol)) {
            filters.get(symbol).forEach(currentMarketObserver ->
                    currentMarketObserver.updateCurrentMarket(symbol, buySide, sellSide));
        }
    }

}
