package uk.ac.warwick.cs126.models;

import uk.ac.warwick.cs126.interfaces.ICustomer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class to store properties for a Customer.
 * @author jamesvh (James Van Hinsbergh) -
 * <a href="mailto:J.van-Hinsbergh@warwick.ac.uk">J.van-Hinsbergh@warwick.ac.uk</a>
 * @author iant (Ian Tu) -
 * <a href="mailto:I.Tu@warwick.ac.uk">I.Tu@warwick.ac.uk</a>
 * @version 1.0
 */
public class Customer implements ICustomer {

    private Long id;
    private String firstName;
    private String lastName;
    private Date dateJoined;
    private float latitude;
    private float longitude;

    /**
     * Creates a Customer object with all properties set on initialisation.
     * @param id The ID of the Customer.
     * @param firstName The First Name of the Customer.
     * @param lastName The Last Name of the Customer.
     * @param dateJoined The date that the Customer joined.
     * @param latitude The latitude of the Customers home location.
     * @param longitude The longitude of the Customers home location.
     */
    public Customer(Long id, String firstName, String lastName, Date dateJoined, float latitude, float longitude) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateJoined = dateJoined;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the ID of the Customer.
     * @return The ID of the Customer.
     */
    public Long getID() {
        return id;
    }

    /**
     * Returns the string representation of the ID of the Customer.
     * @return The string representation of the ID of the Customer.
     */
    public String getStringID() { return String.valueOf(id); }

    /**
     * Sets the ID of the Customer.
     * @param id The new ID to assign to the Customer.
     */
    public void setID(Long id) {
        this.id = id;
    }

    /**
     * Returns the First Name of the Customer.
     * @return The First Name of the Customer.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the First Name of the Customer.
     * @param firstName The new First Name to assign to the Customer.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the Last Name of the Customer.
     * @return The Last Name of the Customer.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the Last Name of the Customer.
     * @param lastName The new Last Name to assign to the Customer.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the date that the Customer joined.
     * @return The date that the Customer joined.
     */
    public Date getDateJoined() {
        return dateJoined;
    }

    /**
     * Sets the date that the Customer joined.
     * @param dateJoined The new date that the Customer joined.
     */
    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    /**
     * Returns the latitude of the Customers home location.
     * @return The latitude of the Customers home location.
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the Customers home location.
     * @param latitude The new latitude of the Customers home location.
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the longitude of the Customers home location.
     * @return The longitude of the Customers home location.
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the Customers home location.
     * @param longitude The new longitude of the Customers home location.
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the human readable version of the Customer.
     * @return The human readable string of the Customer.
     */
    public String toString() {
        String str = "";
        str += String.format("ID: %16s", this.id);
        str += "    ";
        str += String.format("First Name: %-30s", this.firstName);
        str += "    ";
        str += String.format("Last Name: %-30s", this.lastName);
        str += "    ";
        if (this.dateJoined != null) {
            str += String.format("Date Joined: %-19s",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.dateJoined));
        } else {
            str += String.format("Date Joined: %-19s", (Object) null);
        }
        str += "    ";
        str += String.format("Latitude: %8.5f", this.latitude);
        str += "    ";
        str += String.format("Longitude: %9.6f", this.longitude);
        return str;
    }
}
