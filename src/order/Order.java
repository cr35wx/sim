package order;

import price.Price;
import exceptions.DataValidationException;
import product.BookSide;

/**
 * The Order class represents an order to buy or sell a volume (i.e., quantity) of stock at a certain price.
 * The order will also maintain its processing state.
 */
public class Order {
    /** A 3 letter user code. */
    private final String user;

    /** A 1-5 letter stock symbol. */
    private final String product;

    private final Price price;
    private final BookSide side;

    /** A unique identifier for this order. */
    private final String id;

    /** The quantity of the stock being ordered. */
    private final int originalVolume;

    private int remainingVolume;
    private int cancelledVolume;
    private int filledVolume;


    public Order(String user, String product, Price price, int originalVolume , BookSide side)
            throws DataValidationException {
        this.user = validateUser(user);
        this.product = validateProduct(product);
        this.price = price;
        this.side = validateSide(side);
        id = user + product + price + System.nanoTime();
        this.originalVolume = validateOrigVol(originalVolume);
        remainingVolume = originalVolume;
        cancelledVolume = 0;
        filledVolume = 0;
    }

    /**
     * This method will validate the Order constructor user parameter and return it if valid.
     * @return a validated user String.
     * @throws DataValidationException If the user parameter is null or does not match the pattern ^[A-Z]{3}$
     * (any 3 uppercase letters from A-Z).
     */
    private String validateUser(String user) throws DataValidationException {
        if (user == null || !user.matches("^[A-Z]{3}$")) {
            throw new DataValidationException("Invalid User");
        }

        return user;

    }
    /**
     * This method will validate the Order constructor product parameter and return it if valid.
     * @return a validated product String.
     * @throws DataValidationException If the product parameter is null or does not match the pattern ^[A-Z]{3}$
     * (any 3 uppercase letters from A-Z).
     */
    private String validateProduct(String product) throws DataValidationException {
        if (product == null || !product.matches("^[A-Z0-9.]{1,5}$")) {
            throw new DataValidationException("Invalid Product");
        }

        return product;
    }

    /**
     * This method will validate the Order constructor side parameter and return it if valid.
     * @param side The side of the order (BUY or SELL).
     * @return a validated side BookSide.
     * @throws DataValidationException If side is null.
     */
    private BookSide validateSide(BookSide side) throws DataValidationException {
        if (side == null) {
            throw new DataValidationException("Invalid Side");
        }
        return side;
    }

    /**
     * This method will validate the Order constructor originalVolume parameter and return it if valid.
     * @param originalVolume The original volume of the order.
     * @return a validated originalVolume int.
     * @throws DataValidationException If originalVolume is less than 1 or greater than 10000.
     */
    private int validateOrigVol(int originalVolume) throws DataValidationException {
        if (originalVolume < 1 || originalVolume > 10000) {
            throw new DataValidationException("Invalid Original Volume");
        }
        return originalVolume;
    }

    /**
     * @return a Data Transfer Object for this order.
     */
    public OrderDTO makeTradableDTO() {
        return new OrderDTO(user, product, price, side, id,
                originalVolume, remainingVolume, cancelledVolume, filledVolume);

    }

    public String getProduct() {
        return product;
    }

    public Price getPrice() {
        return price;
    }

    public BookSide getSide() {
        return side;
    }

    public String getId() {
        return id;
    }

    public int getRemainingVolume() {
        return remainingVolume;
    }

    public void setRemainingVolume(int amount) {
        remainingVolume = amount;
    }

    public int getCancelledVolume() {
        return cancelledVolume;
    }

    public void setCancelledVolume(int amount) {
        cancelledVolume = amount;
    }

    public int getFilledVolume() {
        return filledVolume;
    }

    public void setFilledVolume(int filledVolume) {
        this.filledVolume = filledVolume;
    }

    /**
     * Example output:
     * <p>
     * XRF order: BUY AMZN at $96.38, Orig Vol: 50, Rem Vol: 50, Fill Vol: 0, CXL Vol: 0, ID:
     * AAAAMZN$96.38158311853491300
     */
    @Override
    public String toString() {
        return user + " order: " + side + " " + product + " at " + price + ", Orig Vol: " + originalVolume + ", Rem Vol: "
                + remainingVolume + ", Fill Vol: " + filledVolume + ", CXL Vol: " + cancelledVolume + ", ID: " + id;
    }


}
