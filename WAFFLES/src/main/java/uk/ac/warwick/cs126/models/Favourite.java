package uk.ac.warwick.cs126.models;

import uk.ac.warwick.cs126.interfaces.IFavourite;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class to store properties for a Favourite.
 * @author jamesvh (James Van Hinsbergh) -
 * <a href="mailto:J.van-Hinsbergh@warwick.ac.uk">J.van-Hinsbergh@warwick.ac.uk</a>
 * @author iant (Ian Tu) -
 * <a href="mailto:I.Tu@warwick.ac.uk">I.Tu@warwick.ac.uk</a>
 * @version 1.0
 */
public class Favourite implements IFavourite {

    private Long id;
    private Long customerID;
    private Long restaurantID;
    private Date dateFavourited;

    /**
     * Creates a Favourite object with all properties set on initialisation.
     * @param id The ID of the Favourite.
     * @param customerID The ID of the Customer the has made the Favourite.
     * @param restaurantID The ID of the Restaurant that the Favourite is for.
     * @param dateFavourited The date that the Favourite was made.
     */
    public Favourite(Long id, Long customerID, Long restaurantID, Date dateFavourited) {
        this.id = id;
        this.customerID = customerID;
        this.restaurantID = restaurantID;
        this.dateFavourited = dateFavourited;
    }

    /**
     * Returns the ID of the Favourite.
     * @return The ID of the Favourite.
     */
    public Long getID() {
        return id;
    }

    /**
     * Sets the ID of the Favourite.
     * @param id The new ID to assign to the Favourite.
     */
    public void setID(Long id) {
        this.id = id;
    }

    /**
     * Returns the ID of the Customer the has made the Favourite.
     * @return The ID of the Customer the has made the Favourite.
     */
    public Long getCustomerID() {
        return customerID;
    }

    /**
     * Sets the ID of the Customer the has made the Favourite.
     * @param customerID The new ID of the Customer the has made the Favourite.
     */
    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    /**
     * Returns the ID of the Restaurant that the Favourite is for.
     * @return The ID of the Restaurant that the Favourite is for.
     */
    public Long getRestaurantID() {
        return restaurantID;
    }

    /**
     * Sets the ID of the Restaurant that the Favourite is for.
     * @param restaurantID The new ID of the Restaurant that the Favourite is for.
     */
    public void setRestaurantID(Long restaurantID) {
        this.restaurantID = restaurantID;
    }

    /**
     * Returns the date that the Favourite was made.
     * @return The date that the Favourite was made.
     */
    public Date getDateFavourited() {
        return dateFavourited;
    }

    /**
     * Sets the date that the Favourite was made.
     * @param dateFavourited The new date that the Favourite was made.
     */
    public void setDateFavourited(Date dateFavourited) {
        this.dateFavourited = dateFavourited;
    }


    /**
     * Returns the human readable version of the Favourite.
     * @return The human readable string of the Favourite.
     */
    public String toString() {
        String str = "";
        str += String.format("ID: %16s", this.id);
        str += "    ";
        str += String.format("Customer ID: %16s", this.customerID);
        str += "    ";
        str += String.format("Restaurant ID: %16s", this.restaurantID);
        str += "    ";
        if (this.dateFavourited != null) {
            str += String.format("Date Favourited: %19s",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.dateFavourited));
        } else {
            str += String.format("Date Favourited: %19s", (Object) null);
        }
        return str;
    }
}
