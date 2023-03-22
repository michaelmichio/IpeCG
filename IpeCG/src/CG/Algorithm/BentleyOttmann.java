package CG.Algorithm;

import CG.Object.LineSegment;
import CG.Object.Point;
import Ipe.Object.Layer;
import Ipe.Object.Path;

import java.util.ArrayList;
import java.util.HashMap;

public class BentleyOttmann {
    public ArrayList<Layer> layers = new ArrayList<>();
    public ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public BentleyOttmann(ArrayList<Path> paths) {
        setLineSegments(paths);
        sortLineSegments();
        generateLayers();
    }

    public void generateLayers() {
        double maxY = getMaxtY() + 16; // y for p1 sweep line
        double minY = getMinY() - 16; // y for p2 sweep line

        ArrayList<Point> eventPoints = getEventPoints();

        //ArrayList<LineSegment> totalPreorders = new ArrayList<>();

        for (int i = 0; i < eventPoints.size(); i++) {
            HashMap<String, String> attributes = new HashMap<>();
            ArrayList<Path> paths = new ArrayList<>();
            ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point(String.valueOf(eventPoints.get(i).x), String.valueOf(maxY), "m"));
            strPoints.add(new Ipe.Object.Point(String.valueOf(eventPoints.get(i).x), String.valueOf(minY), "l"));
            attributes.put("layer", String.valueOf(i+1));
            attributes.put("stroke", "red");
            attributes.put("pen", "ultrafat");
            paths.add(new Path(strPoints, attributes));
            layers.add(new Layer(paths, null));
        }
    }

    public ArrayList<Point> getEventPoints() {
        ArrayList<Point> eventPoints = new ArrayList<>();
        eventPoints.add(lineSegments.get(0).p1);
        eventPoints.add(lineSegments.get(0).p2);
        for (int i = 1; i < lineSegments.size(); i++) {
            for (int j = 0; j < eventPoints.size(); j++) {
                if (lineSegments.get(i).p1.x < eventPoints.get(j).x) {
                    eventPoints.add(j, lineSegments.get(i).p1);
                    break;
                }
                else if (j == eventPoints.size() - 1) {
                    eventPoints.add(lineSegments.get(i).p1);
                    break;
                }
            }
            for (int j = 0; j < eventPoints.size(); j++) {
                if (lineSegments.get(i).p2.x < eventPoints.get(j).x) {
                    eventPoints.add(j, lineSegments.get(i).p2);
                    break;
                }
                else if (j == eventPoints.size() - 1) {
                    eventPoints.add(lineSegments.get(i).p2);
                    break;
                }
            }
        }
        return eventPoints;
    }

    public double getMaxtY() {
        double max = Double.MIN_VALUE;
        for (LineSegment ls : lineSegments) {
            if (ls.p1.y > max) {
                max = ls.p1.y;
            }
            if (ls.p2.y > max) {
                max = ls.p2.y;
            }
        }
        return max;
    }

    public double getMinY() {
        double min = Double.MAX_VALUE;
        for (LineSegment ls : lineSegments) {
            if (ls.p1.y < min) {
                min = ls.p1.y;
            }
            if (ls.p2.y < min) {
                min = ls.p2.y;
            }
        }
        return min;
    }

    public void sortLineSegments() {
        ArrayList<LineSegment> lineSegmentsTemp = new ArrayList<>();
        // sort line segment direction by x and left to right
        for (LineSegment ls : lineSegments) {
            if(ls.p1.x <= ls.p2.x) {
                lineSegmentsTemp.add(
                        new LineSegment(
                                new Point(ls.p1.x, ls.p1.y),
                                new Point(ls.p2.x, ls.p2.y)
                        )
                );
            }
            else {
                lineSegmentsTemp.add(
                        new LineSegment(
                                new Point(ls.p2.x, ls.p2.y),
                                new Point(ls.p1.x, ls.p1.y)
                        )
                );
            }
        }
        lineSegments.clear();
        lineSegments.addAll(lineSegmentsTemp);
        lineSegmentsTemp.clear();
        // sort line segments from left to right
        int idx = 0;
        int n = lineSegments.size();
        for (int i = 0; i < n; i++) {
            double min = Double.MAX_VALUE;
            for (int j = 0; j < lineSegments.size(); j++) {
                if(lineSegments.get(j).p1.x < min) {
                    min = lineSegments.get(j).p1.x;
                    idx = j;
                }
            }
            lineSegmentsTemp.add(
                    new LineSegment(
                            new Point(lineSegments.get(idx).p1.x, lineSegments.get(idx).p1.y),
                            new Point(lineSegments.get(idx).p2.x, lineSegments.get(idx).p2.y)
                    )
            );
            lineSegments.remove(idx);
        }
        lineSegments.clear();
        lineSegments.addAll(lineSegmentsTemp);
        lineSegmentsTemp.clear();
    }

    public void setLineSegments(ArrayList<Path> paths) {
        for (Path path : paths) {
            lineSegments.add(
                    new LineSegment(
                            new Point(Double.parseDouble(path.points.get(0).x), Double.parseDouble(path.points.get(0).y)),
                            new Point(Double.parseDouble(path.points.get(1).x), Double.parseDouble(path.points.get(1).y))
                    )
            );
        }
    }
}
