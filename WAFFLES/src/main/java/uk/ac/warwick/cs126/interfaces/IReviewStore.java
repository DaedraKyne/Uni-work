package uk.ac.warwick.cs126.interfaces;

import uk.ac.warwick.cs126.models.Review;

import java.io.InputStream;

/**
 * Describes the operations on Reviews that are required by the data store.
 * @author jamesvh (James Van Hinsbergh) -
 * <a href="mailto:J.van-Hinsbergh@warwick.ac.uk">J.van-Hinsbergh@warwick.ac.uk</a>
 * @author iant (Ian Tu) -
 * <a href="mailto:I.Tu@warwick.ac.uk">I.Tu@warwick.ac.uk</a>
 * @version 1.0
 */
public interface IReviewStore {

    /**
     * Loads data from a TSV file containing the Review data into a Review array, parsing the attributes where required.
     * @param resource       The source csv file to be loaded.
     * @return A Review array with all Reviews contained within the data file, regardless of the validity of the ID.
     */
    Review[] loadReviewDataToArray(InputStream resource);

    /**
     * Add a new Review to the store. The method should return true if the Review is successfully added to the data store.
     * The Review should not be added if a Review with the same ID already exists in the store.
     * If a duplicate ID is encountered, the existing Review should be removed and the ID blacklisted from further use.
     * If a Review has a unique ID but there already exists a Review with the same Customer ID and Restaurant ID, you replace it with the newest of the pair.
     * The ID of the Review that was subsequently replaced is now blacklisted, and should not exist in the store.
     * An invalid ID is one that contains zeros or more than 3 of the same digit, these should not be added, although they do not need to be blacklisted.
     * @param review       The Review object to add to the data store.
     * @return True if the Review was successfully added, false otherwise.
     */
    boolean addReview(Review review);

    /**
     * Add new Reviews in the input array to the store. The method should return true if the Reviews are all successfully added to the data store.
     * Reference the {@link #addReview(Review) addReview} method for details on ID handling and existing Customer/Restaurant Reviews.
     * @param reviews       An array of Review objects to add to the data store.
     * @return True if all of the Reviews were successfully added, false otherwise.
     */
    boolean addReview(Review[] reviews);

    /**
     * Returns a single Review, the Review with the given ID, or null if not found.
     * @param id       The ID of the Review to be retrieved.
     * @return The Review with the given ID, or null if not found.
     */
    Review getReview(Long id);

    /**
     * Returns an array of all Reviews, sorted in ascending order of ID.
     * The Review with the lowest ID should be the first element in the array.
     * @return A sorted array of Review objects, with lowest ID first.
     */
    Review[] getReviews();

    /**
     * Returns an array of all Reviews, sorted in descending order of date reviewed (newest first).
     * If the date reviewed is the same, then sort by ascending order of ID.
     * The newest Review should be the first element in the array, followed by lowest ID should the date reviewed be equal.
     * @return A sorted array of Review objects, with the newest Review first.
     */
    Review[] getReviewsByDate();

    /**
     * Returns an array of all Reviews, sorted in descending order of rating (highest first).
     * If the rating is the same, then sort by date reviewed (newest first), if still same then sort by ascending order of ID.
     * The highest rated Review should be the first element in the array, followed by lowest ID should the ratings be equal.
     * @return A sorted array of Review objects, with the highest rated Review first.
     */
    Review[] getReviewsByRating();

    /**
     * Returns an array of all Reviews by the Customer with the given ID.
     * The array is sorted by date reviewed from newest to oldest, with ascending order of ID for matching dates.
     * The newest Review should be the first element in the array, with the lowest ID should the date reviewed be equal.
     * @param id       The ID of the Customer who's Reviews are to be retrieved.
     * @return A sorted array of Review objects, with the newest Review first.
     */
    Review[] getReviewsByCustomerID(Long id);

    /**
     * Returns an array of all Reviews for the Restaurant with the given ID.
     * The array should be sorted using the criteria defined for the {@link #getReviewsByCustomerID(Long) getReviewsByCustomerID} method.
     * @param id       The ID of the Restaurant who's Reviews are to be retrieved.
     * @return A sorted array of Review objects, with the newest Review first.
     */
    Review[] getReviewsByRestaurantID(Long id);

    /**
     * Returns the average rating given by a Customer (to 1 dp), with the given ID.
     * If no ratings are found, return a rating of 0.
     * @param id       The ID of the Customer to retrieve the average rating for.
     * @return The average rating given by the Customer with the given ID, or 0 if no ratings found.
     */
    float getAverageCustomerReviewRating(Long id);

    /**
     * Returns the rating for a Restaurant (to 1 dp), with the given ID, which is taken as the average of all ratings for that Restaurant.
     * If no ratings are found, return a rating of 0.
     * @param id       The ID of the Restaurant to retrieve the rating for.
     * @return The rating for the Restaurant with the given ID, or 0 if no ratings found.
     */
    float getAverageRestaurantReviewRating(Long id);

    /**
     * Returns an array of 5 counts, corresponding to the Review ratings given by the Customer.
     * e.g. int[0] will contain the number of 1-star reviews given by the Customer, int[1] will contain the number of 2-star reviews etc.
     * @param id       The ID of the Customer who's Rating breakdown is to be retrieved.
     * @return An array of 5 counts detailing the breakdown of number of n-star reviews given by the Customer.
     */
    int[] getCustomerReviewHistogramCount(Long id);

    /**
     * Returns an array of 5 counts, corresponding to the Review ratings for the given Restaurant.
     * e.g. int[0] will contain the number of 1-star reviews for the Restaurant, int[1] will contain the number of 2-star reviews etc.
     * @param id       The ID of the Restaurant who's Rating breakdown is to be retrieved.
     * @return An array of 5 counts detailing the breakdown of number of n-star reviews for the given Restaurant.
     */
    int[] getRestaurantReviewHistogramCount(Long id);

    /**
     * Returns an array of 20 Customer IDs that have reviewed the most Restaurants.
     * If there are less than 20 IDs, the remaining indexes should be set to null.
     * The array should be sorted by descending Review count, then by date of the oldest Review, and finally by ascending order of Customer ID for matching counts.
     * @return A sorted array of 20 Customer IDs, with the Customer with the highest Review count first.
     */
    Long[] getTopCustomersByReviewCount();

    /**
     * Returns an array of 20 Restaurant IDs that have been reviewed the most.
     * If there are less than 20 IDs, the remaining indexes should be set to null.
     * The array should be sorted by descending Review count, then by date of the oldest Review, and finally by ascending order of Restaurant ID for matching counts.
     * @return A sorted array of 20 Restaurant IDs, with the Restaurant with the highest Review count first.
     */
    Long[] getTopRestaurantsByReviewCount();

    /**
     * Returns an array of 20 Restaurant IDs that have the highest rating.
     * If there are less than 20 IDs, the remaining indexes should be set to null.
     * The array should be sorted by descending average Review rating, then by date of the oldest Review, and finally by ascending order of Restaurant ID for matching counts.
     * @return A sorted array of 20 Restaurant IDs, with the Restaurant with the highest Review count first.
     */
    Long[] getTopRatedRestaurants();

    /**
     * Returns an array of 5 keywords for the given Restaurant that have the highest frequency of use in the reviews.
     * If there are less than 5 keywords, the remaining indexes should be set to null.
     * The array should be sorted by descending count of frequency, then by alphabetical order (0-9 then A-Z) for matching counts.
     * @param id       The ID of the Restaurant who's top keywords are to be retrieved.
     * @return A sorted array of 5 keywords for the given Restaurant, with the highest frequency first.
     */
    String[] getTopKeywordsForRestaurant(Long id);

    /**
     * Return an array of all the Reviews whose review message contain the given query.
     * Search queries are accent-insensitive, case-insensitive and space-insensitive.
     * The array should be sorted using the criteria defined for the {@link #getReviewsByDate() getReviewsByDate} method.
     * @param searchTerm       The search string to find.
     * @return A array of Customer objects, sorted using the criteria defined for the {@link #getReviewsByDate() getReviewsByDate} method.
     */
    Review[] getReviewsContaining(String searchTerm);
}
