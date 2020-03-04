package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.ICustomerStore;
import uk.ac.warwick.cs126.models.Customer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;

import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

public class CustomerStore implements ICustomerStore {

    private MyArrayList<Customer> customerArray;
    private DataChecker dataChecker;

    public CustomerStore() {
        // Initialise variables here
        customerArray = new MyArrayList<>();
        dataChecker = new DataChecker();
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
        if (costumer != null) {
            customerArray.add(customer);
            return true;
        }
        return false;
    }

    public boolean addCustomer(Customer[] customers) {
        // DONE
        for (Customer customer : customers) {
            if (customer != null) {
                customerArray.add(customer);
            }
            else {
                return false;
            }
        }
        return true;
    }

    public Customer getCustomer(Long id) {
        // DONE
        return customerArray.get((int)id);;
    }

    public Customer[] getCustomers() {
        // DONE
        Customer[] arr = sortByID(customerArray.toArray());
        return arr;
    }

    public Customer[] getCustomers(Customer[] customers) {
        // DONE
        Customer[] arr = sortByID(customers);
        return arr;
    }

    public Customer[] getCustomersByName() {
        // DONE
        Customer[] arr = sortByName(customerArray.toArray());
    }

    public Customer[] getCustomersByName(Customer[] customers) {
        // DONE
        Customer[] arr = sortByName(customers);
        return arr;
    }

    public Customer[] getCustomersContaining(String searchTerm) {
        // TODO
        String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        // String searchTermConvertedFaster = stringFormatter.convertAccentsFaster(searchTerm);
        Customer[] arr = new Customer[customerArray.size()];
        
        return new Customer[0];
    }

    //ADDED FUNCTIONS

    public Customer[] sortByID(Customer[] customers) {
        Customer temp;
        Customer[] arr = customers.clone();
        for (int i = 0; i < arr.length; i++) {
            for (int i2 = 0; i < arr.length - i; i++) {
                if (arr[i2].getID() < arr[i].getID()) {
                    temp = arr[i];
                    arr[i] = arr[i2];
                    arr[i2] = temp;
                }
            }
        }
        return arr;

    }

    public Customer[] sortByName(Customer[] customers) {
        Customer temp;
        Customer[] arr = customers.clone();
        int compare;
        for (int i = 0; i < arr.length; i++) {
            for (int i2 = 0; i < arr.length - i; i++) {
                compare = arr[i2].getLastName().compareTo(arr[i].getLastName());
                if (compare == 0) {
                    compare = arr[i2].getFirstName().compareTo(arr[i].getFirstName());
                    if (compare == 0) {
                        compare = arr[i2].getID() - arr[i].getID();
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

}
