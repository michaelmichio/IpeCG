package CG.Object;

public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceToOtherPoint(Point p) {
        return Math.hypot(this.x - p.x, this.y - p.y);
    }

    public double distanceToOtherPoint(double x2, double y2) {
        return Math.hypot(this.x - x2, this.y - y2);
    }

    public double distanceToLineSegment(LineSegment lineSegment) {
        double A = x - lineSegment.p1.x;
        double B = y - lineSegment.p1.y;
        double C = lineSegment.p2.x - lineSegment.p1.x;
        double D = lineSegment.p2.y - lineSegment.p1.y;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = -1;
        if (len_sq != 0) //in case of 0 length line
            param = dot / len_sq;

        double xx, yy;

        if (param < 0) {
            xx = lineSegment.p1.x;
            yy = lineSegment.p1.y;
        }
        else if (param > 1) {
            xx = lineSegment.p2.x;
            yy = lineSegment.p2.y;
        }
        else {
            xx = lineSegment.p1.x + param * C;
            yy = lineSegment.p1.y + param * D;
        }

        double dx = x - xx;
        double dy = y - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double distanceToLineSegment(Point p1, Point p2) {
        double A = x - p1.x;
        double B = y - p1.y;
        double C = p2.x - p1.x;
        double D = p2.y - p1.y;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = -1;
        if (len_sq != 0) //in case of 0 length line
            param = dot / len_sq;

        double xx, yy;

        if (param < 0) {
            xx = p1.x;
            yy = p1.y;
        }
        else if (param > 1) {
            xx = p2.x;
            yy = p2.y;
        }
        else {
            xx = p1.x + param * C;
            yy = p1.y + param * D;
        }

        double dx = x - xx;
        double dy = y - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double distanceToLineSegment(double x1, double y1, double x2, double y2) {
        double A = x - x1;
        double B = y - y1;
        double C = x2 - x1;
        double D = y2 - y1;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = -1;
        if (len_sq != 0) //in case of 0 length line
            param = dot / len_sq;

        double xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        }
        else if (param > 1) {
            xx = x2;
            yy = y2;
        }
        else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        double dx = x - xx;
        double dy = y - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return this.x + " " + this.y;
    }
}
