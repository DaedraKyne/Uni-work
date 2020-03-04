package uk.ac.warwick.cs126.interfaces;

import uk.ac.warwick.cs126.models.Restaurant;

public interface IRestaurantDistance {

    Restaurant getRestaurant();
    float getDistance();

    void setRestaurant(Restaurant restaurant);
    void setDistance(float distance);

}
