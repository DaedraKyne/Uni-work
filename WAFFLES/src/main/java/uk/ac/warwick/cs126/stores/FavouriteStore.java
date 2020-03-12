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

import java.time.Instant;



public class FavouriteStore implements IFavouriteStore {

    private MyArrayList<Favourite> favouriteArray;
    private DataChecker dataChecker;
    private MyAvlTree<Long, Favourite> favouritesById;
    private MyAvlTree<Long, MyAvlTree<Long, Favourite>> favouritesByRestaurantId;
    private MyAvlTree<Long, MyAvlTree<Long, Favourite>> favouritesByCustomerId;
    private MyAvlTree<Long, Boolean> idBlacklist;
    private MyAvlTree<Long, MyArrayList<Favourite>> ignoredFavouritesByCustomerId;

    public FavouriteStore() {
        // Initialise variables here
        dataChecker = new DataChecker();

        favouriteArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        favouritesById = new MyAvlTree<Long, Favourite>();
        favouritesByRestaurantId = new MyAvlTree<Long, MyAvlTree<Long, Favourite>>();
        favouritesByCustomerId = new MyAvlTree<Long, MyAvlTree<Long, Favourite>>();
        idBlacklist = new MyAvlTree<Long, Boolean>();
        ignoredFavouritesByCustomerId = new MyAvlTree<Long, MyArrayList<Favourite>>();

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
            Favourite duplicate = favouritesById.getData(favourite.getID());
            Boolean blackListed = idBlacklist.getData(favourite.getID());
            if (blackListed != null) {
                return false;
            }
            if (duplicate != null) {
                favouritesById.remove(favourite.getID());
                idBlacklist.add(favourite.getID(), true);
                MyArrayList<Favourite> hidden_matches = ignoredFavouritesByCustomerId.getData(favourite.getCustomerID());
                Favourite hidden_favourite = null;
                if (hidden_matches != null) {
                    for (int i = 0; i < hidden_matches.size(); i++) {
                        if (hidden_matches.get(i).getRestaurantID() == favourite.getRestaurantID()) {
                            hidden_favourite = hidden_matches.get(i);
                            hidden_matches.remove(hidden_favourite);
                            break;
                        }
                    }
                }
                if (hidden_favourite != null) {
                    Boolean hidden_fav_state = idBlacklist.getData(hidden_favourite.getID());
                    if (hidden_fav_state == false) {
                        addAll(hidden_favourite);
                        ignoredFavouritesByCustomerId.setData(hidden_favourite.getCustomerID(), hidden_matches);
                        idBlacklist.remove(hidden_favourite.getID());
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
                    favouritesById.remove(old_fav.getID());
                    MyArrayList<Favourite> hidden_matches = ignoredFavouritesByCustomerId.getData(old_fav.getCustomerID());
                    if (hidden_matches == null) {
                        hidden_matches = new MyArrayList<Favourite>();
                        hidden_matches.add(old_fav);
                        ignoredFavouritesByCustomerId.add(old_fav.getCustomerID(), hidden_matches);
                    } 
                    else {
                            hidden_matches.add(old_fav);
                            ignoredFavouritesByCustomerId.setData(old_fav.getCustomerID(), hidden_matches);
                    }
                } else {
                    MyArrayList<Favourite> hidden_matches = ignoredFavouritesByCustomerId.getData(favourite.getCustomerID());
                    if (hidden_matches == null) {
                        hidden_matches = new MyArrayList<Favourite>();
                        hidden_matches.add(favourite);
                        ignoredFavouritesByCustomerId.setData(favourite.getCustomerID(), hidden_matches);
                    }
                    else {
                        hidden_matches.add(favourite);
                        ignoredFavouritesByCustomerId.add(favourite.getCustomerID(), hidden_matches);
                    }
                    return false;
                }
            }
            addAll(favourite);
            return true;
            }
        return false;
    }

    private void addAll(Favourite favourite) {
        favouritesById.add(favourite.getID(), favourite);
        MyAvlTree<Long, Favourite> restaurantTree = favouritesByRestaurantId.getData(favourite.getRestaurantID());
        if (restaurantTree != null) {
            restaurantTree.add((-1) * favourite.getDateFavourited().getTime(), favourite);
            favouritesByRestaurantId.setData(favourite.getRestaurantID(), restaurantTree);
        } else {
            restaurantTree = new MyAvlTree<Long, Favourite>();
            restaurantTree.add((-1) * favourite.getDateFavourited().getTime(), favourite);
            favouritesByRestaurantId.add(favourite.getRestaurantID(), restaurantTree);
        }
        MyAvlTree<Long, Favourite> customerTree = favouritesByCustomerId.getData(favourite.getCustomerID());
        if (customerTree != null) {
            customerTree.add((-1) * favourite.getDateFavourited().getTime(), favourite);
            favouritesByCustomerId.setData(favourite.getRestaurantID(), customerTree);
        } else {
            customerTree = new MyAvlTree<Long, Favourite>();
            customerTree.add((-1) * favourite.getDateFavourited().getTime(), favourite);
            favouritesByCustomerId.add(favourite.getRestaurantID(), customerTree);
        }
    }

    private Favourite findSimilarFavourite(Favourite favourite, Favourite[] sameCustomer, Favourite[] sameRestaurant) {
        int ptr1 = 0;
        int ptr2 = 0;
        int compare;
        while (ptr1 != sameCustomer.length && ptr2 != sameRestaurant.length) {
            if (sameCustomer[ptr1].getID() == sameRestaurant[ptr2].getID()) {
                return sameCustomer[ptr1];
            }
            if (ptr1 == sameCustomer.length) {
                ptr2++;
                continue;
            }
            if (ptr2 == sameRestaurant.length) {
                ptr1++;
                continue;
            }
            compare = sameCustomer[ptr1].getDateFavourited().compareTo(sameRestaurant[ptr2].getDateFavourited());
            if (compare <= 0) {
                ptr2++;
            } else {
                ptr1++;
            }
        }
        return null;
    }

    public boolean addFavourite(Favourite[] favourites) {
        // TODO
        boolean fully_added = true;
        int i = 0;
        for (Favourite favourite : favourites) {
            if (!addFavourite(favourite)) {
                fully_added = false;
            }
            i++;
            if (i % 100 == 0) {System.out.println(i);}
        }
        return fully_added;
    }

    public Favourite getFavourite(Long id) {
        // TODO
        return favouritesById.getData(id);
    }

    public Favourite[] getFavourites() {
        // TODO
        Favourite[] fav_array = new Favourite[favouritesById.size()];
        int i = 0;
        for (Favourite favourite : favouritesById) {
            fav_array[i++] = favourite;
        }
        return fav_array;
    }

    public Favourite[] getFavouritesByCustomerID(Long id) {
        // TODO
        /*MyAvlTree<Long, Favourite> original_tree = favouritesByCustomerId.getData(id);
        if (original_tree != null) {
            int i = 0;
            Favourite[] fav_array = new Favourite[original_tree.size()];
            for (Favourite favourite : original_tree) {
                fav_array[i++] = favourite;
            }
            return fav_array;
        }
        return new Favourite[0];*/
        return getSortedArrayByTree(favouritesByCustomerId.getData(id));
    }

    public Favourite[] getFavouritesByRestaurantID(Long id) {
        // TODO
        return getSortedArrayByTree(favouritesByRestaurantId.getData(id));
    }

    private Favourite[] getSortedArrayByTree(MyAvlTree<Long, Favourite> original_tree) {
        if (original_tree != null) {
            Favourite[] fav_array = new Favourite[original_tree.size()];
            MyArrayList<MyBinaryTree<Long, Integer>> repeats = new MyArrayList<MyBinaryTree<Long, Integer>>();
            long time = 0L;
            boolean equality = false;
            Favourite old_fav = null;
            int i = 0;
            for (Favourite favourite : original_tree) {
                fav_array[i] = favourite;
                if (old_fav != null && time == favourite.getDateFavourited().getTime()) {
                    if (equality) {
                        repeats.get(repeats.size() - 1).add(favourite.getID(), i);
                    } else {
                        equality = true;
                        MyBinaryTree<Long, Integer> new_tree = new MyBinaryTree<Long, Integer>();
                        new_tree.add(old_fav.getID(), i-1);
                        new_tree.add(favourite.getID(), i);
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
                MyBinaryTree<Long, Integer> same_time_list = repeats.get(i);
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
            }
            return fav_array;
        }
        return new Favourite[0];

    }

    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // TODO
        return new Long[0];
    }

    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // TODO
        return new Long[0];
    }

    public Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // TODO
        return new Long[0];
    }

    public Long[] getTopCustomersByFavouriteCount() {
        // TODO
        return new Long[20];
    }

    public Long[] getTopRestaurantsByFavouriteCount() {
        // TODO
        return new Long[20];
    }
}
