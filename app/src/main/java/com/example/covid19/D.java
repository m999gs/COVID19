package com.example.covid19;

public class D {
    String country ,confirmed ,recovered ,death,code;
    D(String c,String con,String rec,String dea,String cCode){
        confirmed=con;
        country=c;
        recovered=rec;
        death=dea;
        code=cCode;
    }
    public String getCountry(){
        return country;
    }

    public String getConfirmed(){
        return confirmed;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getDeath() {
        return death;
    }

    public String getCode() {
        return code;
    }


}
