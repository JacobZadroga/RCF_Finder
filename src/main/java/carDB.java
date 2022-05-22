import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.github.bonigarcia.wdm.managers.SafariDriverManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class carDB {
    final private boolean insSQL;
    final private String url;

    public carDB(String link, boolean insertSQL) {
        this.url = link;
        this.insSQL = insertSQL;
    }

    public void getListings() {

        SafariDriverManager.getInstance(DriverManagerType.SAFARI).setup();
        WebDriver d = new SafariDriver();

        d.get(url);

        String pageHtml = d.getPageSource();

        d.quit();

        int start = pageHtml.indexOf("window.__PREFLIGHT__ = ");
        int end = pageHtml.indexOf("window.__PREFLIGHT__.pageTitle");

        JSONObject data = new JSONObject(pageHtml.substring(start+23, end));
        for (Iterator<String> it = data.keys(); it.hasNext(); ) {
            String s = it.next();
            //System.out.println(s);
        }


        System.out.println(data.get("totalListings"));

        JSONArray listings = (JSONArray) data.get("listings");
        //System.out.println(listings.get(0));

        ArrayList<RCF> Fs = new ArrayList<RCF>();

        for(int i = 0; i < listings.length(); i++) {
            data = (JSONObject) listings.get(i);
            Fs.add(new RCF((int)data.get("id"),(int)data.get("carYear"),(int)data.get("mileage"),((BigDecimal)data.get("price")).doubleValue(),
                    (String)data.get("exteriorColorName"),((BigDecimal)data.get("dealScore")).doubleValue()));
        }

        if(insSQL) {

        } else {
            Collections.sort(Fs);


            for(RCF car : Fs) {
                System.out.println("id : " + car.id);
                System.out.println("Year : " + car.year);
                System.out.println("Mileage : " + car.mileage);
                System.out.println("Price : $" + car.price);
                System.out.println("Exterior Color : " + car.color);
                System.out.println("Deal Score : " + car.dealScore);
                System.out.println("-----------------------------------");
            }
        }
    }
}
