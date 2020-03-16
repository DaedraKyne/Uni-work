package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IReviewStore;
import uk.ac.warwick.cs126.models.Review;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyAvlTree;


import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.KeywordChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

import java.util.Iterator;


/*
 * Main class, in charge of dealing with all review tasks.
 */
public class ReviewStore implements IReviewStore {

    private DataChecker dataChecker;
    private KeywordChecker keywordChecker;
    private MyAvlTree<Long, Long, Review> reviewsById; // AVL tree of favourites sorted by ID
    private MyAvlTree<Long, Long, Review> reviewsByDate; // AVL tree of favourites sorted by date/ID
    private MyAvlTree<Long, Long, Review> reviewsByRating; // AVL tree of favourites sorted by Rating/date/ID
    private MyAvlTree<Long, Long, MyAvlTree<Long, Long, Review>> reviewsByCustomerId; // AVL tree of AVL trees of reviews (sorted by ID) for each restaurantID, sorted by customer ID
    private MyAvlTree<Long, Long, MyAvlTree<Long, Long, Review>> reviewsByRestaurantId; // AVL tree of AVL trees of reviews (sorted by ID) for each restaurantID, sorted by restaurant ID
    private MyAvlTree<Long, Long, Boolean> idBlacklist; // AVL tree of blacklisted IDs sorted by ID
    private MyAvlTree<Long, Long, MyArrayList<Review>> ignoredFavouritesByCustomerId; // AVL tree of lists of blacklisted reviews that can be un-blacklisted, sorted by customerID



  /*
   * Constructor class for FavouriteStore.
   * Initializes values.
   */
    public ReviewStore() {
        // Initialise variables here
        dataChecker = new DataChecker();
        keywordChecker = new KeywordChecker();
        reviewsById = new MyAvlTree<Long, Long, Review>();
        reviewsByDate = new MyAvlTree<Long, Long, Review>();
        reviewsByRating = new MyAvlTree<Long, Long, Review>();
        reviewsByCustomerId = new MyAvlTree<Long, Long, MyAvlTree<Long, Long, Review>>();
        reviewsByRestaurantId = new MyAvlTree<Long, Long, MyAvlTree<Long, Long, Review>>();
        idBlacklist = new MyAvlTree<Long, Long, Boolean>();
        ignoredFavouritesByCustomerId = new MyAvlTree<Long, Long, MyArrayList<Review>>();

    }

    public Review[] loadReviewDataToArray(InputStream resource) {
        Review[] reviewArray = new Review[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Review[] loadedReviews = new Review[lineCount - 1];

            BufferedReader tsvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int reviewCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            tsvReader.readLine();
            while ((row = tsvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split("\t");
                    Review review = new Review(
                            Long.parseLong(data[0]),
                            Long.parseLong(data[1]),
                            Long.parseLong(data[2]),
                            formatter.parse(data[3]),
                            data[4],
                            Integer.parseInt(data[5]));
                    loadedReviews[reviewCount++] = review;
                }
            }
            tsvReader.close();

            reviewArray = loadedReviews;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return reviewArray;
    }


  /*
   * Attempts to add review to the store
   * If review's id is blacklisted, do not add.
   * If there already exists a stored review with that id,
   * remove it and add blacklist the id.
   * 
   * If the review is valid and does not have an ID that has been blacklisted, is a
   * duplicate, or is invalid: if there exists a review already inside the store with
   * the same Customer ID and Restaurant ID, and if this review is newer than
   * the one in the store, you must replace it with this review . If this replace
   * happens, the ID of the review originally in the store should be blacklisted
   * from further use.
   *
   * Returns true if review was successfully added.
   * 
   * @return         boolean
   */
    public boolean addReview(Review review) {
        // DONE
        if (dataChecker.isValid(review)) {
            Review duplicate = reviewsById.getData(review.getID(), null, null, null);
            Boolean blackListed = idBlacklist.getData(review.getID(), null, null, null);
            if (blackListed != null) {
                return false;
            }
            if (duplicate != null) {
                removeAll(duplicate);
                idBlacklist.add(review.getID(), null, null, null, true);
                MyArrayList<Review> hidden_matches = ignoredFavouritesByCustomerId.getData(duplicate.getCustomerID(), null, null, null);
                Review hidden_favourite = null;
                if (hidden_matches != null) {
                    for (int i = 0; i < hidden_matches.size(); i++) {
                        System.out.print("l");
                        if (hidden_matches.get(i).getRestaurantID() == review.getRestaurantID()) {
                            hidden_favourite = hidden_matches.get(i);
                            hidden_matches.remove(hidden_favourite);
                            break;
                        }
                    }
                }
                if (hidden_favourite != null) {
                    Boolean hidden_fav_state = idBlacklist.getData(hidden_favourite.getID(), null, null, null);
                    if (hidden_fav_state == false) {
                        addAll(hidden_favourite);
                        ignoredFavouritesByCustomerId.setData(hidden_favourite.getCustomerID(), null, null, null, hidden_matches);
                        idBlacklist.remove(hidden_favourite.getID(), null, null, null);
                        }
                }
                return false;
            }
            Review[] sameCustomer = getReviewsByCustomerID(review.getCustomerID());
            Review[] sameRestaurant = getReviewsByRestaurantID(review.getRestaurantID());    
            Review old_fav = findSimilarReview(review, sameCustomer, sameRestaurant);
            if (old_fav != null) {
                int date_diff = review.getDateReviewed().compareTo(old_fav.getDateReviewed());
                if (date_diff > 0) {
                    removeAll(old_fav);
                    idBlacklist.add(old_fav.getID(), null, null, null, false);
                    MyArrayList<Review> hidden_matches = ignoredFavouritesByCustomerId.getData(old_fav.getCustomerID(), null, null, null);
                    if (hidden_matches == null) {
                        hidden_matches = new MyArrayList<Review>();
                        hidden_matches.add(old_fav);
                        ignoredFavouritesByCustomerId.add(old_fav.getCustomerID(), null, null, null, hidden_matches);
                    } 
                    else {
                        hidden_matches.add(old_fav);
                        ignoredFavouritesByCustomerId.setData(old_fav.getCustomerID(), null, null, null, hidden_matches);
                    }
                } else {
                    MyArrayList<Review> hidden_matches = ignoredFavouritesByCustomerId.getData(review.getCustomerID(), null, null, null);
                    if (hidden_matches == null) {
                        hidden_matches = new MyArrayList<Review>();
                        hidden_matches.add(review);
                        ignoredFavouritesByCustomerId.setData(review.getCustomerID(), null, null, null, hidden_matches);
                    }
                    else {
                        hidden_matches.add(review);
                        ignoredFavouritesByCustomerId.add(review.getCustomerID(), null, null, null, hidden_matches);
                    }
                    return false;
                }
            }
            addAll(review);
            return true;
            }
        return false;
    }


  /*
   * Attempts to add valid Review objects from the reviews input array to
   * the store.
   * Return true if the all the reviews are all successfully added to the data
   * store, otherwise false .
   *
   * @return         boolean
   */
    public boolean addReview(Review[] reviews) {
        // DONE
        if (reviews == null) {return false;}
        boolean fully_added = true;
        int i = 0;
        int length = reviews.length;
        for (Review review : reviews) {
            if (!addReview(review)) {
                fully_added = false;
            }
            i++;
            if (i % 1000 == 0) {System.out.println((int)Math.floor(((float)i / (float)length) * 100) + "." + (int)Math.floor(((float)i / (float)length) * 1000) % 10 + "%");}
        }
        return fully_added;
    }


  /*
   * Returns the review with the matching ID id from the store, otherwise
   * return null.
   *
   * @return    review
   */
    public Review getReview(Long id) {
        // DONE
        if (dataChecker.isValid(id)) {
            return reviewsById.getData(id, null, null, null);
        }
        return null;
    }


  /*
   * Returns an array of all reviews in the store, sorted in 
   * ascending order of ID.
   *
   * @return    sorted reviews
   */
    public Review[] getReviews() {
        // DONE
        Review[] reviews = new Review[reviewsById.size()];
        int i = 0;
        for (Review review : reviewsById) {
            reviews[i] = review;
            i++;
        }
        if (reviewsByRating.size() != i) {
            Review[] regulated_reviews = new Review[i];
            for (int i2 = 0; i2 < i; i2++) {
                regulated_reviews[i2] = reviews[i2];
            }
            reviews = regulated_reviews;
        }
        return reviews;
    }


  /*
   * Returns an array of all reviews in the store, sorted by 
   * date favourited.
   * If they have the same Date Reviewed, then it is sorted in
   * ascending order of their ID
   * 
   * @return    sorted reviews
   */
    public Review[] getReviewsByDate() {
        // DONE
        Review[] reviews = new Review[reviewsByDate.size()];
        int i = 0;
        for (Review review : reviewsByDate) {
            reviews[i] = review;
            i++;
        }
        if (reviewsByDate.size() != i) {
            Review[] regulated_reviews = new Review[i];
            for (int i2 = 0; i2 < i; i2++) {
                regulated_reviews[i2] = reviews[i2];
            }
            reviews = regulated_reviews;
        }
        return reviews;
    }

    
  /*
   * Returns an array of all reviews in the store, sorted by 
   * descending order of Rating.
   * If they have the same Rating, then it is sorted in
   * terms of Date reviewed.
   * If they have the same Date Reviewed, then it is sorted in
   * ascending order of their ID.
   * 
   * @return    sorted reviews
   */
    public Review[] getReviewsByRating() {
        // DONE
        Review[] reviews = new Review[reviewsByRating.size()];
        int i = 0;
        for (Review review : reviewsByRating) {
            reviews[i] = review;
            i++;
        }
        if (reviewsByRating.size() != i) {
            Review[] regulated_reviews = new Review[i];
            for (int i2 = 0; i2 < i; i2++) {
                regulated_reviews[i2] = reviews[i2];
            }
            reviews = regulated_reviews;
        }

        return reviews;
    }


  /*
   * Return a review array with all the reviews from the store
   * that have id for its Customer ID.
   *
   * @return    sorted reviews
   */
    public Review[] getReviewsByCustomerID(Long id) {
        // DONE
        if (dataChecker.isValid(id)) {
            return getSortedArrayByTree(reviewsByCustomerId.getData(id, null, null, null));
        }
        return new Review[0];
    }


  /*
   * Return a review array with all the reviews from the store
   * that have id for its restaurant ID.
   *
   * @return    sorted reviews
   */
    public Review[] getReviewsByRestaurantID(Long id) {
        // DONE
        if (dataChecker.isValid(id)) {
            return getSortedArrayByTree(reviewsByRestaurantId.getData(id, null, null, null));
        }
        return new Review[0];
    }


  /*
   * Return the average overall average review rating of
   * the given customer.
   *
   * @return    average rating
   */
    public float getAverageCustomerReviewRating(Long id) {
        // DONE
        if (dataChecker.isValid(id)) {
            Review[] reviews = getReviewsByCustomerID(id);
            float rating = 0.0f;
            int rating_decimal = 0;
            int i;
            for (i = 0; i < reviews.length; i++) {
                rating += reviews[i].getRating();
            }
            rating /= i;
            rating_decimal = (int) Math.round(rating * 10 % 10);
            rating = (float) Math.floor(rating) + (rating_decimal * 0.1f);
            if (Math.floor(rating * 10 % 10) == 9 && rating_decimal == 0) {
                rating += 1;
            }
            return rating;
        }
        return 0.0f;
    }


  /*
   * Return the average overall average review rating of
   * the given restaurant.
   *
   * @return    average rating
   */
    public float getAverageRestaurantReviewRating(Long id) {
        // DONE
        if (dataChecker.isValid(id)) {
            Review[] reviews = getReviewsByRestaurantID(id);
            float rating = 0.0f;
            int rating_decimal = 0;
            int i;
            for (i = 0; i < reviews.length; i++) {
                rating += reviews[i].getRating();
            }
            rating /= i;
            rating_decimal = (int) Math.round(rating * 10 % 10);
            rating = (float) Math.floor(rating) + (rating_decimal * 0.1f);
            if (Math.floor(rating * 10 % 10) == 9 && rating_decimal == 0) {
                rating += 1;
            }
            return rating;
        }
        return 0.0f;
    }


  /*
   * Return the histogram count of the ratings from all the
   * reviews from the store that have id for its Customer ID.
   * 
   * @return    counts
   */
    public int[] getCustomerReviewHistogramCount(Long id) {
        // DONE
        if (dataChecker.isValid(id)) {
            Review[] reviews = getReviewsByCustomerID(id);
            int[] count = new int[5];
            for (int i = 0; i < 5; i++) {
                count[i] = 0;
            }
            for (int i = 0; i < reviews.length; i++) {
                count[reviews[i].getRating() - 1] += 1;
            }
            return count;
        }
        return new int[5];
    }


  /*
   * Return the histogram count of the ratings from all the
   * reviews from the store that have id for its restaurant ID.
   * 
   * @return    counts
   */
    public int[] getRestaurantReviewHistogramCount(Long id) {
        // DONE
        if (dataChecker.isValid(id)) {
            Review[] reviews = getReviewsByRestaurantID(id);
            int[] count = new int[5];
            for (int i = 0; i < 5; i++) {
                count[i] = 0;
            }
            for (int i = 0; i < reviews.length; i++) {
                count[reviews[i].getRating() - 1] += 1;
            }
            return count;
        }
        return new int[5];
    }


  /*
   * Returns the Customer ID’s of top 20 customers who reviewed
   * the most.
   * If they have the same review count, then it is sorted in
   * terms of last review date, from oldest to newest.
   * If they have the same Date Reviewed, then it is sorted in
   * ascending order of their ID.
   * 
   * @return    sorted customerIDs
   */
    public Long[] getTopCustomersByReviewCount() {
        // DONE
        return getTopFavouriteCountOfTree(reviewsByCustomerId, "customer");
    }


  /*
   * Returns the Restaurant ID’s of top 20 restaurant who bave
   * the most reviews.
   * If they have the same review count, then it is sorted in
   * terms of last review date, from oldest to newest.
   * If they have the same Date Reviewed, then it is sorted in
   * ascending order of their ID.
   * 
   * @return    sorted restaurantIDs
   */
    public Long[] getTopRestaurantsByReviewCount() {
        // DONE
        return getTopFavouriteCountOfTree(reviewsByRestaurantId, "restaurant");
    }


  /*
   * Returns the Restaurant ID’s of top 20 restaurants that have
   * the highest average review rating.
   * If they have the same review count, then it is sorted in
   * terms of last review date, from oldest to newest.
   * If they have the same Date Reviewed, then it is sorted in
   * ascending order of their ID.
   * 
   * @return    sorted restaurantIDs
   */
    public Long[] getTopRatedRestaurants() {
        // DONE
        return getTopFavouriteCountOfTree(reviewsByRestaurantId, "top restaurants");
    }


  /*
   * Return the top 5 keywords, in lowercase form, associated with
   * the Restaurant with Restaurant ID id.
   * If they have the same appearance count, then it is sorted
   * alphabetically.
   * 
   * @return    sorted restaurantIDs
   */
    public String[] getTopKeywordsForRestaurant(Long id) {
        // DONE
        MyAvlTree<Integer, String, String> top_keyword_tree = new MyAvlTree<Integer, String, String>();
        MyAvlTree<String, String, Integer> keyword_tree = new MyAvlTree<String, String, Integer>();
        MyArrayList<String> found_keywords = new MyArrayList<String>();
        int count;
        Review[] reviews = getReviewsByRestaurantID(id);
        if (reviews == null) {
            return new String[5];
        }
        for (Review review : reviews) {
            String[] words = review.getReview().split(" ");
            for (int i = 0; i < words.length; i++) {
                words[i] = words[i].toLowerCase();
                for (int i2 = 0; i2 < words[i].length(); i2++) {
                    char c = words[i].charAt(i2);
                    if ((c < 'a' || c > 'z') && c != '0') {
                        if (i2 != words[i].length() - 1) {
                            words[i] = words[i].substring(0, i2) + words[i].substring(i2 + 1, words[i].length());
                            i2 -= 1;
                        } else {
                            words[i] = words[i].substring(0, i2);
                        }
                    }
                }
            }
            MyArrayList<String> wordList;
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                if (word.equals("") || word == null || word.isEmpty()) {
                    continue;
                }
                wordList = new MyArrayList<String>();
                wordList.add(word);
                if (keywordChecker.isAKeyword(word)) {
                    if (found_keywords.contains(word)) {
                        count = keyword_tree.getData(word, null, null, null);
                        keyword_tree.setData(word, null, null, null, count + 1);
                        top_keyword_tree.remove(count, wordList, null, null);
                        top_keyword_tree.add(count + 1, wordList, null, null, word);
                    } else {
                        found_keywords.add(word);
                        keyword_tree.add(word, null, null, null, 1);
                        top_keyword_tree.add(1, wordList, null, null, word);
                    }
                }
                if (top_keyword_tree.size() > 5) {
                    top_keyword_tree.removeLargest();
                }
            }
        }
        String[] topKeywords = new String[5];
        int i = 0;
        for (String keyword : top_keyword_tree) {
            topKeywords[i] = keyword;
            i++;
        }
        return topKeywords;
    }


  /*
   * Return an array of all the reviews from the store whose Review
   * contains the given query str .
   *
   * @param     searchterm      term that is searched for
   * @return    reviews
   */
    public Review[] getReviewsContaining(String searchTerm) {
        // DONE
        // String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        // String searchTermConvertedFaster = stringFormatter.convertAccentsFaster(searchTerm);
        if (searchTerm.isEmpty()) {
            return new Review[0];
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
                i -= 1;
            }
        }
        String searchTermConverted = StringFormatter.convertAccentsFaster(searchTerm);
        searchTermConverted = searchTermConverted.toLowerCase();
        Review[] reviews = getReviewsByDate();
        Review[] reviewMatches = new Review[reviews.length];
        String name;
        int found = 0;
        i = 0;
        for (Review review : reviews) {
            if (review == null) {continue;}
            name = review.getReview();
            if (name == null || name.equals("") || name.isEmpty()) {continue;}
            name = StringFormatter.convertAccents(name);
            name = name.toLowerCase();
            for (int i2 = 0; i2 < name.length() - searchTermConverted.length() + 1; i2++) {
                if (name.substring(i2, i2 + searchTermConverted.length()).equals(searchTermConverted)) {
                    reviewMatches[found++] = review;
                    break; //Once a match has been found for a given review, move on to next review
                }
            }
            i++;
        }
        Review[] arr = new Review[found];
        for (i = 0; i < found; i++) {
            arr[i] = reviewMatches[i];
        }
        return arr;
    }

    // ADDED FUNCTIONS


  /*
   * Adds the given review to all restaurant AVL trees.
   *
   * @param     review
   */
    private void addAll(Review review) {
        reviewsById.add(review.getID(), null, null, null, review);
        Long date = review.getDateReviewed().getTime() * (-1);
        MyArrayList<Long> reviewID = new MyArrayList<Long>();
        reviewID.add(review.getID());
        reviewsByDate.add(date, reviewID, null, null, review);
        MyAvlTree<Long, Long, Review> customerTree = reviewsByCustomerId.getData(review.getCustomerID(), null, null, null);
        if (customerTree != null) {
            customerTree.add(date, null , reviewID, null, review);
            reviewsByCustomerId.setData(review.getCustomerID(), null, null, null, customerTree);
        } else {
            customerTree = new MyAvlTree<Long, Long, Review>();
            customerTree.add(date, null , reviewID, null, review);
            reviewsByCustomerId.add(review.getCustomerID(), null, null, null, customerTree);
        }
        MyAvlTree<Long, Long, Review> restaurantTree = reviewsByRestaurantId.getData(review.getRestaurantID(), null, null, null);
        if (restaurantTree != null) {
            restaurantTree.add(date, null , reviewID, null, review);
            reviewsByRestaurantId.setData(review.getRestaurantID(), null, null, null, restaurantTree);
        } else {
            restaurantTree = new MyAvlTree<Long, Long, Review>();
            restaurantTree.add(date, null , reviewID, null, review);
            reviewsByRestaurantId.add(review.getRestaurantID(), null, null, null, restaurantTree);

        }
        reviewID = new MyArrayList<Long>();
        reviewID.add(date);
        reviewID.add(review.getID());
        Long rating = (long) (-1) * review.getRating();
        reviewsByRating.add(rating, reviewID, null, null, review);

    }


  /*
   * Removes the given review from all restaurant AVL trees.
   *
   * @param     review
   */
    private void removeAll(Review review) {
        reviewsById.remove(review.getID(), null, null, null);
        long date = review.getDateReviewed().getTime() * (-1);
        MyArrayList<Long> reviewID = new MyArrayList<Long>();
        reviewID.add(review.getID());
        reviewsByDate.remove(date, reviewID, null, null);
        reviewsByCustomerId.getData(review.getCustomerID(), null, null, null).remove(
            date, null, reviewID, null);
        reviewsByRestaurantId.getData(review.getRestaurantID(), null, null, null).remove(
            date, null, reviewID, null);
        reviewID = new MyArrayList<Long>();
        reviewID.add(date);
        reviewID.add(review.getID());
        Long rating = (long) (-1) * review.getRating();
        reviewsByRating.remove(rating, reviewID, null, null);

    }


  /*
   * Checks to see if there exists a review in the given review arrays with
   * the same restaurantID and customerID.
   * If so, returns it
   *
   * @param     review
   * @param     sorted customerFavourites
   * @param     sorted restaurantFavourites
   * @return    review match
   */
    private Review findSimilarReview(Review review, Review[] sameCustomer, Review[] sameRestaurant) {
        if (sameCustomer.length == 0 || sameRestaurant.length == 0) {return null;}
        int ptr1 = 0;
        int ptr2 = 0;
        int compare;
        while (ptr1 < (sameCustomer.length - 1) || ptr2 < (sameRestaurant.length - 1)) {
            if (sameCustomer[ptr1].getID() == sameRestaurant[ptr2].getID()) {
                return sameCustomer[ptr1];
            }
            if (ptr1 == (sameCustomer.length - 1) && ptr2 < (sameRestaurant.length - 1)) {
                ptr2++;
                continue;
            }
            if (ptr2 == (sameRestaurant.length - 1) && ptr1 < (sameCustomer.length - 1)) {
                ptr1++;
                continue;
            }
            compare = sameCustomer[ptr1].getDateReviewed().compareTo(sameRestaurant[ptr2].getDateReviewed());
            if (compare == 0) {
                compare = sameCustomer[ptr1].getID().compareTo(sameRestaurant[ptr2].getID());
            }
            if (compare > 0 && (ptr2 < (sameRestaurant.length - 1))) {
                ptr2++;
            } else if (compare <= 0 && ptr1 < (sameCustomer.length - 1)) {
                ptr1++;
            }
        }
        return null;
    }


  /*
   * Given a tree of reviews, returns array of all sorted contained
   * in it.
   *
   * @param     reviews AVL tree
   * @return    sorted reviews
   */
    private Review[] getSortedArrayByTree(MyAvlTree<Long, Long, Review> original_tree) {
        if (original_tree != null) {
            Review[] fav_array = new Review[original_tree.size()];
            int i = 0;
            for (Review review : original_tree) {
                fav_array[i] = review;
                i++;
            }
            if (original_tree.size() != i) {
                Review[] regulated_reviews = new Review[i];
                for (int i2 = 0; i2 < i; i2++) {
                    regulated_reviews[i2] = fav_array[i2];
                }
                fav_array = regulated_reviews;    
            }
            return fav_array;
        }
        return new Review[0];

    }


  /*
   * Given a tree of trees of reviews, and a store type, returns array
   * of top IDs or top ratings.
   *
   * @param     reviewsByTreeOfTrees
   * @param     store -- "customer", "restaurant", "top restaurants"
   * @return    sorted IDs/ratings
   */
    private Long[] getTopFavouriteCountOfTree(MyAvlTree<Long, Long, MyAvlTree<Long, Long, Review>> reviewsByTreeOfTrees, String store) {
        // DONE
        MyAvlTree<Long, Long, Long> top_store_tree = new MyAvlTree<Long, Long, Long>();
        Long date;
        MyArrayList<Long> compare;
        Long count;
        Long reviewID;
        Long storeID = null;
        Review review;
        for (MyAvlTree<Long, Long, Review> tree : reviewsByTreeOfTrees) {
            count = 0L;
            date = 0L;
            reviewID = 9223372036854775807L;
            if (!store.equals("top restaurants")) {
                count = (long) tree.size();
            }
            Iterator<Review> iter = tree.iterator(); //iterating from oldest to youngest favourite, BUT from biggest to smallest favouriteID
            if (iter.hasNext()) {
                review = iter.next();
                date = review.getDateReviewed().getTime();
                reviewID = review.getID();
                if (store.equals("customer")) {
                    storeID = review.getCustomerID();
                } else if (store.equals("restaurant") || store.equals("top restaurants")) {
                    storeID = review.getRestaurantID();
                }
                if (store.equals("top restaurants")) {
                    count = (long) getAverageRestaurantReviewRating(storeID);;
                }
                compare = new MyArrayList<Long>();
                compare.add(date);
                compare.add(reviewID);
                top_store_tree.add((-1) * count, null , compare, null, storeID);
                if (top_store_tree.size() > 20) {
                    top_store_tree.removeLargest();
                }    
            }
        }
        Long[] topStores = new Long[20];
        int i = 0;
        for (Long ID : top_store_tree) {
            topStores[i] = ID;
            i++;
        }
        return topStores;
    }




}
