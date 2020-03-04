package uk.ac.warwick.cs126.test;

import uk.ac.warwick.cs126.stores.*;
import uk.ac.warwick.cs126.util.*;

public class TestTheConstructorsAndInitializers extends TestRunner {

    TestTheConstructorsAndInitializers() {
        System.out.println("\n[Testing Constructors And Initializers]");

        // Run tests, comment out if you want to omit a test, feel free to modify or add more.
        testCustomerStoreConstructor();
        testFavouriteStoreConstructor();
        testRestaurantStoreConstructor();
        testReviewStoreConstructor();
        testConvertToPlaceConstructor();
        testDataCheckerConstructor();
        testHaversineDistanceCalculatorConstructor();
        testKeywordCheckerConstructor();
        testCustomerStoreConstructor();
        testStringFormatterInitialize();
    }

    private void testCustomerStoreConstructor() {
        try {
            new CustomerStore();
            System.out.println("[SUCCESS]    CustomerStore: Instance created.");
        } catch (Exception e) {
            System.out.println(" [FAILED]    CustomerStore: Failed to create instance.");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testFavouriteStoreConstructor() {
        try {
            new FavouriteStore();
            System.out.println("[SUCCESS]    FavouriteStore: Instance created.");
        } catch (Exception e) {
            System.out.println(" [FAILED]    FavouriteStore: Failed to create instance.");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testRestaurantStoreConstructor() {
        try {
            new RestaurantStore();
            System.out.println("[SUCCESS]    RestaurantStore: Instance created.");
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: Failed to create instance.");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testReviewStoreConstructor() {
        try {
            new ReviewStore();
            System.out.println("[SUCCESS]    ReviewStore: Instance created.");
        } catch (Exception e) {
            System.out.println(" [FAILED]    ReviewStore: Failed to create instance");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testConvertToPlaceConstructor() {
        try {
            new ConvertToPlace();
            System.out.println("[SUCCESS]    ConvertToPlace: Instance created.");
        } catch (Exception e) {
            System.out.println(" [FAILED]    ConvertToPlace: Failed to create instance");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testDataCheckerConstructor() {
        try {
            new DataChecker();
            System.out.println("[SUCCESS]    DataChecker: Instance created.");
        } catch (Exception e) {
            System.out.println(" [FAILED]    DataChecker: Failed to create instance");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testHaversineDistanceCalculatorConstructor() {
        try {
            HaversineDistanceCalculator.inKilometres(0.0f,0.0f,0.0f,0.0f);
            HaversineDistanceCalculator.inMiles(0.0f,0.0f,0.0f,0.0f);
            System.out.println("[SUCCESS]    HaversineDistanceCalculator: Instance ran.");
        } catch (Exception e) {
            System.out.println(" [FAILED]    HaversineDistanceCalculator: Failed to run instance");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testKeywordCheckerConstructor() {
        try {
            new KeywordChecker();
            System.out.println("[SUCCESS]    KeywordChecker: Instance created.");
        } catch (Exception e) {
            System.out.println(" [FAILED]    KeywordChecker: Failed to create instance");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testStringFormatterInitialize() {
        try {
            StringFormatter.convertAccents("");
            StringFormatter.convertAccentsFaster("");
            System.out.println("[SUCCESS]    StringFormatter: Instance ran.");
        } catch (Exception | ExceptionInInitializerError e) {
            System.out.println(" [FAILED]    StringFormatter: Failed to run instance");
            e.printStackTrace();
            System.out.println();
        }
    }
}
