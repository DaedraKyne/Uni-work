package uk.ac.warwick.cs126.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestRunner {

    public static void main(String[] args) {
        // Initialise and run tests, comment out if you want to omit those tests
        new TestTheConstructorsAndInitializers();
        new TestTheCustomerStore();
        new TestTheFavouriteStore();
        new TestTheRestaurantStore();
        new TestTheReviewStore();
        new TestTheUtils();
    }

    static InputStream loadData(String filename) {
        try {
            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
            String filePath = Paths.get(currentPath, "data", filename).toString();
            File initialFile = new File(filePath);
            return new FileInputStream(initialFile);
        } catch (FileNotFoundException e) {
            System.out.println("\n [FAILED]    Could not load file:");
            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
            String filePath = Paths.get(currentPath, "data", filename).toString();
            System.out.println("             " + filePath);
            return null;
        }
    }

    static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            System.out.println("\n [FAILED]    Could not parse date.");
            return null;
        }
    }
}
