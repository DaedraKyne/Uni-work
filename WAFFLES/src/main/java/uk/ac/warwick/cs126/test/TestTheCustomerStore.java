package uk.ac.warwick.cs126.test;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.stores.CustomerStore;

public class TestTheCustomerStore extends TestRunner {
    TestTheCustomerStore(){
        System.out.println("\n[Testing CustomerStore]");

        // Run tests, comment out if you want to omit a test, feel free to modify or add more.
        testAddCustomer();
        testAddCustomers();
        testGetCustomer();
        testGetCustomers();
        testGetCustomersInputArray();
        testGetCustomersByName();
        testGetCustomersByNameInputArray();
        testGetCustomersContaining();
    }

    private void testAddCustomer() {
        try {
            // Initialise new store
            // We do this for every method so we have a fresh copy without any data in it
            CustomerStore customerStore = new CustomerStore();

            // Create a customer object
            // Customer(Long customerID,
            //          String firstName,
            //          String lastName,
            //          Date dateJoined,
            //          float latitude,
            //          float longitude)
            Customer customer = new Customer(
                    1112223334445556L,
                    "John",
                    "Cena",
                    parseDate("1977-04-23"),
                    52.38380f,
                    -1.560065f
            );

            // Add to store
            boolean result = customerStore.addCustomer(customer);

            if (result) {
                System.out.println("[SUCCESS]    CustomerStore: testAddCustomer()");
            } else {
                System.out.println(" [FAILED]    CustomerStore: testAddCustomer()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    CustomerStore: testAddCustomer()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testAddCustomers() {
        try {
            // Initialise new store
            CustomerStore customerStore = new CustomerStore();

            // Load test data from /data folder
            Customer[] customers = customerStore.loadCustomerDataToArray(
                    loadData("/test-customer/customer-10.csv"));

            // Add to store to be processed, should return true as all the data is valid
            boolean result = customerStore.addCustomer(customers);

            if (result) {
                System.out.println("[SUCCESS]    CustomerStore: testAddCustomers()");
            } else {
                System.out.println(" [FAILED]    CustomerStore: testAddCustomers()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    CustomerStore: testAddCustomers()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetCustomer() {
        try {
            // Initialise new store
            CustomerStore customerStore = new CustomerStore();

            // Load test data from /data folder
            Customer[] customers = customerStore.loadCustomerDataToArray(
                    loadData("/test-customer/customer-10.csv"));

            // Add to store to be processed
            customerStore.addCustomer(customers);

            // Should return true as the customer with ID 3367675867544843 exists
            // The L at the end tells the compiler it is a Long object
            boolean result = customerStore.getCustomer(3367675867544843L) != null
                    && customerStore.getCustomer(3367675867544843L).getID().equals(3367675867544843L);

            if (result) {
                System.out.println("[SUCCESS]    CustomerStore: testGetCustomer()");
            } else {
                System.out.println(" [FAILED]    CustomerStore: testGetCustomer()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    CustomerStore: testGetCustomer()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetCustomers() {
        try {
            // Initialise new store
            CustomerStore customerStore = new CustomerStore();

            // Load test data from /data folder
            Customer[] customers = customerStore.loadCustomerDataToArray(
                    loadData("/test-customer/customer-10.csv"));

            // Add to store to be processed
            customerStore.addCustomer(customers);

            // Get customers sorted by ID from store
            Customer[] gotCustomers = customerStore.getCustomers();

            // Load manually sorted data from /data folder to verify with
            Customer[] expectedCustomers = customerStore.loadCustomerDataToArray(
                    loadData("/test-customer/customer-10-sorted-by-id.csv"));

            // Now we can compare, realistically we should compare every field to make sure the objects are the same
            // Remember we should not use A == B as this is an object, we should use A.equals(B)
            // Note A.equals(B) only works properly if the method exists for object A, and is comparable with object B
            // In this case we are comparing 2 Long objects which are from the Java library
            // So .equals has been implemented Long objects and works between them
            // We should not do it for Customer objects as this has not been implemented (why make it easy for you!)
            boolean result = true;
            if (gotCustomers.length == expectedCustomers.length) {
                for (int i = 0; i < expectedCustomers.length; i++) {
                    result = gotCustomers[i].getID().equals(expectedCustomers[i].getID());
                    if(!result){
                        break;
                    }
                }
            } else {
                result = false;
            }

            // If outputs are not the same we print it out to see how wrong we were
            // We have a toString() method implemented for the Customer object so the compiler implicitly knows to call it
            if(!result){
                System.out.println("\n[Expected]");
                for (Customer c: expectedCustomers){
                    System.out.println(c);
                }

                System.out.println("\n[Got]");
                if (gotCustomers.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Customer c: gotCustomers){
                    System.out.println(c);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    CustomerStore: testGetCustomers()");
            } else {
                System.out.println(" [FAILED]    CustomerStore: testGetCustomers()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    CustomerStore: testGetCustomers()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetCustomersInputArray() {
        try {
            // Initialise new store
            CustomerStore customerStore = new CustomerStore();

            // Load test data from /data folder
            Customer[] customers = customerStore.loadCustomerDataToArray(
                    loadData("/test-customer/customer-10.csv"));

            // Similar to above method but...
            // Instead of adding it to the store, we use the method to get customers sorted by ID
            Customer[] gotCustomers = customerStore.getCustomers(customers);

            // Load manually sorted data from /data folder to verify with
            Customer[] expectedCustomers = customerStore.loadCustomerDataToArray(
                    loadData("/test-customer/customer-10-sorted-by-id.csv"));

            // Verify
            boolean result = true;
            if (gotCustomers.length == expectedCustomers.length) {
                for (int i = 0; i < expectedCustomers.length; i++) {
                    result = gotCustomers[i].getID().equals(expectedCustomers[i].getID());
                    if(!result){
                        break;
                    }
                }
            } else {
                result = false;
            }

            // Print if wrong
            if(!result){
                System.out.println("\n[Expected]");
                for (Customer c: expectedCustomers){
                    System.out.println(c);
                }

                System.out.println("\n[Got]");
                if (gotCustomers.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Customer c: gotCustomers){
                    System.out.println(c);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    CustomerStore: testGetCustomersInputArray()");
            } else {
                System.out.println(" [FAILED]    CustomerStore: testGetCustomersInputArray()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    CustomerStore: testGetCustomersInputArray()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetCustomersByName() {
        try {
            // Initialise new store
            CustomerStore customerStore = new CustomerStore();

            // Load test data from /data folder
            Customer[] customers = customerStore.loadCustomerDataToArray(
                    loadData("/test-customer/customer-10.csv"));

            // Add to store to be processed
            customerStore.addCustomer(customers);

            // Get customers sorted by name from store
            Customer[] gotCustomers = customerStore.getCustomersByName();

            // Load manually sorted data from /data folder to verify with
            Customer[] expectedCustomers = customerStore.loadCustomerDataToArray(
                    loadData("/test-customer/customer-10-sorted-by-name.csv"));

            // Verify
            boolean result = true;
            if (gotCustomers.length == expectedCustomers.length) {
                for (int i = 0; i < expectedCustomers.length; i++) {
                    result = gotCustomers[i].getID().equals(expectedCustomers[i].getID());
                    if(!result){
                        break;
                    }
                }
            } else {
                result = false;
            }

            // Print if wrong
            if(!result){
                System.out.println("\n[Expected]");
                for (Customer c: expectedCustomers){
                    System.out.println(c);
                }

                System.out.println("\n[Got]");
                if (gotCustomers.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Customer c: gotCustomers){
                    System.out.println(c);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    CustomerStore: testGetCustomersByName()");
            } else {
                System.out.println(" [FAILED]    CustomerStore: testGetCustomersByName()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    CustomerStore: testGetCustomersByName()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetCustomersByNameInputArray() {
        try {
            // Initialise new store
            CustomerStore customerStore = new CustomerStore();

            // Load test data from /data folder
            Customer[] customers = customerStore.loadCustomerDataToArray(
                    loadData("/test-customer/customer-10.csv"));

            // Get customers sorted by name from store via method
            Customer[] gotCustomers = customerStore.getCustomersByName(customers);

            // Load manually sorted data from /data folder to verify with
            Customer[] expectedCustomers = customerStore.loadCustomerDataToArray(
                    loadData("/test-customer/customer-10-sorted-by-name.csv"));

            // Verify
            boolean result = true;
            if (gotCustomers.length == expectedCustomers.length) {
                for (int i = 0; i < expectedCustomers.length; i++) {
                    result = gotCustomers[i].getID().equals(expectedCustomers[i].getID());
                    if(!result){
                        break;
                    }
                }
            } else {
                result = false;
            }

            // Print if wrong
            if(!result){
                System.out.println("\n[Expected]");
                for (Customer c: expectedCustomers){
                    System.out.println(c);
                }

                System.out.println("\n[Got]");
                if (gotCustomers.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Customer c: gotCustomers){
                    System.out.println(c);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    CustomerStore: testGetCustomersByNameInputArray()");
            } else {
                System.out.println(" [FAILED]    CustomerStore: testGetCustomersByNameInputArray()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    CustomerStore: testGetCustomersByNameInputArray()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetCustomersContaining() {
        try {
            // Initialise new store
            CustomerStore customerStore = new CustomerStore();

            // Load test data from /data folder
            Customer[] customers = customerStore.loadCustomerDataToArray(
                    loadData("/test-customer/customer-10.csv"));

            // Add to store to be processed
            customerStore.addCustomer(customers);

            // Find customers with an o in their name
            // We use an accent here to make sure we use our convertAccentsFaster method
            Customer[] gotCustomers = customerStore.getCustomersContaining("Ö");

            // Load manually found and sorted data from /data folder to verify with
            Customer[] expectedCustomers = customerStore.loadCustomerDataToArray(
                    loadData("/test-customer/customer-10-containing-o.csv"));

            // Verify
            boolean result = true;
            if (gotCustomers.length == expectedCustomers.length) {
                for (int i = 0; i < expectedCustomers.length; i++) {
                    result = gotCustomers[i].getID().equals(expectedCustomers[i].getID());
                    if(!result){
                        break;
                    }
                }
            } else {
                result = false;
            }

            // Print if wrong
            if(!result){
                System.out.println("\n[Expected]");
                for (Customer c: expectedCustomers){
                    System.out.println(c);
                }

                System.out.println("\n[Got]");
                if (gotCustomers.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Customer c: gotCustomers){
                    System.out.println(c);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    CustomerStore: testGetCustomersContaining()");
            } else {
                System.out.println(" [FAILED]    CustomerStore: testGetCustomersContaining()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    CustomerStore: testGetCustomersContaining()");
            e.printStackTrace();
            System.out.println();
        }
    }
}
