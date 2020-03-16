package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IFavouriteStore;
import uk.ac.warwick.cs126.models.Favourite;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyAvlTree;
import uk.ac.warwick.cs126.util.DataChecker;

import java.util.Iterator;


/*
 * Main class, in charge of dealing with all favourite tasks.
 */
public class FavouriteStore implements IFavouriteStore {

    private DataChecker dataChecker;
    private MyAvlTree<Long, Long, Favourite> favouritesById; // AVL tree of favourites sorted by ID
    private MyAvlTree<Long, Long, MyAvlTree<Long, Long, Favourite>> favouritesByRestaurantId; // AVL tree of AVL trees of favourites (sorted by ID) for each restaurantID, sorted by restaurant ID
    private MyAvlTree<Long, Long, MyAvlTree<Long, Long, Favourite>> favouritesByCustomerId; // AVL tree of AVL trees of favourites (sorted by ID) for each customerID, sorted by customer ID
    private MyAvlTree<Long, Long, Boolean> idBlacklist; // AVL tree of blacklisted IDs sorted by ID
    private MyAvlTree<Long, Long, MyArrayList<Favourite>> ignoredFavouritesByCustomerId; // AVL tree of lists of blacklisted favourites that can be un-blacklisted, sorted by customerID


  /*
   * Constructor class for FavouriteStore.
   * Initializes values.
   */
    public FavouriteStore() {
        // Initialise variables here
        dataChecker = new DataChecker();
        favouritesById = new MyAvlTree<Long, Long, Favourite>(); 
        favouritesByRestaurantId = new MyAvlTree<Long, Long, MyAvlTree<Long, Long, Favourite>>();
        favouritesByCustomerId = new MyAvlTree<Long, Long, MyAvlTree<Long, Long, Favourite>>();
        idBlacklist = new MyAvlTree<Long, Long, Boolean>();
        ignoredFavouritesByCustomerId = new MyAvlTree<Long, Long, MyArrayList<Favourite>>();

    }

    public Favourite[] loadFavouriteDataToArray(InputStream resource) {
        Favourite[] favouriteArray = new Favourite[0];

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

            Favourite[] loadedFavourites = new Favourite[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int favouriteCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");
                    Favourite favourite = new Favourite(
                            Long.parseLong(data[0]),
                            Long.parseLong(data[1]),
                            Long.parseLong(data[2]),
                            formatter.parse(data[3]));
                    loadedFavourites[favouriteCount++] = favourite;
                }
            }
            csvReader.close();

            favouriteArray = loadedFavourites;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return favouriteArray;
    }


  /*
   * Attempts to add favourite to the store
   * If favourite's id is blacklisted, do not add.
   * If there already exists a stored favourite with that id,
   * remove it and add blacklist the id.
   * 
   * If the favourite is valid and does not have an ID that has been blacklisted, is a
   * duplicate, or is invalid: if there exists a Favourite already inside the store with
   * the same Customer ID and Restaurant ID, and if this favourite is older than
   * the one in the store, you must replace it with this favourite . If this replace
   * happens, the ID of the Favourite originally in the store should be blacklisted
   * from further use.
   *
   * Returns true if favourite was successfully added.
   * 
   * @return         boolean
   */
    public boolean addFavourite(Favourite favourite) {
        // DONE
        if (dataChecker.isValid(favourite)) {
            Favourite duplicate = favouritesById.getData(favourite.getID(), null, null, null);
            Boolean blackListed = idBlacklist.getData(favourite.getID(), null, null, null);
            if (blackListed != null) {
                return false;
            }
            if (duplicate != null) {
                removeAll(duplicate);
                idBlacklist.add(favourite.getID(), null, null, null, true);
                MyArrayList<Favourite> hidden_matches = ignoredFavouritesByCustomerId.getData(duplicate.getCustomerID(), null, null, null);
                Favourite hidden_favourite = null;
                if (hidden_matches != null) {
                    for (int i = 0; i < hidden_matches.size(); i++) {
                        System.out.print("l");
                        if (hidden_matches.get(i).getRestaurantID() == favourite.getRestaurantID()) {
                            hidden_favourite = hidden_matches.get(i);
                            hidden_matches.remove(hidden_favourite);
                            break;
                        }
                    }
                }
                if (hidden_favourite != null) {
                    Boolean hidden_fav_state = idBlacklist.getData(hidden_favourite.getID(), null, null, null);
                    if (hidden_fav_state == false) {
                        addAll(hidden_favourite);
                        ignoredFavouritesByCustomerId.setData(hidden_favourite.getCustomerID(), null, null, null, hidden_matches);
                        idBlacklist.remove(hidden_favourite.getID(), null, null, null);
                        }
                }
                return false;
            }
            Favourite[] sameCustomer = getFavouritesByCustomerID(favourite.getCustomerID());
            Favourite[] sameRestaurant = getFavouritesByRestaurantID(favourite.getRestaurantID());    
            Favourite old_fav = findSimilarFavourite(favourite, sameCustomer, sameRestaurant);
            if (old_fav != null) {
                int date_diff = favourite.getDateFavourited().compareTo(old_fav.getDateFavourited());
                if (date_diff < 0) {
                    removeAll(old_fav);
                    idBlacklist.add(old_fav.getID(), null, null, null, false);
                    MyArrayList<Favourite> hidden_matches = ignoredFavouritesByCustomerId.getData(old_fav.getCustomerID(), null, null, null);
                    if (hidden_matches == null) {
                        hidden_matches = new MyArrayList<Favourite>();
                        hidden_matches.add(old_fav);
                        ignoredFavouritesByCustomerId.add(old_fav.getCustomerID(), null, null, null, hidden_matches);
                    } 
                    else {
                        hidden_matches.add(old_fav);
                        ignoredFavouritesByCustomerId.setData(old_fav.getCustomerID(), null, null, null, hidden_matches);
                    }
                } else {
                    MyArrayList<Favourite> hidden_matches = ignoredFavouritesByCustomerId.getData(favourite.getCustomerID(), null, null, null);
                    if (hidden_matches == null) {
                        hidden_matches = new MyArrayList<Favourite>();
                        hidden_matches.add(favourite);
                        ignoredFavouritesByCustomerId.setData(favourite.getCustomerID(), null, null, null, hidden_matches);
                    }
                    else {
                        hidden_matches.add(favourite);
                        ignoredFavouritesByCustomerId.add(favourite.getCustomerID(), null, null, null, hidden_matches);
                    }
                    return false;
                }
            }
            addAll(favourite);
            return true;
            }
        return false;
    }


  /*
   * Attempts to add valid Favourite objects from the favourites input array to
   * the store.
   * Return true if the all the favourites are all successfully added to the data
   * store, otherwise false .
   *
   * @return         boolean
   */
    public boolean addFavourite(Favourite[] favourites) {
        // DONE
        if (favourites == null) {return false;}
        boolean fully_added = true;
        int i = 0;
        int length = favourites.length;
        for (Favourite favourite : favourites) {
            if (!addFavourite(favourite)) {
                fully_added = false;
            }
            i++;
            if (i % 1000 == 0) {System.out.println((int)Math.floor(((float)i / (float)length) * 100) + "." + (int)Math.floor(((float)i / (float)length) * 1000) % 10 + "%");}
        }
        return fully_added;
    }


  /*
   * Returns the favourite with the matching ID id from the store, otherwise
   * return null.
   *
   * @return    favourite
   */
    public Favourite getFavourite(Long id) {
        // DONE
        if (dataChecker.isValid(id)) {
            return favouritesById.getData(id, null, null, null);
        }
        return null;
    }


  /*
   * Returns an array of all favourites in the store, sorted in 
   * ascending order of ID.
   *
   * @return    sorted favourites
   */
    public Favourite[] getFavourites() {
        // DONE
        Favourite[] fav_array = new Favourite[favouritesById.size()];
        int i = 0;
        for (Favourite favourite : favouritesById) {
            fav_array[i++] = favourite;
        }
        return fav_array;
    }


  /*
   * Return a favourite array with all the favourites from the store
   * that have id for its Customer ID.
   *
   * @return    sorted favourites
   */
    public Favourite[] getFavouritesByCustomerID(Long id) {
        // DONE
        return getSortedArrayByTree(favouritesByCustomerId.getData(id, null, null, null));
    }


  /*
   * Return a favourite array with all the favourites from the store
   * that have id for its restaurant ID.
   *
   * @return    sorted favourites
   */
    public Favourite[] getFavouritesByRestaurantID(Long id) {
        // DONE
        return getSortedArrayByTree(favouritesByRestaurantId.getData(id, null, null, null));
    }


  /*
   * Returns theRestaurant IDs from the favourites in-common between
   * Customer 1 with ID id1 and Customer 2 with ID id2.
   * The resulting in-common favourites should be sorted by Date Favourited,
   * from newest to oldest.
   * If they have the same Date Favourited, then it is sorted in ascending order
   * of their Restaurant ID.
   *
   * @return    sorted restaurantIDs
   */
    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // DONE
        return getRestaurantsByCustomerRelation(customer1ID, customer2ID, "common");
    }


  /*
   * Returns theRestaurant IDs from the favourites that are favourited by
   * Customer 1 with ID id1 but not favourited by Customer 2 with ID id2.
   * The resulting in-common favourites should be sorted by Date Favourited,
   * from newest to oldest.
   * If they have the same Date Favourited, then it is sorted in ascending order
   * of their Restaurant ID.
   *
   * @return    sorted restaurantIDs
   */
    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // DONE
        return getRestaurantsByCustomerRelation(customer1ID, customer2ID, "missing");
    }


  /*
   * Returns theRestaurant IDs from the favourites that are favourited by
   * Customer 1 with ID id1 but not favourited by Customer 2 with ID id2,
   * as well as the favourites that are favourited by Customer 2 with ID
   * id2 but not favourited by Customer 1 with ID id1.
   * The resulting in-common favourites should be sorted by Date Favourited,
   * from newest to oldest.
   * If they have the same Date Favourited, then it is sorted in ascending order
   * of their Restaurant ID.
   *
   * @return    sorted restaurantIDs
   */
    public Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // DONE
        return getRestaurantsByCustomerRelation(customer1ID, customer2ID, "not common");
    }


  /*
   * Returns the Customer ID’s of the top 20 customers who favourited the most.
   * If they have the same favourite count, then it should be sorted by Date
   * Favourited, from newest to oldest.
   * If they have the same Date Favourited, then it is sorted in ascending order
   * of their Restaurant ID.
   *
   * @return    sorted customerIDs
   */
    public Long[] getTopCustomersByFavouriteCount() {
        // DONE
        return getTopFavouriteCountOfTree(favouritesByCustomerId, "customer");
    }


  /*
   * Returns the Restaurant ID’s of top 20 restaurants that have the most
   * favourites.
   * If they have the same favourite count, then it should be sorted by Date
   * Favourited, from newest to oldest.
   * If they have the same Date Favourited, then it is sorted in ascending order
   * of their Restaurant ID.
   *
   * @return    sorted restaurantIDs
   */
    public Long[] getTopRestaurantsByFavouriteCount() {
        // DONE
        return getTopFavouriteCountOfTree(favouritesByRestaurantId, "restaurant");
    }


// ADDED FUNCTIONS


  /*
   * Adds the given favourite to all restaurant AVL trees.
   *
   * @param     favourite
   */
    private void addAll(Favourite favourite) {
        MyArrayList<Long> favouriteID;
        favouriteID = new MyArrayList<Long>();
        favouriteID.add(favourite.getID());
        favouritesById.add(favourite.getID(), null, null, null, favourite);
        MyAvlTree<Long, Long, Favourite> customerTree = favouritesByCustomerId.getData(favourite.getCustomerID(), null, null, null);
        if (customerTree != null) {
            customerTree.add((-1) * favourite.getDateFavourited().getTime(), null , favouriteID, null, favourite);
            favouritesByCustomerId.setData(favourite.getCustomerID(), null, null, null, customerTree);
        } else {
            customerTree = new MyAvlTree<Long, Long, Favourite>();
            customerTree.add((-1) * favourite.getDateFavourited().getTime(), null , favouriteID, null, favourite);
            favouritesByCustomerId.add(favourite.getCustomerID(), null, null, null, customerTree);
        }
        MyAvlTree<Long, Long, Favourite> restaurantTree = favouritesByRestaurantId.getData(favourite.getRestaurantID(), null, null, null);
        if (restaurantTree != null) {
            restaurantTree.add((-1) * favourite.getDateFavourited().getTime(), null , favouriteID, null, favourite);
            favouritesByRestaurantId.setData(favourite.getRestaurantID(), null, null, null, restaurantTree);
        } else {
            restaurantTree = new MyAvlTree<Long, Long, Favourite>();
            restaurantTree.add((-1) * favourite.getDateFavourited().getTime(), null , favouriteID, null, favourite);
            favouritesByRestaurantId.add(favourite.getRestaurantID(), null, null, null, restaurantTree);

        }
    }


  /*
   * Removes the given favourite from all restaurant AVL trees.
   *
   * @param     favourite
   */
    private void removeAll(Favourite favourite) {
        MyArrayList<Long> favouriteID = new MyArrayList<Long>();
        favouriteID.add(favourite.getID());
        favouritesById.remove(favourite.getID(), null, null, null);
        favouritesByCustomerId.getData(favourite.getCustomerID(), null, null, null).remove(
            favourite.getDateFavourited().getTime() * (-1), null, favouriteID, null);
        favouritesByRestaurantId.getData(favourite.getRestaurantID(), null, null, null).remove(
            favourite.getDateFavourited().getTime() * (-1), null, favouriteID, null);
    }


  /*
   * Checks to see if there exists a favourite in the given favourite arrays with
   * the same restaurantID and customerID.
   * If so, returns it
   *
   * @param     favourite
   * @param     sorted customerFavourites
   * @param     sorted restaurantFavourites
   * @return    favourite match
   */
    private Favourite findSimilarFavourite(Favourite favourite, Favourite[] sameCustomer, Favourite[] sameRestaurant) {
        if (sameCustomer.length == 0 || sameRestaurant.length == 0) {return null;}
        int ptr1 = 0;
        int ptr2 = 0;
        int compare;
        while (ptr1 < (sameCustomer.length - 1) || ptr2 < (sameRestaurant.length - 1)) {
            if (sameCustomer[ptr1].getID() == sameRestaurant[ptr2].getID()) {
                return sameCustomer[ptr1];
            }
            if (ptr1 == (sameCustomer.length - 1) && ptr2 < (sameRestaurant.length - 1)) {
                ptr2++;
                continue;
            }
            if (ptr2 == (sameRestaurant.length - 1) && ptr1 < (sameCustomer.length - 1)) {
                ptr1++;
                continue;
            }
            compare = sameCustomer[ptr1].getDateFavourited().compareTo(sameRestaurant[ptr2].getDateFavourited());
            if (compare == 0) {
                compare = sameCustomer[ptr1].getID().compareTo(sameRestaurant[ptr2].getID());
            }
            if (compare > 0 && (ptr2 < (sameRestaurant.length - 1))) {
                ptr2++;
            } else if (compare <= 0 && ptr1 < (sameCustomer.length - 1)) {
                ptr1++;
            }
        }
        return null;
    }



  /*
   * Given a tree of favourites, returns array of all favourites contained
   * in it.
   *
   * @param     favourite AVL tree
   * @return    sorted favourites
   */
    private Favourite[] getSortedArrayByTree(MyAvlTree<Long, Long, Favourite> original_tree) {
        if (original_tree != null) {
            Favourite[] fav_array = new Favourite[original_tree.size()];
            int i = 0;
            for (Favourite favourite : original_tree) {
                fav_array[i] = favourite;
                i++;
            }
            if (original_tree.size() > i) {
                Favourite[] arr = new Favourite[i];
                int i2 = 0;
                for (Favourite favourite : fav_array) {
                    arr[i2] = favourite;
                    i2++;
                }
                fav_array = arr;
            }
            return fav_array;
        }
        return new Favourite[0];

    }



  /*
   * Given two customer IDs, returns an array of restaurant IDs that
   * match the given relation between the two.
   *
   * @param     customer ID 1
   * @param     customer ID 2
   * @param     relation -- "common", "missing", "not common"
   * @return    sorted restaurantIDs
   */
    private Long[] getRestaurantsByCustomerRelation(Long customer1ID, Long customer2ID, String relation) {
        Favourite[] favouritesCustomer1 = getFavouritesByCustomerID(customer1ID);
        Favourite[] favouritesCustomer2 = getFavouritesByCustomerID(customer2ID);
        if (favouritesCustomer1.length == 0 || favouritesCustomer2.length == 0) {
            return new Long[0];
        }
        MyAvlTree<Long, Long, Long> mergedTree = new MyAvlTree<Long, Long, Long>();
        MyAvlTree<Long, Long, Favourite> favouritesCustomer1Tree = new MyAvlTree<Long, Long, Favourite>();
        MyAvlTree<Long, Long, Favourite> favouritesCustomer2Tree = new MyAvlTree<Long, Long, Favourite>();
        for (Favourite favourite : favouritesCustomer1) {
            favouritesCustomer1Tree.add(favourite.getRestaurantID(), null, null, null, favourite);
        }
        for (Favourite favourite : favouritesCustomer2) {
            favouritesCustomer2Tree.add(favourite.getRestaurantID(), null, null, null, favourite);
        }
        Iterator<Favourite> iterator1 = favouritesCustomer1Tree.iterator();
        Iterator<Favourite> iterator2 = favouritesCustomer2Tree.iterator();
        Favourite temp_fav1 = null;
        Favourite temp_fav2 = null;
        int compare;
        Long restaurantTime;
        MyArrayList<Long> restaurantID;
        boolean last_time = false;
        while (iterator1.hasNext() || iterator2.hasNext() || last_time) { //Iterates through both at the same time, allowing a much faster scan of both trees
            if (temp_fav1 == null && iterator1.hasNext()) {temp_fav1 = iterator1.next();}
            if (temp_fav2 == null && iterator2.hasNext()) {temp_fav2 = iterator2.next();}
            compare = temp_fav1.getRestaurantID().compareTo(temp_fav2.getRestaurantID());
            if (compare == 0 && relation.equals("common")) {
                restaurantID = new MyArrayList<Long>();
                restaurantID.add(temp_fav1.getRestaurantID());
                if (temp_fav1.getDateFavourited().after(temp_fav2.getDateFavourited())) {
                    restaurantTime = temp_fav1.getDateFavourited().getTime() * (-1);
                } else {
                    restaurantTime = temp_fav2.getDateFavourited().getTime() * (-1);
                }
                mergedTree.add(restaurantTime, null , restaurantID, null, temp_fav1.getRestaurantID());
            }
            else if (compare != 0 && relation.equals("missing")) {
                restaurantID = new MyArrayList<Long>();
                restaurantID.add(temp_fav1.getRestaurantID());
                restaurantTime = temp_fav1.getDateFavourited().getTime() * (-1);
                if (!mergedTree.contains(restaurantTime, null , restaurantID, null)) {
                    mergedTree.add(restaurantTime, null , restaurantID, null, temp_fav1.getRestaurantID());
                }
            }
            else if (compare != 0 && relation.equals("not common")) {
                restaurantID = new MyArrayList<Long>();
                restaurantID.add(temp_fav1.getRestaurantID());
                restaurantTime = temp_fav1.getDateFavourited().getTime() * (-1);
                if (!mergedTree.contains(restaurantTime, null , restaurantID, null)) {
                    mergedTree.add(restaurantTime, null , restaurantID, null, temp_fav1.getRestaurantID());
                }
                restaurantID = new MyArrayList<Long>();
                restaurantID.add(temp_fav2.getRestaurantID());
                restaurantTime = temp_fav2.getDateFavourited().getTime() * (-1);
                if (!mergedTree.contains(restaurantTime, null , restaurantID, null)) {
                    mergedTree.add(restaurantTime, null , restaurantID, null, temp_fav2.getRestaurantID());
                }
            }
            if (last_time) {
                last_time = false;
                continue;
            }
            if ((compare > 0 && iterator2.hasNext()) || (!iterator1.hasNext() && iterator2.hasNext())) {
                temp_fav2 = iterator2.next();
            } else if ((compare <= 0 && iterator1.hasNext()) || (iterator1.hasNext() && !iterator2.hasNext())) {
                temp_fav1 = iterator1.next();
            }
            if (!iterator2.hasNext() && !iterator1.hasNext()) {
                last_time = true;
            }
        }
        Long[] merged = new Long[mergedTree.size()];
        if (merged.length == 0) {return merged;}
        int i = 0;
        for (Long restaurant : mergedTree) {
            merged[i] = restaurant;
            i++;
        }
        return merged;
    }



  /*
   * Given a tree of trees of favourites and a store type, returns the top 20 IDs of that tree,
   * based on the size of each tree.
   *
   * @param     favouritesByTreeofTrees
   * @param     store -- "customer", "restaurant"
   * @return    sorted IDs
   */
    private Long[] getTopFavouriteCountOfTree(MyAvlTree<Long, Long, MyAvlTree<Long, Long, Favourite>> favouritesByTreeOfTrees, String store) {
        MyAvlTree<Long, Long, Long> top_store_tree = new MyAvlTree<Long, Long, Long>();
        Long date = null;
        MyArrayList<Long> compare;
        Long count;
        Long favouriteID = null;
        Long storeID = null;
        Favourite fav;
        for (MyAvlTree<Long, Long, Favourite> tree : favouritesByTreeOfTrees) {
            date = 0L;
            favouriteID = 0L;
            count = (long) tree.size();
            Iterator<Favourite> iter = tree.iterator(); //iterating from youngest to oldest
            if (iter.hasNext()) {
                fav = iter.next();
                date = fav.getDateFavourited().getTime();
                favouriteID = fav.getID();
                if (store == "customer") {
                    storeID = fav.getCustomerID();
                } else if (store == "restaurant") {
                    storeID = fav.getRestaurantID();
                }
                compare = new MyArrayList<Long>();
                compare.add(date);
                compare.add(favouriteID);
                top_store_tree.add((-1) * count, null , compare, null, storeID);
                if (top_store_tree.size() > 20) {
                    top_store_tree.removeLargest();
                }    
            }
        }
        Long[] topCustomers = new Long[20];
        int i = 0;
        for (Long ID : top_store_tree) {
            topCustomers[i] = ID;
            i++;
        }
        return topCustomers;
    }

}
