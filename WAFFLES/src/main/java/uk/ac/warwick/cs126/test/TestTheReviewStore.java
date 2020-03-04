package uk.ac.warwick.cs126.test;

import uk.ac.warwick.cs126.models.Review;
import uk.ac.warwick.cs126.stores.ReviewStore;

public class TestTheReviewStore extends TestRunner {
    TestTheReviewStore() {
        System.out.println("\n[Testing ReviewStore]");

        // Run tests, comment out if you want to omit a test, feel free to modify or add more.
        testAddReview();
        testAddReviews();
        testGetReview();
        testGetReviews();
        testGetReviewsByDate();
        testGetReviewsByRating();
        testGetReviewsByRestaurantID();
        testGetReviewsByCustomerID();
        testGetAverageCustomerReviewRating();
        testGetAverageRestaurantReviewRating();
        testGetCustomerReviewHistogramCount();
        testGetRestaurantReviewHistogramCount();
        testGetTopCustomersByReviewCount();
        testGetTopRestaurantsByReviewCount();
        testGetTopRatedRestaurants();
        testGetTopKeywordsForRestaurant();
        testGetReviewsContaining();
    }

    private void testAddReview() {
        try {
            // Initialise new store
            ReviewStore reviewStore = new ReviewStore();

            // Create a review object
            // Review(Long reviewID,
            //        Long customerID,
            //        Long restaurantID,
            //        Date dateReviewed,
            //        String review,
            //        int rating)
            Review review = new Review(
                    1112223334445556L,
                    1112223334445557L,
                    1112223334445558L,
                    parseDate("2020-12-31"),
                    "Awesome",
                    5
            );

            // Add to store
            boolean result = reviewStore.addReview(review);

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testAddReview()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testAddReview()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testAddReview()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testAddReviews() {
        try {
            // Initialise new store
            ReviewStore reviewStore = new ReviewStore();

            // Load test data from /data folder
            Review[] reviews = reviewStore.loadReviewDataToArray(
                    loadData("/test-review/review-10.tsv"));

            // Add to store to be processed, should return true as all the data is valid
            boolean result = reviewStore.addReview(reviews);

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testAddReviews()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testAddReviews()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testAddReviews()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetReview() {
        try {
            // Initialise new store
            ReviewStore reviewStore = new ReviewStore();

            // Load test data from /data folder
            Review[] reviews = reviewStore.loadReviewDataToArray(
                    loadData("/test-review/review-10.tsv"));

            // Add to store to be processed
            reviewStore.addReview(reviews);

            // Should return true as the review with ID 2835272316399964 exists
            boolean result = reviewStore.getReview(2835272316399964L) != null
                    && reviewStore.getReview(2835272316399964L).getID().equals(2835272316399964L);

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testGetReview()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testGetReview()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testGetReview()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetReviews() {
        try {
            // Initialise new store
            ReviewStore reviewStore = new ReviewStore();

            // Load test data from /data folder
            Review[] reviews = reviewStore.loadReviewDataToArray(
                    loadData("/test-review/review-10.tsv"));

            // Add to store to be processed
            reviewStore.addReview(reviews);

            // Get reviews sorted by ID from store
            Review[] gotReviews = reviewStore.getReviews();

            // Load manually sorted data from /data folder to verify with
            Review[] expectedReviews = reviewStore.loadReviewDataToArray(
                    loadData("/test-review/review-10-sorted-by-id.tsv"));

            // Now we compare
            boolean result = true;
            if (gotReviews.length == expectedReviews.length) {
                for (int i = 0; i < expectedReviews.length; i++) {
                    result = gotReviews[i].getID().equals(expectedReviews[i].getID());
                    if (!result) {
                        break;
                    }
                }
            } else {
                result = false;
            }

            // Print if wrong
            if (!result) {
                System.out.println("\n[Expected]");
                for (Review r : expectedReviews) {
                    System.out.println(r);
                }

                System.out.println("\n[Got]");
                if (gotReviews.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Review r : gotReviews) {
                    System.out.println(r);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testGetReviews()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testGetReviews()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testGetReviews()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetReviewsByDate() {
        try {
            //TODO
            ReviewStore reviewStore = new ReviewStore();

            boolean result = false;

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testGetReviewsByDate()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testGetReviewsByDate()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testGetReviewsByDate()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetReviewsByRating() {
        try {
            //TODO
            ReviewStore reviewStore = new ReviewStore();

            boolean result = false;

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testGetReviewsByRating()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testGetReviewsByRating()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testGetReviewsByRating()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetReviewsByRestaurantID() {
        try {
            //TODO
            ReviewStore reviewStore = new ReviewStore();

            boolean result = false;

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testGetReviewsByRestaurantID()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testGetReviewsByRestaurantID()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testGetReviewsByRestaurantID()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetReviewsByCustomerID() {
        try {
            //TODO
            ReviewStore reviewStore = new ReviewStore();

            boolean result = false;

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testGetReviewsByCustomerID()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testGetReviewsByCustomerID()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testGetReviewsByCustomerID()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetAverageCustomerReviewRating() {
        try {
            //TODO
            ReviewStore reviewStore = new ReviewStore();

            boolean result = false;

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testGetAverageCustomerReviewRating()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testGetAverageCustomerReviewRating()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testGetAverageCustomerReviewRating()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetAverageRestaurantReviewRating() {
        try {
            //TODO
            ReviewStore reviewStore = new ReviewStore();

            boolean result = false;

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testGetAverageRestaurantReviewRating()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testGetAverageRestaurantReviewRating()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testGetAverageRestaurantReviewRating()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantReviewHistogramCount() {
        try {
            //TODO
            ReviewStore reviewStore = new ReviewStore();

            boolean result = false;

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testGetRestaurantReviewHistogramCount()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testGetRestaurantReviewHistogramCount()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testGetRestaurantReviewHistogramCount()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetCustomerReviewHistogramCount() {
        try {
            //TODO
            ReviewStore reviewStore = new ReviewStore();

            boolean result = false;

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testGetCustomerReviewHistogramCount()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testGetCustomerReviewHistogramCount()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testGetCustomerReviewHistogramCount()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetTopRestaurantsByReviewCount() {
        try {
            //TODO
            ReviewStore reviewStore = new ReviewStore();

            boolean result = false;

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testGetTopRestaurantsByReviewCount()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testGetTopRestaurantsByReviewCount()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testGetTopRestaurantsByReviewCount()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetTopCustomersByReviewCount() {
        try {
            //TODO
            ReviewStore reviewStore = new ReviewStore();

            boolean result = false;

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testGetTopCustomersByReviewCount()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testGetTopCustomersByReviewCount()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testGetTopCustomersByReviewCount()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetTopRatedRestaurants() {
        try {
            //TODO
            ReviewStore reviewStore = new ReviewStore();

            boolean result = false;

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testGetTopRatedRestaurants()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testGetTopRatedRestaurants()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testGetTopRatedRestaurants()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetTopKeywordsForRestaurant() {
        try {
            //TODO
            ReviewStore reviewStore = new ReviewStore();

            boolean result = false;

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testGetTopKeywordsForRestaurant()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testGetTopKeywordsForRestaurant()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testGetTopKeywordsForRestaurant()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetReviewsContaining() {
        try {
            //TODO
            ReviewStore reviewStore = new ReviewStore();

            boolean result = false;

            if (result) {
                System.out.println("[SUCCESS]    ReviewStore: testGetReviewsContaining()");
            } else {
                System.out.println(" [FAILED]    ReviewStore: testGetReviewsContaining()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: testGetReviewsContaining()");
            e.printStackTrace();
            System.out.println();
        }
    }
}
