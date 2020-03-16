package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.ICustomerStore;
import uk.ac.warwick.cs126.models.Customer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyAvlTree;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;


/*
 * Main class, in charge of dealing with all customer tasks.
 */
public class CustomerStore implements ICustomerStore {

    private DataChecker dataChecker;

    private MyAvlTree<Long, Long, Customer> idSortedCustomerTree; // AVL tree of customers sorted by ID
    private MyAvlTree<String, Long, Customer> nameSortedCustomerTree; // AVL tree of customers sorted by lastname/firstname/id
    private MyAvlTree<Long, Long, Boolean> idBlacklist; // AVL tree of blacklisted IDs sorted by ID
    

  /*
   * Constructor class for CustomerStore.
   * Initializes values.
   */
    public CustomerStore() {
        // Initialise variables here
        dataChecker = new DataChecker();
        idSortedCustomerTree = new MyAvlTree<Long, Long, Customer>();
        nameSortedCustomerTree = new MyAvlTree<String, Long, Customer>();
        idBlacklist = new MyAvlTree<Long, Long, Boolean>();
    }

    public Customer[] loadCustomerDataToArray(InputStream resource) {
        Customer[] customerArray = new Customer[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line=lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Customer[] loadedCustomers = new Customer[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int customerCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Customer customer = (new Customer(
                            Long.parseLong(data[0]),
                            data[1],
                            data[2],
                            formatter.parse(data[3]),
                            Float.parseFloat(data[4]),
                            Float.parseFloat(data[5])));

                    loadedCustomers[customerCount++] = customer;
                }
            }
            csvReader.close();

            customerArray = loadedCustomers;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return customerArray;
    }



  /*
   * Attempts to add customer to the store
   * If customer's id is blacklisted, do not add.
   * If there already exists a stored customer with that id,
   * remove it and add blacklist the id.
   * Returns true if customer was successfully added.
   * 
   * @return         boolean
   */
    public boolean addCustomer(Customer customer) {
        // DONE
        if (dataChecker.isValid(customer)) {
            Long customerID = customer.getID();
            if (idBlacklist.contains(customerID, null, null, null)) {
                return false;
            }
            MyArrayList<String> nameList = new MyArrayList<String>();
            MyArrayList<Long> idList = new MyArrayList<Long>();
            nameList.add(customer.getFirstName());
            idList.add(customerID);
            if (idSortedCustomerTree.contains(customerID, null, null, null)) {
                idSortedCustomerTree.remove(customerID, null, null, null);
                nameSortedCustomerTree.remove(customer.getLastName(), null, nameList, idList);    
                idBlacklist.add(customerID, null, null, null, true);
                return false;
            }
            idSortedCustomerTree.add(customerID, null, null, null, customer);
            nameSortedCustomerTree.add(customer.getLastName(), null, nameList, idList, customer);
            return true;
        }
        return false;
    }


  /*
   * Attempts to add valid Customer objects from the customers input array to
   * the store.
   * Return true if the all the customers are all successfully added to the data
   * store, otherwise false .
   *
   * @return         boolean
   */
    public boolean addCustomer(Customer[] customers) {
        // DONE
        if (customers != null) {
            boolean fully_added = true;
            for (Customer customer : customers) {
                if (!addCustomer(customer)) {
                    fully_added = false;
                }
                
            }
            return fully_added;    
        }
        return false;
    }


  /*
   * Returns the Customer with the matching ID id from the store, otherwise
   * return null.
   *
   * @return    customer
   */
    public Customer getCustomer(Long id) {
        // DONE
        if (id != null) {
            return idSortedCustomerTree.getData(id, null, null, null);
        }
        return null;
    }


  /*
   * Returns an array of all customers in the store, sorted in 
   * ascending order of ID.
   *
   * @return    customers
   */
    public Customer[] getCustomers() {
        // DONE
        Customer[] arr = new Customer[idSortedCustomerTree.size()];
        int i = 0;
        for (Customer customer : idSortedCustomerTree) {
            arr[i++] = customer;
        }
        return arr;
    }


  /*
   * Returns an array of all valid customers from the given array,
   * sorted in ascending order of ID.
   *
   * @return    customers
   */
    public Customer[] getCustomers(Customer[] customers) {
        // DONE
        //Customer[] arr = sortByID(customers);
        if (customers != null) {
            MyAvlTree<Long, Long, Customer> custom_tree = new MyAvlTree<Long, Long, Customer>();
            for (Customer customer : customers) {
                if (dataChecker.isValid(customer)) {
                    custom_tree.add(customer.getID(), null, null, null, customer);
                }
            }
            Customer[] arr = new Customer[custom_tree.size()];
            int i = 0;
            for (Customer customer : custom_tree) {
                arr[i++] = customer;
            }
            return arr;    
        }
        return new Customer[0];
    }


  /*
   * Returns an array of all customers in the store, sorted alphabetically by Last
   * Name, if they have same Last Name then alphabetically by First Name.
   * If they have the same Last Name and First Name, then it is sorted in ascending
   * order of ID.
   *
   * @return    customers
   */
    public Customer[] getCustomersByName() {
        // DONE
        Customer[] arr = new Customer[nameSortedCustomerTree.size()];
        int i = 0;
        for (Customer customer : nameSortedCustomerTree) {
            arr[i] = customer;
            i++;
        }
        return arr;
    }


  /*
   * Returns an array of all valid customers from the given array, sorted 
   * alphabetically by Last Name, if they have same Last Name then alphabetically 
   * by First Name.
   * If they have the same Last Name and First Name, then it is sorted in ascending
   * order of ID.
   *
   * @return    customers
   */
    public Customer[] getCustomersByName(Customer[] customers) {
        // DONE
        if (customers != null) {
            MyAvlTree<String, Long, Customer> custom_tree = new MyAvlTree<String, Long, Customer>();
            MyArrayList<String> nameList;
            MyArrayList<Long> idList;
            for (Customer customer : customers) {
                if (dataChecker.isValid(customer)) {
                    nameList = new MyArrayList<String>();
                    idList = new MyArrayList<Long>();
                    nameList.add(customer.getFirstName());
                    idList.add(customer.getID());
                    custom_tree.add(customer.getLastName(), null, nameList, idList, customer);
                }
            }
            Customer[] arr = new Customer[custom_tree.size()];
            int i = 0;
            for (Customer customer : custom_tree) {
                arr[i] = customer;
                i++;
            }
            return arr;
        }
        return new Customer[0];
    }


  /*
   * Return an array of all the customers from the store whose First Name and 
   * LastName contain the given query str.
   *
   * @return    customers
   */
    public Customer[] getCustomersContaining(String searchTerm) {
        // DONE
        if (searchTerm.isEmpty()) {
            return new Customer[0];
        }
        int i = 0;
        while (searchTerm.charAt(i) == ' ') {i++;}
        searchTerm = searchTerm.substring(i); //remove white spaces at start of searchterm
        i = 1;
        while (searchTerm.charAt(searchTerm.length() - i) == ' ') {i++;} //remove white spaces at end of searchterm
        searchTerm = searchTerm.substring(0, searchTerm.length() - i + 1);
        for (i = 0; i < searchTerm.length() - 1; i++) {
            if (searchTerm.charAt(i) == ' ' && searchTerm.charAt(i+1) == ' ') { // remove extra whitespaces between characters, keep only one
                searchTerm = searchTerm.substring(0, i) + searchTerm.substring(i+1);
                i -= 1;
            }
        }
        String searchTermConverted = StringFormatter.convertAccentsFaster(searchTerm);
        searchTermConverted = searchTermConverted.toLowerCase();
        Customer[] arr = new Customer[nameSortedCustomerTree.size()];
        int found = 0;
        for (Customer customer : nameSortedCustomerTree) {
            String customer_name = customer.getFirstName() + " " + customer.getLastName();
            customer_name = StringFormatter.convertAccents(customer_name);
            customer_name = customer_name.toLowerCase();
            for (int i2 = 0; i2 < customer_name.length() - searchTermConverted.length() + 1; i2++) {
                if (customer_name.substring(i2, i2 + searchTermConverted.length()).equals(searchTermConverted)) {
                    arr[found++] = customer;
                    break; //Once a match has been found for a given customer, move on to next customer
                }
            }
        }
        Customer[] arr1 = new Customer[found]; // create new array to ensure all un-used null elements of results array are removed
        for (i = 0; i < found; i++) {
            arr1[i] = arr[i];
        }
        return arr1;
    }

}
