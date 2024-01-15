import simulator.TrafficSim;
import exceptions.DataValidationException;
import exceptions.InvalidPriceOperation;
import exceptions.OrderNotFoundException;

public class Main {
    public static void main(String[] args)
            throws OrderNotFoundException, InvalidPriceOperation, DataValidationException {
        TrafficSim.runSim();
    }
}