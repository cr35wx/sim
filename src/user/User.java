package user;

import currentmarket.CurrentMarketObserver;
import currentmarket.CurrentMarketSide;
import exceptions.DataValidationException;
import order.OrderDTO;

import java.util.HashMap;

/**
 * This class represents a user (trader) of the trading system. Traders will enter orders
 * for trading in our system. The user will also maintain a collection of orders submitted.
 */
public class User implements CurrentMarketObserver {

    private final String userId;
    private final HashMap<String, OrderDTO> orders;
    private final HashMap<String, CurrentMarketSide[]> currentMarkets;
    public User(String userId) throws DataValidationException {
        this.userId = setUserId(userId);
        this.orders = new HashMap<>();
         this.currentMarkets = new HashMap<>();
    }

    /**
     * This method will validate the userId constructor parameter and set the userId field.
     * @param userId The user id to be validated and set.
     * @return The validated userId.
     * @throws DataValidationException If the user id is null or does not match the pattern ^[A-Z]{3}$
     * (any 3 uppercase letters from A-Z).
     */
    private String setUserId(String userId) throws DataValidationException {
        if (userId == null || !userId.matches("^[A-Z]{3}$")) {
            throw new DataValidationException("Invalid setUserId parameter");
        }

        return userId;
    }

    public String getUserId() {
        return userId;
    }

    /**
     * Add the incoming OrderDTO to the user's orders HashMap
     * @param o The OrderDTO to be added.
     * @throws DataValidationException If OrderDTO o is null.
     */
    public void addOrder(OrderDTO o) throws DataValidationException {
        if (o == null) {
            throw new DataValidationException("Invalid addOrder parameter");
        }

        orders.put(o.id, o);
    }

    /**
     * @return True if any of the OrderDTOs in the user's orders HashMap have a remainingQuantity > 0. False otherwise.
     */
    public boolean hasOrderWithRemainingQty() {
        for (var o : orders.entrySet()) {
            if (o.getValue().remainingVolume > 0) return true;
        }

        return false;
    }

    /**
     * @return Any order from the user's orders HashMap that has a remainingQuantity > 0. Null otherwise.
     */
    public OrderDTO getOrderWithRemainingQty() {
        for (var o : orders.entrySet()) {
            if (o.getValue().remainingVolume > 0) {
                return o.getValue();
            }
        }

        return null;
    }

    /**
     * Isn't this just beautiful?
     * <p>
     * Example output:
     * User Id: CAT
     *   Product: GOOG, Price: $52.45, OriginalVolume: 220, RemainingVolume: 0, CancelledVolume:
     *   0, FilledVolume: 220, User: CAT, Side: SELL, Id: CATGOOG$52.4557855274865100
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("User ID: ").append(userId).append("\n");
        for (var o : orders.entrySet()) {
            OrderDTO order = o.getValue();
            s.append("  Product: ").append(order.product).append(", Price: ").append(order.price)
                    .append(", OriginalVolume: ").append(order.originalVolume).append(", RemainingVolume: ")
                    .append(order.remainingVolume).append(", CancelledVolume: ").append(order.cancelledVolume)
                    .append(", FilledVolume: ").append(order.filledVolume).append(", User: ").append(order.user)
                    .append(", Side: ").append(order.side).append(", Id: ").append(order.id).append("\n");
        }

        return s.toString();
    }

    /**
     * This method should create a 2-element array of CurrentMarketSide objects ([0]=Buy, [1]=Sell).
     * Then put the values in the currentMarkets HashMap, using the stock symbol as the key and the array of
     * CurrentMarketSide objects as the value.
     * @param symbol The stock symbol/id (e.g. "TSLA", "AAPL").
     * @param buySide CurrentMarketSide object representing the buy side of the market.
     * @param sellSide CurrentMarketSide object representing the sell side of the market.
     */
    @Override
    public void updateCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide) {
        CurrentMarketSide[] marketSides = new CurrentMarketSide[] {buySide, sellSide};
        currentMarkets.put(symbol, marketSides);
    }

    /**
     * Example output:
     * TGT $87.45x210 - $87.65x75
     * WMT $70.20x170 - $70.65x15
     *
     * @return A summary of the values in the currentMarkets HashMap.
     */
    public String getCurrentMarkets() {
        StringBuilder s = new StringBuilder();
        for (var market : currentMarkets.entrySet()) {
            s.append(market.getKey()).append("   ").append(market.getValue()[0])
                    .append(" - ").append(market.getValue()[1]).append("\n");
        }


        return s.toString();
    }
}
