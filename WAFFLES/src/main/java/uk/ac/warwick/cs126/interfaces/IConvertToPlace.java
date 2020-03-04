package uk.ac.warwick.cs126.interfaces;

import uk.ac.warwick.cs126.models.Place;

public interface IConvertToPlace {

    Place convert(float latitude, float longitude);

    Place[] getPlacesArray();
}
