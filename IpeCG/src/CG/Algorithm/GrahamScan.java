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
    public ArrayList<Point> initialPoints = new ArrayList<>();
    public ArrayList<Point> duplicates = new ArrayList<>();
    public ArrayList<Point> points = new ArrayList<>();
    public Stack<Point> hull = new Stack<>();
    public Point initialPoint;


    public GrahamScan(ArrayList<Use> uses) {
        if(uses.size() > 2) {
            setPoints(uses);
            sortPoints();
            generateLayers();
        }
        else {
            System.out.println("requires at least 3 points");
        }
    }

    public ArrayList<Use> setInitialPointsIpe() {
        ArrayList<Use> uses = new ArrayList<>();

        for (Point point : initialPoints) {
            HashMap<String, String> attributes = new HashMap<>();

            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("name", "mark/disk(sx)");
            attributes.put("pos", (point.x) + " " + (point.y));
            attributes.put("size", "large (5.0)");
            attributes.put("stroke", "black");

            uses.add(new Use(new Ipe.Object.Point(String.valueOf(point.x), String.valueOf(point.y)), attributes));
        }

        return uses;
    }

    public ArrayList<Text> setInitialTextPointsIpe() {
        ArrayList<Text> texts = new ArrayList<>();

        for (int i = 0; i < initialPoints.size(); i++) {
            HashMap<String, String> attributes = new HashMap<>();

            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("transformations", "translations");
            attributes.put("pos", (initialPoints.get(i).x + 8) + " " + (initialPoints.get(i).y + 8));
            attributes.put("stroke", "black");
            attributes.put("type", "label");
            attributes.put("width", "14.6575");
            attributes.put("height", "18.59");
            attributes.put("depth", "0.0825");
            attributes.put("valign", "baseline");
            texts.add(new Text(String.valueOf(i+1), attributes));
        }

        return texts;
    }
    public ArrayList<Use> setPointsIpe() {
        ArrayList<Use> uses = new ArrayList<>();

        for (Point point : initialPoints) {
            if (!duplicates.contains(point)) {
                HashMap<String, String> attributes = new HashMap<>();

                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("name", "mark/disk(sx)");
                attributes.put("pos", (point.x) + " " + (point.y));
                attributes.put("size", "large (5.0)");
                attributes.put("stroke", "black");
                uses.add(new Use(new Ipe.Object.Point(String.valueOf(point.x), String.valueOf(point.y)), attributes));
            }
        }

        return uses;
    }

    public ArrayList<Text> setTextPointsIpe() {
        ArrayList<Text> texts = new ArrayList<>();

        for (int i = 0; i < initialPoints.size(); i++) {
            if (!duplicates.contains(initialPoints.get(i))) {
                HashMap<String, String> attributes = new HashMap<>();

                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("transformations", "translations");
                attributes.put("pos", (initialPoints.get(i).x + 8) + " " + (initialPoints.get(i).y + 8));
                attributes.put("stroke", "black");
                attributes.put("type", "label");
                attributes.put("width", "14.6575");
                attributes.put("height", "18.59");
                attributes.put("depth", "0.0825");
                attributes.put("valign", "baseline");
                texts.add(new Text(String.valueOf(i+1), attributes));
            }
        }

        return texts;
    }

    public ArrayList<Path> setInitialCircleIpe() {
        ArrayList<Path> paths = new ArrayList<>();
        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "e"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        return  paths;
    }

    public ArrayList<Path> setArcIpe() {
        ArrayList<Path> paths = new ArrayList<>();
        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "a"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        attributes.put("arrow", "normal/normal");
        paths.add(new Path(strPoints, attributes));

        return  paths;
    }

    public ArrayList<Path> setDashedLineIpe() {
        ArrayList<Path> paths = new ArrayList<>();

        if (duplicates.size() == 0) {
            for (Point point : points) {
                HashMap<String, String> attributes = new HashMap<>();
                ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
                strPoints.add(new Ipe.Object.Point(String.valueOf(point.x), String.valueOf(point.y), "l"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                attributes.put("dash", "dashed");
                paths.add(new Path(strPoints, attributes));
            }
        }
        else {
            for (Point point : points) {
                if (!duplicates.contains(point)) {
                    HashMap<String, String> attributes = new HashMap<>();
                    ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

                    strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
                    strPoints.add(new Ipe.Object.Point(String.valueOf(point.x), String.valueOf(point.y), "l"));
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("stroke", "black");
                    attributes.put("dash", "dashed");
                    paths.add(new Path(strPoints, attributes));
                }
            }
        }

        return paths;
    }

    public ArrayList<Path> setStackIpe() {
        ArrayList<Path> paths = new ArrayList<>();

        for (int i = 0; i < 18; i++) {
            HashMap<String, String> attributes = new HashMap<>();
            ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point("0", String.valueOf(i * 32), "m"));
            strPoints.add(new Ipe.Object.Point("0", String.valueOf((i + 1) * 32), "l"));
            strPoints.add(new Ipe.Object.Point("64", String.valueOf((i + 1) * 32), "l"));
            strPoints.add(new Ipe.Object.Point("64", String.valueOf(i * 32), "l"));
            strPoints.add(new Ipe.Object.Point("h"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "black");
            paths.add(new Path(strPoints, attributes));
        }

        return paths;
    }

    public ArrayList<Path> setInitialCirclesIpe() {
        ArrayList<Path> paths = new ArrayList<>();
        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "e"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        strPoints = new ArrayList<>();
        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(0).x), String.valueOf(points.get(0).y), "e"));
        attributes = new HashMap<>();
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        strPoints = new ArrayList<>();
        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(1).x), String.valueOf(points.get(1).y), "e"));
        attributes = new HashMap<>();
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        return paths;
    }

    public ArrayList<Text> setHullsIpe(int val) {
        ArrayList<Text> texts = new ArrayList<>();

        for (int i = 0; i < hull.size() + val; i++) {
            HashMap<String, String> attributes = new HashMap<>();

            if ((initialPoints.indexOf(hull.get(i)) + 1) > 9) {
                attributes.put("pos", 16 + " " + (8 + (32 * i)));
            }
            else {
                attributes.put("pos", 24 + " " + (8 + (32 * i)));
            }

            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("transformations", "translations");
            attributes.put("stroke", "black");
            attributes.put("type", "label");
            attributes.put("width", "14.6575");
            attributes.put("height", "18.59");
            attributes.put("depth", "0.0825");
            attributes.put("valign", "baseline");
            texts.add(new Text(String.valueOf(initialPoints.indexOf(hull.get(i)) + 1), attributes));
        }

        return texts;
    }

    public ArrayList<Path> setInitialLinesIpe() {
        ArrayList<Path> paths = new ArrayList<>();
        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(0).x), String.valueOf(points.get(0).y), "l"));
        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(1).x), String.valueOf(points.get(1).y), "l"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "blue");
        attributes.put("pen", "ultrafat (2.0)");
        paths.add(new Path(strPoints, attributes));

        return paths;
    }

    public ArrayList<Path> setCirclesIpe(int i) {
        ArrayList<Path> paths = new ArrayList<>();
        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-2).x), String.valueOf(hull.get(hull.size()-2).y), "e"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        strPoints = new ArrayList<>();
        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-1).x), String.valueOf(hull.get(hull.size()-1).y), "e"));
        attributes = new HashMap<>();
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        strPoints = new ArrayList<>();
        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(i).x), String.valueOf(points.get(i).y), "e"));
        attributes = new HashMap<>();
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        return paths;
    }

    public ArrayList<Path> setLinesIpe(int val) {
        ArrayList<Path> paths = new ArrayList<>();
        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

        for (int j = 0; j < hull.size() + val; j++) {
            if (j == 0) {
                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "m"));
            }
            else {
                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "l"));
            }
        }
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "blue");
        attributes.put("pen", "ultrafat (2.0)");
        paths.add(new Path(strPoints, attributes));

        return paths;
    }

    public ArrayList<Path> setRedDashIpe(int i) {
        ArrayList<Path> paths = new ArrayList<>();
        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.peek().x), String.valueOf(hull.peek().y), "m"));
        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(i).x), String.valueOf(points.get(i).y), "l"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "red");
        attributes.put("pen", "ultrafat (2.0)");
        attributes.put("dash", "dashed");
        paths.add(new Path(strPoints, attributes));

        return paths;
    }

    public ArrayList<Path> setGreenDashIpe(int i) {
        ArrayList<Path> paths = new ArrayList<>();
        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.peek().x), String.valueOf(hull.peek().y), "m"));
        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(i).x), String.valueOf(points.get(i).y), "l"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "green");
        attributes.put("pen", "ultrafat (2.0)");
        attributes.put("dash", "dashed");
        paths.add(new Path(strPoints, attributes));

        return paths;
    }

    public ArrayList<Path> setHullPolygonIpe() {
        ArrayList<Path> paths = new ArrayList<>();
        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

        for (int j = 0; j < hull.size(); j++) {
            if (j == 0) {
                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "m"));
            }
            else {
                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "l"));
            }
        }
        strPoints.add(new Ipe.Object.Point("h"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "blue");
        attributes.put("pen", "ultrafat (2.0)");
        paths.add(new Path(strPoints, attributes));

        return paths;
    }

    public void generateLayers() {
        ArrayList<Use> uses = new ArrayList<>();
        ArrayList<Text> texts = new ArrayList<>();
        ArrayList<Path> paths = new ArrayList<>();

        // Scenario (0)
        uses = setInitialPointsIpe();
        texts = setInitialTextPointsIpe();
        layers.add(new Layer(null, uses, texts));

        // Scenario (circle on initial points)
        uses = new ArrayList<>(setInitialPointsIpe());
        texts = new ArrayList<>(setInitialTextPointsIpe());
        paths = new ArrayList<>(setInitialCircleIpe());
        layers.add(new Layer(paths, uses, texts));

        // Scenario (arc on initial point)
        uses = new ArrayList<>(setInitialPointsIpe());
        texts = new ArrayList<>(setInitialTextPointsIpe());
        paths = new ArrayList<>(setInitialCircleIpe());
        paths.addAll(setArcIpe());
        layers.add(new Layer(paths, uses, texts));

        // Scenario (initial dashed line)
        uses = new ArrayList<>(setInitialPointsIpe());
        texts = new ArrayList<>(setInitialTextPointsIpe());
        paths = new ArrayList<>(setInitialCircleIpe());
        paths.addAll(setArcIpe());
        paths.addAll(setDashedLineIpe());
        layers.add(new Layer(paths, uses, texts));

        // Scenario (remove duplicates)
        uses = new ArrayList<>(setPointsIpe());
        texts = new ArrayList<>(setTextPointsIpe());
        paths = new ArrayList<>(setInitialCircleIpe());
        paths.addAll(setArcIpe());
        paths.addAll(setDashedLineIpe());
        layers.add(new Layer(paths, uses, texts));

        // Scenario (stack on left side layer)
        uses = new ArrayList<>(setPointsIpe());
        texts = new ArrayList<>(setTextPointsIpe());
        paths = new ArrayList<>(setDashedLineIpe());
        paths.addAll(setStackIpe());
        layers.add(new Layer(paths, uses, texts));

        // Scenario (0: circles)
        uses = new ArrayList<>(setPointsIpe());
        texts = new ArrayList<>(setTextPointsIpe());
        paths = new ArrayList<>(setDashedLineIpe());
        paths.addAll(setStackIpe());
        paths.addAll(setInitialCirclesIpe());
        layers.add(new Layer(paths, uses, texts));

        // Scenario (0: lines)
        uses = new ArrayList<>(setPointsIpe());
        texts = new ArrayList<>(setTextPointsIpe());
        paths = new ArrayList<>(setDashedLineIpe());
        paths.addAll(setStackIpe());
        hull.push(initialPoint);
        hull.push(points.get(0));
        hull.push(points.get(1));
        texts.addAll(setHullsIpe(0));
        paths.addAll(setInitialCirclesIpe()); //
        paths.addAll(setInitialLinesIpe()); //
        layers.add(new Layer(paths, uses, texts));

        // Scenario (0: removed circles)
        uses = new ArrayList<>(setPointsIpe());
        texts = new ArrayList<>(setTextPointsIpe());
        paths = new ArrayList<>(setDashedLineIpe());
        paths.addAll(setStackIpe());
        texts.addAll(setHullsIpe(0));
        paths.addAll(setInitialLinesIpe()); //
        layers.add(new Layer(paths, uses, texts));

        // start algorithm
        int i = 2;
        while (i < points.size()) {
            // start add circle layer //
            uses = new ArrayList<>(setPointsIpe());
            texts = new ArrayList<>(setTextPointsIpe());
            paths = new ArrayList<>(setDashedLineIpe());
            paths.addAll(setStackIpe());
            texts.addAll(setHullsIpe(0));
            paths.addAll(setCirclesIpe(i));
            paths.addAll(setLinesIpe(0));
            layers.add(new Layer(paths, uses, texts));
            // start add circle layer //

            Point p1 = new Point(hull.get(hull.size()-2).x, hull.get(hull.size()-2).y);
            Point p2 = new Point(hull.get(hull.size()-1).x, hull.get(hull.size()-1).y);
            Point p3 = new Point(points.get(i).x, points.get(i).y);
            LineSegment lineSegment = new LineSegment(p1, p2);

            if (lineSegment.crossProductToPoint(p3) < 0) {

                // start red dashed layer //
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setTextPointsIpe());
                paths = new ArrayList<>(setDashedLineIpe());
                paths.addAll(setStackIpe());
                texts.addAll(setHullsIpe(0));
                paths.addAll(setCirclesIpe(i));
                paths.addAll(setLinesIpe(0));
                paths.addAll(setRedDashIpe(i));
                layers.add(new Layer(paths, uses, texts));
                // end red dashed layer //

                // start removed red dashed layer //
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setTextPointsIpe());
                paths = new ArrayList<>(setDashedLineIpe());
                paths.addAll(setStackIpe());
                texts.addAll(setHullsIpe(0));
                paths.addAll(setCirclesIpe(i));
                paths.addAll(setLinesIpe(0));
                layers.add(new Layer(paths, uses, texts));
                // end removed red dashed layer //

                // start removed blue layer //
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setTextPointsIpe());
                paths = new ArrayList<>(setDashedLineIpe());
                paths.addAll(setStackIpe());
                texts.addAll(setHullsIpe(-1));
                paths.addAll(setCirclesIpe(i));
                paths.addAll(setLinesIpe(-1));
                layers.add(new Layer(paths, uses, texts));
                // end remove blue layer //

                hull.pop();
            }
            else {

                // start green dashed layer //
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setTextPointsIpe());
                paths = new ArrayList<>(setDashedLineIpe());
                paths.addAll(setStackIpe());
                texts.addAll(setHullsIpe(0));
                paths.addAll(setCirclesIpe(i));
                paths.addAll(setLinesIpe(0));
                paths.addAll(setGreenDashIpe(i));
                layers.add(new Layer(paths, uses, texts));
                // end green dashed layer //

                hull.push(points.get(i));

                // start green to blue layer //
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setTextPointsIpe());
                paths = new ArrayList<>(setDashedLineIpe());
                paths.addAll(setStackIpe());
                texts.addAll(setHullsIpe(0));
                paths.addAll(setCirclesIpe(i));
                paths.addAll(setLinesIpe(0));
                layers.add(new Layer(paths, uses, texts));
                // end green to blue layer //

                i++;
            }

            // start remove circle layer //
            uses = new ArrayList<>(setPointsIpe());
            texts = new ArrayList<>(setTextPointsIpe());
            paths = new ArrayList<>(setDashedLineIpe());
            paths.addAll(setStackIpe());
            texts.addAll(setHullsIpe(0));
            paths.addAll(setLinesIpe(0));
            layers.add(new Layer(paths, uses, texts));
            // end remove circle layer //
        }
        // end algorithm

        // Scenario (end)
        uses = new ArrayList<>(setPointsIpe());
        texts = new ArrayList<>(setTextPointsIpe());
        paths = new ArrayList<>(setDashedLineIpe());
        paths.addAll(setStackIpe());
        texts.addAll(setHullsIpe(0));
        paths.addAll(setHullPolygonIpe());
        layers.add(new Layer(paths, uses, texts));
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

        initialPoints.addAll(points);
    }
}
