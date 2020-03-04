package uk.ac.warwick.cs126.interfaces;

import java.util.Date;

public interface IReview {

    Long getID();
    Long getCustomerID();
    Long getRestaurantID();
    Date getDateReviewed();
    String getReview();
    int getRating();

    void setID(Long id);
    void setCustomerID(Long customerID);
    void setRestaurantID(Long restaurantID);
    void setDateReviewed(Date dateReviewed);
    void setReview(String review);
    void setRating(int rating);

}
