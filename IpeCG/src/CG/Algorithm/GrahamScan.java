package CG.Algorithm;

import CG.Object.LineSegment;
import CG.Object.Point;
import Ipe.Object.Layer;
import Ipe.Object.Path;
import Ipe.Object.Text;
import Ipe.Object.Use;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class GrahamScan {
    public ArrayList<Layer> layers = new ArrayList<>();
    public ArrayList<Point> points = new ArrayList<>();
    public Point initialPoint;


    public GrahamScan(ArrayList<Use> uses) {
        if(uses.size() > 2) {
            setPoints(uses);
            setInitialLayers();
            sortPoints();
            generateLayers();
        }
        else {
            System.out.println("requires at least 3 points");
        }
    }

    public void setInitialLayers() {
        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Path> paths = new ArrayList<>();
        ArrayList<Text> texts = new ArrayList<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();


        for (int i = 0; i < points.size(); i++) {
            attributes = new HashMap<>();

            attributes.put("layer", String.valueOf(layers.size()+1));
            attributes.put("transformations", "translations");
            attributes.put("pos", (points.get(i).x + 8) + " " + (points.get(i).y + 8));
            attributes.put("stroke", "black");
            attributes.put("type", "label");
            attributes.put("width", "14.6575");
            attributes.put("height", "18.59");
            attributes.put("depth", "0.0825");
            attributes.put("valign", "baseline");
            texts.add(new Text(String.valueOf(i+1), attributes));
        }
        layers.add(new Layer(null, null, texts));
    }

    public void generateLayers() {

        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Path> paths = new ArrayList<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

        // Scenario 1
        // Scenario 2
        // Scenario 3

        // Scenario 4
        Stack<Point> hull = new Stack<>();
        hull.push(initialPoint);
        hull.push(points.get(0));
        int i = 1;
        while (i < points.size()) {
            attributes = new HashMap<>();
            paths = new ArrayList<>();
            strPoints = new ArrayList<>();

            Point p1 = new Point(hull.get(hull.size()-2).x, hull.get(hull.size()-2).y);
            Point p2 = new Point(hull.get(hull.size()-1).x, hull.get(hull.size()-1).y);
            Point p3 = new Point(points.get(i).x, points.get(i).y);
            LineSegment lineSegment = new LineSegment(p1, p2);

            if (lineSegment.crossProductToPoint(p3) <= 0) {
                hull.pop();
            }
            else {
                hull.push(points.get(i));

                for (int j = 0; j < hull.size(); j++) {
                    if (j == 0) {
                        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "m"));
                    }
                    else {
                        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "l"));
                    }
                }
                attributes.put("layer", String.valueOf(layers.size()+1));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));
                layers.add(new Layer(paths, null, null));

                i++;
            }
        }

        // Scenario 5
        attributes = new HashMap<>();
        paths = new ArrayList<>();
        strPoints = new ArrayList<>();
        for (int j = 0; j < hull.size(); j++) {
            if (j == 0) {
                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "m"));
            }
            else {
                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "l"));
            }
        }
        strPoints.add(new Ipe.Object.Point("h"));
        attributes.put("layer", String.valueOf(layers.size()+1));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));
        layers.add(new Layer(paths, null, null));
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
        ArrayList<Point> duplicates = new ArrayList<>();
        pointsTemp.sort((a, b) -> {
            if (new LineSegment(new Point(initialPoint.x, initialPoint.y), new Point(a.x, a.y)).crossProductToPoint(b) > 0) {
                return -1;
            }
            else if (new LineSegment(new Point(initialPoint.x, initialPoint.y), new Point(a.x, a.y)).crossProductToPoint(b) < 0) {
                return 1;
            }
            else {
                if (a.y > b.y) {
                    duplicates.add(b);
                }
                else if (a.y < b.y) {
                    duplicates.add(a);
                }
                else if (a.x > b.x) {
                    duplicates.add(b);
                }
                else if (a.x < b.x) {
                    duplicates.add(a);
                }
                else {
                    duplicates.add(a);
                }
                return 0;
            }
        });

        for (Point dupe : duplicates) {
            pointsTemp.remove(dupe);
        }

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
