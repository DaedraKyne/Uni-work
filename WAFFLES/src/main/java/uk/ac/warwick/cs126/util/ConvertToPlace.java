package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IConvertToPlace;
import uk.ac.warwick.cs126.models.Place;
import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyAvlTree;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

public class ConvertToPlace implements IConvertToPlace {

    private MyAvlTree<Float, MyArrayList<Place>> placesTree;

    public ConvertToPlace() {
        System.out.println("\n\nInstantiating");
        // Initialise things here
        placesTree = new MyAvlTree<Float, MyArrayList<Place>>();
        Place[] places = getPlacesArray();
        System.out.println("Looping times: " + places.length);
        MyArrayList<Place> equal_places;
        for (int i = 0; i < places.length; i++) {
            if (i % 1000 == 0) {
                System.out.println(i);
            }
            Place place = places[i];
            equal_places = placesTree.getData(place.getLatitude());
            if (equal_places == null) {
                equal_places = new MyArrayList<Place>();
                equal_places.add(place);
                placesTree.add(place.getLatitude(), equal_places);
            }
            else {
                equal_places.add(place);
                placesTree.setData(place.getLatitude(), equal_places);
            }
        }
    }

    public Place convert(float latitude, float longitude) {
        // TODO
        System.out.print("\n\nCONVERTING");
        MyArrayList<Place> equal_places = placesTree.getData(latitude);
        if (equal_places != null) {
            for (int i = 0; i < equal_places.size(); i++) {
                System.out.println("Checking in array list");
                if (equal_places.get(i).getLongitude() == longitude) {
                    return equal_places.get(i);
                }
            }
        } 
        return new Place("", "", 0.0f, 0.0f);
    }

    public Place[] getPlacesArray() {
        Place[] placeArray = new Place[0];

        try {
            InputStream resource = ConvertToPlace.class.getResourceAsStream("/data/placeData.tsv");
            if (resource == null) {
                String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
                String resourcePath = Paths.get(currentPath, "data", "placeData.tsv").toString();
                File resourceFile = new File(resourcePath);
                resource = new FileInputStream(resourceFile);
            }

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

            Place[] loadedPlaces = new Place[lineCount - 1];

            BufferedReader tsvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int placeCount = 0;
            String row;

            tsvReader.readLine();
            while ((row = tsvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split("\t");
                    Place place = new Place(
                            data[0],
                            data[1],
                            Float.parseFloat(data[2]),
                            Float.parseFloat(data[3]));
                    loadedPlaces[placeCount++] = place;
                }
            }
            tsvReader.close();

            placeArray = loadedPlaces;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return placeArray;
    }
}

