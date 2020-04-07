package com.example.covid19;

public class D {
    private String country, code;
    private int confirmed, recovered, death;
    private double latitude, longitude;

    D(String c, int con, int rec, int dea, String cCode, String lati, String longi) {
        confirmed = con;
        country = c;
        recovered = rec;
        death = dea;
        code = cCode;
        latitude = Double.parseDouble(lati);
        longitude = Double.parseDouble(longi);
    }

    public String getCountry() {
        return country;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public int getRecovered() {
        return recovered;
    }

    public int getDeath() {
        return death;
    }

    public String getCode() {
        return code;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


}
