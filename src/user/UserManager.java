package user;

import exceptions.DataValidationException;
import order.OrderDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * The UserManager singleton class maintains a collections of all users in the system. It acts as a Facade to the users.
 */
public final class UserManager {
    private static UserManager instance;
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }

        return instance;
    }

    private UserManager() {}

    /** &lt;userId, User object&gt; */
    private final HashMap<String, User> users = new HashMap<>();

    /**
     * Creates each user from the ids in the usersIn array, then adds each new user to the users HashMap.
     * @param usersIn An array with the id's of some amount of new users
     * @throws DataValidationException Each userId must match the pattern ^[A-Z]{3}$ (any 3 uppercase letters from A-Z).
     */
    public void init(String[] usersIn) throws DataValidationException {
        for (String userId : usersIn) {
            User user = new User(userId);
            users.put(userId, user);
        }
    }

    /**
     * @return A random User object from the users HashMap.
     */
    public User getRandomUser() {
        ArrayList<User> u = new ArrayList<>(users.values());
        int randomIndex = new Random().nextInt(u.size());
        return u.get(randomIndex);
    }

    /**
     * Adds the OrderDTO to the specified User (using the User's "addOrder" method).
     * @param userId The id of the user to add the order to.
     * @param o The OrderDTO to add to the user.
     * @throws DataValidationException If o is null.
     */
    public void addToUser(String userId, OrderDTO o) throws DataValidationException {
        users.get(userId).addOrder(o);
    }

    public User getUser(String id) {
        return users.getOrDefault(id, null);
    }

    /**
     * Example output:
     * <p>
     * User Id: ANN
     *   Product: GOOG, Price: $52.95, OriginalVolume: 270, RemainingVolume: 270, CancelledVolume: 0,
     *   FilledVolume: 0, User: ANN, Side: SELL, Id: ANNGOOG$52.9558518804111600
     *   Product: WMT, Price: $70.40, OriginalVolume: 305, RemainingVolume: 155, CancelledVolume: 0,
     *   FilledVolume: 150, User: ANN, Side: BUY, Id: ANNWMT$70.4058518805034300
     * <p>
     * User Id: BOB
     *   Product: TGT, Price: $88.75, OriginalVolume: 300, RemainingVolume: 300, CancelledVolume: 0,
     *   FilledVolume: 0, User: BOB, Side: BUY, Id: BOBTGT$88.7558518798475100
     *   Product: WMT, Price: $70.59, OriginalVolume: 210, RemainingVolume: 210, CancelledVolume: 0,
     *   FilledVolume: 0, User: BOB, Side: SELL, Id: BOBWMT$70.5958518807798900
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (User user : users.values()) {
            s.append(user.toString()).append("\n");
        }
        return s.toString();
    }
}
