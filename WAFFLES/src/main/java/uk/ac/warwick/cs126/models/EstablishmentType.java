package uk.ac.warwick.cs126.models;

/**
 * Possible types of establishment that a Restaurant can be.
 * @author jamesvh (James Van Hinsbergh) -
 * <a href="mailto:J.van-Hinsbergh@warwick.ac.uk">J.van-Hinsbergh@warwick.ac.uk</a>
 * @author iant (Ian Tu) -
 * <a href="mailto:I.Tu@warwick.ac.uk">I.Tu@warwick.ac.uk</a>
 * @version 1.0
 */
public enum EstablishmentType {
    Restaurant, FastFood, DessertShop, Cafe, Bakery, Bar, Pub, StreetFood, SnackBar, MarketStall, Diner, Tavern, Takeaway;

    /**
     * Returns the human readable version of the establishment type, with spaces and capitalisation.
     * @return The human readable string of the establishment type.
     */
    public String toString() {
        String[] words = this.name().split("(?=\\p{Upper})");
        String output = "";
        for (String word : words) {
            output += (word + " ");
        }
        return output.trim();
    }
}
