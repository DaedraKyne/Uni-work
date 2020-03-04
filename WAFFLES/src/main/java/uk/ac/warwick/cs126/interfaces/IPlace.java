package uk.ac.warwick.cs126.interfaces;

public interface IPlace {

    String getName();
    String getPostcode();
    float getLatitude();
    float getLongitude();

    void setName(String name);
    void setPostcode(String postcode);
    void setLatitude(float latitude);
    void setLongitude(float longitude);

}
