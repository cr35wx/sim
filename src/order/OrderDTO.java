package order;

import price.Price;
import product.BookSide;

/**
 * The OrderDTO class contains a copy of an order's data, used to transfer or return data about an order without
 * giving out a reference to the actual order.
 */
public class OrderDTO {
    public final String user;
    public final String product;
    public final Price price;
    public final BookSide side;
    public final String id;
    public final int originalVolume;
    public int remainingVolume;
    public int cancelledVolume;
    public int filledVolume;

    public OrderDTO(String user, String product, Price price, BookSide side, String id, int originalVolume,
                    int remainingVolume, int cancelledVolume, int filledVolume) {
        this.user = user;
        this.product = product;
        this.price = price;
        this.side = side;
        this.id = id;
        this.originalVolume = originalVolume;
        this.remainingVolume = originalVolume;
        this.cancelledVolume = cancelledVolume;
        this.filledVolume = filledVolume;
    }

    @Override
    public String toString() {
        return user + " order: " + side + " " + product + " at " + price + ", Orig Vol: " + originalVolume + ", Rem Vol: "
                + remainingVolume + ", Fill Vol: " + filledVolume + ", CXL Vol: " + cancelledVolume + ", ID: " + id;
    }
}
