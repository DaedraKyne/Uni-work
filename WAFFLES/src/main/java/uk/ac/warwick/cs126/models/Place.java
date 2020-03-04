package uk.ac.warwick.cs126.models;

import uk.ac.warwick.cs126.interfaces.IPlace;

/**
 * Class to store properties for a Place.
 * This stores a linking between latitude and longitude to a name and postcode.
 * @author jamesvh (James Van Hinsbergh) -
 * <a href="mailto:J.van-Hinsbergh@warwick.ac.uk">J.van-Hinsbergh@warwick.ac.uk</a>
 * @author iant (Ian Tu) -
 * <a href="mailto:I.Tu@warwick.ac.uk">I.Tu@warwick.ac.uk</a>
 * @version 1.0
 */
public class Place implements IPlace {

    private String name;
    private String postcode;
    private float latitude;
    private float longitude;

    /**
     * Creates a Place object with all properties set on initialisation.
     * @param name The name of the Place.
     * @param postcode The postcode of the Place.
     * @param latitude The latitude of the Place.
     * @param longitude The longitude of the Place.
     */
    public Place(String name, String postcode, float latitude, float longitude) {
        this.name = name;
        this.postcode = postcode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the name of the Place.
     * @return The name of the Place.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the Place.
     * @param name The new name to assign to the Place.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the postcode of the Place.
     * @return The postcode of the Place.
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * Sets the postcode of the Place.
     * @param postcode The new postcode to assign to the Place.
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * Returns the latitude of the Place.
     * @return The latitude of the Place.
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the Place.
     * @param latitude The new latitude to assign to the Place.
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the longitude of the Place.
     * @return The longitude of the Place.
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the Place.
     * @param longitude The new longitude to assign to the Place.
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the human readable version of the Place.
     * @return The human readable string of the Place.
     */
    public String toString() {
        String str = "";
        str += String.format("Postcode: %-8s", this.postcode);
        str += "    ";
        str += String.format("Latitude: %8.5f", this.latitude);
        str += "    ";
        str += String.format("Longitude: %9.6f", this.longitude);
        str += "    ";
        str += String.format("Name: %s", this.name);
        return str;
    }

}
