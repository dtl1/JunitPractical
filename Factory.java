package impl;

import interfaces.IFactory;
import interfaces.IVendingMachineProduct;
import interfaces.IVendingMachine;
import interfaces.IProductRecord;


/**
 * This class implements a singleton factory.
 */
public final class Factory implements IFactory {

    private static IFactory factoryInstance = null;

    private Factory() {

    }

    /**
     * Method which returns an instance of the singleton Factory class.
     *
     * @return the instance of the Factory
     */
    public static IFactory getInstance() {
        if (factoryInstance == null) {
            factoryInstance = new Factory();
        }
        return factoryInstance;
    }

    @Override
    public IVendingMachineProduct makeVendingMachineProduct(String laneCode, String description) {
        laneCode = laneCode.toUpperCase();

        char[] laneChars = laneCode.toCharArray();

        if (laneChars.length != 2 || (int) laneChars[0] > 90 || (int) laneChars[0] < 65 || (int) laneChars[1] > 57 || (int) laneChars[1] < 48) {
            return null;
        } else {
            return new VendingMachineProduct(laneCode, description);
        }
    }

    @Override
    public IProductRecord makeProductRecord(IVendingMachineProduct vendingMachineProduct) {
        return new ProductRecord(vendingMachineProduct);
    }

    @Override
    public IVendingMachine makeVendingMachine() {
        return new VendingMachine();
    }

}
