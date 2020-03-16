# CS126 WAFFLES Coursework Report [1900481]
## CustomerStore
### Overview
<!-- * <- is a bullet point, you can also use - minuses or + pluses instead -->
<!-- And this is *italic* and this is **bold** -->
<!-- Words in the grave accents, or in programming terms backticks, formats it as code: `put code here` -->

* I have used an `AVL tree` structure to store and process customers because it allows me to quickly store, sort and return elements of it. I am favouring time over space, as we have lots of space to use but processing time isn't cheap. Therefore, throughout this coursework, we will avoid using structures and methods that take O(n^2) to store and sort.
* I used `AVL tree's automatic sorting` to sort customers by name and ID as it only takes O(log n) time to simultaneously store and  sort the tree (no need to re-sort tree later, only during insertion). Although this means we sort everything during the start-up time of the site, this means the site should run much smoother during run-time

### Space Complexity
 Store         | Worst Case | Description                                                  
 ------------- | ---------- | ------------------------------------------------------------ 
 CustomerStore | O(n)       | I have used two `AVL trees` to store customers by ID, and by name. <br> Additionally, I have used an`AVL tree` to store blacklisted IDs. Although this tree's worst case space-complexity would be n/2, the average case for it is much lower than that. <br>Where `n` is total customers added. 

### Time Complexity
<!-- Tell us the time complexity of each method and give a very short description. -->
<!-- These examples may or may not be correct, these examples have not taken account for other requirements like duplicate IDs and such.  -->
<!-- Again, the template is only a guide, you are free to make any changes. -->
<!-- If you did not do a method, enter a dash "-" -->
<!-- Technically, for the getCustomersContaining(s) average case, you are suppose to do it relative to s -->
<!-- Note, that this is using the original convertToAccents() method -->
<!-- So O(a*s + n*(a*t + t)) -->
<!-- Where a is the amount of accents -->
<!-- Where s is the length of the search term String  -->
<!-- Where t is the average length of a name -->
<!-- Where n is the total number of customers -->
<!-- But you can keep it simple -->

Method                           | Average Case     | Description
-------------------------------- | ---------------- | -----------
addCustomer(Customer c)          | O(log n)        | Adding/removing from AVL trees takes log n time. <br>`n` is total customers in the store 
addCustomer(Customer[] c)        | O(m log n)       | Add all customers <br>`n` is total customers in the store <br/>`m` is the length of the input array 
getCustomer(Long id)             | O(log n)        | Binary AVL tree search <br>`n` is total customers in the store 
getCustomers()                   | O(n)     | Iteration through AVL tree<br>`n` is total customers in the store<br>Worst case is O(n log n), but in most cases retrieving the next element in the tree only takes O(1) time (O(log n) is when next element requires going all the way up or down the tree) 
getCustomers(Customer[] c)       | O(m log m) | Creating new customer AVL tree sorted by ID and re-iterating through it after to make sorted array <br>`m` is the length of the input array 
getCustomersByName()             | O(n)           | Binary AVL tree search<br>`n` is total customers in the store 
getCustomersByName(Customer[] c) | O(m log m)      | Creating new customer AVL tree sorted by name and re-iterating through it after to make sorted array <br/>`m` is the length of the input array 
getCustomersContaining(String s) | O(a + n*(a + b)) | Searches all customers <br>`a` is the average time it takes to convert accents <br>`n` is total customers <br>`b` is average string search time

<div style="page-break-after: always;"></div>

## FavouriteStore
### Overview
* I have used `AVL Trees` to store favourites sorted by ID, Restaurant ID / Customer ID (containing `AVL Trees` of all the favourites for each restaurant/customer). This allows for a smoother runtime experience as everything has already been stored at start-up time.

* I also store blacklisted IDs and ignored IDs (IDs that were blacklisted but can be un-blacklisted in the future).

* Note: considering the sheer amount of reviews, I have decided to sort them all at start-up time, this takes a bit more than a minute but allows for a much smoother runtime.

  

### Space Complexity
 Store          | Worst Case | Description                                                  
 -------------- | ---------- | ------------------------------------------------------------ 
 FavouriteStore | O(n)       | I have used `AVL Trees` . <br>Where `n` is favourites stored. 

### Time Complexity
Method                                                          | Average Case     | Description
--------------------------------------------------------------- | ---------------- | -----------
addFavourite(Favourite f)                                       | O(log n)       | Adds/removes elements from AVL tree, depending on conditions.<br>`n` is total favourites in the store 
addFavourite(Favourite[] f)                                     | O(m log n)    | Add all favourites<br/>`n` is total favourites in the store <br/>`m` is the length of the input array 
getFavourite(Long id)                                           | O(log n)       | Binary AVL tree search <br/>`n` is total favourites in the store 
getFavourites()                                                 | O(n)           | Iteration through AVL tree<br/>`n` is total favourites in the store<br/>Worst case is O(n log n), but in most cases retrieving the next element in the tree only takes O(1) time (O(log n) is when next element requires going all the way up or down the tree) 
getFavouritesByCustomerID(Long id)                              | O(log m)      | AVL tree search<br>`m` is total customers shared among favourites 
getFavouritesByRestaurantID(Long id)                            | O(log m)      | AVL tree search<br/>`m` is total restaurants shared among favourites 
getCommonFavouriteRestaurants(<br>&emsp; Long id1, Long id2)    | O(log m + a log a + b log b ) | Finds all favourites for both ids', then iterates through both set of favourites at same time to find relation (sets are already sorted) <br>`m` is total customers shared among favourites<br>`a` is number of favourites with customer id1<br> `b` is number of favourites with customer id2 <br>Comparison between both arrays is much faster as we advance through both at same time (therefore max time taken is max(a, b))) 
getMissingFavouriteRestaurants(<br>&emsp; Long id1, Long id2)   | O(log m + a log a + b log b ) | Same technique as getCommonFavouriteRestaurants 
getNotCommonFavouriteRestaurants(<br>&emsp; Long id1, Long id2) | O(log m + a log a + b log b ) | Same technique as getCommonFavouriteRestaurants 
getTopCustomersByFavouriteCount()                               | O(m)           | Iterates through each customer id, storing size of linked favourites<br/>`m` is total customers shared among favourites<br/> 
getTopRestaurantsByFavouriteCount()                             | O(m)          | Iterates through each restaurant id, storing size of linked favourites<br/>`m` is total restaurants shared among favourites<br/> 

<div style="page-break-after: always;"></div>

## RestaurantStore
### Overview
* I have used `AVL Trees` to store favourites sorted by ID, name, date established, warwick stars (doesn't store every restaurant) and rating. This allows for a smoother runtime experience as everything has already been stored at start-up time.
* I also used `AVL trees` to store blacklisted IDs.

### Space Complexity
 Store           | Worst Case | Description                                                  
 --------------- | ---------- | ------------------------------------------------------------ 
 RestaurantStore | O(n)       | I have used `AVL Trees` . <br/>Where `n` is restaurants stored. 

### Time Complexity
Method                                                                        | Average Case     | Description
----------------------------------------------------------------------------- | ---------------- | -----------
addRestaurant(Restaurant r)                                                   | O(log n)       | Adding/removing from AVL trees takes log n time. <br/>`n` is total restaurants in the store 
addRestaurant(Restaurant[] r)                                                 | O(m log n)     | Add all restaurants <br/>`n` is total restaurants in the store <br/>`m` is the length of the input array 
getRestaurant(Long id)                                                        | O(log n)       | Binary AVL tree search <br/>`n` is total restaurants in the store 
getRestaurants()                                                              | O(n)           | Iteration through AVL tree<br/>`n` is total restaurants in the store<br/>Worst case is O(n log n), but in most cases retrieving the next element in the tree only takes O(1) time (O(log n) is when next element requires going all the way up or down the tree) 
getRestaurants(Restaurant[] r)                                                | O(m log m)     | Creating new restaurant AVL tree sorted by ID and re-iterating through it after to make sorted array <br/>`m` is the length of the input array 
getRestaurantsByName()                                                        | O(n)       | Iteration through AVL tree<br/>`n` is total restaurants in the store 
getRestaurantsByDateEstablished()                                             | O(n)      | Iteration through AVL tree<br/>`n` is total restaurants in the store 
getRestaurantsByDateEstablished(<br>&emsp; Restaurant[] r)                    | O(m log m)     | Creating new restaurant AVL tree sorted by date/name/ID and re-iterating through it after to make sorted array <br/>`m` is the length of the input array 
getRestaurantsByWarwickStars()                                                | O(n)           | Iteration through AVL tree<br/>`n` is total restaurants in the store 
getRestaurantsByRating(Restaurant[] r)                                        | O(m log m)     | Creating new restaurant AVL tree sorted by rating/nameID and re-iterating through it after to make sorted array <br/>`m` is the length of the input array 
getRestaurantsByDistanceFrom(<br>&emsp; float lat, float lon)                 | O(n log n)     | Iteration through restaurants, then creation of new AVL tree sorted by distance/ID<br>`n` is total restaurants in the store 
getRestaurantsByDistanceFrom(<br>&emsp; Restaurant[] r, float lat, float lon) | O(m log m)     | Creating new restaurant AVL tree sorted by distance/ID <br/>`m` is the length of the input array 
getRestaurantsContaining(String s)                                            | O(a + n*(a + b)) | Searches all restaurants<br/>`a` is the average time it takes to convert accents <br/>`n` is total customers <br/>`b` is average string search time 

<div style="page-break-after: always;"></div>

## ReviewStore
### Overview
* I have used `AVL Trees` to store reviews sorted by ID, date, rating, Restaurant ID / Customer ID (containing `AVL Trees` of all the reviews for each restaurant/customer). This allows for a smoother runtime experience as everything has already been stored at start-up time.

* I also store blacklisted IDs and ignored IDs (IDs that were blacklisted but can be un-blacklisted in the future).

* Note: considering the sheer amount of reviews, I have decided to sort them all at start-up time, this takes a bit more than a minute but allows for a much smoother runtime.

  

### Space Complexity
 Store       | Worst Case | Description                                                  
 ----------- | ---------- | ------------------------------------------------------------ 
 ReviewStore | O(n)       | I have used `AVL Trees` . <br/>Where `n` is favourites stored. 

### Time Complexity
Method                                     | Average Case     | Description
------------------------------------------ | ---------------- | -----------
addReview(Review r)                        | O(log n)    | Adding/removing from AVL trees takes log n time. <br/>`n` is total reviews in the store 
addReview(Review[] r)                      | O(m log n)    | Add all reviews<br/>`n` is total reviews in the store <br/>`m` is the length of the input array 
getReview(Long id)                         | O(log n)       | Binary AVL tree search <br/>`n` is total reviews in the store 
getReviews()                               | O(n)           | Iteration through AVL tree<br/>`n` is total reviews in the store<br/>Worst case is O(n log n), but in most cases retrieving the next element in the tree only takes O(1) time (O(log n) is when next element requires going all the way up or down the tree) 
getReviews(Review[] r)                     | O(m log m)     | Description <br>`...` is ...
getReviewsByDate()                         | O(n)           | Iteration through AVL tree<br/>`n` is total reviews in the store 
getReviewsByRating()                       | O(n)           | Iteration through AVL tree<br/>`n` is total reviews in the store 
getReviewsByRestaurantID(Long id)          | O(log m)      | AVL tree search<br/>`m` is total restaurants shared among reviews 
getReviewsByCustomerID(Long id)            | O(log m)      | AVL tree search<br/>`m` is total customers shared among reviews 
getAverageCustomerReviewRating(Long id)    | O(log m + o) | AVL tree search<br/>`m` is total customers shared among reviews<br>o is total reviews of given customer 
getAverageRestaurantReviewRating(Long id)  | O(log m + o) | AVL tree search<br/>`m` is total customers shared among reviews<br/>o is total reviews of given restaurant 
getCustomerReviewHistogramCount(Long id)   | O(log m + o) | AVL tree search<br/>`m` is total customers shared among reviews<br/>o is total reviews of given customer 
getRestaurantReviewHistogramCount(Long id) | O(log m + o) | AVL tree search<br/>`m` is total customers shared among reviews<br/>o is total reviews of given restaurant 
getTopCustomersByReviewCount()             | O(m)           | Iteration through AVL tree of customer trees<br/>`m` is total customers shared among reviews 
getTopRestaurantsByReviewCount()           | O(m)          | Iteration through AVL tree of restaurant trees<br/>`m` is total restaurants shared among reviews 
getTopRatedRestaurants()                   | O(m)           | Iteration through AVL tree of restaurant trees<br/>`m` is total restaurants shared among reviews 
getTopKeywordsForRestaurant(Long id)       | O(log n + m*w) | Get reviews for restaurant, iterate through reviews, searching for keywords<br>`n` is total customers in the store<br>`m` is total number of reviews for given restaurant<br>`w` is average review length 
getReviewsContaining(String s)             | O(a + n*(a + b)) | Searches all restaurants<br/>`a` is the average time it takes to convert accents <br/>`n` is total customers <br/>`b` is average string search time 

<div style="page-break-after: always;"></div>

## Util
### Overview
* **ConvertToPlace** 
    * Initialized Places array at start, storing in Place array but creating additional two-dimensional array of ints to delimit search through places array.
* **DataChecker**
    * Simple checks for each function, does exactly as said by guide.
* **HaversineDistanceCalculator (HaversineDC)**
    * Simply uses formula given by guide to calculate distance.
* **KeywordChecker**
    * Searches through keywords using binary search.
* **StringFormatter**
    * Uses hashing to map accent chars and their non-accent counterparts, to quicken conversion.

### Space Complexity
Util               | Worst Case | Description
-------------------| ---------- | -----------
ConvertToPlace     | O(n)       | I have used an array of `n` places to store all places. <br>Additionally, a 2-dimensional int array to quicken search time.
DataChecker        | O(1)     | Does not use space. 
HaversineDC        | O(1)     | Does not use space. 
KeywordChecker     | O(n)     | `n` is amount of keywords 
StringFormatter    | O(n)     | `n` is amount of accent conversions<br>Creates hashmap using char value. 

### Time Complexity
Util              | Method                                                                             | Average Case     | Description
----------------- | ---------------------------------------------------------------------------------- | ---------------- | -----------
ConvertToPlace    | convert(float lat, float lon)                                                      | O(n)             | Usually only looks through 1/10th of places array<br>Uses limiter 2d int array to determine whereabouts of given latitude/longitude info in places array 
DataChecker       | extractTrueID(String[] repeatedID)                                                 | O(1)           | Tries to find repetition of ID in string array. 
DataChecker       | isValid(Long id)                                                                   | O(1)           | For each number 1-9 in long, adds to AVL tree and checks whether associated number (amount of times it was added to tree) is superior to 3. 
DataChecker       | isValid(Customer customer)                                                         | O(1)           | Performs various checks on given customer. 
DataChecker       | isValid(Favourite favourite)                                                       | O(1)           | Performs various checks on given favourite. 
DataChecker       | isValid(Restaurant restaurant)                                                     | O(1)           | Performs various checks on given restaurant. 
DataChecker       | isValid(Review review)                                                             | O(1)           | Performs various checks on given review. 
HaversineDC       | inKilometres(<br>&emsp; float lat1, float lon1, <br>&emsp; float lat2, float lon2) | O(1)           | Uses formula given by guide to calculate distance. 
HaversineDC       | inMiles(<br>&emsp; float lat1, float lon1, <br>&emsp; float lat2, float lon2)      | O(1)           | Uses above function to calculate distance, then simply divides by kilometresInAMile 
KeywordChecker    | isAKeyword(String s)                                                               | O(log n)   | Binary search through keywords (that are sorted alphabetically) to find match with given keyword.<br/>`n` is amount of keywords 
StringFormatter   | convertAccentsFaster(String s)                                                     | O(1)           | Although worst case is O(n), using hashing, chances of collisions in a hashtable of 500 is low<br>Calculates hash value from value of char modulo 500 
