package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IReviewStore;
import uk.ac.warwick.cs126.models.Review;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;

import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.KeywordChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

public class ReviewStore implements IReviewStore {

    private MyArrayList<Review> reviewArray;
    private DataChecker dataChecker;

    public ReviewStore() {
        // Initialise variables here
        reviewArray = new MyArrayList<>();
        dataChecker = new DataChecker();
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
        return false;
    }

    public boolean addReview(Review[] reviews) {
        // TODO
        return false;
    }

    public Review getReview(Long id) {
        // TODO
        return null;
    }

    public Review[] getReviews() {
        // TODO
        return new Review[0];
    }

    public Review[] getReviewsByDate() {
        // TODO
        return new Review[0];
    }

    public Review[] getReviewsByRating() {
        // TODO
        return new Review[0];
    }

    public Review[] getReviewsByCustomerID(Long id) {
        // TODO
        return new Review[0];
    }

    public Review[] getReviewsByRestaurantID(Long id) {
        // TODO
        return new Review[0];
    }

    public float getAverageCustomerReviewRating(Long id) {
        // TODO
        return 0.0f;
    }

    public float getAverageRestaurantReviewRating(Long id) {
        // TODO
        return 0.0f;
    }

    public int[] getCustomerReviewHistogramCount(Long id) {
        // TODO
        return new int[5];
    }

    public int[] getRestaurantReviewHistogramCount(Long id) {
        // TODO
        return new int[5];
    }

    public Long[] getTopCustomersByReviewCount() {
        // TODO
        return new Long[20];
    }

    public Long[] getTopRestaurantsByReviewCount() {
        // TODO
        return new Long[20];
    }

    public Long[] getTopRatedRestaurants() {
        // TODO
        return new Long[20];
    }

    public String[] getTopKeywordsForRestaurant(Long id) {
        // TODO
        return new String[5];
    }

    public Review[] getReviewsContaining(String searchTerm) {
        // TODO
        // String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        // String searchTermConvertedFaster = stringFormatter.convertAccentsFaster(searchTerm);
        return new Review[0];
    }
}
