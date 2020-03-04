package uk.ac.warwick.cs126.models;

/**
 * Possible cuisine's that a Restaurant can specialise in.
 * @author jamesvh (James Van Hinsbergh) -
 * <a href="mailto:J.van-Hinsbergh@warwick.ac.uk">J.van-Hinsbergh@warwick.ac.uk</a>
 * @author iant (Ian Tu) -
 * <a href="mailto:I.Tu@warwick.ac.uk">I.Tu@warwick.ac.uk</a>
 * @version 1.0
 */
public enum Cuisine {
    Ale, African, American, Brazilian, British, Burger, Cake, Caribbean, Chinese, Cocktails, Dessert, Egyptian, European, FishAndChips, French, Gelato, Greek, Indian, Italian, Jamaican, Japanese, Korean, Lebanese, Malaysian, Mediterranean, Mexican, Moroccan, Pakistani, Persian, Pizza, Polish, Romanian, Salad, Scandinavian, Seafood, Soups, SouthAmerican, Spanish, Steakhouse, Sushi, Tapas, Thai, Turkish, Vietnamese, Wine;

    /**
     * Returns the human readable version of the cuisine, with spaces and capitalisation.
     * @return The human readable string of the cuisine.
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
