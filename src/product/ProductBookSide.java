package product;

import order.Order;
import order.OrderDTO;
import price.Price;

import java.util.*;

/**
 * A ProductBookSide maintains the contents of one side (Buy or Sell) of a stock product "book". A product book "side"
 * holds the buy or sell orders for a stock that are not yet tradable.
 * Buy-side orders are in descending order, sell-side orders are in ascending order.
 */
public class ProductBookSide {
    private final BookSide side;
    private final HashMap<Price, ArrayList<Order>> bookEntries;

    public ProductBookSide(BookSide side) {
        this.side = side;
        bookEntries = new HashMap<>();
    }

    /**
     * Adds the incoming order to the bookEntries HashMap. If the price for the order does not exist as a key, add the
     * price to the HashMap as the key and a new ArrayList&lt;Order&gt; as the value. Then add the order to that
     * ArrayList.
     * @param o The order to be added.
     * @return An OrderDTO representing the order that was added.
     */
    public OrderDTO add(Order o) {
        if (!bookEntries.containsKey(o.getPrice())) {
            bookEntries.put(o.getPrice(), new ArrayList<>());
            bookEntries.get(o.getPrice()).add(o);
        } else {
            bookEntries.get(o.getPrice()).add(o);
        }
        return o.makeTradableDTO();
    }

    // TODO: ..........
    public OrderDTO cancel(String orderId) {
        Price p = null;
        Order o = null;
        for (var a : bookEntries.entrySet()) {
            for (var b : a.getValue()) {
                if (b.getId().equals(orderId)) {
                    p = a.getKey();
                    o = b;
                    break;
                }
            }
        }
        if (p == null) return null;
        bookEntries.get(p).remove(o);
        o.setCancelledVolume(o.getCancelledVolume() + o.getRemainingVolume());
        o.setRemainingVolume(0);
        if (bookEntries.get(p).isEmpty())
            bookEntries.remove(p);
        System.out.println("CANCEL: " + side + " Order: " + o.getId() + " Cxl Qty: " + o.getCancelledVolume());
        return o.makeTradableDTO();
    }

    /**
     * @return The highest price in the bookEntries HashMap if the side is BUY, or the lowest price if the side is SELL.
     */
    public Price topOfBookPrice() {
        if (bookEntries.isEmpty()) return null;

        TreeMap<Price, ArrayList<Order>> sortedBook =
                side == BookSide.BUY
                ? new TreeMap<>(Comparator.reverseOrder())
                : new TreeMap<>();

        sortedBook.putAll(bookEntries);
        return sortedBook.firstKey();
    }


    /**
     * @return The total of all order volumes at the highest price in the bookEntries HashMap if the side is BUY,
     * or the lowest if the side is SELL.
     */
    public int topOfBookVolume() {
        if (bookEntries.isEmpty()) return 0;

        TreeMap<Price, ArrayList<Order>> sortedBook =
                side == BookSide.BUY
                        ? new TreeMap<>(Comparator.reverseOrder())
                        : new TreeMap<>();

        sortedBook.putAll(bookEntries);
        int totalVol = 0;
        for (Order order : bookEntries.get(sortedBook.firstKey())) {
            totalVol += order.getRemainingVolume();
        }
        return totalVol;
    }

    /**
     * Trade out any orders at or better than the Price passed in, up to the volume value passed in.
     */
    public void tradeOut(Price price, int vol) {
        int remVol = vol;
        ArrayList<Order> orders = bookEntries.get(price);

        while (remVol > 0) {
            Order order = orders.get(0);
            if (order.getRemainingVolume() <= remVol) {
                bookEntries.get(price).remove(order);
                int currentOrderRemVol = order.getRemainingVolume();
                order.setFilledVolume(order.getFilledVolume() + currentOrderRemVol);
                order.setRemainingVolume(0);
                remVol -= currentOrderRemVol;
                System.out.println("FILL: (" + side + " " + currentOrderRemVol + ") " + order);
            } else {
                order.setFilledVolume(order.getFilledVolume() + remVol);
                order.setRemainingVolume(order.getRemainingVolume() - remVol);
                System.out.println("PARTIAL FILL: (" + side + " " + remVol + ") " + order);
                remVol = 0;
            }
        }
        if (orders.isEmpty()) bookEntries.remove(price);

    }

    /**
     * BUY Example Output:
     * Side: BUY
     *   Price: $9.95
     *      CCC order: BUY AMZN at $9.95, Orig Vol: 70, Rem Vol: 70, Fill Vol: 0, CXL Vol: 0, ID: CCCAMZN$9.95189383477035100
     *   Price: $9.90
     *      DDD order: BUY AMZN at $9.90, Orig Vol: 25, Rem Vol: 25, Fill Vol: 0, CXL Vol: 0, ID: DDDAMZN$9.90189383477465600
     * <p>
     * SELL Example Output:
     * Side: SELL
     *   Price: $10.10
     *      EEE order: SELL AMZN at $10.10, Orig Vol: 120, Rem Vol: 10, Fill Vol: 110, CXL Vol: 0, ID: EEEAMZN$10.10189383478470500
     *   Price: $10.20
     *      FFF order: SELL AMZN at $10.20, Orig Vol: 45, Rem Vol: 45, Fill Vol: 0, CXL Vol: 0, ID: FFFAMZN$10.20189383478919000
     *   Price: $10.25
     *      GGG order: SELL AMZN at $10.25, Orig Vol: 90, Rem Vol: 90, Fill Vol: 0, CXL Vol: 0, ID: GGGAMZN$10.25189383479288700
     */
    @Override
    public String toString() {
        TreeMap<Price, ArrayList<Order>> sortedBook =
                side == BookSide.BUY
                        ? new TreeMap<>(Comparator.reverseOrder())
                        : new TreeMap<>();

        sortedBook.putAll(bookEntries);

        StringBuilder sideSummary = new StringBuilder();
        sideSummary.append("Side: ").append(side).append("\n");
        if (sortedBook.isEmpty()) {
            sideSummary.append("     <Empty>\n");
        } else {
            sortedBook.forEach((price, orders) -> {
                sideSummary.append(" Price: ").append(price).append("\n");
                orders.forEach(order ->
                        sideSummary.append("     ").append(order).append("\n"));
            });
        }

        return sideSummary.toString();
    }

}

//    private ArrayList<Price.Price> getSortedPrices() {
//        ArrayList<Price.Price> sorted = new ArrayList<>(bookEntries.keySet());
//        Collections.sort(sorted);
//        if (side == product.BookSide.BUY) {
//            Collections.reverse(sorted);
//        }
//        return sorted;
//    }