package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IRestaurantStore;
import uk.ac.warwick.cs126.models.Cuisine;
import uk.ac.warwick.cs126.models.EstablishmentType;
import uk.ac.warwick.cs126.models.Place;
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

public class RestaurantStore implements IRestaurantStore {

    private DataChecker dataChecker;
    private ConvertToPlace convertToPlace;
    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Restaurant> restaurantsByID;
    private MyAvlTree<String, String, MyArrayList<String>, MyArrayList<String>, MyArrayList<String>, Restaurant> restaurantsByName;
    private MyAvlTree<Long, String, MyArrayList<String>, MyArrayList<Long>, MyArrayList<String>, Restaurant> restaurantsByDateEstablished;
    private MyAvlTree<Long, String, MyArrayList<String>, MyArrayList<Long>, MyArrayList<String>, Restaurant> restaurantsByWarwickStars;
    private MyAvlTree<Double, String, MyArrayList<String>, MyArrayList<Double>, MyArrayList<String>, Restaurant> restaurantsByRating;
    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Boolean> idBlacklist;

    public RestaurantStore() {
        // Initialise variables here
        dataChecker = new DataChecker();
        convertToPlace = new ConvertToPlace();
        restaurantsByID = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Restaurant>();
        restaurantsByName = new MyAvlTree<String, String, MyArrayList<String>, MyArrayList<String>, MyArrayList<String>, Restaurant>();
        restaurantsByDateEstablished = new MyAvlTree<Long, String, MyArrayList<String>, MyArrayList<Long>, MyArrayList<String>, Restaurant>();
        restaurantsByWarwickStars = new MyAvlTree<Long, String, MyArrayList<String>, MyArrayList<Long>, MyArrayList<String>, Restaurant>();
        restaurantsByRating = new MyAvlTree<Double, String, MyArrayList<String>, MyArrayList<Double>, MyArrayList<String>, Restaurant>();
        idBlacklist = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Boolean>();
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

    public boolean addRestaurant(Restaurant restaurant) {
        // TODO
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

    public boolean addRestaurant(Restaurant[] restaurants) {
        // TODO
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

    public Restaurant getRestaurant(Long id) {
        // TODO
        if (id != null) {
            return restaurantsByID.getData(id, null, null, null);
        }
        return null;
    }

    public Restaurant[] getRestaurants() {
        // TODO
        Restaurant[] restaurants = new Restaurant[restaurantsByID.size()];
        int i = 0;
        for (Restaurant restaurant : restaurantsByID) {
            restaurants[i] = restaurant;
            i++;
        }
        return restaurants;
    }

    public Restaurant[] getRestaurants(Restaurant[] restaurants) {
        // TODO
        if (restaurants != null) {
            MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Restaurant> custom_tree = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Restaurant>();
            for (Restaurant restaurant : restaurants) {
                if (dataChecker.isValid(restaurant)) {
                    Long trueID = dataChecker.extractTrueID(restaurant.getRepeatedID());
                    custom_tree.add(trueID, null, null, null, restaurant);
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

    public Restaurant[] getRestaurantsByName() {
        // TODO
        Restaurant[] restaurants = new Restaurant[restaurantsByName.size()];
        int i = 0;
        for (Restaurant restaurant : restaurantsByName) {
            restaurants[i] = restaurant;
            i++;
        }
        return restaurants;
    }

    public Restaurant[] getRestaurantsByDateEstablished() {
        // TODO
        Restaurant[] restaurants = new Restaurant[restaurantsByDateEstablished.size()];
        int i = 0;
        for (Restaurant restaurant : restaurantsByDateEstablished) {
            restaurants[i] = restaurant;
            i++;
        }
        return restaurants;
    }

    public Restaurant[] getRestaurantsByDateEstablished(Restaurant[] restaurants) {
        // TODO
        if (restaurants != null) {
            MyAvlTree<Long, String, MyArrayList<String>, MyArrayList<Long>, MyArrayList<String>, Restaurant> custom_tree = new MyAvlTree<Long, String, MyArrayList<String>, MyArrayList<Long>, MyArrayList<String>, Restaurant>();
            for (Restaurant restaurant : restaurants) {
                if (dataChecker.isValid(restaurant)) {
                    Long trueID = dataChecker.extractTrueID(restaurant.getRepeatedID());
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

    public Restaurant[] getRestaurantsByWarwickStars() {
        // TODO
        Restaurant[] restaurants = new Restaurant[restaurantsByWarwickStars.size()];
        int i = 0;
        for (Restaurant restaurant : restaurantsByWarwickStars) {
            restaurants[i] = restaurant;
            i++;
        }
        return restaurants;
    }

    public Restaurant[] getRestaurantsByRating(Restaurant[] restaurants) {
        // TODO
        Restaurant[] restaurants_list = new Restaurant[restaurantsByRating.size()];
        int i = 0;
        for (Restaurant restaurant : restaurantsByRating) {
            restaurants_list[i] = restaurant;
            i++;
        }
        return restaurants_list;
    }

    public RestaurantDistance[] getRestaurantsByDistanceFrom(float latitude, float longitude) {
        // TODO
        MyAvlTree<Double, Long, MyArrayList<Long>, MyArrayList<Double>, MyArrayList<Long>, RestaurantDistance> restaurantDistanceTree = new MyAvlTree<Double, Long, MyArrayList<Long>, MyArrayList<Double>, MyArrayList<Long>, RestaurantDistance>();
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

    public RestaurantDistance[] getRestaurantsByDistanceFrom(Restaurant[] restaurants, float latitude, float longitude) {
        // TODO
        if (restaurants != null) {
            MyAvlTree<Double, Long, MyArrayList<Long>, MyArrayList<Double>, MyArrayList<Long>, RestaurantDistance> restaurantDistanceTree = new MyAvlTree<Double, Long, MyArrayList<Long>, MyArrayList<Double>, MyArrayList<Long>, RestaurantDistance>();
            RestaurantDistance[] restaurantDistances = new RestaurantDistance[restaurantsByID.size()];
            float distance;
            Long ID;
            MyArrayList<Long> idList;
            RestaurantDistance restaurantDistance;
            for (Restaurant restaurant : restaurants) {
                if (!dataChecker.isValid(restaurant)) {
                    return new RestaurantDistance[0];                    
                }
                ID = dataChecker.extractTrueID(restaurant.getRepeatedID());
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

    public Restaurant[] getRestaurantsContaining(String searchTerm) {
        // TODO
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
            for (int i2 = 0; i2 < name.length() - searchTermConverted.length(); i2++) {
                if (name.substring(i2, i2 + searchTermConverted.length()).equals(searchTermConverted)) {
                    restaurantMatches[found++] = restaurant;
                    match = true;
                    break; //Once a match has been found for a given restaurant, move on to next restaurant
                }
            }
            if (match) {break;} //Once a match has been found for a given restaurant, move on to next restaurant
            cuisine_name = restaurant.getCuisine().name();
            cuisine_name = StringFormatter.convertAccents(cuisine_name);
            cuisine_name = cuisine_name.toLowerCase();
            for (int i2 = 0; i2 < cuisine_name.length() - searchTermConverted.length(); i2++) {
                if (cuisine_name.substring(i2, i2 + searchTermConverted.length()).equals(searchTermConverted)) {
                    restaurantMatches[found++] = restaurant;
                    match = true;
                    break; //Once a match has been found for a given restaurant, move on to next restaurant
                }
            }
            if (match) {break;} //Once a match has been found for a given restaurant, move on to next restaurant
            place_name = convertToPlace.convert(restaurant.getLatitude(), restaurant.getLongitude()).getName();
            place_name = StringFormatter.convertAccents(place_name);
            place_name = place_name.toLowerCase();
            for (int i2 = 0; i2 < place_name.length() - searchTermConverted.length(); i2++) {
                if (place_name.substring(i2, i2 + searchTermConverted.length()).equals(searchTermConverted)) {
                    restaurantMatches[found++] = restaurant;
                    match = true;
                    break; //Once a match has been found for a given restaurant, move on to next restaurant
                }
            }

        }
        return new Restaurant[0];
    }

    // ADDED FUNCTIONS

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
            restaurantsByDateEstablished.add(-warwickStars, nameList, idList, null, restaurant);
        }
        MyArrayList<Double> idListD = new MyArrayList<Double>();
        idListD.add((double) trueID);
        Double rating = (double) restaurant.getCustomerRating();
        restaurantsByRating.add(rating, nameList, idListD, null, restaurant);
    }
    
    private void removeAll(Restaurant restaurant, Long trueID) {
        restaurantsByID.remove(trueID, null, null, null);
        String name = restaurant.getName().toLowerCase();
        restaurantsByName.remove(name, null, null, null);
        Long date = restaurant.getDateEstablished().getTime();
        MyArrayList<String> nameList = new MyArrayList<String>();
        MyArrayList<Long> idList = new MyArrayList<Long>();
        nameList.add(name);
        idList.add(trueID);
        restaurantsByDateEstablished.remove(date, nameList, idList, null);
        Long warwickStars = (long) restaurant.getWarwickStars();
        if (restaurant.getWarwickStars() > 1) {
            restaurantsByDateEstablished.remove(-warwickStars, nameList, idList, null);
        }
        MyArrayList<Double> idListD = new MyArrayList<Double>();
        idListD.add((double) trueID);
        Double rating = (double) restaurant.getCustomerRating();
        restaurantsByRating.remove(rating, nameList, idListD, null);
    }

}
