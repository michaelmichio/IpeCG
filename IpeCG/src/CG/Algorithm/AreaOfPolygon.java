package CG.Algorithm;

import CG.Object.LineSegment;
import CG.Object.Point;
import Ipe.Object.Layer;
import Ipe.Object.Path;

import java.util.ArrayList;
import java.util.HashMap;

public class AreaOfPolygon {
    public ArrayList<Layer> layers = new ArrayList<>();
    public ArrayList<Point> vertices = new ArrayList<>();

    public AreaOfPolygon(Path path) {
        setVertices(path);
        sortVertices();
        generateLayer();
    }

    public void generateLayer() {
        for (int i = 0; i < vertices.size()-2; i++) {
            HashMap<String, String> attributes = new HashMap<>();
            ArrayList<Path> paths = new ArrayList<>();
            ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

            Point p1 = new Point(vertices.get(0).x, vertices.get(0).y);
            Point p2 = new Point(vertices.get(i+1).x, vertices.get(i+1).y);
            Point p3 = new Point(vertices.get(i+2).x, vertices.get(i+2).y);
            LineSegment lineSegment = new LineSegment(p1, p2);

            if (lineSegment.crossProductToPoint(p3) < 0) {
                strPoints.add(new Ipe.Object.Point(String.valueOf(vertices.get(0).x), String.valueOf(vertices.get(0).y), "m"));
                for (int j = 1; j <= i+2; j++) {
                    strPoints.add(new Ipe.Object.Point(String.valueOf(vertices.get(j).x), String.valueOf(vertices.get(j).y), "l"));
                }
                strPoints.add(new Ipe.Object.Point("h"));
                attributes.put("fill", "green");
                attributes.put("opacity", "50%");
                attributes.put("layer", String.valueOf(layers.size()+1));

                paths.add(new Path(strPoints, attributes));

                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(vertices.get(0).x), String.valueOf(vertices.get(0).y), "m"));
                strPoints.add(new Ipe.Object.Point(String.valueOf(vertices.get(i+1).x), String.valueOf(vertices.get(i+1).y), "l"));
                strPoints.add(new Ipe.Object.Point(String.valueOf(vertices.get(i+2).x), String.valueOf(vertices.get(i+2).y), "l"));
                strPoints.add(new Ipe.Object.Point("h"));
                attributes.put("fill", "red");
                attributes.put("opacity", "50%");
                attributes.put("layer", String.valueOf(layers.size()+1));

                paths.add(new Path(strPoints, attributes));

                layers.add(new Layer(paths, null, null));
            }

            attributes = new HashMap<>();
            paths = new ArrayList<>();
            strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point(String.valueOf(vertices.get(0).x), String.valueOf(vertices.get(0).y), "m"));
            for (int j = 1; j <= i+2; j++) {
                strPoints.add(new Ipe.Object.Point(String.valueOf(vertices.get(j).x), String.valueOf(vertices.get(j).y), "l"));
            }
            strPoints.add(new Ipe.Object.Point("h"));
            attributes.put("fill", "green");
            attributes.put("opacity", "50%");
            attributes.put("layer", String.valueOf(layers.size()+1));

            paths.add(new Path(strPoints, attributes));
            layers.add(new Layer(paths, null, null));
        }
    }

    public void sortVertices() {
        ArrayList<Point> pointsTemp = new ArrayList<>();
        // find point with the lowest y
        double minY = Integer.MAX_VALUE;
        int idxMinY = 0;
        for(int i = 0; i < vertices.size(); i++) {
            if(vertices.get(i).y < minY) {
                minY = vertices.get(i).y;
                idxMinY = i;
            }
        }
        if(idxMinY != 0) { // sort y
            if(idxMinY == vertices.size() - 1) {
                pointsTemp.add(vertices.get(idxMinY));
                for(int i = 0; i < vertices.size() - 1; i++) {
                    pointsTemp.add(vertices.get(i));
                }
            }
            else {
                for(int i = idxMinY; i < vertices.size(); i++) {
                    pointsTemp.add(vertices.get(i));
                }
                for(int i = 0; i < idxMinY; i++) {
                    pointsTemp.add(vertices.get(i));
                }
            }
            vertices.clear();
            vertices.addAll(pointsTemp);
        }
        pointsTemp.clear();
        if(vertices.get(1).x < vertices.get(vertices.size()-1).x) { // sort x
            pointsTemp.add(vertices.get(0));
            for(int i = vertices.size() - 1; i > 0; i--) {
                pointsTemp.add(vertices.get(i));
            }
            vertices.clear();
            vertices.addAll(pointsTemp);
        }
        pointsTemp.clear();
    }

    public void setVertices(Path path) {
        for (Ipe.Object.Point point : path.points) {
            if (point.type.equals("m") || point.type.equals("l")) {
                try {
                    vertices.add(new Point(Double.parseDouble(point.x), Double.parseDouble(point.y)));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
}
