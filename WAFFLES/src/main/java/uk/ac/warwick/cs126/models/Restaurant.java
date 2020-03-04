package uk.ac.warwick.cs126.models;

import uk.ac.warwick.cs126.interfaces.IRestaurant;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class to store properties for a Restaurant.
 *
 * @author jamesvh (James Van Hinsbergh) -
 * <a href="mailto:J.van-Hinsbergh@warwick.ac.uk">J.van-Hinsbergh@warwick.ac.uk</a>
 * @author iant (Ian Tu) -
 * <a href="mailto:I.Tu@warwick.ac.uk">I.Tu@warwick.ac.uk</a>
 * @version 1.0
 */
public class Restaurant implements IRestaurant {

    private Long id;
    private String repeatedID;
    private String name;
    private String ownerFirstName;
    private String ownerLastName;
    private Cuisine cuisine;
    private EstablishmentType establishmentType;
    private PriceRange priceRange;
    private Date dateEstablished;
    private float latitude;
    private float longitude;
    private boolean vegetarianOptions;
    private boolean veganOptions;
    private boolean glutenFreeOptions;
    private boolean nutFreeOptions;
    private boolean lactoseFreeOptions;
    private boolean halalOptions;
    private Date lastInspectedDate;
    private int foodInspectionRating;
    private int warwickStars;
    private float customerRating;

    /**
     * Creates a Restaurant object with all properties set on initialisation.
     *
     * @param repeatedID           The repeated ID string of the Restaurant.
     * @param name                 The name of the Restaurant.
     * @param ownerFirstName       The owner of the Restaurants first name.
     * @param ownerLastName        The owner of the Restaurants last name.
     * @param cuisine              The cuisine of the Restaurant.
     * @param establishmentType    The establishment type of the Restaurant.
     * @param priceRange           The price range of the Restaurant.
     * @param dateEstablished      The date that the Restaurant was established.
     * @param latitude             The latitude of the Restaurant.
     * @param longitude            The longitude of the Restaurant.
     * @param vegetarianOptions    The availability of vegetarian options on the menu at the Restaurant.
     * @param veganOptions         The availability of vegan options on the menu at the Restaurant.
     * @param glutenFreeOptions    The availability of gluten free options on the menu at the Restaurant.
     * @param nutFreeOptions       The availability of nut free options on the menu at the Restaurant.
     * @param lactoseFreeOptions   The availability of lactose free options on the menu at the Restaurant.
     * @param halalOptions         The availability of halal options on the menu at the Restaurant.
     * @param lastInspectedDate    The date that the Restaurant was last inspected.
     * @param foodInspectionRating The food inspection rating of the Restaurant.
     * @param warwickStars         The number of Warwick Stars that the Restaurant has.
     * @param customerRating       The average customer rating of the Restaurant.
     */
    public Restaurant(String repeatedID,
                      String name,
                      String ownerFirstName,
                      String ownerLastName,
                      Cuisine cuisine,
                      EstablishmentType establishmentType,
                      PriceRange priceRange,
                      Date dateEstablished,
                      float latitude,
                      float longitude,
                      boolean vegetarianOptions,
                      boolean veganOptions,
                      boolean glutenFreeOptions,
                      boolean nutFreeOptions,
                      boolean lactoseFreeOptions,
                      boolean halalOptions,
                      Date lastInspectedDate,
                      int foodInspectionRating,
                      int warwickStars,
                      float customerRating) {
        this.id = -1L;
        this.repeatedID = repeatedID;
        this.name = name;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.cuisine = cuisine;
        this.establishmentType = establishmentType;
        this.priceRange = priceRange;
        this.dateEstablished = dateEstablished;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vegetarianOptions = vegetarianOptions;
        this.veganOptions = veganOptions;
        this.glutenFreeOptions = glutenFreeOptions;
        this.nutFreeOptions = nutFreeOptions;
        this.lactoseFreeOptions = lactoseFreeOptions;
        this.halalOptions = halalOptions;
        this.lastInspectedDate = lastInspectedDate;
        this.foodInspectionRating = foodInspectionRating;
        this.warwickStars = warwickStars;
        this.customerRating = customerRating;
    }

    /**
     * Creates a Restaurant object with all properties set on initialisation.
     * The customer rating is set to the default of 0.0.
     *
     * @param repeatedID           The repeated ID string of the Restaurant.
     * @param name                 The name of the Restaurant.
     * @param ownerFirstName       The owner of the Restaurants first name.
     * @param ownerLastName        The owner of the Restaurants last name.
     * @param cuisine              The cuisine of the Restaurant.
     * @param establishmentType    The establishment type of the Restaurant.
     * @param priceRange           The price range of the Restaurant.
     * @param dateEstablished      The date that the Restaurant was established.
     * @param latitude             The latitude of the Restaurant.
     * @param longitude            The longitude of the Restaurant.
     * @param vegetarianOptions    The availability of vegetarian options on the menu at the Restaurant.
     * @param veganOptions         The availability of vegan options on the menu at the Restaurant.
     * @param glutenFreeOptions    The availability of gluten free options on the menu at the Restaurant.
     * @param nutFreeOptions       The availability of nut free options on the menu at the Restaurant.
     * @param lactoseFreeOptions   The availability of lactose free options on the menu at the Restaurant.
     * @param halalOptions         The availability of halal options on the menu at the Restaurant.
     * @param lastInspectedDate    The date that the Restaurant was last inspected.
     * @param foodInspectionRating The food inspection rating of the Restaurant.
     * @param warwickStars         The number of Warwick Stars that the Restaurant has.
     */
    public Restaurant(String repeatedID, String name, String ownerFirstName, String ownerLastName, Cuisine cuisine, EstablishmentType establishmentType, PriceRange priceRange, Date dateEstablished, float latitude, float longitude, boolean vegetarianOptions, boolean veganOptions, boolean glutenFreeOptions, boolean nutFreeOptions, boolean lactoseFreeOptions, boolean halalOptions, Date lastInspectedDate, int foodInspectionRating, int warwickStars) {
        this.id = -1L;
        this.repeatedID = repeatedID;
        this.name = name;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.cuisine = cuisine;
        this.establishmentType = establishmentType;
        this.priceRange = priceRange;
        this.dateEstablished = dateEstablished;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vegetarianOptions = vegetarianOptions;
        this.veganOptions = veganOptions;
        this.glutenFreeOptions = glutenFreeOptions;
        this.nutFreeOptions = nutFreeOptions;
        this.lactoseFreeOptions = lactoseFreeOptions;
        this.halalOptions = halalOptions;
        this.lastInspectedDate = lastInspectedDate;
        this.foodInspectionRating = foodInspectionRating;
        this.warwickStars = warwickStars;
        this.customerRating = 0.0f;
    }

    /**
     * Returns the ID of the Restaurant.
     * @return The ID of the Restaurant.
     */
    public Long getID() {
        return id;
    }

    /**
     * Returns the string representation of the ID of the Restaurant.
     * @return The string representation of the ID of the Restaurant.
     */
    public String getStringID() { return String.valueOf(id); }

    /**
     * Returns the repeated IDs of the Restaurant.
     * @return The repeated IDs of the Restaurant.
     */
    public String[] getRepeatedID() {
        if (repeatedID == null) {
            return null;
        }

        String[] repeatedIDSplitArray = repeatedID.split("(?<=\\G.{16})");

        if (repeatedIDSplitArray.length >= 3) {
            return repeatedIDSplitArray;
        } else {
            String[] fullRepeatedIDSplitArray = new String[3];
            for (int i = 0; i < repeatedIDSplitArray.length; i++) {
                fullRepeatedIDSplitArray[i] = repeatedIDSplitArray[i];
            }
            return fullRepeatedIDSplitArray;
        }
    }

    /**
     * Returns the name of the Restaurant.
     * @return The name of the Restaurant.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Restaurant owner's first name.
     * @return The Restaurant owner's first name.
     */
    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    /**
     * Returns the Restaurant owner's last name.
     * @return The Restaurant owner's last name.
     */
    public String getOwnerLastName() {
        return ownerLastName;
    }

    /**
     * Returns the cuisine of the Restaurant.
     * @return The cuisine of the Restaurant.
     */
    public Cuisine getCuisine() {
        return cuisine;
    }

    /**
     * Returns the establishment type of the Restaurant.
     *
     * @return The establishment type of the Restaurant.
     */
    public EstablishmentType getEstablishmentType() {
        return establishmentType;
    }

    /**
     * Returns the price range of the Restaurant.
     * @return The price range of the Restaurant.
     */
    public PriceRange getPriceRange() {
        return priceRange;
    }

    /**
     * Returns the date that the Restaurant was established.
     * @return The date that the Restaurant was established.
     */
    public Date getDateEstablished() {
        return dateEstablished;
    }

    /**
     * Returns the latitude of the Restaurant.
     * @return The latitude of the Restaurant.
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude of the Restaurant.
     * @return The longitude of the Restaurant.
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * Returns the availability of vegetarian options on the menu at the Restaurant.
     * @return The availability of vegetarian options on the menu at the Restaurant.
     */
    public boolean getVegetarianOptions() {
        return vegetarianOptions;
    }

    /**
     * Returns the availability of vegan options on the menu at the Restaurant.
     * @return The availability of vegan options on the menu at the Restaurant.
     */
    public boolean getVeganOptions() {
        return veganOptions;
    }

    /**
     * Returns the availability of gluten free options on the menu at the Restaurant.
     * @return The availability of gluten free options on the menu at the Restaurant.
     */
    public boolean getGlutenFreeOptions() {
        return glutenFreeOptions;
    }

    /**
     * Returns the availability of nut free options on the menu at the Restaurant.
     * @return The availability of nut free options on the menu at the Restaurant.
     */
    public boolean getNutFreeOptions() {
        return nutFreeOptions;
    }

    /**
     * Returns the availability of lactose free options on the menu at the Restaurant.
     * @return The availability of lactose free options on the menu at the Restaurant.
     */
    public boolean getLactoseFreeOptions() {
        return lactoseFreeOptions;
    }

    /**
     * Returns the availability of halal options on the menu at the Restaurant.
     *
     * @return The availability of halal options on the menu at the Restaurant.
     */
    public boolean getHalalOptions() {
        return halalOptions;
    }

    /**
     * Returns the date that the Restaurant was last inspected.
     * @return The date that the Restaurant was last inspected.
     */
    public Date getLastInspectedDate() {
        return lastInspectedDate;
    }

    /**
     * Returns the food inspection rating of the Restaurant.
     * @return The food inspection rating of the Restaurant.
     */
    public int getFoodInspectionRating() {
        return foodInspectionRating;
    }

    /**
     * Returns the number of Warwick Stars that the Restaurant has.
     * @return The number of Warwick Stars that the Restaurant has.
     */
    public int getWarwickStars() {
        return warwickStars;
    }

    /**
     * Returns the average customer rating of the Restaurant.
     * @return The average customer rating of the Restaurant.
     */
    public float getCustomerRating() {
        return customerRating;
    }

    /**
     * Sets the ID of the Restaurant.
     * @param id The new ID to assign to the Restaurant.
     */
    public void setID(Long id) {
        this.id = id;
    }

    /**
     * Sets the repeated ID of the Restaurant.
     * @param repeatedID The new repreated ID to assign to the Restaurant.
     */
    public void setRepeatedID(String repeatedID) {
        this.repeatedID = repeatedID;
    }

    /**
     * Sets the name of the Restaurant.
     * @param name The new name to assign to the Restaurant.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the Restaurant owner's first name.
     * @param ownerFirstName The new First Name to assign to the Restaurant owner.
     */
    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    /**
     * Sets the Restaurant owner's last name.
     * @param ownerLastName The new Last Name to assign to the Restaurant owner.
     */
    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }

    /**
     * Sets the cuisine of the Restaurant.
     * @param cuisine The new cuisine to assign to the Restaurant.
     */
    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    /**
     * Sets the establishment type of the Restaurant.
     * @param establishmentType The new establishment type to assign to the Restaurant.
     */
    public void setEstablishmentType(EstablishmentType establishmentType) {
        this.establishmentType = establishmentType;
    }

    /**
     * Sets the price range of the Restaurant.
     * @param priceRange The new price range to assign to the Restaurant.
     */
    public void setPriceRange(PriceRange priceRange) {
        this.priceRange = priceRange;
    }

    /**
     * Sets the date that the Restaurant was established.
     * @param dateEstablished The new date that the Restaurant was established.
     */
    public void setDateEstablished(Date dateEstablished) {
        this.dateEstablished = dateEstablished;
    }

    /**
     * Sets the latitude of the Restaurant.
     * @param latitude The new latitude to assign to the Restaurant.
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * Sets the longitude of the Restaurant.
     * @param longitude The new longitude to assign to the Restaurant.
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * Sets the availability of vegetarian options on the menu at the Restaurant.
     * @param vegetarianOptions The new availability of vegetarian options on the menu at the Restaurant.
     */
    public void setVegetarianOptions(boolean vegetarianOptions) {
        this.vegetarianOptions = vegetarianOptions;
    }

    /**
     * Sets the availability of vegan options on the menu at the Restaurant.
     * @param veganOptions The new availability of vegan options on the menu at the Restaurant.
     */
    public void setVeganOptions(boolean veganOptions) {
        this.veganOptions = veganOptions;
    }

    /**
     * Sets the availability of gluten free options on the menu at the Restaurant.
     * @param glutenFreeOptions The new availability of gluten free options on the menu at the Restaurant.
     */
    public void setGlutenFreeOptions(boolean glutenFreeOptions) {
        this.glutenFreeOptions = glutenFreeOptions;
    }

    /**
     * Sets the availability of nut free options on the menu at the Restaurant.
     * @param nutFreeOptions The new availability of nut free options on the menu at the Restaurant.
     */
    public void setNutFreeOptions(boolean nutFreeOptions) {
        this.nutFreeOptions = nutFreeOptions;
    }

    /**
     * Sets the availability of lactose free options on the menu at the Restaurant.
     * @param lactoseFreeOptions The new availability of lactose free options on the menu at the Restaurant.
     */
    public void setLactoseFreeOptions(boolean lactoseFreeOptions) {
        this.lactoseFreeOptions = lactoseFreeOptions;
    }

    /**
     * Sets the availability of halal options on the menu at the Restaurant.
     * @param halalOptions The new availability of halal options on the menu at the Restaurant.
     */
    public void setHalalOptions(boolean halalOptions) {
        this.halalOptions = halalOptions;
    }

    /**
     * Sets the date that the Restaurant was last inspected.
     * @param lastInspectedDate The new date that the Restaurant was last inspected.
     */
    public void setLastInspectedDate(Date lastInspectedDate) {
        this.lastInspectedDate = lastInspectedDate;
    }

    /**
     * Sets the food inspection rating of the Restaurant.
     * @param foodInspectionRating The new food inspection rating of the Restaurant.
     */
    public void setFoodInspectionRating(int foodInspectionRating) {
        this.foodInspectionRating = foodInspectionRating;
    }

    /**
     * Sets the number of Warwick Stars that the Restaurant has.
     * @param warwickStars The new number of Warwick Stars that the Restaurant has.
     */
    public void setWarwickStars(int warwickStars) {
        this.warwickStars = warwickStars;
    }

    /**
     * Sets the average customer rating of the Restaurant.
     * @param customerRating The new average customer rating of the Restaurant.
     */
    public void setCustomerRating(float customerRating) {
        this.customerRating = customerRating;
    }

    /**
     * Returns the human readable version of the Restaurant.
     * @return The human readable string of the Restaurant.
     */
    public String toString() {
        String str = "";
        str += String.format("ID: %16s", this.id);
        str += "    ";
        str += String.format("RID: %48s", this.repeatedID);
        str += "    ";
        str += String.format("Name: %s", this.name);
        str += "\n";
        str += String.format("Owner Name: %s", this.ownerFirstName + " " + this.ownerLastName);
        str += "    ";
        str += String.format("Cuisine: %s", this.cuisine);
        str += "    ";
        str += String.format("Establishment Type: %s", this.establishmentType);
        str += "    ";
        str += String.format("Price Range: %s", this.priceRange);
        str += "\n";
        if (this.dateEstablished != null) {
            str += String.format("Date Established: %-19s",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.dateEstablished));
        } else {
            str += String.format("Date Established: %-19s", (Object) null);
        }
        str += "    ";
        str += String.format("Latitude: %8.5f", this.latitude);
        str += "    ";
        str += String.format("Longitude: %9.6f", this.longitude);
        str += "\n";
        str += String.format("Vegetarian: %s", this.vegetarianOptions);
        str += "    ";
        str += String.format("Vegan: %s", this.veganOptions);
        str += "    ";
        str += String.format("Gluten-Free: %s", this.glutenFreeOptions);
        str += "    ";
        str += String.format("Nut-Free: %s", this.nutFreeOptions);
        str += "    ";
        str += String.format("Lactose-Free: %s", this.lactoseFreeOptions);
        str += "    ";
        str += String.format("Halal-Free: %s", this.halalOptions);
        str += "\n";
        if (this.lastInspectedDate != null) {
            str += String.format("Last Inspected Date: %-19s",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.lastInspectedDate));
        } else {
            str += String.format("Last Inspected Date: %-19s", (Object) null);
        }
        str += "    ";
        str += String.format("Food Inspection Rating: %1d", this.foodInspectionRating);
        str += "    ";
        str += String.format("Warwick Stars: %1d", this.warwickStars);
        str += "    ";
        str += String.format("Customer Rating: %3.1f", this.customerRating);
        str += "\n";
        return str;
    }
}
