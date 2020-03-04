package uk.ac.warwick.cs126.interfaces;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.Review;

public interface IDataChecker {
    Long extractTrueID(String[] repeatedID);
    boolean isValid(Long id);
    boolean isValid(Customer customer);
    boolean isValid(Favourite favourite);
    boolean isValid(Restaurant restaurant);
    boolean isValid(Review review);
}
