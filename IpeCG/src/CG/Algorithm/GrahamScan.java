package CG.Algorithm;

import CG.Object.LineSegment;
import CG.Object.Point;
import Ipe.Object.Layer;
import Ipe.Object.Path;
import Ipe.Object.Use;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

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
        Stack<Point> stack = new Stack<>();

        stack.push(initialPoint);
        stack.push(points.get(0));

        int i = 1;
        while (i < points.size()) {
            HashMap<String, String> attributes = new HashMap<>();
            ArrayList<Path> paths = new ArrayList<>();
            ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

            Point p1 = new Point(stack.get(stack.size()-2).x, stack.get(stack.size()-2).y);
            Point p2 = new Point(stack.get(stack.size()-1).x, stack.get(stack.size()-1).y);
            Point p3 = new Point(points.get(i).x, points.get(i).y);
            LineSegment lineSegment = new LineSegment(p1, p2);

            if (lineSegment.crossProductToPoint(p3) < 0) {
                stack.pop();
            }
            else {
                stack.push(points.get(i));

                for (int j = 0; j < stack.size(); j++) {
                    if (j == 0) {
                        strPoints.add(new Ipe.Object.Point(String.valueOf(stack.get(j).x), String.valueOf(stack.get(j).y), "m"));
                    }
                    else {
                        strPoints.add(new Ipe.Object.Point(String.valueOf(stack.get(j).x), String.valueOf(stack.get(j).y), "l"));
                    }
                }
                attributes.put("layer", String.valueOf(layers.size()+1));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));
                layers.add(new Layer(paths, null));

                i++;
            }
        }

        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Path> paths = new ArrayList<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();
        for (int j = 0; j < stack.size(); j++) {
            if (j == 0) {
                strPoints.add(new Ipe.Object.Point(String.valueOf(stack.get(j).x), String.valueOf(stack.get(j).y), "m"));
            }
            else {
                strPoints.add(new Ipe.Object.Point(String.valueOf(stack.get(j).x), String.valueOf(stack.get(j).y), "l"));
            }
        }
        strPoints.add(new Ipe.Object.Point("h"));
        attributes.put("layer", String.valueOf(layers.size()+1));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));
        layers.add(new Layer(paths, null));
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
