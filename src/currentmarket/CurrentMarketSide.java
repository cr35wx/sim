package currentmarket;

import price.Price;

/**
 * This class holds the top price and top volume (at that price) for one market side. It is used with current market
 * publications.
 */
public class CurrentMarketSide {
    private final Price price;
    private final int volume;

    public CurrentMarketSide(Price price, int volume) {
        this.price = price;
        this.volume = volume;
    }

    /**
     * @return A string with the top of book price and volume formatted as: $98.10x105
     */
    @Override
    public String toString() {
       return price + "x" + volume;
    }
}
