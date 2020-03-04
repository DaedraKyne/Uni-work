package uk.ac.warwick.cs126.interfaces;

import uk.ac.warwick.cs126.models.Cuisine;
import uk.ac.warwick.cs126.models.EstablishmentType;
import uk.ac.warwick.cs126.models.PriceRange;

import java.util.Date;

public interface IRestaurant {

    Long getID();
    String[] getRepeatedID();
    String getName();
    String getOwnerFirstName();
    String getOwnerLastName();
    Cuisine getCuisine();
    EstablishmentType getEstablishmentType();
    PriceRange getPriceRange();
    Date getDateEstablished();
    float getLatitude();
    float getLongitude();
    boolean getVegetarianOptions();
    boolean getVeganOptions();
    boolean getGlutenFreeOptions();
    boolean getNutFreeOptions();
    boolean getLactoseFreeOptions();
    boolean getHalalOptions();
    Date getLastInspectedDate();
    int getFoodInspectionRating();
    int getWarwickStars();
    float getCustomerRating();

}
