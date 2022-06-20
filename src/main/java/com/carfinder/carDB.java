package com.carfinder;

import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class carDB {
    final private boolean insSQL;
    final private String url;
    private Connection connect = null;

    public carDB(String link, boolean insertSQL) {
        this.url = link;
        this.insSQL = insertSQL;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/rcf","root","password");
        } catch (Exception e) {
            System.out.println("Connection not made : " + e.toString());
        }

        // Setup the connection with the DB

    }

    public void getListings() {

        ChromeDriverManager.getInstance(DriverManagerType.CHROME).setup();
        WebDriver d = new ChromeDriver();

        d.get(url);

        String pageHtml = d.getPageSource();



        int start = pageHtml.indexOf("window.__PREFLIGHT__ = ");
        int end = pageHtml.indexOf("window.__PREFLIGHT__.pageTitle");

        JSONObject data = new JSONObject(pageHtml.substring(start+23, end));


        //System.out.println(data.get("totalListings"));

        JSONArray listings = (JSONArray) data.get("listings");
        //System.out.println(listings.get(0));

        ArrayList<RCF> Fs = new ArrayList<RCF>();

        int id;
        int year;
        int mileage;
        double price;
        String color;
        double dealScore;
        String sellerName;
        String sellerCity;
        String zipcode;
        for(int i = 0; i < listings.length(); i++) {
            data = (JSONObject) listings.get(i);
            try { id = (int)data.get("id"); } catch(Exception e) { id = (int)(Math.random() * 999999);}
            try { year = (int)data.get("carYear"); } catch(Exception e) { year = -1;}
            try { mileage = (int)data.get("mileage"); } catch(Exception e) { mileage = -1;}
            try { price = ((BigDecimal)data.get("price")).doubleValue(); } catch(Exception e) { price = -1;}
            try { color = (String)data.get("exteriorColorName"); } catch(Exception e) { color = "UNKWN";}
            try { dealScore = ((BigDecimal)data.get("dealScore")).doubleValue(); } catch(Exception e) { dealScore = -99;}
            try {
                sellerName = (String)data.get("serviceProviderName");
                if(sellerName.length() > 50) {
                    sellerName = sellerName.substring(0,50);
                }
            } catch(Exception e) {
                sellerName = "UNKWN";
            }
            try { sellerCity = (String)data.get("sellerCity"); } catch(Exception e) { sellerCity = "UNKWN";}
            try { zipcode = (String)data.get("sellerPostalCode"); } catch(Exception e) { zipcode = "UNKWN";}

            Fs.add(new RCF(id,year,mileage,price,color,dealScore,sellerName,sellerCity,zipcode));
        }

        if(insSQL) {
            try {
                Statement stm = connect.createStatement();
                for(RCF car : Fs) {
                    String query = "INSERT into guru (id, year, mileage, price, color, dealscore, lastseen, sellername, sellercity, zipcode)"
                            + " VALUES ("
                            + car.id + ","
                            + car.year + ","
                            + car.mileage + ","
                            + (int) car.price + ","
                            + "'" + car.color + "',"
                            + car.dealScore + ","
                            + "'" + car.date + "',"
                            + "'" + car.sellerName + "',"
                            + "'" + car.sellerCity + "',"
                            + "'" + car.zipcode + "')"
                            + "\nON DUPLICATE KEY UPDATE"
                            + " year=" + car.year + ","
                            + " mileage=" + car.mileage + ","
                            + " price=" + (int) car.price + ","
                            + " color='" + car.color + "',"
                            + " dealscore=" + car.dealScore + ","
                            + " lastseen='" + car.date + "',"
                            + " sellername='" + car.sellerName + "',"
                            + " sellercity='" + car.sellerCity + "',"
                            + " zipcode='" + car.zipcode + "';";
                    //System.out.println(query);
                    stm.execute(query);
                }
            } catch(Exception e) {
                System.out.println("Failed to insert to table : " + e.toString());
            }

        } else {
            Collections.sort(Fs);

            for(RCF car : Fs) {
                System.out.println("id : " + car.id);
                System.out.println("Year : " + car.year);
                System.out.println("Mileage : " + car.mileage);
                System.out.println("Price : $" + car.price);
                System.out.println("Exterior Color : " + car.color);
                System.out.println("Deal Score : " + car.dealScore);
                System.out.println("Seller City : " + car.sellerCity);
                System.out.println("Seller Name : " + car.sellerName);
                System.out.println("Seller Zip : " + car.zipcode);
                System.out.println("Date : " + car.date);
                System.out.println("-----------------------------------");
            }
        }

        d.quit();

    }
}
