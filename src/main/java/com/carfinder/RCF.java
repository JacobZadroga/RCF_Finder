package com.carfinder;

public class RCF implements Comparable<RCF> {
    public int id;
    public int year;
    public int mileage;
    public double price;
    public String color;
    public double dealScore;
    public String date;
    public String sellerName;
    public String sellerCity;
    public String zipcode;

    public RCF() {
        year = 0;
        mileage = 0;
        price = 0;
        color = "";
        dealScore = 0.0;
        date = "0000-00-00";
    }

    public RCF(int id, int y, int m, double p, String c, double d, String name, String city, String zip) {
        this.id = id;
        year = y;
        mileage = m;
        price = p;
        color = c;
        dealScore = d;
        date = java.time.LocalDate.now().toString();
        sellerName = name;
        sellerCity = city;
        zipcode = zip;
    }

    @Override
    public int compareTo(RCF o) {
        return (int) (price - o.price);
    }
}
