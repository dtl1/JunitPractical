package test;

import common.*;
import impl.VendingMachineProduct;
import org.junit.jupiter.api.Test;

import interfaces.IVendingMachineProduct;
import interfaces.IVendingMachine;
import interfaces.IProductRecord;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a JUnit test class for the Vending Machine ADT.
 */
public class Tests extends AbstractFactoryClient {

    //Regularly used basic class wide objects that any test can access
    IVendingMachineProduct vendingMachineProduct = getFactory().makeVendingMachineProduct("A1", "Haggis Crisps");
    IProductRecord productRecord = getFactory().makeProductRecord(vendingMachineProduct);
    IVendingMachine vendingMachine = getFactory().makeVendingMachine();

    /**
     * Checks that the factory was able to call a sensible constructor to get a non-null instance of IVendingMachineProduct.
     */
    @Test
    public void vendingMachineProductNotNull() {
        assertNotNull(vendingMachineProduct);
    }

    /**
     * Checks that the factory was able to call a sensible constructor to get a non-null instance of IVendingMachine.
     */
    @Test
    public void vendingMachineNotNull() {
        assertNotNull(vendingMachine);

    }

    /**
     * Checks that the factory was able to call a sensible constructor to get a non-null instance of IProductRecord.
     */
    @Test
    public void productRecordNotNull() {
        assertNotNull(productRecord);
    }

    /**
     * Checks to see if products are correctly not created when fed a badly formatted lane code.
     */
    @Test
    public void createProductWithIncorrectLaneFormat() {
        IVendingMachineProduct wrongFormat1 = getFactory().makeVendingMachineProduct("1A", "Haggis Crisps");
        assertNull(wrongFormat1);

        IVendingMachineProduct wrongFormat2 = getFactory().makeVendingMachineProduct("AA", "Haggis Crisps");
        assertNull(wrongFormat2);

        IVendingMachineProduct wrongFormat3 = getFactory().makeVendingMachineProduct("11", "Haggis Crisps");
        assertNull(wrongFormat3);

        IVendingMachineProduct wrongFormat4 = getFactory().makeVendingMachineProduct("A!", "Haggis Crisps");
        assertNull(wrongFormat4);

        IVendingMachineProduct wrongFormat5 = getFactory().makeVendingMachineProduct("!A", "Haggis Crisps");
        assertNull(wrongFormat5);

        IVendingMachineProduct rightFormat1 = getFactory().makeVendingMachineProduct("a1", "Haggis Crisps");
        assertNotNull(rightFormat1);

        IVendingMachineProduct rightFormat2 = getFactory().makeVendingMachineProduct("Z0", "Haggis Crisps");
        assertNotNull(rightFormat2);
    }

    /**
     *Checks to see if products are correctly not created when fed a lane code of the wrong length.
     */
    @Test
    public void createProductWithIncorrectLaneLength() {
        IVendingMachineProduct wrongFormat1 = getFactory().makeVendingMachineProduct("A", "Haggis Crisps");
        assertNull(wrongFormat1);

        IVendingMachineProduct wrongFormat2 = getFactory().makeVendingMachineProduct("A1A", "Haggis Crisps");
        assertNull(wrongFormat2);

        IVendingMachineProduct wrongFormat3 = getFactory().makeVendingMachineProduct("", "Haggis Crisps");
        assertNull(wrongFormat3);

    }

    /**
     * Checks that the machine was able to correctly register a product.
     */
    @Test
    public void registerProduct() throws LaneCodeAlreadyInUseException {
        vendingMachine.registerProduct(vendingMachineProduct);
        assertEquals(1, vendingMachine.getNumberOfProducts());
    }

    /**
     * Checks if the correct exception is thrown when
     * trying to register a product to a machine where the lane code is already registered to an existing product.
     */
    @Test
    public void laneCodeAlreadyRegistered() throws LaneCodeAlreadyInUseException {
        IVendingMachineProduct vendingMachineProductCopy = getFactory().makeVendingMachineProduct("A1", "Haggis Crisps: now with more haggis!");
        vendingMachine.registerProduct(vendingMachineProduct);


        assertThrows(LaneCodeAlreadyInUseException.class, () -> {
            vendingMachine.registerProduct(vendingMachineProductCopy);
        });
    }

    /**
     * Checks that the machine was able to unregister a product.
     */
    @Test
    public void unregisterProduct() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException {
        vendingMachine.registerProduct(vendingMachineProduct);
        assertEquals(1, vendingMachine.getNumberOfProducts());

        vendingMachine.unregisterProduct(vendingMachineProduct);
        assertEquals(0, vendingMachine.getNumberOfProducts());
    }

    /**
     * Checks if the correct exception is thrown when trying to unregister a non-registered product from the machine.
     */
    @Test
    public void unregisterNonRegisteredProduct() {
        assertThrows(LaneCodeNotRegisteredException.class, () -> {
            vendingMachine.unregisterProduct(vendingMachineProduct);
        });
    }

    /**
     * Checks if an item of product can be correctly added to a lane.
     */
    @Test
    public void addItem() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException {
        vendingMachine.registerProduct(vendingMachineProduct);
        vendingMachine.addItem(vendingMachineProduct.getLaneCode());
        assertEquals(1, vendingMachine.getNumberOfItems(vendingMachineProduct.getLaneCode()));
    }

    /**
     * Checks if the correct exception is thrown when trying to add an item to a non-registered product.
     */
    @Test
    public void addItemToNonRegisteredProduct() {
        assertThrows(LaneCodeNotRegisteredException.class, () -> {
            vendingMachine.addItem(vendingMachineProduct.getLaneCode());
        });
    }

    /**
     * Checks to see if a product can correctly be bought from the machine.
     */
    @Test
    public void buyItem() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException, ProductUnavailableException {
        vendingMachine.registerProduct(vendingMachineProduct);
        vendingMachine.addItem(vendingMachineProduct.getLaneCode());
        vendingMachine.buyItem(vendingMachineProduct.getLaneCode());
        assertEquals(1, vendingMachine.getNumberOfSales(vendingMachineProduct.getLaneCode()));
    }

    /**
     * Checks if the correct exception is thrown when trying to buy an item from a product with no items.
     */
    @Test
    public void buyUnavailableProduct() throws LaneCodeAlreadyInUseException {
        vendingMachine.registerProduct(vendingMachineProduct);

        assertThrows(ProductUnavailableException.class, () -> {
            vendingMachine.buyItem(vendingMachineProduct.getLaneCode());
        });
    }

    /**
     * Checks if the correct exception is thrown when trying to buy an item from a non-registered product.
     */
    @Test
    public void buyItemFromNonRegisteredProduct() {
        assertThrows(LaneCodeNotRegisteredException.class, () -> {
            vendingMachine.buyItem(vendingMachineProduct.getLaneCode());
        });
    }
    /**
     * Checks to see if the method to get the number of products in the machine correctly works.
     */
    @Test
    public void getNumberOfProducts() throws LaneCodeAlreadyInUseException {
        IVendingMachineProduct product2 = getFactory().makeVendingMachineProduct("A3", "Irn Bru");

        vendingMachine.registerProduct(vendingMachineProduct);
        vendingMachine.registerProduct(product2);


        assertEquals(2, vendingMachine.getNumberOfProducts());

    }

    /**
     * Checks to see if the machine only returns the number of unique types of products, differentiated by description.
     */
    @Test
    public void getNumberOfProductsWithSameProductInTwoLanes() throws LaneCodeAlreadyInUseException {
        IVendingMachineProduct sameDescProduct = getFactory().makeVendingMachineProduct("A2", "Haggis Crisps");
        IVendingMachineProduct diffDescProduct = getFactory().makeVendingMachineProduct("A3", "Irn Bru");

        vendingMachine.registerProduct(sameDescProduct);
        vendingMachine.registerProduct(diffDescProduct);
        vendingMachine.registerProduct(vendingMachineProduct);

        assertEquals(2, vendingMachine.getNumberOfProducts());

    }

    /**
     * Checks to see if the vending machine can correctly retrieve the most popular product.
     */
    @Test
    public void getMostPopularProduct() throws LaneCodeNotRegisteredException, LaneCodeAlreadyInUseException, ProductUnavailableException {
        vendingMachine.registerProduct(vendingMachineProduct);


        IVendingMachineProduct vendingMachineProductCopy = getFactory().makeVendingMachineProduct("A2", "Irn Bru");
        vendingMachine.registerProduct(vendingMachineProductCopy);

        vendingMachine.addItem("A1");
        vendingMachine.addItem("A1");
        vendingMachine.addItem("A2");


        vendingMachine.buyItem("A1");
        vendingMachine.buyItem("A1");
        vendingMachine.buyItem("A2");

        assertEquals(vendingMachineProduct, vendingMachine.getMostPopular());

    }

    /**
     * Checks to see if the vending machine correctly receives a null object
     * when trying to get the most popular product when nothing has been bought.
     */
    @Test
    public void getMostPopularProductWithNoBoughtItems() throws LaneCodeNotRegisteredException, LaneCodeAlreadyInUseException, ProductUnavailableException {
        vendingMachine.registerProduct(vendingMachineProduct);
        vendingMachine.addItem("A1");


        assertNull(vendingMachine.getMostPopular());

    }

    /**
     * Checks if the correct exception is thrown when trying to get the most popular product when no products are registered.
     */
    @Test
    public void getMostPopularProductWithNoRegisteredProducts() {
        assertThrows(LaneCodeNotRegisteredException.class, () -> {
            vendingMachine.getMostPopular();
        });
    }

    /**
     * Checks to see whether the machine can correctly get the number of items for a given registered product.
     */
    @Test
    public void getItems() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException {

        vendingMachine.registerProduct(vendingMachineProduct);

        vendingMachine.addItem(vendingMachineProduct.getLaneCode());

        assertEquals(1, vendingMachine.getNumberOfItems(vendingMachineProduct.getLaneCode()));
    }

    /**
     *Checks to see whether the machine can correctly get the total number of items across all products.
     */
    @Test
    public void getTotalItems() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException {
        IVendingMachineProduct vendingMachineProductCopy = getFactory().makeVendingMachineProduct("A2", "Haggis Crisps: now with more haggis!");

        vendingMachine.registerProduct(vendingMachineProduct);
        vendingMachine.registerProduct(vendingMachineProductCopy);

        vendingMachine.addItem(vendingMachineProduct.getLaneCode());
        vendingMachine.addItem(vendingMachineProductCopy.getLaneCode());

        assertEquals(2, vendingMachine.getTotalNumberOfItems());
    }

    /**
     * Checks that the correct exception is thrown when getting the number of items from a non - registered product.
     */
    @Test
    public void getItemsOfNonRegisteredProduct() {
        assertThrows(LaneCodeNotRegisteredException.class, () -> {
            vendingMachine.getNumberOfItems(vendingMachineProduct.getLaneCode());
        });
    }

    /**
     * Checks that the number of sales can correctly be retrieved for a given registered product.
     */
    @Test
    public void getSales() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException, ProductUnavailableException {
        vendingMachine.registerProduct(vendingMachineProduct);

        vendingMachine.addItem(vendingMachineProduct.getLaneCode());
        vendingMachine.buyItem(vendingMachineProduct.getLaneCode());

        assertEquals(1, vendingMachine.getNumberOfSales(vendingMachineProduct.getLaneCode()));
    }

    /**
     *Checks that the correct exception is thrown when getting the number of sales from a non - registered product.
     */
    @Test
    public void getSalesOfNonRegisteredProduct() {
        assertThrows(LaneCodeNotRegisteredException.class, () -> {
            vendingMachine.getNumberOfSales(vendingMachineProduct.getLaneCode());
        });
    }







}
