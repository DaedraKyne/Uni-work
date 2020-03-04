package uk.ac.warwick.cs126.models;

/**
 * Possible price ranges that a Restaurant can categorised as.
 * @author jamesvh (James Van Hinsbergh) -
 * <a href="mailto:J.van-Hinsbergh@warwick.ac.uk">J.van-Hinsbergh@warwick.ac.uk</a>
 * @author iant (Ian Tu) -
 * <a href="mailto:I.Tu@warwick.ac.uk">I.Tu@warwick.ac.uk</a>
 * @version 1.0
 */
public enum PriceRange {
    CheapEats, MidRange, FineDining;

    /**
     * Returns the human readable version of the price range, with spaces and capitalisation.
     * @return The human readable string of the price range.
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
