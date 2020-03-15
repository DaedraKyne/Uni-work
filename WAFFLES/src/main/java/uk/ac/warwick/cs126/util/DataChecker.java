package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IDataChecker;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Review;
import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyAvlTree;

import java.util.Date;

public class DataChecker implements IDataChecker {

    public DataChecker() {
        // Initialise things here
    }

    public Long extractTrueID(String[] repeatedID) {
        // DONE
        if (repeatedID.length != 3) {
            return null;
        }
        Long id1 = Long.valueOf(repeatedID[0]);
        Long id2 = Long.valueOf(repeatedID[1]);
        Long id3 = Long.valueOf(repeatedID[2]);
        if (id1.equals(id2) || id1.equals(id3)) {
            return id1;
        }
        else if (id2.equals(id3)) {
            return id2;
        }
        return null;
    }

    public boolean isValid(Long inputID) {
        // DONE
        if (inputID == null) {
            return false;
        }
        if (inputID.toString().length() != 16) {
            System.out.println("Id is not long enough, length: " + inputID.toString().length() + ", id: " + inputID);
            return false;
        }
        MyAvlTree<Integer, Integer, MyArrayList<Integer>, MyArrayList<Integer>, MyArrayList<Integer>, Integer> tree = new MyAvlTree<Integer, Integer, MyArrayList<Integer>, MyArrayList<Integer>, MyArrayList<Integer>, Integer>();
        char[] id = Long.toString(inputID).toCharArray();
        Integer data;
        int integer;
        for (char c : id) {
            try {
                integer = Integer.parseInt(""+c);
            } catch (NumberFormatException e) {
                return false;
            }
            if ((data = tree.getData(integer, null, null, null)) != null) {
                if (data >= 3) {
                    return false;
                }
                tree.setData(integer, null, null, null, data + 1);
            } else {
                tree.add(integer, null, null, null, 1);
            }
        }
        return true;
    }

    public boolean isValid(Customer customer) {
        // DONE
        if (customer == null) {
            return false;
        }
        if (   customer.getDateJoined() == null
            || customer.getFirstName() == null
            || customer.getLastName() == null
            || customer.getStringID() == null
        ) {
                return false;
        }
        if (!isValid(customer.getID())) {
            return false;
        }
        return true;
    }

    public boolean isValid(Restaurant restaurant) {
        // DONE
        if (restaurant == null) {
            return false;
        }
        if (   restaurant.getCuisine() == null
            || restaurant.getDateEstablished() == null
            || restaurant.getEstablishmentType() == null
            || restaurant.getLastInspectedDate() == null
            || restaurant.getName() == null
            || restaurant.getOwnerFirstName() == null
            || restaurant.getOwnerLastName() == null
            || restaurant.getPriceRange() == null
            || restaurant.getRepeatedID() == null
            || restaurant.getStringID() == null
        ) {
            return false;
        }
        if (!isValid(restaurant.getID())) {
            return false;
        }
        if (!restaurant.getLastInspectedDate()
              .after(restaurant.getDateEstablished())
        ) {
            return false;
        }
        if (   restaurant.getFoodInspectionRating() < 0
            || restaurant.getFoodInspectionRating() > 5
        ) {
            return false;
            }
        if (   restaurant.getWarwickStars() < 0
            || restaurant.getWarwickStars() > 3
        ) {
            return false;
            }
        if (   (restaurant.getCustomerRating() != 0.0f
            && restaurant.getCustomerRating() < 1.0f)
            || restaurant.getCustomerRating() > 5.0f
        ) {
            return false;
            }
        return true;
    }

    public boolean isValid(Favourite favourite) {
        // DONE
        if (favourite == null) {
            return false;
        }
        if (favourite.getDateFavourited() == null) {
            return false;
        }
        if (   !isValid(favourite.getCustomerID())
            || !isValid(favourite.getID())
            || !isValid(favourite.getRestaurantID())
        ) {
            return false;
        }
        return true;
    }

    public boolean isValid(Review review) {
        // DONE
        if (review == null) {
            return false;
        }
        if (   review.getDateReviewed() == null
            || review.getReview() == null
            || review.getStringID() == null
        ) {
            return false;
        }
        if (   !isValid(review.getID())
            || !isValid(review.getCustomerID())
            || !isValid(review.getRestaurantID())
        ) {
            return false;
        }
        return true;
    }
}