package CG.Object;

public class LineSegment {
    public Point p1;
    public Point p2;

    public LineSegment(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public double crossProductToPoint(Point p) {
        double Bx = this.p2.x;
        double By = this.p2.y;
        double Px = p.x;
        double Py = p.y;
        Bx -= this.p1.x;
        By -= this.p1.y;
        Px -= this.p1.x;
        Py -= this.p1.y;

        return Bx * Py - By * Px;
    }

    public Point getIntersectPoint(LineSegment ls) {
        Point point = new Point();
        if (isIntersect(ls)) {
            double denom = (ls.p2.y-ls.p1.y)*(p2.x-p1.x)-(ls.p2.x-ls.p1.x)*(p2.y-p1.y);
            double a = ((ls.p2.x-ls.p1.x)*(p1.y-ls.p1.y)-(ls.p2.y-ls.p1.y)*(p1.x-ls.p1.x))/denom;
            // double b = ((p2.x-p1.x)*(p1.y-ls.p1.y)-(p2.y-p1.y)*(p1.x-ls.p1.x))/denom;
            return new Point((p1.x+a*(p2.x-p1.x)), (p1.y+a*(p2.y-p1.y)));
        }
        return point;
    }

    public boolean isIntersect(LineSegment ls) {
        double x1 = p1.x;
        double y1 = p1.y;
        double x2 = p2.x;
        double y2 = p2.y;
        double x3 = ls.p1.x;
        double y3 = ls.p1.y;
        double x4 = ls.p2.x;
        double y4 = ls.p2.y;

        // Returns true if segment 1-2 intersects segment 3-4

        if (x1 == x2 && x3 == x4) {
            // Both segments are vertical
            if (x1 != x3) return false;
            if (min(y1,y2) < min(y3,y4)) {
                return max(y1,y2) >= min(y3,y4);
            } else {
                return max(y3,y4) >= min(y1,y2);
            }
        }
        if (x1 == x2) {
            // Only segment 1-2 is vertical. Does segment 3-4 cross it? y = a*x + b
            double a34 = (y4-y3)/(x4-x3);
            double b34 = y3 - a34*x3;
            double y = a34 * x1 + b34;
            return y >= min(y1,y2) && y <= max(y1,y2) && x1 >= min(x3,x4) && x1 <= max(x3,x4);
        }
        if (x3 == x4) {
            // Only segment 3-4 is vertical. Does segment 1-2 cross it? y = a*x + b
            double a12 = (y2-y1)/(x2-x1);
            double b12 = y1 - a12*x1;
            double y = a12 * x3 + b12;
            return y >= min(y3,y4) && y <= max(y3,y4) && x3 >= min(x1,x2) && x3 <= max(x1,x2);
        }
        double a12 = (y2-y1)/(x2-x1);
        double b12 = y1 - a12*x1;
        double a34 = (y4-y3)/(x4-x3);
        double b34 = y3 - a34*x3;
        if (closeToZero(a12 - a34)) {
            // Parallel lines
            return closeToZero(b12 - b34);
        }
        // Non parallel non vertical lines intersect at x. Is x part of both segments?
        double x = -(b12-b34)/(a12-a34);
        return x >= min(x1,x2) && x <= max(x1,x2) && x >= min(x3,x4) && x <= max(x3,x4);
    }

    public double min(double... v) {
        double ans = v[0];
        for (int i=1; i<v.length; i++) {
            ans = Math.min(ans, v[i]);
        }
        return ans;
    }

    public double max(double... v) {
        double ans = v[0];
        for (int i=1; i<v.length; i++) {
            ans = Math.max(ans, v[i]);
        }
        return ans;
    }

    public boolean closeToZero(double v) {
        // Check if double is close to zero, considering precision issues.
        return Math.abs(v) <= 0.0000000000001;
    }
}
