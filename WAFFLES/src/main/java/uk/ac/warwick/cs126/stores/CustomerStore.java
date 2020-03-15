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
import uk.ac.warwick.cs126.structures.MyBinaryTree;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

public class CustomerStore implements ICustomerStore {

    private MyArrayList<Customer> customerArray;
    private DataChecker dataChecker;

    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Customer> idSortedCustomerArray;
    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Boolean> idBlacklist;
    

    public CustomerStore() {
        // Initialise variables here
        customerArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        idSortedCustomerArray = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Customer>();
        idBlacklist = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Boolean>();
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

    public boolean addCustomer(Customer customer) {
        // DONE
        if (dataChecker.isValid(customer)) {
            Long customerID = customer.getID();
            if (idBlacklist.contains(customerID, null, null, null)) {
                return false;
            }
            if (idSortedCustomerArray.contains(customerID, null, null, null)) {
                idSortedCustomerArray.remove(customerID, null, null, null);
                idBlacklist.add(customerID, null, null, null, true);
                return false;
            }
            customerArray.add(customer);
            idSortedCustomerArray.add(customerID, null, null, null, customer);
            return true;
        }
        return false;
    }

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

    public Customer getCustomer(Long id) {
        // DONE
        if (id != null) {
            return idSortedCustomerArray.getData(id, null, null, null);
        }
        return null;
    }

    public Customer[] getCustomers() {
        // DONE
        /*Customer[] arr = new Customer[customerArray.size()];
        for (int i = 0; i < customerArray.size(); i++) {
            arr[i] = customerArray.get(i);
        }
        arr = sortByID(arr);
        return arr;*/
        Customer[] arr = new Customer[idSortedCustomerArray.size()];
        int i = 0;
        for (Customer customer : idSortedCustomerArray) {
            arr[i++] = customer;
        }
        return arr;
    }

    public Customer[] getCustomers(Customer[] customers) {
        // DONE
        //Customer[] arr = sortByID(customers);
        if (customers != null) {
            Customer[] arr = new Customer[customers.length];
            MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Customer> custom_tree = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Customer>();
            for (Customer customer : customers) {
                custom_tree.add(customer.getID(), null, null, null, customer);
            }
            int i = 0;
            for (Customer customer : custom_tree) {
                arr[i++] = customer;
            }
            return arr;    
        }
        return new Customer[0];
    }

    public Customer[] getCustomersByName() {
        // DONE
        Customer[] arr = new Customer[customerArray.size()];
        for (int i = 0; i < customerArray.size(); i++) {
            arr[i] = customerArray.get(i);
        }
        arr = sortByName(arr);
        return arr;
    }

    public Customer[] getCustomersByName(Customer[] customers) {
        // DONE
        Customer[] arr = sortByName(customers);
        return arr;
    }

    public Customer[] getCustomersContaining(String searchTerm) {
        // TODO
        if (searchTerm.isEmpty()) {
            return new Customer[0];
        }
        int i = 0;
        while (searchTerm.charAt(i) == ' ') {i++;}
        searchTerm = searchTerm.substring(i);
        i = 1;
        while (searchTerm.charAt(searchTerm.length() - i) == ' ') {i++;}
        searchTerm = searchTerm.substring(0, searchTerm.length() - i + 1);
        for (i = 0; i < searchTerm.length() - 1; i++) {
            if (searchTerm.charAt(i) == ' ' && searchTerm.charAt(i+1) == ' ') {
                searchTerm = searchTerm.substring(0, i) + searchTerm.substring(i+1);
            }
        }
        String searchTermConverted = StringFormatter.convertAccentsFaster(searchTerm);
        searchTermConverted = searchTermConverted.toLowerCase();
        Customer[] arr = new Customer[customerArray.size()];
        int found = 0;
        Customer customer;
        for (i = 0; i < customerArray.size(); i++) {
            customer = customerArray.get(i);
            String customer_name = customer.getFirstName() + " " + customer.getLastName();
            customer_name = StringFormatter.convertAccents(customer_name);
            customer_name = customer_name.toLowerCase();
            for (int i2 = 0; i2 < customer_name.length() - searchTermConverted.length(); i2++) {
                if (customer_name.substring(i2, i2 + searchTermConverted.length()).equals(searchTermConverted)) {
                    arr[found++] = customer;
                    break; //Once a match has been found for a given customer, move on to next customer
                }
            }
        }
        Customer[] arr1 = new Customer[found];
        for (i = 0; i < found; i++) {
            arr1[i] = arr[i];
        }
        arr1 = getCustomersByName(arr1);
        return arr1;
    }

    //ADDED FUNCTIONS

    public Customer[] sortByID(Customer[] customers) {
        if (customers != null) {
            Customer temp;
            Customer[] arr = customers.clone();
            for (int i = 0; i < arr.length; i++) {
                for (int i2 = i; i2 < arr.length; i2++) {
                    if (arr[i2].getID() < arr[i].getID()) {
                        temp = arr[i];
                        arr[i] = arr[i2];
                        arr[i2] = temp;
                    }
                }
            }
            return arr;    
        }
        return new Customer[0];

    }

    public Customer[] sortByName(Customer[] customers) {
        if (customers != null) {
            Customer temp;
            Customer[] arr = customers.clone();
            int compare;
            for (int i = 0; i < arr.length; i++) {
                for (int i2 = i+1; i2 < arr.length; i2++) {
                    compare = arr[i2].getLastName().toUpperCase().compareTo(arr[i].getLastName().toUpperCase());
                    if (compare == 0) {
                        compare = arr[i2].getFirstName().toUpperCase().compareTo(arr[i].getFirstName().toUpperCase());
                        if (compare == 0) {
                            compare = ((Long)(arr[i2].getID() - arr[i].getID())).intValue();
                        }
                    }
                    if (compare < 0) {
                        temp = arr[i];
                        arr[i] = arr[i2];
                        arr[i2] = temp;
                    }
                }
            }
            return arr;    
        }
        return new Customer[0];
    }

}
