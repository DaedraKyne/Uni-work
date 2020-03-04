package uk.ac.warwick.cs126.interfaces;

import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.RestaurantDistance;

import java.io.InputStream;

/**
 * Describes the operations on Restaurants that are required by the data store.
 * @author jamesvh (James Van Hinsbergh) -
 * <a href="mailto:J.van-Hinsbergh@warwick.ac.uk">J.van-Hinsbergh@warwick.ac.uk</a>
 * @author iant (Ian Tu) -
 * <a href="mailto:I.Tu@warwick.ac.uk">I.Tu@warwick.ac.uk</a>
 * @version 1.0
 */
public interface IRestaurantStore {

    /**
     * Loads data from a CSV file containing the Restaurant data into a Restaurant array, parsing the attributes where required.
     * @param resource       The source csv file to be loaded.
     * @return A Restaurant array with all Restaurants contained within the data file, regardless of the validity of the ID.
     */
    Restaurant[] loadRestaurantDataToArray(InputStream resource);

    /**
     * Add a new Restaurant to the store. The method should return true if the Restaurant is successfully added to the data store.
     * The Restaurant contains a repeated ID string (3 repeats of a 16 digit ID). These need to have the true ID extracted and verified before the duplicate and invalid checks.
     * The repeated ID string is corrupt if there is no consensus (i.e. there is no majority).
     * A Restaurant with a corrupt repeated ID string should not be added to the store.
     * The Restaurant should not be added if a Restaurant with the same ID already exists in the store.
     * If a duplicate ID is encountered, the existing Restaurant should be removed and the ID blacklisted from further use.
     * An invalid ID is one that contains zeros or more than 3 of the same digit, these should not be added, although they do not need to be blacklisted.
     * @param restaurant       The Restaurant object to add to the data store.
     * @return True if the Restaurant was successfully added, false otherwise.
     */
    boolean addRestaurant(Restaurant restaurant);

    /**
     * Add new Restaurants in the input array to the store. The method should return true if the Restaurants are all successfully added to the data store.
     * Reference the {@link #addRestaurant(Restaurant) addRestaurant} method for details on ID handling.
     * @param restaurants       An array of Restaurant objects to add to the data store.
     * @return True if all of the Restaurants were successfully added, false otherwise.
     */
    boolean addRestaurant(Restaurant[] restaurants);

    /**
     * Returns a single Restaurant, the Restaurant with the given ID, or null if not found.
     * @param id       The ID of the Restaurant to be retrieved.
     * @return The Restaurant with the given ID, or null if not found.
     */
    Restaurant getRestaurant(Long id);

    /**
     * Returns an array of all Restaurants, sorted in ascending order of ID.
     * The Restaurant with the lowest ID should be the first element in the array.
     * @return A sorted array of Restaurant objects, with lowest ID first.
     */
    Restaurant[] getRestaurants();

    /**
     * Returns an array of Restaurants, sorted in ascending order of ID.
     * The Restaurant with the lowest ID should be the first element in the array.
     * Similar functionality to the {@link #getRestaurants() getRestaurants} method.
     * @param restaurants       An array of Restaurant objects to be sorted.
     * @return A sorted array of Restaurant objects, with lowest ID first.
     */
    Restaurant[] getRestaurants(Restaurant[] restaurants);

    /**
     * Returns an array of all Restaurants, sorted in alphabetical order of Restaurant name.
     * If the Restaurant names are identical for multiple Restaurants, they should be further sorted in ascending order of ID.
     * The Restaurant with the Restaurant name that is nearest to 'A' alphabetically should be the first element in the array.
     * @return A sorted array of Restaurant objects, where the first element is the Restaurant with the Restaurant name that is nearest to 'A' alphabetically, followed by ID if the Restaurant names are equal.
     */
    Restaurant[] getRestaurantsByName();

    /**
     * Returns an array of all Restaurants, sorted in ascending order of date established (oldest first).
     * If the date established is the same, then sort by restaurant name in alphabetical order and finally in ascending order of ID.
     * The oldest Restaurant should be the first element in the array, with the Restaurant name that is nearest to 'A' alphabetically, followed by lowest ID should the date established be equal.
     * @return A sorted array of Restaurant objects, with the oldest Restaurant first.
     */
    Restaurant[] getRestaurantsByDateEstablished();

    /**
     * Returns an array of Restaurants, sorted in ascending order of date established (oldest first).
     * The array should be sorted using the criteria defined for the {@link #getRestaurantsByDateEstablished() getRestaurantsByDateEstablished} method.
     * @param restaurants       An array of Restaurant objects to be sorted.
     * @return A sorted array of Restaurant objects, with the oldest Restaurant first.
     */
    Restaurant[] getRestaurantsByDateEstablished(Restaurant[] restaurants);

    /**
     * Returns an array of all Restaurants that have at least 1 Warwick Star, sorted in descending order Stars.
     * If the number of Stars is the same, then sort by restaurant name in alphabetical order and finally in ascending order of ID.
     * The first element in the array should be the Restaurant with the highest number of Stars, and the Restaurant name that is nearest to 'A' alphabetically, followed by lowest ID should the number of Stars be equal.
     * @return A sorted array of Restaurant objects, with the Restaurant with the highest number of Stars first.
     */
    Restaurant[] getRestaurantsByWarwickStars();

    /**
     * Returns an array of Restaurants, sorted in descending order of rating.
     * The rating is calculated by averaging all review ratings for that Restaurant.
     * If the Restaurant rating is the same, then sort by restaurant name in alphabetical order and finally in ascending order of ID.
     * The first element in the array should be the Restaurant with the highest highest rating, and the Restaurant name that is nearest to 'A' alphabetically, followed by lowest ID should the ratings be equal.
     * @param restaurants       An array of Restaurant objects to be sorted.
     * @return A sorted array of Restaurant objects, with the Restaurant with the highest rating first.
     */
    Restaurant[] getRestaurantsByRating(Restaurant[] restaurants);

    /**
     * Returns an array of RestaurantDistance objects, sorted in ascending order of distance from the input coordinates, for all Restaurants.
     * If the distance is the same, then sort by ascending order of ID.
     * The first element in the array should be the RestaurantDistance object with the smallest distance from the input coordinate, followed by lowest Restaurant ID should the distances be equal.
     * @param latitude          The latitude of the comparison location.
     * @param longitude         The longitude of the comparison location.
     * @return A sorted array of RestaurantDistance objects, with the nearest Restaurant to the input coordinates first.
     */
    RestaurantDistance[] getRestaurantsByDistanceFrom(float latitude, float longitude);

    /**
     * Returns an array of RestaurantDistance objects, sorted in ascending order of distance from the input coordinates, for the given input Restaurants.
     * The array should be sorted using the criteria defined for the {@link #getRestaurantsByDistanceFrom(float, float) getRestaurantsByDistanceFrom} method.
     * The first element in the array should be the RestaurantDistance object with the smallest distance from the input coordinate, followed by lowest Restaurant ID should the distances be equal.
     * @param restaurants       An array of Restaurant objects to have the distance calculated.
     * @param latitude          The latitude of the comparison location.
     * @param longitude         The longitude of the comparison location.
     * @return A sorted array of RestaurantDistance objects, with the nearest Restaurant to the input coordinates first.
     */
    RestaurantDistance[] getRestaurantsByDistanceFrom(Restaurant[] restaurants, float latitude, float longitude);

    /**
     * Return an array of all the Restaurants whose name, cuisine or place name contain the given query.
     * Search queries are accent-insensitive, case-insensitive and space-insensitive.
     * The array should be sorted using the criteria defined for the {@link #getRestaurantsByName() getRestaurantsByName} method.
     * @param searchTerm       The search string to find.
     * @return A array of Restaurant objects, sorted using the criteria defined for the {@link #getRestaurantsByName() getRestaurantsByName} method.
     */
    Restaurant[] getRestaurantsContaining(String searchTerm);

}
