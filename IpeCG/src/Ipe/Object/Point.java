package Ipe.Object;

public class Point {
    public String x;
    public String y;
    public String type;

    public Point(String x, String y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Point(String x, String y) {
        this.x = x;
        this.y = y;
    }

    public Point(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        if(type.equals("a")) {
            return "64 0 0 64 " + x + " " + y + " " + (Double.parseDouble(x) - 64) + " " + y + " " + type;
        }
        else if(type.equals("e")) {
            return "16 0 0 16 " + x + " " + y + " " + type;
        }
        else if(type.equals("h")) {
            return "h";
        }
        return x + " " + y + " " + type;
    }
}
