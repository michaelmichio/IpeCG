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
        if(type != null) {
            return x + " " + y + " " + type;
        }
        return x + " " + y;
    }
}
