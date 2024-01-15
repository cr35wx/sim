package price;

import exceptions.InvalidPriceOperation;

import java.util.HashMap;

/**
 * The PriceFactory class creates Price objects, and stores them in a HashMap to avoid creating duplicate Price objects.
 * Price objects should not be created with "new Price()" outside of this class.
 */
public abstract class PriceFactory {
    private static final HashMap<Integer, Price> prices = new HashMap<>();

    /**
     * Creates a Price object from an int.
     * @param value An int value representing the price in cents.
     * @return A new Price object.
     */
    public static Price makePrice(int value) {
        if (prices.containsKey(value)) return prices.get(value);

        Price price = new Price(value);
        prices.put(value, price);
        return price;
    }

    /**
     * Creates a Price object from a String.
     * @param stringValueIn The String of numbers to be parsed into a Price object.
     * @return A new Price object.
     * @throws InvalidPriceOperation If the String is null, or if the String contains no numbers.
     */
    public static Price makePrice(String stringValueIn) throws InvalidPriceOperation {
        if (stringValueIn == null) {
            throw new InvalidPriceOperation("makePrice parameter is null");
        }

        String parsedInput = stringValueIn.replaceAll("[^-0-9]", "");

        if (parsedInput.equals("")) {
            throw new InvalidPriceOperation("A price with no numbers?");
        }

        int value = Integer.parseInt(parsedInput);

        if (prices.containsKey(value)) return prices.get(value);

        Price price = new Price(value);
        prices.put(value, price);
        return price;

    }
}