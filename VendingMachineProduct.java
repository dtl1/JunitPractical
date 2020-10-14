package impl;

import interfaces.IVendingMachineProduct;

/**
 * This class represents products that can be stocked and sold in a vending machine in a specific lane.
 */
public class VendingMachineProduct implements IVendingMachineProduct {

    private final String laneCode;
    private final String description;

    @Override
    public String getLaneCode() {
        return this.laneCode;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    VendingMachineProduct(String laneCode, String description) {
        this.laneCode = laneCode;
        this.description = description;
    }

}
