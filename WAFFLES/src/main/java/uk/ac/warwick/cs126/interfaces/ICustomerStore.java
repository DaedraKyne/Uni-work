package uk.ac.warwick.cs126.interfaces;

import uk.ac.warwick.cs126.models.Customer;

import java.io.InputStream;

/**
 * Describes the operations on Customers that are required by the data store.
 * @author jamesvh (James Van Hinsbergh) -
 * <a href="mailto:J.van-Hinsbergh@warwick.ac.uk">J.van-Hinsbergh@warwick.ac.uk</a>
 * @author iant (Ian Tu) -
 * <a href="mailto:I.Tu@warwick.ac.uk">I.Tu@warwick.ac.uk</a>
 * @version 1.0
 */
public interface ICustomerStore {

    /**
     * Loads data from a csv file containing the Customer data into a Customer array, parsing the attributes where required.
     * @param resource       The source csv file to be loaded.
     * @return A Customer array with all Customers contained within the data file, regardless of the validity of the ID.
     */
    Customer[] loadCustomerDataToArray(InputStream resource);

    /**
     * Add a new Customer to the store. The method should return true if the Customer is successfully added to the data store.
     * The Customer should not be added if a Customer with the same ID already exists in the store.
     * If a duplicate ID is encountered, the existing Customer should be removed and the ID blacklisted from further use.
     * An invalid ID is one that contains zeros or more than 3 of the same digit, these should not be added, although they do not need to be blacklisted.
     * @param customer       The Customer object to add to the data store.
     * @return True if the Customer was successfully added, false otherwise.
     */
    boolean addCustomer(Customer customer);

    /**
     * Add new Customers in the input array to the store. The method should return true if the Customers are all successfully added to the data store.
     * Reference the {@link #addCustomer(Customer) addCustomer} method for details on ID handling.
     * @param customers       An array of Customer objects to add to the data store.
     * @return True if all of the Customers were successfully added, false otherwise.
     */
    boolean addCustomer(Customer[] customers);

    /**
     * Returns a single Customer, the Customer with the given ID, or null if not found.
     * @param id       The ID of the Customer to be retrieved.
     * @return The Customer with the given ID, or null if not found.
     */
    Customer getCustomer(Long id);

    /**
     * Returns an array of all Customers, sorted in ascending order of ID.
     * The Customer with the lowest ID should be the first element in the array.
     * @return A sorted array of Customer objects, with lowest ID first.
     */
    Customer[] getCustomers();

    /**
     * Returns an array of Customers, sorted in ascending order of ID.
     * The Customer with the lowest ID should be the first element in the array.
     * Similar functionality to the {@link #getCustomers() getCustomers} method.
     * @param customers       An array of Customer objects to be sorted.
     * @return A sorted array of Customer objects, with lowest ID first.
     */
    Customer[] getCustomers(Customer[] customers);

    /**
     * Returns an array of all Customers, sorted in alphabetical order of Last Name, then First Name.
     * If the First Name and Last Name are identical for multiple Customers, they should be further sorted in ascending order of ID.
     * The Customer with the Last Name, First Name combination that is nearest to 'A' alphabetically should be the first element in the array.
     * @return A sorted array of Customer objects, where the first element is the Customer with the Last Name, First Name combination that is nearest to 'A' alphabetically, followed by ID if the Last Name, First Name combination is equal.
     */
    Customer[] getCustomersByName();

    /**
     * Returns an array of all Customers, sorted in alphabetical order of Last Name, then First Name.
     * If the First Name and Last Name are identical for multiple Customers, they should be further sorted in ascending order of ID.
     * The Customer with the Last Name, First Name combination that is nearest to 'A' alphabetically should be the first element in the array.
     * Similar functionality to the {@link #getCustomersByName() getCustomersByName} method.
     * @param customers       An array of Customer objects to be sorted.
     * @return A sorted array of Customer objects, where the first element is the Customer with the Last Name, First Name combination that is nearest to 'A' alphabetically, followed by ID if the Last Name, First Name combination is equal.
     */
    Customer[] getCustomersByName(Customer[] customers);

    /**
     * Return an array of all the Customers whose Last Name and First Name (sorted in that order) contain the given query.
     * Search queries are accent-insensitive, case-insensitive and space-insensitive.
     * The array should be sorted using the criteria defined for the {@link #getCustomersByName() getCustomersByName} method.
     * @param searchTerm       The search string to find.
     * @return A array of Customer objects, sorted using the criteria defined for the {@link #getCustomersByName() getCustomersByName} method.
     */
    Customer[] getCustomersContaining(String searchTerm);

}
