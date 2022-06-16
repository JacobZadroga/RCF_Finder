

public class main {
    final static String URL = "https://www.cargurus.com/Cars/inventorylisting/viewDetailsFilterViewInventoryListing.action?maxAccidents=0&zip=06037&maxPrice=70000&maxMileage=40000&hideFrameDamaged=true&showNegotiable=true&sortDir=ASC&sourceContext=carGurusHomePageModel&distance=500&sortType=DEAL_SCORE&entitySelectingHelper.selectedEntity=d2297";
    //final static int millis = 10 * 60 * 1000; //10 minute
    final static int millis = 10 * 1000; //10 seconds
    public static void main(String[] args) {
        carDB database = new carDB(URL, true);
        while(true) {

            database.getListings();

            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
