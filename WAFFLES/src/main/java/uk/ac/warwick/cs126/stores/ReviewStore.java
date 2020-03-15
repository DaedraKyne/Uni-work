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


public class ReviewStore implements IReviewStore {

    private MyArrayList<Review> reviewArray;
    private DataChecker dataChecker;
    private KeywordChecker keywordChecker;
    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review> reviewsById;
    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review> reviewsByDate;
    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review> reviewsByRating;
    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review>> reviewsByCustomerId;
    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review>> reviewsByRestaurantId;
    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Boolean> idBlacklist;
    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Review>> ignoredFavouritesByCustomerId;




    public ReviewStore() {
        // Initialise variables here
        reviewArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        keywordChecker = new KeywordChecker();
        reviewsById = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review>();
        reviewsByDate = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review>();
        reviewsByRating = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review>();
        reviewsByCustomerId = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review>>();
        reviewsByRestaurantId = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review>>();
        idBlacklist = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Boolean>();
        ignoredFavouritesByCustomerId = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Review>>();

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

    public boolean addReview(Review review) {
        // TODO
        if (dataChecker.isValid(review)) {
            Review duplicate = reviewsById.getData(review.getID(), null, null, null);
            Boolean blackListed = idBlacklist.getData(review.getID(), null, null, null);
            if (blackListed != null) {
                return false;
            }
            if (duplicate != null) {
                /*if (favourite.toString().equals("ID: 9353171919852385    Customer ID: 9142424817356729    Restaurant ID: 8842391383657217    Date Favourited: 2016-08-16 06:15:49")) {
                    System.out.println("Found already existing id");
                    System.out.println(duplicate);
                }*/
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
                    System.out.print("found hidden favourite");
                    Boolean hidden_fav_state = idBlacklist.getData(hidden_favourite.getID(), null, null, null);
                    if (hidden_fav_state == false) {
                        System.out.println("- falsified, good hidden");
                        addAll(hidden_favourite);
                        ignoredFavouritesByCustomerId.setData(hidden_favourite.getCustomerID(), null, null, null, hidden_matches);
                        idBlacklist.remove(hidden_favourite.getID(), null, null, null);
                        }
                }
                return false;
            }
            Review[] sameCustomer = getReviewsByCustomerID(review.getCustomerID());
            Review[] sameRestaurant = getReviewsByRestaurantID(review.getRestaurantID());    
            Review old_fav = findSimilarFavourite(review, sameCustomer, sameRestaurant);
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
                    System.out.println(": archiving");
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

    public boolean addReview(Review[] reviews) {
        // TODO
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

    public Review getReview(Long id) {
        // TODO
        if (dataChecker.isValid(id)) {
            return reviewsById.getData(id, null, null, null);
        }
        return null;
    }

    public Review[] getReviews() {
        // TODO
        Review[] reviews = new Review[reviewsById.size()];
        int i = 0;
        for (Review review : reviewsById) {
            reviews[i] = review;
            i++;
        }
        return reviews;
    }

    public Review[] getReviewsByDate() {
        // TODO
        Review[] reviews = new Review[reviewsByDate.size()];
        int i = 0;
        for (Review review : reviewsByDate) {
            reviews[i] = review;
            i++;
        }
        return reviews;
    }

    public Review[] getReviewsByRating() {
        // TODO
        Review[] reviews = new Review[reviewsByRating.size()];
        int i = 0;
        for (Review review : reviewsByRating) {
            reviews[i] = review;
            i++;
        }
        return reviews;
    }

    public Review[] getReviewsByCustomerID(Long id) {
        // TODO
        if (dataChecker.isValid(id)) {
            getSortedArrayByTree(reviewsByCustomerId.getData(id, null, null, null));
        }
        return new Review[0];
    }

    public Review[] getReviewsByRestaurantID(Long id) {
        // TODO
        if (dataChecker.isValid(id)) {
            getSortedArrayByTree(reviewsByRestaurantId.getData(id, null, null, null));
        }
        return new Review[0];
    }

    public float getAverageCustomerReviewRating(Long id) {
        // TODO
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

    public float getAverageRestaurantReviewRating(Long id) {
        // TODO
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

    public int[] getCustomerReviewHistogramCount(Long id) {
        // TODO
        if (dataChecker.isValid(id)) {
            Review[] reviews = getReviewsByCustomerID(id);
            int[] count = new int[5];
            for (int i = 0; i < 5; i++) {
                count[i] = 0;
            }
            for (int i = 0; i < reviews.length; i++) {
                count[reviews[i].getRating()] += 1;
            }
            return count;
        }
        return new int[5];
    }

    public int[] getRestaurantReviewHistogramCount(Long id) {
        // TODO
        if (dataChecker.isValid(id)) {
            Review[] reviews = getReviewsByRestaurantID(id);
            int[] count = new int[5];
            for (int i = 0; i < 5; i++) {
                count[i] = 0;
            }
            for (int i = 0; i < reviews.length; i++) {
                count[reviews[i].getRating()] += 1;
            }
            return count;
        }
        return new int[5];
    }

    public Long[] getTopCustomersByReviewCount() {
        // TODO
        return getTopFavouriteCountOfTree(reviewsByCustomerId, "customer");
    }

    public Long[] getTopRestaurantsByReviewCount() {
        // TODO
        return getTopFavouriteCountOfTree(reviewsByRestaurantId, "restaurant");
    }

    public Long[] getTopRatedRestaurants() {
        // TODO
        return getTopFavouriteCountOfTree(reviewsByRestaurantId, "top restaurants");
    }

    public String[] getTopKeywordsForRestaurant(Long id) {
        // TODO
        MyAvlTree<Integer, String, MyArrayList<String>, MyArrayList<Integer>, MyArrayList<String>, String> top_keyword_tree = new MyAvlTree<Integer, String, MyArrayList<String>, MyArrayList<Integer>, MyArrayList<String>, String>();
        MyAvlTree<String, String, MyArrayList<String>, MyArrayList<String>, MyArrayList<String>, Integer> keyword_tree = new MyAvlTree<String, String, MyArrayList<String>, MyArrayList<String>, MyArrayList<String>, Integer>();
        MyArrayList<String> found_keywords = new MyArrayList<String>();
        int count;
        for (MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review> tree : reviewsByRestaurantId) {
            for (Review review : tree) {
                String[] words = review.getReview().split(" ");
                for (int i = 0; i < words.length; i++) {
                    words[i] = words[i].toLowerCase();
                    for (int i2 = 0; i < words[i].length(); i++) {
                        char c = words[i].charAt(i2);
                        if ((c < 'a' || c > 'z') && c != '0') {
                            if (i2 != words[i].length() - 1) {
                                words[i] = words[i].substring(0, i2) + words[i].substring(i2 + 1, words.length);
                            } else {
                                words[i] = words[i].substring(0, i2);
                            }
                        }
                    }
                }
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    MyArrayList<String> wordList = new MyArrayList<String>();
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

        }
        String[] topKeywords = new String[5];
        int i = 0;
        for (String keyword : top_keyword_tree) {
            topKeywords[i] = keyword;
            i++;
        }
        return topKeywords;
    }

    public Review[] getReviewsContaining(String searchTerm) {
        // TODO
        // String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        // String searchTermConvertedFaster = stringFormatter.convertAccentsFaster(searchTerm);
        return new Review[0];
    }

    // ADDED FUNCTIONS

    private void addAll(Review review) {
        MyArrayList<Long> reviewID = new MyArrayList<Long>();
        reviewID.add(review.getID());
        reviewsById.add(review.getID(), null, null, null, review);
        Long date = review.getDateReviewed().getTime() * (-1);
        reviewsByDate.add(date, reviewID, null, null, review);
        MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review> customerTree = reviewsByCustomerId.getData(review.getCustomerID(), null, null, null);
        if (customerTree != null) {
            customerTree.add(date, null , reviewID, null, review);
            reviewsByCustomerId.setData(review.getCustomerID(), null, null, null, customerTree);
        } else {
            customerTree = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review>();
            customerTree.add(date, null , reviewID, null, review);
            reviewsByCustomerId.add(review.getCustomerID(), null, null, null, customerTree);
        }
        MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review> restaurantTree = reviewsByRestaurantId.getData(review.getRestaurantID(), null, null, null);
        if (restaurantTree != null) {
            restaurantTree.add(date, null , reviewID, null, review);
            reviewsByRestaurantId.setData(review.getRestaurantID(), null, null, null, restaurantTree);
        } else {
            restaurantTree = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review>();
            restaurantTree.add(date, null , reviewID, null, review);
            reviewsByRestaurantId.add(review.getRestaurantID(), null, null, null, restaurantTree);

        }
        reviewID.set(0, date);
        reviewID.add(review.getID());
        Long rating = (long) (-1) * review.getRating();
        reviewsByRating.add(rating, reviewID, null, null, review);

    }

    private void removeAll(Review review) {
        /*if (favourite.toString().equals("ID: 9353171919852385    Customer ID: 5473464975788313    Restaurant ID: 7816833756189615    Date Favourited: 2020-01-05 16:27:09")) {
            System.out.println("Removing from id tree");
        }*/
        MyArrayList<Long> reviewID = new MyArrayList<Long>();
        reviewID.add(review.getID());
        reviewsById.remove(review.getID(), null, null, null);
        Long date = review.getDateReviewed().getTime() * (-1);
        reviewsByDate.remove(date, reviewID, null, null);
        reviewsByCustomerId.getData(review.getCustomerID(), null, null, null).remove(
            date, null, reviewID, null);
        reviewsByRestaurantId.getData(review.getRestaurantID(), null, null, null).remove(
            date, null, reviewID, null);
        reviewID.set(0, date);
        reviewID.add(review.getID());
        Long rating = (long) (-1) * review.getRating();
        reviewsByRating.remove(rating, reviewID, null, null);
    }

    private Review findSimilarFavourite(Review review, Review[] sameCustomer, Review[] sameRestaurant) {
        if (sameCustomer.length == 0 || sameRestaurant.length == 0) {return null;}
        int ptr1 = 0;
        int ptr2 = 0;
        int compare;
        while (ptr1 < (sameCustomer.length - 1) || ptr2 < (sameRestaurant.length - 1)) {
            if (sameCustomer[ptr1] == null) {
                System.out.println("customers: " + sameCustomer.length + " <- " + review.getCustomerID());
                for (int i = 0; i < sameCustomer.length; i++) {
                    System.out.println(sameCustomer[i]);
                }
            }
            if (sameRestaurant[ptr2] == null) {
                System.out.println("restaurants: " + sameRestaurant.length + " <- " + review.getRestaurantID());
                for (int i = 0; i < sameRestaurant.length; i++) {
                    System.out.println(sameRestaurant[i]);
                }
            }
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

    private Review[] getSortedArrayByTree(MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review> original_tree) {
        if (original_tree != null) {
            Review[] fav_array = new Review[original_tree.size()];
            int i = 0;
            Iterator<Review> iter = original_tree.iterator();
            if (original_tree.size() > 0 && !iter.hasNext()) {
                System.out.println("Cannot iterate through non-empty tree for some reason: ");
            }
            for (Review review : original_tree) {
                if (review == null) {
                    System.out.println("Size of tree: " + original_tree.size());
                    System.out.println("Error at: " + i);
                }
                fav_array[i] = review;
                i++;
            }
            if (original_tree.size() != i) {
                System.out.println("Not iterated through whole tree");
                System.out.println("Size of tree: " + original_tree.size());
                System.out.println("Size of i: " + i);
            }
            return fav_array;
        }
        return new Review[0];

    }

    private Long[] getTopFavouriteCountOfTree(MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review>> reviewsByTree, String store) {
        // TODO
        MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Long> top_store_tree = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Long>();
        Long earliest_date = 0L; //Initial set to max value of long
        Long date;
        Long smallest_id = 9223372036854775807L; //Initial set to max value of long
        MyArrayList<Long> compare;
        Long count;
        Long reviewID;
        Long storeID = null;
        Review review;
        for (MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Review> tree : reviewsByTree) {
            count = 0L;
            earliest_date = 0L;
            smallest_id = 9223372036854775807L;
            if (!store.equals("top restaurants")) {
                count = (long) tree.size();
            }
            Iterator<Review> iter = tree.iterator(); //iterating from oldest to youngest favourite, BUT from biggest to smallest favouriteID
            while (iter.hasNext()) {
                review = iter.next();
                if (store.equals("top restaurants")) {
                    count = (long) getAverageRestaurantReviewRating(review.getRestaurantID());;
                }    
                date = review.getDateReviewed().getTime();
                reviewID = review.getID();
                if (date < earliest_date && earliest_date != 0L) {
                    break;
                }
                if (reviewID <= smallest_id) {
                    earliest_date = date;
                    smallest_id = reviewID;
                    if (store.equals("customer")) {
                        storeID = review.getCustomerID();
                    } else if (store.equals("restaurant") || store.equals("top restaurants")) {
                        storeID = review.getRestaurantID();
                    }
                }              
            }
            compare = new MyArrayList<Long>();
            compare.add(earliest_date);
            compare.add(smallest_id);
            top_store_tree.add((-1) * count, null , compare, null, storeID);
            if (top_store_tree.size() > 20) {
                top_store_tree.removeLargest();
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
