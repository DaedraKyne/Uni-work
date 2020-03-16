package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IRestaurantStore;
import uk.ac.warwick.cs126.models.Cuisine;
import uk.ac.warwick.cs126.models.EstablishmentType;
import uk.ac.warwick.cs126.models.PriceRange;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.RestaurantDistance;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyAvlTree;
import uk.ac.warwick.cs126.util.ConvertToPlace;
import uk.ac.warwick.cs126.util.HaversineDistanceCalculator;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;


/*
 * Main class, in charge of dealing with all restaurant tasks.
 */
public class RestaurantStore implements IRestaurantStore {

    private DataChecker dataChecker;
    private ConvertToPlace convertToPlace;
    private MyAvlTree<Long, Long, Restaurant> restaurantsByID; // AVL tree of restaurants sorted by ID
    private MyAvlTree<String, String, Restaurant> restaurantsByName; // AVL tree of restaurants sorted by name/ID
    private MyAvlTree<Long, String, Restaurant> restaurantsByDateEstablished; // AVL tree of restaurants sorted by date/name/ID
    private MyAvlTree<Long, String, Restaurant> restaurantsByWarwickStars; // AVL tree of restaurants sorted by stars/name/ID
    private MyAvlTree<Double, String, Restaurant> restaurantsByRating; // AVL tree of restaurants sorted by rating/name/ID
    private MyAvlTree<Long, Long, Boolean> idBlacklist; // AVL tree of blacklisted IDs sorted by ID


  /*
   * Constructor class for RestaurantStore.
   * Initializes values.
   */
    public RestaurantStore() {
        // Initialise variables here
        dataChecker = new DataChecker();
        convertToPlace = new ConvertToPlace();
        restaurantsByID = new MyAvlTree<Long, Long, Restaurant>();
        restaurantsByName = new MyAvlTree<String, String, Restaurant>();
        restaurantsByDateEstablished = new MyAvlTree<Long, String, Restaurant>();
        restaurantsByWarwickStars = new MyAvlTree<Long, String, Restaurant>();
        restaurantsByRating = new MyAvlTree<Double, String, Restaurant>();
        idBlacklist = new MyAvlTree<Long, Long, Boolean>();
    }

    public Restaurant[] loadRestaurantDataToArray(InputStream resource) {
        Restaurant[] restaurantArray = new Restaurant[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Restaurant[] loadedRestaurants = new Restaurant[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            String row;
            int restaurantCount = 0;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Restaurant restaurant = new Restaurant(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            Cuisine.valueOf(data[4]),
                            EstablishmentType.valueOf(data[5]),
                            PriceRange.valueOf(data[6]),
                            formatter.parse(data[7]),
                            Float.parseFloat(data[8]),
                            Float.parseFloat(data[9]),
                            Boolean.parseBoolean(data[10]),
                            Boolean.parseBoolean(data[11]),
                            Boolean.parseBoolean(data[12]),
                            Boolean.parseBoolean(data[13]),
                            Boolean.parseBoolean(data[14]),
                            Boolean.parseBoolean(data[15]),
                            formatter.parse(data[16]),
                            Integer.parseInt(data[17]),
                            Integer.parseInt(data[18]));

                    loadedRestaurants[restaurantCount++] = restaurant;
                }
            }
            csvReader.close();

            restaurantArray = loadedRestaurants;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return restaurantArray;
    }


  /*
   * Attempts to add restaurant to the store
   * If restaurant's id is blacklisted, do not add.
   * If there already exists a stored restaurant with that id,
   * remove it and add blacklist the id.
   * Returns true if restaurant was successfully added.
   * 
   * @return         boolean
   */
    public boolean addRestaurant(Restaurant restaurant) {
        // DONE
        if (restaurant != null) {
            Long trueID = dataChecker.extractTrueID(restaurant.getRepeatedID());
            restaurant.setID(trueID);
            if (trueID != null && dataChecker.isValid(restaurant)) {
                if (idBlacklist.contains(trueID, null, null, null)) {
                    return false;
                }
                if (restaurantsByID.contains(trueID, null, null, null)) {
                    removeAll(restaurant, trueID);
                    idBlacklist.add(trueID, null, null, null, true);
                    return false;
                }
                addAll(restaurant, trueID);
                return true;
            }
        }
        return false;
    }


  /*
   * Attempts to add valid Restaurant objects from the restaurants input array to
   * the store.
   * Return true if the all the restaurants are all successfully added to the data
   * store, otherwise false .
   *
   * @return         boolean
   */
    public boolean addRestaurant(Restaurant[] restaurants) {
        // DONE
        if (restaurants != null) {
            boolean fully_added = true;
            for (Restaurant restaurant : restaurants) {
                if (!addRestaurant(restaurant)) {
                    fully_added = false;
                }
            }
            return fully_added;
        }
        return false;
    }


  /*
   * Returns the restaurant with the matching ID id from the store, otherwise
   * return null.
   *
   * @return    restaurant
   */
    public Restaurant getRestaurant(Long id) {
        // DONE
        if (id != null) {
            return restaurantsByID.getData(id, null, null, null);
        }
        return null;
    }


  /*
   * Returns an array of all restaurants in the store, sorted in 
   * ascending order of ID.
   *
   * @return    sorted restaurants
   */
    public Restaurant[] getRestaurants() {
        // DONE
        Restaurant[] restaurants = new Restaurant[restaurantsByID.size()];
        int i = 0;
        for (Restaurant restaurant : restaurantsByID) {
            restaurants[i] = restaurant;
            i++;
        }
        return restaurants;
    }


  /*
   * Returns an array of all valid restaurants from the given array,
   * sorted in ascending order of ID.
   *
   * @param     unsorted restaurants
   * @return    sorted restaurants
   */
    public Restaurant[] getRestaurants(Restaurant[] restaurants) {
        // DONE
        if (restaurants != null) {
            MyAvlTree<Long, Long, Restaurant> custom_tree = new MyAvlTree<Long, Long, Restaurant>();
            for (Restaurant restaurant : restaurants) {
                restaurant.setID(dataChecker.extractTrueID(restaurant.getRepeatedID()));
                if (dataChecker.isValid(restaurant)) {
                    custom_tree.add(restaurant.getID(), null, null, null, restaurant);
                }
            }
            Restaurant[] custom_restaurants = new Restaurant[custom_tree.size()];
            int i = 0;
            for (Restaurant restaurant : custom_tree) {
                custom_restaurants[i] = restaurant;
                i++;
            }
            return custom_restaurants;
        }
        return new Restaurant[0];
    }


  /*
   * Returns an array of all restaurants in the store, sorted alphabetically by name.
   * If they have the same name, then it is sorted in ascending order of ID.
   *
   * @return    sorted restaurants
   */
    public Restaurant[] getRestaurantsByName() {
        // DONE
        Restaurant[] restaurants = new Restaurant[restaurantsByName.size()];
        int i = 0;
        for (Restaurant restaurant : restaurantsByName) {
            restaurants[i] = restaurant;
            i++;
        }
        return restaurants;
    }


  /*
   * Returns an array of all restaurants in the store, sorted by Date Established,
   * from oldest to most recent.
   * If the have the same date, then it is sorted alphabetically by name.
   * If they have the same name, then it is sorted in ascending order of ID.
   *
   * @return    sorted restaurants
   */
    public Restaurant[] getRestaurantsByDateEstablished() {
        // DONE
        Restaurant[] restaurants = new Restaurant[restaurantsByDateEstablished.size()];
        int i = 0;
        for (Restaurant restaurant : restaurantsByDateEstablished) {
            restaurants[i] = restaurant;
            i++;
        }
        return restaurants;
    }


  /*
   * Returns an array of all valid restaurants from the given array, sorted by Date Established,
   * from oldest to most recent.
   * If the have the same date, then it is sorted alphabetically by name.
   * If they have the same name, then it is sorted in ascending order of ID.
   *
   * @param     unsorted restaurants
   * @return    sorted restaurants
   */
    public Restaurant[] getRestaurantsByDateEstablished(Restaurant[] restaurants) {
        // DONE
        if (restaurants != null) {
            MyAvlTree<Long, String, Restaurant> custom_tree = new MyAvlTree<Long, String, Restaurant>();
            long trueID;
            for (Restaurant restaurant : restaurants) {
                restaurant.setID((trueID = dataChecker.extractTrueID(restaurant.getRepeatedID())));
                if (dataChecker.isValid(restaurant)) {
                    String name = restaurant.getName().toLowerCase();
                    Long date = restaurant.getDateEstablished().getTime();
                    MyArrayList<String> nameList = new MyArrayList<String>();
                    MyArrayList<Long> idList = new MyArrayList<Long>();
                    nameList.add(name);
                    idList.add(trueID);
                    custom_tree.add(date, nameList, idList, null, restaurant);            
                }
            }
            Restaurant[] custom_restaurants = new Restaurant[custom_tree.size()];
            int i = 0;
            for (Restaurant restaurant : custom_tree) {
                custom_restaurants[i] = restaurant;
                i++;
            }
            return custom_restaurants;
        }
        return new Restaurant[0];
    }


  /*
   * Returns an array of all the restaurants in the store that have at least 1 Warwick
   * Star, sorted in descending order of Warwick Stars.
   * If they have as many Warwick Stars, then it is sorted alphabetically by name.
   * If they have the same name, then it is sorted in ascending order of ID.
   *
   * @return    sorted restaurants
   */
    public Restaurant[] getRestaurantsByWarwickStars() {
        // DONE
        Restaurant[] restaurants = new Restaurant[restaurantsByWarwickStars.size()];
        int i = 0;
        for (Restaurant restaurant : restaurantsByWarwickStars) {
            restaurants[i] = restaurant;
            i++;
        }
        return restaurants;
    }


  /*
   * Returns an array of all valid restaurants from the given array, sorted by 
   * descending order of Rating.
   * If the have the same Rating, then it is sorted alphabetically by name.
   * If they have the same name, then it is sorted in ascending order of ID.
   * 
   * Note: It wasn't clear whether we should return an array based on
   * the restaurants in store, or based on the restaurant array that 
   * is given in the parameters, but since it was already included
   * in the parameters, I decided to work with the array of restaurants
   * given.
   *
   * @param     unsorted restaurants
   * @return    sorted restaurants
   */
    public Restaurant[] getRestaurantsByRating(Restaurant[] restaurants) {
        // DONE
        if (restaurants != null) {
            MyAvlTree<Double, String, Restaurant> custom_tree = new MyAvlTree<Double, String, Restaurant>();
            for (Restaurant restaurant : restaurants) {
                restaurant.setID(dataChecker.extractTrueID(restaurant.getRepeatedID()));
                if (!dataChecker.isValid(restaurant)) {
                    continue;
                }
                MyArrayList<String> nameList = new MyArrayList<String>();
                nameList.add(restaurant.getName());
                MyArrayList<Double> idListD = new MyArrayList<Double>();
                idListD.add((double) restaurant.getID());
                Double rating = (double) (-1) * restaurant.getCustomerRating();    
                custom_tree.add(rating, nameList, idListD, null, restaurant);
            }
            int i = 0;
            Restaurant[] restaurants_list = new Restaurant[restaurants.length];
            for (Restaurant restaurant : custom_tree) {
                restaurants_list[i] = restaurant;
                i++;
            }
            return restaurants_list;    
        }
        return new Restaurant[0];
    }


  /*
   * Returns an array of RestaurantDistance , that is sorted in ascending order
   * of distance from the input coordinates, lat and lon, the returned array is
   * calculated using all the restaurants in the store.
   * If they have the same distance, then it is sorted in ascending order of ID.
   *
   * @return    sorted restaurants
   */
    public RestaurantDistance[] getRestaurantsByDistanceFrom(float latitude, float longitude) {
        // DONE
        MyAvlTree<Double, Long, RestaurantDistance> restaurantDistanceTree = new MyAvlTree<Double, Long, RestaurantDistance>();
        RestaurantDistance[] restaurantDistances = new RestaurantDistance[restaurantsByID.size()];
        float distance;
        MyArrayList<Long> idList;
        RestaurantDistance restaurantDistance;
        for (Restaurant restaurant : restaurantsByID) {
            distance = HaversineDistanceCalculator.inKilometres(latitude, longitude, restaurant.getLatitude(), restaurant.getLongitude());
            idList = new MyArrayList<Long>();
            idList.add(restaurant.getID());
            restaurantDistance = new RestaurantDistance(restaurant, (float) distance);
            restaurantDistanceTree.add((double)distance, idList, null, null, restaurantDistance);
        }
        int i = 0;
        for (RestaurantDistance restaurantDistance2 : restaurantDistanceTree) {
            restaurantDistances[i] = restaurantDistance2;
            i++;
        }
        return restaurantDistances;
    }


  /*
   * Returns an array of RestaurantDistance , that is sorted in ascending order
   * of distance from the input coordinates, lat and lon,  the returned array is
   * calculated using the given array.
   * If they have the same distance, then it is sorted in ascending order of ID.
   *
   * @param     unsorted restaurants
   * @return    sorted restaurants
   */
    public RestaurantDistance[] getRestaurantsByDistanceFrom(Restaurant[] restaurants, float latitude, float longitude) {
        // DONE
        if (restaurants != null) {
            MyAvlTree<Double, Long, RestaurantDistance> restaurantDistanceTree = new MyAvlTree<Double, Long, RestaurantDistance>();
            RestaurantDistance[] restaurantDistances = new RestaurantDistance[restaurantsByID.size()];
            float distance;
            long ID;
            MyArrayList<Long> idList;
            RestaurantDistance restaurantDistance;
            for (Restaurant restaurant : restaurants) {
                restaurant.setID((ID = dataChecker.extractTrueID(restaurant.getRepeatedID())));
                if (!dataChecker.isValid(restaurant)) {
                    return new RestaurantDistance[0];                    
                }
                restaurant.setID(ID);
                distance = HaversineDistanceCalculator.inKilometres(latitude, longitude, restaurant.getLatitude(), restaurant.getLongitude());
                idList = new MyArrayList<Long>();
                idList.add(ID);
                restaurantDistance = new RestaurantDistance(restaurant, (float) distance);
                restaurantDistanceTree.add((double)distance, idList, null, null, restaurantDistance);
            }
            int i = 0;
            for (RestaurantDistance restaurantDistance2 : restaurantDistanceTree) {
                restaurantDistances[i] = restaurantDistance2;
                i++;
            }
            return restaurantDistances;

        }
        return new RestaurantDistance[0];
    }


  /*
   * Return an array of all the customers from the store whose Name, Cuisine or
   * Place name contain the given query str .
   *
   * @param     searchterm      term that is searched for
   * @return    restaurants
   */
    public Restaurant[] getRestaurantsContaining(String searchTerm) {
        // DONE
        if (searchTerm.isEmpty()) {
            return new Restaurant[0];
        }
        int i = 0;
        while (searchTerm.charAt(i) == ' ') {i++;}
        searchTerm = searchTerm.substring(i);
        i = 1;
        while (searchTerm.charAt(searchTerm.length() - i) == ' ') {i++;}
        searchTerm = searchTerm.substring(0, searchTerm.length() - i + 1);
        for (i = 0; i < searchTerm.length() - 1; i++) {
            if (searchTerm.charAt(i) == ' ' && searchTerm.charAt(i+1) == ' ') {
                searchTerm = searchTerm.substring(0, i) + searchTerm.substring(i+1);
                i -= 1;
            }
        }
        String searchTermConverted = StringFormatter.convertAccentsFaster(searchTerm);
        searchTermConverted = searchTermConverted.toLowerCase();
        Restaurant[] restaurants = getRestaurantsByName();
        Restaurant[] restaurantMatches = new Restaurant[restaurants.length];
        String name, cuisine_name, place_name;
        int found = 0;
        boolean match;
        for (Restaurant restaurant : restaurants) {
            match = false;
            name = restaurant.getName();
            name = StringFormatter.convertAccents(name);
            name = name.toLowerCase();
            for (int i2 = 0; i2 < name.length() - searchTermConverted.length() + 1; i2++) {
                if (name.substring(i2, i2 + searchTermConverted.length()).equals(searchTermConverted)) {
                    restaurantMatches[found++] = restaurant;
                    match = true;
                    break; //Once a match has been found for a given restaurant, move on to next restaurant
                }
            }
            if (match) {continue;} //Once a match has been found for a given restaurant, move on to next restaurant
            cuisine_name = restaurant.getCuisine().name();
            cuisine_name = StringFormatter.convertAccents(cuisine_name);
            cuisine_name = cuisine_name.toLowerCase();
            for (int i2 = 0; i2 < cuisine_name.length() - searchTermConverted.length() + 1; i2++) {
                if (cuisine_name.substring(i2, i2 + searchTermConverted.length()).equals(searchTermConverted)) {
                    restaurantMatches[found++] = restaurant;
                    match = true;
                    break; //Once a match has been found for a given restaurant, move on to next restaurant
                }
            }
            if (match) {continue;} //Once a match has been found for a given restaurant, move on to next restaurant
            place_name = convertToPlace.convert(restaurant.getLatitude(), restaurant.getLongitude()).getName();
            place_name = StringFormatter.convertAccents(place_name);
            place_name = place_name.toLowerCase();
            for (int i2 = 0; i2 < place_name.length() - searchTermConverted.length() + 1; i2++) {
                if (place_name.substring(i2, i2 + searchTermConverted.length()).equals(searchTermConverted)) {
                    restaurantMatches[found++] = restaurant;
                    match = true;
                    break; //Once a match has been found for a given restaurant, move on to next restaurant
                }
            }

        }
        Restaurant[] arr = new Restaurant[found];
        for (i = 0; i < found; i++) {
            arr[i] = restaurantMatches[i];
        }
        return arr;
    }

    // ADDED FUNCTIONS


  /*
   * Adds the given restaurant to all restaurant AVL trees.
   *
   * @param     restaurant
   * @param     trueID
   */
    private void addAll(Restaurant restaurant, Long trueID) {
        restaurantsByID.add(trueID, null, null, null, restaurant);
        String name = restaurant.getName().toLowerCase();
        restaurantsByName.add(name, null, null, null, restaurant);
        Long date = restaurant.getDateEstablished().getTime();
        MyArrayList<String> nameList = new MyArrayList<String>();
        MyArrayList<Long> idList = new MyArrayList<Long>();
        nameList.add(name);
        idList.add(trueID);
        restaurantsByDateEstablished.add(date, nameList, idList, null, restaurant);
        Long warwickStars = (long) restaurant.getWarwickStars();
        if (restaurant.getWarwickStars() >= 1) {
            nameList = new MyArrayList<String>();
            idList = new MyArrayList<Long>();
            nameList.add(name);
            idList.add(trueID);
            restaurantsByWarwickStars.add(-warwickStars, nameList, idList, null, restaurant);
        }
        nameList = new MyArrayList<String>();
        nameList.add(name);
        MyArrayList<Double> idListD = new MyArrayList<Double>();
        idListD.add((double) trueID);
        Double rating = (double) restaurant.getCustomerRating();
        restaurantsByRating.add(rating, nameList, idListD, null, restaurant);
    }
    

  /*
   * Removes the given restaurant from all restaurant AVL trees.
   *
   * @param     restaurant
   * @param     trueID
   */
    private void removeAll(Restaurant restaurant, long trueID) {
        restaurantsByID.remove(trueID, null, null, null);
        String name = restaurant.getName().toLowerCase();
        restaurantsByName.remove(name, null, null, null);
        long date = restaurant.getDateEstablished().getTime();
        MyArrayList<String> nameList = new MyArrayList<String>();
        MyArrayList<Long> idList = new MyArrayList<Long>();
        nameList.add(name);
        idList.add(trueID);
        restaurantsByDateEstablished.remove(date, nameList, idList, null);
        Long warwickStars = (long) restaurant.getWarwickStars();
        if (restaurant.getWarwickStars() > 1) {
            nameList = new MyArrayList<String>();
            idList = new MyArrayList<Long>();
            nameList.add(name);
            idList.add(trueID);    
            restaurantsByWarwickStars.remove(-warwickStars, nameList, idList, null);
        }
        nameList = new MyArrayList<String>();
        nameList.add(name);
        MyArrayList<Double> idListD = new MyArrayList<Double>();
        idListD.add((double) trueID);
        Double rating = (double) restaurant.getCustomerRating();
        restaurantsByRating.remove(rating, nameList, idListD, null);
    }

}
