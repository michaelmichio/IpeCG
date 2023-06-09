package CG.Object;

public class Endpoint {
    public double x;
    public double y;
    public int status; // -1 (intersect point), 0 (start point), 1 (end point)
    public int segmentIndex;
    public int intersectSegmentIndex;

    public Endpoint(double x, double y, int status, int segmentIndex) {
        this.x = x;
        this.y = y;
        this.status = status;
        this.segmentIndex = segmentIndex;
    }

    public Endpoint(double x, double y, int status, int segmentIndex, int intersectSegmentIndex) {
        this.x = x;
        this.y = y;
        this.status = status;
        this.segmentIndex = segmentIndex;
        this.intersectSegmentIndex = intersectSegmentIndex;
    }

    @Override
    public String toString() {
        if (status == 0) {
            return "$p_{" + segmentIndex + "}$";
        }
        else if (status == 1) {
            return "$q_{" + segmentIndex + "}$";
        }
        else {
            if (segmentIndex <= intersectSegmentIndex) {
                return "$e_{" + segmentIndex + "," + intersectSegmentIndex + "}$";
            }
            else {
                return "$e_{" + intersectSegmentIndex + "," + segmentIndex + "}$";
            }
        }
    }

}
