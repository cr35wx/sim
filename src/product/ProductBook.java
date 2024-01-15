package product;

import currentmarket.CurrentMarketTracker;
import order.Order;
import order.OrderDTO;
import price.Price;
import exceptions.DataValidationException;
import exceptions.InvalidPriceOperation;
import exceptions.OrderNotFoundException;

/**
 * A ProductBook maintains the Buy and Sell sides of a stock's "book". A stock's book holds the buy and sell orders for
 * a stock that is not yet tradable. The buy-side of the book contains all buy orders that are not yet tradable in
 * descending order. The sell-side of the book contains all sell orders that are not yet tradable in ascending order.
 * Many of the functions owned by the ProductBook class are designed to simply pass along that same call to either
 * the Buy or Sell side of the book.
 */
public class ProductBook {
    /** A 1-5 letter stock symbol. */
    private final String product;
    private ProductBookSide buySide;
    private ProductBookSide sellSide;

    public ProductBook(String product) throws DataValidationException {
        this.product = validateProduct(product);
        buySide = new ProductBookSide(BookSide.BUY);
        sellSide = new ProductBookSide(BookSide.SELL);
    }

    public OrderDTO add(Order o) throws InvalidPriceOperation, OrderNotFoundException {
        if (o == null) throw new OrderNotFoundException("Order not found");
        System.out.println("ADD: " + o.getSide() + ": " + o);
        OrderDTO dto = o.getSide() == BookSide.BUY
                ? buySide.add(o)
                : sellSide.add(o);

        tryTrade();
        updateMarket();
        return dto;
    }

    public OrderDTO cancel(BookSide side, String orderId) throws InvalidPriceOperation {
         OrderDTO cancelledDTO = (side == BookSide.BUY)
                ? buySide.cancel(orderId)
                : sellSide.cancel(orderId);
         updateMarket();
         return cancelledDTO;
    }

    /**
     * Checks to see if the book sides are tradable, and if so, performs the trades. If not, it does nothing.
     * @throws InvalidPriceOperation If topSellPrice is null.
     */
    public void tryTrade() throws InvalidPriceOperation {
        Price topBuyPrice = buySide.topOfBookPrice();
        Price topSellPrice = sellSide.topOfBookPrice();

        while (topBuyPrice != null && topSellPrice != null
                && topBuyPrice.greaterOrEqual(topSellPrice)) {
            int topBuyVol = buySide.topOfBookVolume();
            int topSellVol = sellSide.topOfBookVolume();
            int volToTrade = Math.min(topBuyVol, topSellVol);

            sellSide.tradeOut(topSellPrice, volToTrade);
            buySide.tradeOut(topBuyPrice, volToTrade);

            topBuyPrice = buySide.topOfBookPrice();
            topSellPrice = sellSide.topOfBookPrice();
        }
    }
    /** This method will validate the ProductBook constructor product parameter and return it if valid.
     * @return a validated product String.
     * @throws DataValidationException If the product parameter is null or does not match the pattern "^[A-Z0-9.]{1,5}$"
     * (1-5 uppercase letters from A-Z, numbers from 0-9, or a ".").
     */
    public String validateProduct(String product) throws DataValidationException {
        if (product == null || !product.matches("^[A-Z0-9.]{1,5}$")) {
            throw new DataValidationException("Invalid Product");
        }

        return product;
    }

    private void updateMarket() throws InvalidPriceOperation {
        Price topBuyPrice = buySide.topOfBookPrice();
        Price topSellPrice = sellSide.topOfBookPrice();
        int topBuyVol = buySide.topOfBookVolume();
        int topSellVol = sellSide.topOfBookVolume();

        CurrentMarketTracker.getInstance().updateMarket(product, topBuyPrice, topBuyVol, topSellPrice, topSellVol);
    }


    /**
     * Example output:
     * <p>
     * Product: WMT
     * Side: BUY
     *   Price: $9.95
     *      CCC order: BUY AMZN at $9.95, Orig Vol: 70, Rem Vol: 70, Fill Vol: 0, CXL Vol: 0, ID: CCCAMZN$9.95186495726402400
     *   Price: $9.90
     *      DDD order: BUY AMZN at $9.90, Orig Vol: 25, Rem Vol: 25, Fill Vol: 0, CXL Vol: 0, ID: DDDAMZN$9.90186495726811600
     * Side: SELL
     *   Price: $10.10
     *      EEE order: SELL AMZN at $10.10, Orig Vol: 120, Rem Vol: 10, Fill Vol: 110, CXL Vol: 0, ID: EEEAMZN$10.10186495727149200
     *   Price: $10.20
     *      FFF order: SELL AMZN at $10.20, Orig Vol: 45, Rem Vol: 45, Fill Vol: 0, CXL Vol: 0, ID: FFFAMZN$10.20186495727529400
     *   Price: $10.25
     *      GGG order: SELL AMZN at $10.25, Orig Vol: 90, Rem Vol: 90, Fill Vol: 0, CXL Vol: 0, ID: GGGAMZN$10.25186495727930400
     */
    @Override
    public String toString() {
        return "Product: " + product + "\n" + buySide.toString() + sellSide.toString();
    }


}
