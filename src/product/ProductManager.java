package product;

import exceptions.DataValidationException;
import exceptions.InvalidPriceOperation;
import exceptions.OrderNotFoundException;
import order.Order;
import order.OrderDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * The ProductManager class is a singleton that maintains a collection of ProductBook objects for all stocks used
 * in the system, it acts as a Facade to the ProductBooks.
 */
public final class ProductManager {
    private static ProductManager instance;

    private ProductManager() {}

    public static ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }

        return instance;
    }

    private final HashMap<String, ProductBook> books = new HashMap<>();

    /**
     * Creates a new ProductBook object for the stock symbol passed in, and adds it to the books HashMap.
     * @param symbol The stock symbol to create a ProductBook with.
     * @throws DataValidationException If the symbol is null or doesn't match the pattern ^[A-Z0-9.]{1,5}$
     * (1-5 uppercase letters from A-Z, numbers from 0-9, or a ".").
     */
    public void addProduct(String symbol) throws DataValidationException {
        ProductBook product = new ProductBook(symbol);
        books.put(symbol, product);
    }

    /**
     * @return A randomly selected product symbol from the books HashMap.
     */
    public String getRandomProduct() {
        ArrayList<String> symbols = new ArrayList<>(books.keySet());
        int randomIndex = new Random().nextInt(symbols.size());
        return symbols.get(randomIndex);
    }

    /**
     * Adds the Order o to the ProductBook using the String product symbol from the Order to determine which ProductBook
     * it goes to.
     * @param o The Order to be added.
     * @return An OrderDTO representing the order that was added.
     * @throws OrderNotFoundException If o is null.
     */
    public OrderDTO addOrder(Order o) throws OrderNotFoundException, InvalidPriceOperation {
        String symbol = o.getProduct();
        return books.get(symbol).add(o);
    }

    /**
     * Using the String product symbol from the OrderDTO passed in, find the ProductBook and call that its
     * cancel method.
     * @param o The OrderDTO to be cancelled.
     * @return The OrderDTO of the cancelled order or null if the cancellation failed.
     */
    public OrderDTO cancel(OrderDTO o) throws InvalidPriceOperation {
        ProductBook book = books.get(o.product);
        OrderDTO cancelledOrder = book.cancel(o.side, o.id);
        if (cancelledOrder == null) System.out.println("The cancel process has failed.");
        return cancelledOrder;
    }

    /**
     * Example output:
     * Product: AMZN
     * Side: BUY
     *   Price: $51.15
     *      DOG order: BUY AMZN at $51.15, Orig Vol: 180, Rem Vol: 135, Fill Vol: 45, CXL Vol: 0,
     *      ID: DOGAMZN$51.1581541872963500
     *   Price: $51.00
     *      CAT order: BUY AMZN at $51.00, Orig Vol: 245, Rem Vol: 175, Fill Vol: 70, CXL Vol: 0,
     *      ID: CATAMZN$51.0081541863804400
     *   Price: $50.70
     *      DOG order: BUY AMZN at $50.70, Orig Vol: 140, Rem Vol: 140, Fill Vol: 0, CXL Vol: 0,
     *      ID: DOGAMZN$50.7081541863058100
     *      ANN order: BUY AMZN at $50.70, Orig Vol: 70, Rem Vol: 70, Fill Vol: 0, CXL Vol: 0,
     *      ID: ANNAMZN$50.7081541866669700
     *      DOG order: BUY AMZN at $50.70, Orig Vol: 260, Rem Vol: 260, Fill Vol: 0, CXL Vol: 0,
     *     ID: DOGAMZN$50.7081541872554400
     * <p>
     * Side: SELL
     *   Price: $51.30
     *      DOG order: SELL AMZN at $51.30, Orig Vol: 210, Rem Vol: 210, Fill Vol: 0, CXL Vol: 0,
     *      ID: DOGAMZN$51.3081541823040600
     *   Price: $51.35
     *      BOB order: SELL AMZN at $51.35, Orig Vol: 110, Rem Vol: 110, Fill Vol: 0, CXL Vol: 0,
     *      ID: BOBAMZN$51.3581541866450900
     *   Price: $51.50
     *      CAT order: SELL AMZN at $51.50, Orig Vol: 185, Rem Vol: 185, Fill Vol: 0, CXL Vol: 0,
     *      ID: CATAMZN$51.5081541846788400
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (var book : books.values()) {
            s.append(book.toString()).append("\n");
        }

        return s.toString();
    }

}
