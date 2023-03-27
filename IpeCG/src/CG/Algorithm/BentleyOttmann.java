package CG.Algorithm;

import CG.Object.Endpoint;
import CG.Object.LineSegment;
import CG.Object.Point;
import Ipe.Object.Layer;
import Ipe.Object.Path;

import java.util.*;

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

        TreeMap<Double, Deque<Endpoint>> eventPoints = getEventPoints();
        TreeMap<Double, ArrayList<Integer>> status = new TreeMap<>();

        // status.put(eventPoints.firstEntry().getValue().getFirst().y, new ArrayList<>());

        int n = eventPoints.size();
        for (int i = 0; i < n; i++) {

            HashMap<String, String> attributes = new HashMap<>();
            ArrayList<Path> paths = new ArrayList<>();
            ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

            int m = 0;
            if (eventPoints.firstEntry() != null) {
                m = eventPoints.firstEntry().getValue().size();
            }
            for (int j = 0; j < m; j++) {

                double x = eventPoints.firstEntry().getValue().getFirst().x;
                double prev;
                double y = eventPoints.firstEntry().getValue().getFirst().y;
                double next;
                int segmentIndex = eventPoints.firstEntry().getValue().getFirst().segmenIdx;
                int type = eventPoints.firstEntry().getValue().getFirst().type;
                int intersectSegmentIndex = 0;
                boolean isIntersect;
                Point intersectPoint;

                if (type == 0) { // start

                    System.out.println("masuk " + segmentIndex);

                    // add new line segment to status
                    if (status.containsKey(y)) { // key already exist

                        // check above (same key)
                        intersectSegmentIndex = status.get(y).get(status.get(y).size()-1);
                        isIntersect = lineSegments.get(segmentIndex).isIntersect(lineSegments.get(intersectSegmentIndex));
                        intersectPoint = lineSegments.get(segmentIndex).getIntersectPoint(lineSegments.get(intersectSegmentIndex));

                        if (isIntersect) { // intersect
                            Deque<Endpoint> ep = new ArrayDeque<>();
                            ep.add(new Endpoint(intersectPoint.x, intersectPoint.y, intersectSegmentIndex, segmentIndex, 2));
                            if (eventPoints.containsKey(intersectPoint.x)) {
                                ep.addAll(eventPoints.get(intersectPoint.x));
                            }
                            eventPoints.put(intersectPoint.x, ep);
                        }

                        // check below if exist (next key)
                        if (status.higherKey(y) != null) {
                            // get first
                            next = status.higherKey(y);
                            intersectSegmentIndex = status.get(next).get(0);
                            isIntersect = lineSegments.get(segmentIndex).isIntersect(lineSegments.get(intersectSegmentIndex));
                            intersectPoint = lineSegments.get(segmentIndex).getIntersectPoint(lineSegments.get(intersectSegmentIndex));

                            if (isIntersect) {
                                Deque<Endpoint> ep = new ArrayDeque<>();
                                ep.add(new Endpoint(intersectPoint.x, intersectPoint.y, segmentIndex, intersectSegmentIndex, 2));
                                if (eventPoints.containsKey(intersectPoint.x)) {
                                    ep.addAll(eventPoints.get(intersectPoint.x));
                                }
                                eventPoints.put(intersectPoint.x, ep);
                            }
                        }

                        // add new line segment to status
                        ArrayList<Integer> ls = new ArrayList<>();
                        ls.add(segmentIndex); // add new value
                        if (status.containsKey(y)) {
                            ls.addAll(status.get(y)); // merge with the prev value
                        }
                        status.put(y, ls);
                    }
                    else { // new key

                        // check above if exist (prev key)
                        if (status.lowerKey(y) != null) {
                            // get first
                            prev = status.lowerKey(y);
                            //
                            if (status.get(prev).size() > 0) {
                                intersectSegmentIndex = status.get(prev).get(status.get(prev).size()-1);
                            }
                            //
                            isIntersect = lineSegments.get(segmentIndex).isIntersect(lineSegments.get(intersectSegmentIndex));
                            intersectPoint = lineSegments.get(segmentIndex).getIntersectPoint(lineSegments.get(intersectSegmentIndex));

                            if (isIntersect) {
                                Deque<Endpoint> ep = new ArrayDeque<>();
                                ep.add(new Endpoint(intersectPoint.x, intersectPoint.y, intersectSegmentIndex, segmentIndex, 2));
                                if (eventPoints.containsKey(intersectPoint.x)) {
                                    ep.addAll(eventPoints.get(intersectPoint.x));
                                }
                                eventPoints.put(intersectPoint.x, ep);
                            }
                        }

                        // check below if exist (next key)
                        if (status.higherKey(y) != null) {
                            // get first
                            next = status.higherKey(y);
                            if (status.get(next).size() > 0) {
                                intersectSegmentIndex = status.get(next).get(0);
                            }
                            isIntersect = lineSegments.get(segmentIndex).isIntersect(lineSegments.get(intersectSegmentIndex));
                            intersectPoint = lineSegments.get(segmentIndex).getIntersectPoint(lineSegments.get(intersectSegmentIndex));

                            if (isIntersect) {
                                Deque<Endpoint> ep = new ArrayDeque<>();
                                ep.add(new Endpoint(intersectPoint.x, intersectPoint.y, segmentIndex, intersectSegmentIndex, 2));
                                if (eventPoints.containsKey(intersectPoint.x)) {
                                    ep.addAll(eventPoints.get(intersectPoint.x));
                                }
                                eventPoints.put(intersectPoint.x, ep);
                            }
                        }

                        // add new line segment to status
                        ArrayList<Integer> ls = new ArrayList<>();
                        ls.add(segmentIndex); // add new value
                        if (status.containsKey(y)) {
                            ls.addAll(status.get(y)); // merge with the prev value
                        }
                        status.put(y, ls);

                    }

                }
                else if (type == 1) { // end

                    System.out.println("keluar " + segmentIndex);

                    // check above and below
                    int lsAbove = -1;
                    int lsBelow = -1;

                    if (status.get(y) != null) {
                        if (status.get(y).indexOf(segmentIndex) > 0) {
                            lsAbove = status.get(y).indexOf(status.get(y).indexOf(segmentIndex)-1);
                        }
                        else if (status.lowerKey(y) != null) {
                            lsAbove = status.get(status.lowerKey(y)).get(status.get(status.lowerKey(y)).size()-1);
                        }

                        if (status.get(y).indexOf(segmentIndex) < status.get(y).size()-1) {
                            lsBelow = status.get(y).indexOf(status.get(y).indexOf(segmentIndex)+1);
                        }
                        else if (status.lowerKey(y) != null) {
                            lsBelow = status.get(status.higherKey(y)).get(0);
                        }
                    }

                    if (lsAbove != -1 && lsBelow != -1) {
                        if (lineSegments.get(lsAbove).isIntersect(lineSegments.get(lsBelow))) {
                            Point ip = lineSegments.get(lsAbove).getIntersectPoint(lineSegments.get(lsBelow));
                            Deque<Endpoint> ep = new ArrayDeque<>();
                            if (eventPoints.containsKey(ip.x)) {
                                if (eventPoints.get(ip.x).size() > 0) {
                                    ep.addAll(eventPoints.get(ip.x));
                                }
                            }
                            else {
                                n++;
                            }
                            ep.add(new Endpoint(ip.x, ip.y, lsBelow, lsAbove, 2));
                            eventPoints.put(ip.x, ep);
                        }
                    }

                    //

                    ArrayList<Integer> ls = new ArrayList<>();
                    if (status.get(y) != null) {
                        ls.addAll(status.get(y));
                        ls.remove(Integer.valueOf(segmentIndex));

                        if (status.get(y).size() == 0) {
                            status.remove(y);
                        }
                    }
                    status.put(y, ls);
                    if (status.get(y).size() == 0) {
                        status.remove(y);
                    }
                }
                else { // intersection

                    n++;

                    System.out.println("intersect " + segmentIndex);

                    int ls1 = eventPoints.firstEntry().getValue().getFirst().segmenIdx;
                    int ls2 = eventPoints.firstEntry().getValue().getFirst().intersectSegmentIndex;
                    int lsAbove = -1;
                    for (Map.Entry<Double, ArrayList<Integer>> entry : status.entrySet()) {
                        ArrayList<Integer> ls = entry.getValue();
                        for (int k = 0; k < ls.size(); k++) {
                            if (ls.get(k) == ls1) {
                                ls.set(k, ls2);
                                if (lsAbove != -1) {
                                    if (lineSegments.get(ls2).isIntersect(lineSegments.get(lsAbove))) {
                                        Point ip = lineSegments.get(ls2).getIntersectPoint(lineSegments.get(lsAbove));
                                        Deque<Endpoint> ep = new ArrayDeque<>();
                                        if (eventPoints.containsKey(ip.x)) {
                                            ep.addAll(eventPoints.get(ip.x));
                                        }
                                        else {
                                            //n++;
                                        }
                                        ep.add(new Endpoint(ip.x, ip.y, ls2, lsAbove, 2));
                                        eventPoints.put(ip.x, ep);
                                    }
                                }
                            }
                            else if (ls.get(k) == ls2) {
                                ls.set(k, ls1);
                                if (k < ls.size()-1) {
                                    if (lineSegments.get(ls1).isIntersect(lineSegments.get(ls.get(k+1)))) {
                                        Point ip = lineSegments.get(ls1).getIntersectPoint(lineSegments.get(k+1));
                                        Deque<Endpoint> ep = new ArrayDeque<>();
                                        if (eventPoints.containsKey(ip.x)) {
                                            ep.addAll(eventPoints.get(ip.x));
                                        }
                                        else {
                                            //n++;
                                        }
                                        ep.add(new Endpoint(ip.x, ip.y, ls1, ls.get(k+1), 2));
                                        eventPoints.put(ip.x, ep);
                                    }
                                }
                                else if (status.higherKey(entry.getKey()) != null && status.get(status.higherKey(entry.getKey())).size() > 0) {
                                    int lsBelow = status.get(status.higherKey(entry.getKey())).get(0);
                                    if (lineSegments.get(ls1).isIntersect(lineSegments.get(lsBelow))) {
                                        Point ip = lineSegments.get(ls1).getIntersectPoint(lineSegments.get(lsBelow));
                                        Deque<Endpoint> ep = new ArrayDeque<>();
                                        if (eventPoints.containsKey(ip.x)) {
                                            ep.addAll(eventPoints.get(ip.x));
                                        }
                                        else {
                                            //n++;
                                        }
                                        ep.add(new Endpoint(ip.x, ip.y, ls1, ls.get(lsBelow), 2));
                                        eventPoints.put(ip.x, ep);
                                    }
                                }
                            }
                            lsAbove = ls.get(k);
                        }
                    }
                }

                for (Map.Entry<Double, Deque<Endpoint>> entry : eventPoints.entrySet()) {
                    Deque<Endpoint> newDeq = new ArrayDeque<>();
                    newDeq.addAll(entry.getValue());
                }
                System.out.println("status " + status.values());

                // draw sweep line
                strPoints.add(new Ipe.Object.Point(String.valueOf(eventPoints.firstEntry().getValue().getFirst().x), String.valueOf(maxY), "m"));
                strPoints.add(new Ipe.Object.Point(String.valueOf(eventPoints.firstEntry().getValue().getFirst().x), String.valueOf(minY), "l"));
                attributes.put("layer", String.valueOf(i+1));
                attributes.put("stroke", "red");
                attributes.put("pen", "ultrafat");
                paths.add(new Path(strPoints, attributes));
                layers.add(new Layer(paths, null));

                if (eventPoints.firstEntry() != null) {
                    eventPoints.firstEntry().getValue().removeFirst();
                }

            }

            if (eventPoints.firstEntry() != null) {
                eventPoints.pollFirstEntry();
            }
        }
    }

    public TreeMap<Double, Deque<Endpoint>> getEventPoints() {
        TreeMap<Double, Deque<Endpoint>> eventPoints = new TreeMap<>();
        for (int i = 0; i < lineSegments.size(); i++) {
            if (eventPoints.containsKey(lineSegments.get(i).p1.x)) {
                Deque<Endpoint> yList = eventPoints.get(lineSegments.get(i).p1.x);
                yList.add(new Endpoint(lineSegments.get(i).p1.x, lineSegments.get(i).p1.y, i, 0));
                eventPoints.put(lineSegments.get(i).p1.x, yList);
            }
            else {
                Deque<Endpoint> yList = new ArrayDeque<>();
                yList.add(new Endpoint(lineSegments.get(i).p1.x, lineSegments.get(i).p1.y, i, 0));
                eventPoints.put(lineSegments.get(i).p1.x, yList);
            }
            if (eventPoints.containsKey(lineSegments.get(i).p2.x)) {
                Deque<Endpoint> yList = eventPoints.get(lineSegments.get(i).p2.x);
                yList.add(new Endpoint(lineSegments.get(i).p2.x, lineSegments.get(i).p2.y, i, 1));
                eventPoints.put(lineSegments.get(i).p2.x, yList);
            }
            else {
                Deque<Endpoint> yList = new ArrayDeque<>();
                yList.add(new Endpoint(lineSegments.get(i).p2.x, lineSegments.get(i).p2.y, i, 1));
                eventPoints.put(lineSegments.get(i).p2.x, yList);
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
