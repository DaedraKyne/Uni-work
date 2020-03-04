package uk.ac.warwick.cs126.interfaces;

import java.util.Date;

public interface ICustomer {

    Long getID();
    String getFirstName();
    String getLastName();
    Date getDateJoined();
    float getLatitude();
    float getLongitude();

    void setID(Long id);
    void setFirstName(String firstName);
    void setLastName(String lastName);
    void setDateJoined(Date dateJoined);
    void setLatitude(float latitude);
    void setLongitude(float longitude);

}
