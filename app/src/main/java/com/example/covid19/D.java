package com.example.covid19;

public class D {
    String country,code ;
    int confirmed ,recovered ,death;

    D(String c,int con,int rec,int dea,String cCode){
        confirmed=con;
        country=c;
        recovered=rec;
        death=dea;
        code=cCode;
    }
    public String getCountry(){
        return country;
    }

    public int getConfirmed(){
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

}
