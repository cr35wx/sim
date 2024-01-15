package simulator;

import currentmarket.CurrentMarketPublisher;
import order.Order;
import price.Price;
import exceptions.DataValidationException;
import exceptions.InvalidPriceOperation;
import exceptions.OrderNotFoundException;
import price.PriceFactory;
import product.BookSide;
import order.OrderDTO;
import product.ProductManager;
import user.User;
import user.UserManager;

import java.util.HashMap;
import java.util.Random;

/**
 * The TrafficSim class is a trading behavior driver that creates product books and users, then simulates users trading
 * those products using the classes in this project.
 */
public class TrafficSim {
    private static final HashMap<String, Double> basePrices = new HashMap<>();

    public static void runSim()
            throws DataValidationException, InvalidPriceOperation, OrderNotFoundException {
        UserManager.getInstance().init(new String[]{"ANN", "BOB", "CAT", "DOG", "EGG"});

        User ANN = UserManager.getInstance().getUser("ANN");
        User BOB = UserManager.getInstance().getUser("BOB");
        User CAT = UserManager.getInstance().getUser("CAT");
        User DOG = UserManager.getInstance().getUser("DOG");
        User EGG = UserManager.getInstance().getUser("EGG");

        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("WMT", ANN);
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("TGT", ANN);
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("TGT", BOB);
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("TSLA", BOB);
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("AMZN", CAT);
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("TGT", CAT);
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("WMT", CAT);
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("TSLA", DOG);
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("WMT", EGG);

        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("TGT", BOB);

        ProductManager.getInstance().addProduct("WMT");
        ProductManager.getInstance().addProduct("TGT");
        ProductManager.getInstance().addProduct("AMZN");
        ProductManager.getInstance().addProduct("TSLA");
        basePrices.put("WMT", 140.98);
        basePrices.put("TGT", 174.76);
        basePrices.put("AMZN", 102.11);
        basePrices.put("TSLA", 196.81);

        for (int i = 0; i < 100; i++) {
            User randomUser = UserManager.getInstance().getRandomUser();

            if (Math.random() < 0.9) {
                String randomProduct = ProductManager.getInstance().getRandomProduct();
                BookSide randomSide = getRandomSide();
                int volumeH = (int) (25 + (Math.random() * 300)); // random value between 25 and 325
                int volume = (int) Math.round(volumeH / 5.0) * 5;

                Price price = getPrice(randomProduct, randomSide);
                Order order = new Order(randomUser.getUserId(), randomProduct, price, volume, randomSide);
                OrderDTO orderDTO = ProductManager.getInstance().addOrder(order);
                randomUser.addOrder(orderDTO);
            } else {
                 if (randomUser.hasOrderWithRemainingQty()) {
                     OrderDTO randomOrder = randomUser.getOrderWithRemainingQty();
                     OrderDTO cancelledDTO = ProductManager.getInstance().cancel(randomOrder);

                     if (cancelledDTO != null) randomUser.addOrder(cancelledDTO);
                 }
            }
        }
        System.out.println(ProductManager.getInstance().toString());
        System.out.println(UserManager.getInstance().toString());
        System.out.println(ANN.getCurrentMarkets());;
        System.out.println(BOB.getCurrentMarkets());;
        System.out.println(CAT.getCurrentMarkets());;
        System.out.println(DOG.getCurrentMarkets());;
        System.out.println(EGG.getCurrentMarkets());;

        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("WMT", ANN);
        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("TGT", ANN);
        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("TSLA", BOB);
        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("AMZN", CAT);
        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("TGT", CAT);
        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("WMT", CAT);
        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("TSLA", DOG);
        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("WMT", EGG);
    }

    private static Price getPrice(String symbol, BookSide side) throws InvalidPriceOperation {
        Double basePrice = basePrices.get(symbol);
        double priceWidth = 0.02;
        double startPoint = 0.01;
        double tickSize = 0.1;

        double gapFromBase = basePrice * priceWidth; // 5.0
        double priceVariance = gapFromBase * (Math.random());

        if (side == BookSide.BUY) {
            double priceToUse = basePrice * (1 - startPoint);
            priceToUse += priceVariance;
            double priceToTick = Math.round(priceToUse * 1 / tickSize) / 10.0;
            return PriceFactory.makePrice(String.valueOf(priceToTick));
        } else {
            double priceToUse = basePrice * (1 + startPoint);
            priceToUse -= priceVariance;
            double priceToTick = Math.round(priceToUse * 1 / tickSize) / 10.0;
            return PriceFactory.makePrice(String.valueOf(priceToTick));
        }
    }

    public static BookSide getRandomSide() {
        int i = new Random().nextInt(BookSide.values().length);
        return BookSide.values()[i];
    }
}
