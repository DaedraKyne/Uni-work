package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IConvertToPlace;
import uk.ac.warwick.cs126.models.Place;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

import java.lang.Math;

public class ConvertToPlace implements IConvertToPlace {

    private Place[] places;
    private int[][] limiter;

    public ConvertToPlace() {
        // Initialise things here
        places = getPlacesArray();
        limiter = new int[12][2];
        int level;
        Place place;
        for (int i = 0; i < limiter.length; i++) {
            limiter[i][0] = 0;
            limiter[i][1] = 0;
        }
        for (int i = 0; i < places.length; i++) {            
            place = places[i];
            level = (int) Math.floor(place.getLatitude()) - 49;
            if (limiter[level][0] == 0) {
                limiter[level][0] = i;
            }
            limiter[level][1] = i;
        }
    }

    public Place convert(float latitude, float longitude) {
        // TODO
        int level = (int) Math.floor(latitude) - 49;
        int start = limiter[level][0];
        int end = limiter[level][1];
        Place place;
        for (int i = start; i < end; i++) {
            place = places[i];
            if (place.getLatitude() == latitude && place.getLongitude() == longitude) {
                return place;
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

