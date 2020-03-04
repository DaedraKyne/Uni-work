package uk.ac.warwick.cs126.models;

import uk.ac.warwick.cs126.interfaces.IReview;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class to store properties for a Review.
 *
 * @author jamesvh (James Van Hinsbergh) -
 * <a href="mailto:J.van-Hinsbergh@warwick.ac.uk">J.van-Hinsbergh@warwick.ac.uk</a>
 * @author iant (Ian Tu) -
 * <a href="mailto:I.Tu@warwick.ac.uk">I.Tu@warwick.ac.uk</a>
 * @version 1.0
 */
public class Review implements IReview {

    private Long id;
    private Long customerID;
    private Long restaurantID;
    private Date dateReviewed;
    private String review;
    private int rating;

    /**
     * Creates a Review object with all properties set on initialisation.
     * @param id The ID of the Review.
     * @param customerID The ID of the Customer the has made the Review.
     * @param restaurantID The ID of the Restaurant that the Review is for.
     * @param dateReviewed The date that the Review was made.
     * @param review The contents of the review.
     * @param rating The rating of the review.
     */
    public Review(Long id, Long customerID, Long restaurantID, Date dateReviewed, String review, int rating) {
        this.id = id;
        this.customerID = customerID;
        this.restaurantID = restaurantID;
        this.dateReviewed = dateReviewed;
        this.review = review;
        this.rating = rating;
    }

    /**
     * Returns the ID of the Review.
     * @return The ID of the Review.
     */
    public Long getID() {
        return id;
    }

    /**
     * Returns the string representation of the ID of the Review.
     * @return The string representation of the ID of the Review.
     */
    public String getStringID() { return String.valueOf(id); }

    /**
     * Sets the ID of the Review.
     * @param id The new ID to assign to the Review.
     */
    public void setID(Long id) {
        this.id = id;
    }

    /**
     * Returns the ID of the Customer the has made the Review.
     * @return The ID of the Customer the has made the Review.
     */
    public Long getCustomerID() {
        return customerID;
    }

    /**
     * Sets the ID of the Customer the has made the Review.
     * @param customerID The new ID of the Customer the has made the Review.
     */
    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    /**
     * Returns the ID of the Restaurant that the Review is for.
     * @return The ID of the Restaurant that the Review is for.
     */
    public Long getRestaurantID() {
        return restaurantID;
    }

    /**
     * Sets the ID of the Restaurant that the Review is for.
     * @param restaurantID The new ID of the Restaurant that the Review is for.
     */
    public void setRestaurantID(Long restaurantID) {
        this.restaurantID = restaurantID;
    }

    /**
     * Returns the date that the Review was made.
     * @return The date that the Review was made.
     */
    public Date getDateReviewed() {
        return dateReviewed;
    }

    /**
     * Sets the date that the Review was made.
     * @param dateReviewed The new date that the Review was made.
     */
    public void setDateReviewed(Date dateReviewed) {
        this.dateReviewed = dateReviewed;
    }

    /**
     * Returns the contents of the Review.
     * @return The contents of the Review.
     */
    public String getReview() {
        return review;
    }

    /**
     * Sets the contents of the Review.
     * @param review The new contents of the Review.
     */
    public void setReview(String review) {
        this.review = review;
    }

    /**
     * Returns the rating of the Review.
     * @return The rating of the Review.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the rating of the Review.
     * @param rating The new rating of the Review.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Returns the human readable version of the Review.
     * @return The human readable string of the Review.
     */
    public String toString() {
        String str = "";
        str += String.format("ID: %16s", this.id);
        str += "    ";
        str += String.format("Customer ID: %16s", this.customerID);
        str += "    ";
        str += String.format("Restaurant ID: %16s", this.restaurantID);
        str += "    ";
        if (this.dateReviewed != null) {
            str += String.format("Date Reviewed: %19s",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.dateReviewed));
        } else {
            str += String.format("Date Reviewed: %19s", (Object) null);
        }
        str += "    ";
        str += String.format("Rating: %1d", this.rating);
        str += "\n";
        str += String.format("Review: %s", this.review);
        str += "\n";
        return str;
    }

}
