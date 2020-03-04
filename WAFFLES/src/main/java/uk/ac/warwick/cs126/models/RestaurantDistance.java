package uk.ac.warwick.cs126.models;

import uk.ac.warwick.cs126.interfaces.IRestaurantDistance;

/**
 * Class to store properties for the distance to a given Restaurant.
 * @author jamesvh (James Van Hinsbergh) -
 * <a href="mailto:J.van-Hinsbergh@warwick.ac.uk">J.van-Hinsbergh@warwick.ac.uk</a>
 * @author iant (Ian Tu) -
 * <a href="mailto:I.Tu@warwick.ac.uk">I.Tu@warwick.ac.uk</a>
 * @version 1.0
 */
public class RestaurantDistance implements IRestaurantDistance {

    private Restaurant restaurant;
    private float distance;

    /**
     * Creates a RestaurantDistance object with all properties set on initialisation.
     * @param restaurant A Restaurant object.
     * @param distance The distance to the given Restaurant object.
     */
    public RestaurantDistance(Restaurant restaurant, float distance) {
        this.restaurant = restaurant;
        this.distance = distance;
    }

    /**
     * Returns the Restaurant in the RestaurantDistance object.
     * @return The Restaurant in the RestaurantDistance object.
     */
    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Returns the distance to the given Restaurant.
     * @return The distance to the given Restaurant.
     */
    public float getDistance() {
        return distance;
    }

    /**
     * Sets the Restaurant in the RestaurantDistance object.
     * @param restaurant The new Restaurant in the RestaurantDistance object.
     */
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    /**
     * Sets the distance to the given Restaurant.
     * @param distance The new distance to the given Restaurant.
     */
    public void setDistance(float distance) {
        this.distance = distance;
    }

    /**
     * Returns the human readable version of RestaurantDistance.
     * @return The human readable string of RestaurantDistance.
     */
    public String toString() {
        String str = "";
        if (this.restaurant != null) {
            str += String.format("ID: %16s", this.restaurant.getID());
        } else {
            str += String.format("ID: %16s", (Object) null);
        }
        str += "    ";
        str += String.format("Distance: %5s", this.distance);
        str += "    ";
        if (this.restaurant != null) {
            str += String.format("Name: %s", this.restaurant.getName());
        } else {
            str += String.format("Name: %s", (Object) null);
        }
        return str;
    }
}
