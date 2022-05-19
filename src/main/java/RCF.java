public class RCF implements Comparable<RCF> {
    public int id;
    public int year;
    public int mileage;
    public double price;
    public String color;
    public double dealScore;

    public RCF() {
        year = 0;
        mileage = 0;
        price = 0;
        color = "";
        dealScore = 0.0;
    }

    public RCF(int id, int y, int m, double p, String c, double d) {
        this.id = id;
        year = y;
        mileage = m;
        price = p;
        color = c;
        dealScore = d;
    }

    @Override
    public int compareTo(RCF o) {
        return (int) (price - o.price);
    }
}
