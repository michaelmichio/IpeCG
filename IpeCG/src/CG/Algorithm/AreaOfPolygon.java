package CG.Algorithm;

import CG.Object.Point;
import Ipe.Object.Path;

import java.util.ArrayList;

public class AreaOfPolygon {
    ArrayList<Point> vertices = new ArrayList<>();

    public AreaOfPolygon(Path path) {
        setVertices(path);
        sortVertices();
        // ...
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
                    //
                }
            }
        }
    }
}
