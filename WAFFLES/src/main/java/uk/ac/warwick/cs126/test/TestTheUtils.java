package uk.ac.warwick.cs126.test;

import uk.ac.warwick.cs126.util.*;
import uk.ac.warwick.cs126.models.*;

public class TestTheUtils extends TestRunner {
    TestTheUtils() {
        System.out.println("\n[Testing Utils]");

        // Run tests, comment out if you want to omit a test, feel free to modify or add more.
        testConvertToPlace();
        testDataCheckerOnValidIDAndExtractID();
        testDataCheckerOnValidCustomer();
        testDataCheckerOnValidFavourite();
        testDataCheckerOnValidRestaurant();
        testDataCheckerOnValidReview();
        testHaversineDistanceCalculator();
        testKeywordChecker();
        testStringFormatterCorrect();
        testStringFormatterIsFaster();
    }

    private void testConvertToPlace() {
        try {
            ConvertToPlace convertToPlace = new ConvertToPlace();

            Place place = convertToPlace.convert(52.38238f, -1.567791f);

            boolean result = place.getName().equals("Warwick, England")
                    && place.getPostcode().equals("CV4 7SH");

            if (result) {
                System.out.println("[SUCCESS]    ConvertToPlace: testConvertToPlace()");
            } else {
                System.out.println(" [FAILED]    ConvertToPlace: testConvertToPlace()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    ConvertToPlace: testConvertToPlace()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testDataCheckerOnValidIDAndExtractID() {
        try {
            DataChecker DataChecker = new DataChecker();

            boolean isInvalid = !DataChecker.isValid(1112223334445555L);
            boolean isValid = DataChecker.isValid(1112223334445556L);

            Long trueID = DataChecker.extractTrueID(
                    new String[]{"1112223334445556", "1112223334445556", "1112223334445556"});

            boolean result = isInvalid && isValid && trueID.equals(1112223334445556L);

            if (result) {
                System.out.println("[SUCCESS]    DataChecker: testDataCheckerOnValidIDAndExtractID()");
            } else {
                System.out.println(" [FAILED]    DataChecker: testDataCheckerOnValidIDAndExtractID()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    DataChecker: testDataCheckerOnValidIDAndExtractID()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testDataCheckerOnValidCustomer() {
        try {
            DataChecker DataChecker = new DataChecker();

            Customer nullCustomer = null;
            boolean isInvalid = !DataChecker.isValid(nullCustomer);

            Customer validCustomer = new Customer(
                    1112223334445556L,
                    "Elizabeth II",
                    "",
                    parseDate("1926-04-21"),
                    52.38380f,
                    -1.560065f
            );
            boolean isValid = DataChecker.isValid(validCustomer);

            boolean result = isInvalid && isValid;

            if (result) {
                System.out.println("[SUCCESS]    DataChecker: testDataCheckerOnValidCustomer()");
            } else {
                System.out.println(" [FAILED]    DataChecker: testDataCheckerOnValidCustomer()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    DataChecker: testDataCheckerOnValidCustomer()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testDataCheckerOnValidFavourite() {
        try {
            DataChecker DataChecker = new DataChecker();

            Favourite nullFavourite = null;
            boolean isInvalid = !DataChecker.isValid(nullFavourite);

            Favourite validFavourite = new Favourite(
                    1112223334445556L,
                    1112223334445557L,
                    1112223334445558L,
                    parseDate("2020-12-31")
            );
            boolean isValid = DataChecker.isValid(validFavourite);

            boolean result = isInvalid && isValid;

            if (result) {
                System.out.println("[SUCCESS]    DataChecker: testDataCheckerOnValidFavourite()");
            } else {
                System.out.println(" [FAILED]    DataChecker: testDataCheckerOnValidFavourite()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    DataChecker: testDataCheckerOnValidFavourite()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testDataCheckerOnValidRestaurant() {
        try {
            DataChecker DataChecker = new DataChecker();

            Restaurant nullRestaurant = null;
            boolean isInvalid = !DataChecker.isValid(nullRestaurant);

            Restaurant validRestaurant = new Restaurant(
                    "111222333444555611122233344455561112223334445556",
                    "The Queen Victoria",
                    "Peggy",
                    "Mitchell",
                    Cuisine.British,
                    EstablishmentType.Pub,
                    PriceRange.CheapEats,
                    parseDate("2016-05-17"),
                    52.3838f,
                    -1.560065f,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    parseDate("2020-01-01"),
                    3,
                    0,
                    3.0f
            );
            validRestaurant.setID(1112223334445556L);
            boolean isValid = DataChecker.isValid(validRestaurant);

            boolean result = isInvalid && isValid;

            if (result) {
                System.out.println("[SUCCESS]    DataChecker: testDataCheckerOnValidRestaurant()");
            } else {
                System.out.println(" [FAILED]    DataChecker: testDataCheckerOnValidRestaurant()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    DataChecker: testDataCheckerOnValidRestaurant()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testDataCheckerOnValidReview() {
        try {
            DataChecker DataChecker = new DataChecker();

            Review nullReview = null;
            boolean isInvalid = !DataChecker.isValid(nullReview);

            Review validReview = new Review(
                    1112223334445556L,
                    1112223334445557L,
                    1112223334445558L,
                    parseDate("2020-06-06"),
                    "Okay",
                    3
            );
            boolean isValid = DataChecker.isValid(validReview);

            boolean result = isInvalid && isValid;

            if (result) {
                System.out.println("[SUCCESS]    DataChecker: testDataCheckerOnValidReview()");
            } else {
                System.out.println(" [FAILED]    DataChecker: testDataCheckerOnValidReview()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    DataChecker: testDataCheckerOnValidReview()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testHaversineDistanceCalculator() {
        try {
            float distanceInKM = HaversineDistanceCalculator.inKilometres(
                    52.3838f, -1.560065f, 52.379049f, -1.560898f);
            float distanceInMiles = HaversineDistanceCalculator.inMiles(
                    52.3838f, -1.560065f, 52.379049f, -1.560898f);

            boolean result = String.valueOf(distanceInKM).equals("0.5")
                    && String.valueOf(distanceInMiles).equals("0.3");

            if (result) {
                System.out.println("[SUCCESS]    HaversineDistanceCalculator: testHaversineDistanceCalculator()");
            } else {
                System.out.println(" [FAILED]    HaversineDistanceCalculator: testHaversineDistanceCalculator()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    HaversineDistanceCalculator: testHaversineDistanceCalculator()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testKeywordChecker() {
        try {
            KeywordChecker keywordChecker = new KeywordChecker();

            boolean isAKeyword = keywordChecker.isAKeyword("excellent");
            boolean isNotAKeyword = !keywordChecker.isAKeyword("qwerty");

            boolean result = isAKeyword && isNotAKeyword;

            if (result) {
                System.out.println("[SUCCESS]    KeywordChecker: testKeywordChecker()");
            } else {
                System.out.println(" [FAILED]    KeywordChecker: testKeywordChecker()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    KeywordChecker: testKeywordChecker()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testStringFormatterCorrect() {
        try {
            boolean result = StringFormatter.convertAccentsFaster("Á").equals("A");

            if (result) {
                System.out.println("[SUCCESS]    StringFormatter: testStringFormatterCorrect()");
            } else {
                System.out.println(" [FAILED]    StringFormatter: testStringFormatterCorrect()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    StringFormatter: testStringFormatterCorrect()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testStringFormatterIsFaster() {
        try {
            String allAccents = "ÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁÁ";
            String convertedAccents = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

            boolean result = true;
            int warmUpRuns = 100;
            int numRuns = 5000;

            // Warm up original method
            for (int n = 0; n < warmUpRuns; n++) {
                result = StringFormatter.convertAccents(allAccents).equals(convertedAccents);
                if (!result) {
                    break;
                }

            }

            // Time original method
            long startTimeOfOriginal = System.currentTimeMillis();
            for (int n = 0; n < numRuns; n++) {
                result = StringFormatter.convertAccents(allAccents).equals(convertedAccents);
                if (!result) {
                    break;
                }

            }
            long endTimeOfOriginal = System.currentTimeMillis();

            // Warm up faster method
            for (int n = 0; n < warmUpRuns; n++) {
                result = StringFormatter.convertAccentsFaster(allAccents).equals(convertedAccents);
                if (!result) {
                    break;
                }

            }

            // Time faster method
            long startTimeOfFaster = System.currentTimeMillis();
            for (int n = 0; n < numRuns; n++) {
                result = StringFormatter.convertAccentsFaster(allAccents).equals(convertedAccents);
                if (!result) {
                    break;
                }

            }
            long endTimeOfFaster = System.currentTimeMillis();

            long originalTime = endTimeOfOriginal - startTimeOfOriginal;
            long fasterTime = endTimeOfFaster - startTimeOfFaster;
            double speedUp = (double) originalTime / fasterTime;

            boolean isFaster = speedUp > 5;

            if (result && !isFaster) {
                System.out.println(
                        String.format("\n       convertAccents() Time: %.3f secs", originalTime / 1000.0));
                System.out.println(
                        String.format(" convertAccentsFaster() Time: %.3f secs", fasterTime / 1000.0));
                System.out.println(
                        String.format("                    Speed-Up: %.1f x", speedUp));
            }

            if (result && isFaster) {
                System.out.println("[SUCCESS]    StringFormatter: testStringFormatterIsFaster()");
            } else {
                System.out.println(" [FAILED]    StringFormatter: testStringFormatterIsFaster()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    StringFormatter: testStringFormatterIsFaster()");
            e.printStackTrace();
            System.out.println();
        }
    }
}
