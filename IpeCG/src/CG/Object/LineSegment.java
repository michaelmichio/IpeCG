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
}
