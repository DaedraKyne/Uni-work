package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IFavouriteStore;
import uk.ac.warwick.cs126.models.Favourite;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyAvlTree;
import uk.ac.warwick.cs126.structures.MyBinaryTree;
import uk.ac.warwick.cs126.util.DataChecker;

import java.util.Iterator;



public class FavouriteStore implements IFavouriteStore {

    private DataChecker dataChecker;
    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite> favouritesById;
    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite>> favouritesByRestaurantId;
    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite>> favouritesByCustomerId;
    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Boolean> idBlacklist;
    private MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Favourite>> ignoredFavouritesByCustomerId;

    public FavouriteStore() {
        // Initialise variables here
        dataChecker = new DataChecker();
        favouritesById = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite>();
        favouritesByRestaurantId = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite>>();
        favouritesByCustomerId = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite>>();
        idBlacklist = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Boolean>();
        ignoredFavouritesByCustomerId = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Favourite>>();

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

    public boolean addFavourite(Favourite favourite) {
        // DONE
        if (dataChecker.isValid(favourite)) {
            Favourite duplicate = favouritesById.getData(favourite.getID(), null, null, null);
            Boolean blackListed = idBlacklist.getData(favourite.getID(), null, null, null);
            if (blackListed != null) {
                return false;
            }
            if (duplicate != null) {
                /*if (favourite.toString().equals("ID: 9353171919852385    Customer ID: 9142424817356729    Restaurant ID: 8842391383657217    Date Favourited: 2016-08-16 06:15:49")) {
                    System.out.println("Found already existing id");
                    System.out.println(duplicate);
                }*/
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
                    System.out.print("found hidden favourite");
                    Boolean hidden_fav_state = idBlacklist.getData(hidden_favourite.getID(), null, null, null);
                    if (hidden_fav_state == false) {
                        System.out.println("- falsified, good hidden");
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
                    System.out.println(": archiving");
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
            if (i % 100 == 0) {System.out.println((int)Math.floor(((float)i / (float)length) * 100) + "." + (int)Math.floor(((float)i / (float)length) * 1000) % 10 + "%");}
        }
        return fully_added;
    }

    public Favourite getFavourite(Long id) {
        // DONE
        if (dataChecker.isValid(id)) {
            return favouritesById.getData(id, null, null, null);
        }
        return null;
    }

    public Favourite[] getFavourites() {
        // DONE
        Favourite[] fav_array = new Favourite[favouritesById.size()];
        int i = 0;
        for (Favourite favourite : favouritesById) {
            fav_array[i++] = favourite;
        }
        return fav_array;
    }

    public Favourite[] getFavouritesByCustomerID(Long id) {
        // DONE
        /*MyAvlTree<Long, Long, Favourite> original_tree = favouritesByCustomerId.getData(id);
        if (original_tree != null) {
            int i = 0;
            Favourite[] fav_array = new Favourite[original_tree.size()];
            for (Favourite favourite : original_tree) {
                fav_array[i++] = favourite;
            }
            return fav_array;
        }
        return new Favourite[0];*/
        return getSortedArrayByTree(favouritesByCustomerId.getData(id, null, null, null));
    }

    public Favourite[] getFavouritesByRestaurantID(Long id) {
        // DONE
        return getSortedArrayByTree(favouritesByRestaurantId.getData(id, null, null, null));
    }


    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // DONE
        System.out.println("Searching in common");
        return getRestaurantsByCustomerRelation(customer1ID, customer2ID, "common");
    }

    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // DONE
        System.out.println("Searching missing");
        return getRestaurantsByCustomerRelation(customer1ID, customer2ID, "missing");
    }

    public Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // DONE
        System.out.println("Searching not in common");
        return getRestaurantsByCustomerRelation(customer1ID, customer2ID, "not common");
    }


    public Long[] getTopCustomersByFavouriteCount() {
        // DONE
        return getTopFavouriteCountOfTree(favouritesByCustomerId, "customer");
    }

    public Long[] getTopRestaurantsByFavouriteCount() {
        // DONE
        return getTopFavouriteCountOfTree(favouritesByRestaurantId, "restaurant");
    }


// ADDED FUNCTIONS

    private void addAll(Favourite favourite) {
        MyArrayList<Long> favouriteID;
        favouriteID = new MyArrayList<Long>();
        favouriteID.add(favourite.getID());
        if (favourite.getRestaurantID().equals(Long.parseLong("9353171919852385"))) {
            System.out.println("Adding to restaurant tree: ");
        }
        favouritesById.add(favourite.getID(), null, null, null, favourite);
        MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite> customerTree = favouritesByCustomerId.getData(favourite.getCustomerID(), null, null, null);
        if (customerTree != null) {
            customerTree.add((-1) * favourite.getDateFavourited().getTime(), null , favouriteID, null, favourite);
            favouritesByCustomerId.setData(favourite.getCustomerID(), null, null, null, customerTree);
        } else {
            customerTree = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite>();
            customerTree.add((-1) * favourite.getDateFavourited().getTime(), null , favouriteID, null, favourite);
            favouritesByCustomerId.add(favourite.getCustomerID(), null, null, null, customerTree);
        }
        MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite> restaurantTree = favouritesByRestaurantId.getData(favourite.getRestaurantID(), null, null, null);
        if (restaurantTree != null) {
            restaurantTree.add((-1) * favourite.getDateFavourited().getTime(), null , favouriteID, null, favourite);
            favouritesByRestaurantId.setData(favourite.getRestaurantID(), null, null, null, restaurantTree);
        } else {
            restaurantTree = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite>();
            restaurantTree.add((-1) * favourite.getDateFavourited().getTime(), null , favouriteID, null, favourite);
            favouritesByRestaurantId.add(favourite.getRestaurantID(), null, null, null, restaurantTree);

        }
        if (favourite.getRestaurantID().equals(Long.parseLong("9353171919852385"))) {
            System.out.println(" :: size: " + favouritesByRestaurantId.getData(favourite.getRestaurantID(), null, null, null).size());
            System.out.println(favourite);
            for (Favourite fav : favouritesByRestaurantId.getData(favourite.getRestaurantID(), null, null, null)) {
                System.out.println ("  ---> " + fav);
            }
        }
    }

    private void removeAll(Favourite favourite) {
        /*if (favourite.toString().equals("ID: 9353171919852385    Customer ID: 5473464975788313    Restaurant ID: 7816833756189615    Date Favourited: 2020-01-05 16:27:09")) {
            System.out.println("Removing from id tree");
        }*/
        if (favourite.getRestaurantID().equals(Long.parseLong("9353171919852385"))) {
            System.out.println("Removing from restaurant tree: ");
        }
        MyArrayList<Long> favouriteID = new MyArrayList<Long>();
        favouriteID.add(favourite.getID());
        favouritesById.remove(favourite.getID(), null, null, null);
        favouritesByCustomerId.getData(favourite.getCustomerID(), null, null, null).remove(
            favourite.getDateFavourited().getTime() * (-1), null, favouriteID, null);
        favouritesByRestaurantId.getData(favourite.getRestaurantID(), null, null, null).remove(
            favourite.getDateFavourited().getTime() * (-1), null, favouriteID, null);
            if (favourite.getRestaurantID().equals(Long.parseLong("9353171919852385"))) {
                System.out.println(" :: size: " + favouritesByRestaurantId.getData(favourite.getRestaurantID(), null, null, null).size());
                System.out.println(favourite);
                for (Favourite fav : favouritesByRestaurantId.getData(favourite.getRestaurantID(), null, null, null)) {
                    System.out.println ("  ---> " + fav);
                }
            }        
    }

    private Favourite findSimilarFavourite(Favourite favourite, Favourite[] sameCustomer, Favourite[] sameRestaurant) {
        if (sameCustomer.length == 0 || sameRestaurant.length == 0) {return null;}
        int ptr1 = 0;
        int ptr2 = 0;
        int compare;
        while (ptr1 < (sameCustomer.length - 1) || ptr2 < (sameRestaurant.length - 1)) {
            if (sameCustomer[ptr1] == null) {
                System.out.println("customers: " + sameCustomer.length + " <- " + favourite.getCustomerID());
                for (int i = 0; i < sameCustomer.length; i++) {
                    System.out.println(sameCustomer[i]);
                }
            }
            if (sameRestaurant[ptr2] == null) {
                System.out.println("restaurants: " + sameRestaurant.length + " <- " + favourite.getRestaurantID());
                for (int i = 0; i < sameRestaurant.length; i++) {
                    System.out.println(sameRestaurant[i]);
                }
            }
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




    private Favourite[] getSortedArrayByTree(MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite> original_tree) {
        if (original_tree != null) {
            Favourite[] fav_array = new Favourite[original_tree.size()];
            int i = 0;
            Iterator<Favourite> iter = original_tree.iterator();
            if (original_tree.size() > 0 && !iter.hasNext()) {
                System.out.println("Cannot iterate through non-empty tree for some reason: ");
            }
            for (Favourite favourite : original_tree) {
                if (favourite == null) {
                    System.out.println("Size of tree: " + original_tree.size());
                    System.out.println("Error at: " + i);
                }
                fav_array[i] = favourite;
                i++;
            }
            if (original_tree.size() != i) {
                System.out.println("Not iterated through whole tree");
                System.out.println("Size of tree: " + original_tree.size());
                System.out.println("Size of i: " + i);
            }
            return fav_array;
            /*MyArrayList<MyBinaryTree<Long, Long, Integer>> repeats = new MyArrayList<MyBinaryTree<Long, Long, Integer>>();
            long time = 0L;
            boolean equality = false;
            Favourite old_fav = null;
            int i = 0;
            MyBinaryTree<Long, Long, Integer> new_tree;
            for (Favourite favourite : original_tree) {
                fav_array[i] = favourite;
                if (old_fav != null && time == favourite.getDateFavourited().getTime()) {
                    if (equality) {
                        repeats.get(repeats.size() - 1).add(favourite.getID(), null, null, null, i);
                    } else {
                        equality = true;
                        new_tree = new MyBinaryTree<Long, Long, Integer>();
                        new_tree.add(old_fav.getID(), null, null, null, i-1);
                        new_tree.add(favourite.getID(), null, null, null, i);
                        repeats.add(new_tree);
                    }
                } else {
                    equality = false;
                }
                old_fav = favourite;
                i++;
            }
            int lowest = original_tree.size();
            for (i = 0; i < repeats.size(); i++) {
                MyBinaryTree<Long, Long, Integer> same_time_list = repeats.get(i);
                int[] duplicate_index_list = new int[same_time_list.size()];
                int i2 = 0;
                for (int index : same_time_list) {
                    duplicate_index_list[i2] = index;
                    if (lowest > index) {lowest = index;}
                    i2++;
                }
                i2 = 0;
                for (int index : same_time_list) {
                    fav_array[i2 + lowest] = fav_array[index];
                    i2++;
                }
            }*/
        }
        return new Favourite[0];

    }



    private Long[] getRestaurantsByCustomerRelation(Long customer1ID, Long customer2ID, String relation) {
        Favourite[] favouritesCustomer1 = getFavouritesByCustomerID(customer1ID);
        Favourite[] favouritesCustomer2 = getFavouritesByCustomerID(customer2ID);
        if (favouritesCustomer1.length == 0 || favouritesCustomer2.length == 0) {
            return new Long[0];
        }
        MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Long> mergedTree = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Long>();
        MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite> favouritesCustomer1Tree = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite>();
        MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite> favouritesCustomer2Tree = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite>();
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
        while (iterator1.hasNext() || iterator2.hasNext() || last_time) {
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

    private Long[] getTopFavouriteCountOfTree(MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite>> favouritesByTree, String store) {
        // TODO
        MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Long> top_store_tree = new MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Long>();
        Long earliest_date = 0L; //Initial set to max value of long
        Long date;
        Long smallest_id = 9223372036854775807L; //Initial set to max value of long
        MyArrayList<Long> compare;
        Long count;
        Long favouriteID;
        Long storeID = null;
        Favourite fav;
        for (MyAvlTree<Long, Long, MyArrayList<Long>, MyArrayList<Long>, MyArrayList<Long>, Favourite> tree : favouritesByTree) {
            earliest_date = 0L;
            smallest_id = 9223372036854775807L;
            count = (long) tree.size();
            Iterator<Favourite> iter = tree.iterator(); //iterating from oldest to youngest favourite, BUT from biggest to smallest favouriteID
            while (iter.hasNext()) {
                fav = iter.next();
                date = fav.getDateFavourited().getTime();
                favouriteID = fav.getID();
                if (date < earliest_date && earliest_date != 0L) {
                    break;
                }
                if (favouriteID <= smallest_id) {
                    earliest_date = date;
                    smallest_id = favouriteID;
                    if (store == "customer") {
                        storeID = fav.getCustomerID();
                    } else if (store == "restaurant") {
                        storeID = fav.getRestaurantID();
                    }
                }              
            }
            compare = new MyArrayList<Long>();
            compare.add(earliest_date);
            compare.add(smallest_id);
            top_store_tree.add((-1) * count, null , compare, null, storeID);
            if (top_store_tree.size() > 20) {
                top_store_tree.removeLargest();
            }
        }
        Long[] topCustomers = new Long[20];
        int i = 0;
        for (Long ID : top_store_tree) {
            topCustomers[i] = ID;
            i++;
        }
        System.out.println("Returning");
        return topCustomers;
    }

}
