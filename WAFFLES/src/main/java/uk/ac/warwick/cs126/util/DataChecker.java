package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IDataChecker;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Review;

import java.util.Date;

public class DataChecker implements IDataChecker {

    public DataChecker() {
        // Initialise things here
    }

    public Long extractTrueID(String[] repeatedID) {
        // TODO
        return null;
    }

    public boolean isValid(Long inputID) {
        // TODO
        return false;
    }

    public boolean isValid(Customer customer) {
        // TODO
        return false;
    }

    public boolean isValid(Restaurant restaurant) {
        // TODO
        return false;
    }

    public boolean isValid(Favourite favourite) {
        // TODO
        return false;
    }

    public boolean isValid(Review review) {
        // TODO
        return false;
    }
}