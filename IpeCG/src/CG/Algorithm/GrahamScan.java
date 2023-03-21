package CG.Algorithm;

import CG.Object.Point;
import Ipe.Object.Layer;
import Ipe.Object.Use;

import java.util.ArrayList;

public class GrahamScan {
    public ArrayList<Layer> layers = new ArrayList<>();
    public ArrayList<Point> points = new ArrayList<>();
    public Point initialPoint;


    public GrahamScan(ArrayList<Use> uses) {
        if(uses.size() >= 3) {
            setPoints(uses);
            sortPoints();
            generateLayers();
        }
        else {
            System.out.println("requires at least 3 points");
        }
    }

    public void generateLayers() {

    }

    public int getInitialPointIndex() {
        double min = Double.MAX_VALUE;
        int idx = 0;
        for(int i = 0; i < points.size(); i++) {
            if(points.get(i).y < min) {
                min = points.get(i).y;
                idx = i;
            }
        }
        return idx;
    }

    public void sortPoints() {
        int initialPointIndex = getInitialPointIndex();
        initialPoint = points.get(initialPointIndex);
        points.remove(initialPointIndex);
        ArrayList<Point> pointsTemp = new ArrayList<>(points);
        pointsTemp.sort((a, b) -> {
            double cotanA = -(a.x - initialPoint.x) / (a.y - initialPoint.y);
            double cotanB = -(b.x - initialPoint.x) / (b.y - initialPoint.y);
            if (cotanA - cotanB < 0) {
                return -1;
            }
            return 1;
        });
        points.clear();
        points.addAll(pointsTemp);
        pointsTemp.clear();
    }

    public void setPoints(ArrayList<Use> uses) {
        for (Use u : uses) {
            points.add(new Point(Double.parseDouble(u.pos.x), Double.parseDouble(u.pos.y)));
        }
    }
}
