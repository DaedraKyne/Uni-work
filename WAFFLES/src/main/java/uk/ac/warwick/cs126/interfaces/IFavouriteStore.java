package uk.ac.warwick.cs126.interfaces;

import uk.ac.warwick.cs126.models.Favourite;

import java.io.InputStream;

/**
 * Describes the operations on Favourites that are required by the data store.
 * @author jamesvh (James Van Hinsbergh) -
 * <a href="mailto:J.van-Hinsbergh@warwick.ac.uk">J.van-Hinsbergh@warwick.ac.uk</a>
 * @author iant (Ian Tu) -
 * <a href="mailto:I.Tu@warwick.ac.uk">I.Tu@warwick.ac.uk</a>
 * @version 1.0
 */
public interface IFavouriteStore {

    /**
     * Loads data from a csv file containing the Favourite data into a Favourite array, parsing the attributes where required.
     * @param resource       The source csv file to be loaded.
     * @return A Favourite array with all Favourites contained within the data file, regardless of the validity of the ID.
     */
    Favourite[] loadFavouriteDataToArray(InputStream resource);

    /**
     * Add a new Favourite to the store. The method should return true if the Favourite is successfully added to the data store.
     * The Favourite should not be added if a Favourite with the same ID already exists in the store.
     * If a duplicate ID is encountered, the existing Favourite should be removed and the ID blacklisted from further use.
     * If a Favourite has a unique ID but there already exists a Favourite with the same Customer ID and Restaurant ID, you replace it with the oldest of the pair.
     * The ID of the Favourite that was subsequently replaced is now blacklisted, and should not exist in the store.
     * An invalid ID is one that contains zeros or more than 3 of the same digit, these should not be added, although they do not need to be blacklisted.
     * @param favourite       The Favourite object to add to the data store.
     * @return True if the Favourite was successfully added, false otherwise.
     */
    boolean addFavourite(Favourite favourite);

    /**
     * Add new Favourites in the input array to the store. The method should return true if the Favourites are all successfully added to the data store.
     * Reference the {@link #addFavourite(Favourite) addFavourite} method for details on ID handling and existing Customer/Restaurant Favourites.
     * @param favourites       An array of Favourite objects to add to the data store.
     * @return True if all of the Favourites were successfully added, false otherwise.
     */
    boolean addFavourite(Favourite[] favourites);

    /**
     * Returns a single Favourite, the Favourite with the given ID, or null if not found.
     * @param id       The ID of the Favourite to be retrieved.
     * @return The Favourite with the given ID, or null if not found.
     */
    Favourite getFavourite(Long id);

    /**
     * Returns an array of all Favourites, sorted in ascending order of ID.
     * The Favourite with the lowest ID should be the first element in the array.
     * @return A sorted array of Favourite objects, with lowest ID first.
     */
    Favourite[] getFavourites();

    /**
     * Returns an array of all Favourites by the Customer with the given ID.
     * The array is sorted by date favourited from newest to oldest, with ascending order of ID for matching dates.
     * The newest Favourite should be the first element in the array, with the lowest ID should the date favourited be equal.
     * @param id       The ID of the Customer who's Favourites are to be retrieved.
     * @return A sorted array of Favourite objects, with the newest Favourite first.
     */
    Favourite[] getFavouritesByCustomerID(Long id);

    /**
     * Returns an array of all Favourites for the Restaurant with the given ID.
     * The array should be sorted using the criteria defined for the {@link #getFavouritesByCustomerID(Long) getFavouritesByCustomerID} method.
     * @param id       The ID of the Restaurant who's Favourites are to be retrieved.
     * @return A sorted array of Favourite objects, with the newest Favourite first.
     */
    Favourite[] getFavouritesByRestaurantID(Long id);

    /**
     * Return an array of IDs of all the Restaurants that have been favourited by both Customer with ID customer1ID and Customer with ID customer2ID.
     * The date favourited is taken as the latest of the favourited date of either Customer.
     * The array is sorted by date favourited from newest to oldest, with ascending order of Restaurant ID for matching dates.
     * The newest Favourite should be the first element in the array, with the lowest ID should the date favourited be equal.
     * @param customer1ID       The ID of the first Customer.
     * @param customer2ID       The ID of the second Customer.
     * @return A sorted array of Restaurant IDs, with the newest Favourite first.
     */
    Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID);

    /**
     * Return an array of IDs of all the Restaurants that have been favourited by Customer with ID customer1ID but not Customer with ID customer2ID.
     * The array should be sorted using the criteria defined for the {@link #getCommonFavouriteRestaurants(Long, Long) getCommonFavouriteRestaurants} method.
     * @param customer1ID       The ID of the first Customer.
     * @param customer2ID       The ID of the second Customer.
     * @return A sorted array of Restaurant IDs, with the newest Favourite first.
     */
    Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID);

    /**
     * Return an array of IDs of all the Restaurants that have either:
     * <ul>
     *     <li>been favourited by Customer with ID customer1ID but not Customer with ID customer2ID</li>
     *     <li>been favourited by Customer with ID customer2ID but not Customer with ID customer1ID</li>
     * </ul>
     * The array should be sorted using the criteria defined for the {@link #getCommonFavouriteRestaurants(Long, Long) getCommonFavouriteRestaurants} method.
     * @param customer1ID       The ID of the first Customer.
     * @param customer2ID       The ID of the second Customer.
     * @return A sorted array of Restaurant IDs, with the newest Favourite first.
     */
    Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID);

    /**
     * Returns an array of 20 Customer IDs that have favourited the most Restaurants.
     * If there are less than 20 IDs, the remaining indexes should be set to null.
     * The array should be sorted by descending Favourite count, then by date of the oldest Favourite, and finally by ascending order of Customer ID for matching counts.
     * @return A sorted array of 20 Customer IDs, with the Customer with the highest Favourite count first.
     */
    Long[] getTopCustomersByFavouriteCount();

    /**
     * Returns an array of 20 Restaurant IDs that have been favourited the most.
     * If there are less than 20 IDs, the remaining indexes should be set to null.
     * The array should be sorted by descending Favourite count, then by date of the oldest Favourite, and finally by ascending order of Restaurant ID for matching counts.
     * @return A sorted array of 20 Restaurant IDs, with the Restaurant with the highest Favourite count first.
     */
    Long[] getTopRestaurantsByFavouriteCount();
}
