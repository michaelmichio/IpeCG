package CG.Algorithm;

import CG.Object.LineSegment;
import CG.Object.Point;
import Ipe.Object.Layer;
import Ipe.Object.Path;

import java.util.ArrayList;

public class BentleyOttmann {
    public ArrayList<Layer> layers = new ArrayList<>();
    public ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public BentleyOttmann(ArrayList<Path> paths) {
        setLineSegments(paths);
        sortLineSegments();
        // ...
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
