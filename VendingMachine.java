package impl;


import common.AbstractFactoryClient;
import common.LaneCodeAlreadyInUseException;
import common.LaneCodeNotRegisteredException;
import common.ProductUnavailableException;
import interfaces.IVendingMachineProduct;
import interfaces.IVendingMachine;
import interfaces.IProductRecord;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class represents a simple vending machine which can stock and sell products.
 */
public class VendingMachine extends AbstractFactoryClient implements IVendingMachine {


    private ArrayList<ProductRecord> productRecords = new ArrayList<>();

    VendingMachine() {
    }

    @Override
    public void registerProduct(IVendingMachineProduct vendingMachineProduct) throws LaneCodeAlreadyInUseException {


        for (ProductRecord pr : productRecords) {
            if (pr.getProduct().getLaneCode().equals(vendingMachineProduct.getLaneCode())) {
                throw new LaneCodeAlreadyInUseException();
            }
        }

        productRecords.add((ProductRecord) getFactory().makeProductRecord(vendingMachineProduct));
    }

    @Override
    public void unregisterProduct(IVendingMachineProduct vendingMachineProduct) throws LaneCodeNotRegisteredException {


        for (ProductRecord pr : productRecords) {

            if (pr.getProduct().getLaneCode().equals(vendingMachineProduct.getLaneCode())) {
                productRecords.remove(pr);
                return;
            }

        }

        throw new LaneCodeNotRegisteredException();


    }

    @Override
    public void addItem(String laneCode) throws LaneCodeNotRegisteredException {

        for (ProductRecord pr : productRecords) {

            if (pr.getProduct().getLaneCode().equals(laneCode)) {
                pr.addItem();
                return;
            }
        }
        throw new LaneCodeNotRegisteredException();

    }

    @Override
    public void buyItem(String laneCode) throws ProductUnavailableException, LaneCodeNotRegisteredException {
        for (ProductRecord pr : productRecords) {

            if (pr.getProduct().getLaneCode().equals(laneCode)) {
                if (pr.getNumberAvailable() > 0) {
                    pr.buyItem();
                    return;
                } else {
                    throw new ProductUnavailableException();
                }

            }


        }


        throw new LaneCodeNotRegisteredException();


    }

    @Override
    public int getNumberOfProducts() {
        int uniqueProducts = 0;

        String pDesc;
        ArrayList<String> pDescArray = new ArrayList<>();

        for (ProductRecord pr : productRecords) {

            pDesc = pr.getProduct().getDescription();
            if (!(pDescArray.contains(pDesc))) {
                uniqueProducts++;
                pDescArray.add(pDesc);
            }


        }
        return uniqueProducts;
    }

    @Override
    public int getTotalNumberOfItems() {
        int itemTotal = 0;

        for (ProductRecord pr : productRecords) {

            itemTotal = itemTotal + pr.getNumberAvailable();
        }

        return itemTotal;
    }

    @Override
    public int getNumberOfItems(String laneCode) throws LaneCodeNotRegisteredException {
        for (ProductRecord pr : productRecords) {

            if (pr.getProduct().getLaneCode().equals(laneCode)) {
                return pr.getNumberAvailable();
            }

        }

        throw new LaneCodeNotRegisteredException();
    }

    @Override
    public int getNumberOfSales(String laneCode) throws LaneCodeNotRegisteredException {
        for (ProductRecord pr : productRecords) {

            if (pr.getProduct().getLaneCode().equals(laneCode)) {
                return pr.getNumberOfSales();
            }

        }

        throw new LaneCodeNotRegisteredException();
    }

    @Override
    public IVendingMachineProduct getMostPopular() throws LaneCodeNotRegisteredException {

        if (productRecords.size() > 0) {
            ProductRecord mpr = productRecords.get(0);
            for (ProductRecord pr : productRecords) {
                if (pr.getNumberOfSales() > mpr.getNumberOfSales()) {
                    mpr = pr;
                }
            }
            if (mpr.getNumberOfSales() > 0) {
                return mpr.getProduct();
            } else {
                return null;
            }
        } else {
            throw new LaneCodeNotRegisteredException();
        }

    }


}


