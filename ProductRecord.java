package impl;

import common.ProductUnavailableException;
import interfaces.IVendingMachineProduct;
import interfaces.IProductRecord;

import java.util.ArrayList;

/**
 * This class represents a ProductRecord, recording information relating to a product sold in a vending machine.
 */
public class ProductRecord implements IProductRecord {

    private final VendingMachineProduct product;
    private int numSales;
    private int numAvailable;

    ProductRecord(IVendingMachineProduct product) {
        this.product = (VendingMachineProduct) product;
        numSales = 0;
        numAvailable = 0;
    }

    @Override
    public IVendingMachineProduct getProduct() {
        return this.product;
    }

    @Override
    public int getNumberOfSales() {
        return numSales;
    }

    @Override
    public int getNumberAvailable() {
        return numAvailable;
    }

    @Override
    public void addItem() {
        numAvailable++;
    }

    @Override
    public void buyItem() throws ProductUnavailableException {
        if(numAvailable!= 0) {
            numAvailable--;
            numSales++;
        } else{
            throw new ProductUnavailableException();
        }
    }

}
