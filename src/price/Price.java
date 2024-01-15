package price;

import exceptions.InvalidPriceOperation;

/**
 * The Price class represents a price in its cents value to avoid floating point errors.
 */
public class Price implements Comparable<Price> {
    private final int cents;

    Price(int centsIn) {
        cents = centsIn;
    }

    public boolean isNegative() {
        return cents < 0;
    }

    public Price add(Price p) throws InvalidPriceOperation {
        if (p == null) {
            throw new InvalidPriceOperation("Null value passed in for add");
        }
        return PriceFactory.makePrice(cents + p.cents);
    }

    public Price subtract(Price p) throws InvalidPriceOperation {
        if (p == null) {
            throw new InvalidPriceOperation("Null value passed in for subtract");
        }
        return PriceFactory.makePrice(cents - p.cents);
    }

    public Price multiply(int n) {

        return PriceFactory.makePrice(cents * n);
    }

    public boolean greaterOrEqual(Price p) throws InvalidPriceOperation {
        if (p == null) {
            throw new InvalidPriceOperation("Null value passed in for greaterOrEqual");
        }
        return cents >= p.cents;
    }

    public boolean lessOrEqual(Price p) throws InvalidPriceOperation {
        if (p == null) {
            throw new InvalidPriceOperation("Null value passed in for lessOrEqual");
        }
        return cents <= p.cents;
    }

    public boolean greaterThan(Price p) throws InvalidPriceOperation {
        if (p == null) {
            throw new InvalidPriceOperation("Null value passed in for greaterThan");
        }
        return cents > p.cents;
    }

    public boolean lessThan(Price p) throws InvalidPriceOperation {
        if (p == null) {
            throw new InvalidPriceOperation("Null value passed in for lessThan");
        }
        return cents < p.cents;
    }

    @Override
    public int compareTo(Price p) {
        if (p == null) {
            throw new UnsupportedOperationException("Null value passed in for compareTo");
        }
        return cents - p.cents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Price price = (Price) o;

        return cents == price.cents;
    }

    @Override
    public int hashCode() {
        return cents;
    }

    @Override
    public String toString() {
        int dollars = Math.abs(cents) / 100;
        int cents = Math.abs(this.cents) % 100;
        return "$" + (this.cents < 0 ? "-" : "") + dollars + "." + String.format("%02d", cents); // "." + cents
    }


}
