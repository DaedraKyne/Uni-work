package uk.ac.warwick.cs126.interfaces;

import java.util.Date;

public interface IFavourite {

    Long getID();
    Long getCustomerID();
    Long getRestaurantID();
    Date getDateFavourited();

    void setID(Long id);
    void setCustomerID(Long customerID);
    void setRestaurantID(Long restaurantID);
    void setDateFavourited(Date dateFavourited);

}
