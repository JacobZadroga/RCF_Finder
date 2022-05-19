import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.github.bonigarcia.wdm.managers.SafariDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.json.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class main {
    final static String URL = "https://www.cargurus.com/Cars/inventorylisting/viewDetailsFilterViewInventoryListing.action?maxAccidents=0&zip=06037&maxPrice=70000&maxMileage=40000&hideFrameDamaged=true&showNegotiable=true&sortDir=ASC&sourceContext=carGurusHomePageModel&distance=500&sortType=DEAL_SCORE&entitySelectingHelper.selectedEntity=d2297";
    public static void main(String[] args) {

        SafariDriverManager.getInstance(DriverManagerType.SAFARI).setup();
        WebDriver d = new SafariDriver();

        d.get(URL);
        
        String pageHtml = d.getPageSource();
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
//            System.out.println("Year : " + data.get("carYear"));
//            System.out.println("Mileage : " + data.get("mileage"));
//            System.out.println("Price : $" + data.get("price"));
//            System.out.println("Exterior Color : " + data.get("exteriorColorName"));
//            System.out.println("Deal Score : " + data.get("dealScore"));
//            System.out.println("-----------------------------------");
        }

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


        d.quit();
    }
}
