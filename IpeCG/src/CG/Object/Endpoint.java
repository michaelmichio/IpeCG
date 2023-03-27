package CG.Object;

public class Endpoint {
    public double x;
    public double y;
    public int segmenIdx;
    public int intersectSegmentIndex;
    public int type;

    public Endpoint(double x, double y, int segmenIdx, int type) {
        this.x = x;
        this.y = y;
        this.segmenIdx = segmenIdx;
        this.type = type;
    }

    public Endpoint(double x, double y, int segmenIdx, int intersectSegmentIndex, int type) {
        this.x = x;
        this.y = y;
        this.segmenIdx = segmenIdx;
        this.intersectSegmentIndex = intersectSegmentIndex;
        this.type = type;
    }
}
