package CG.Algorithm;

import CG.Object.Endpoint;
import CG.Object.LineSegment;
import CG.Object.Point;
import Ipe.Object.Layer;
import Ipe.Object.Path;
import Ipe.Object.Text;
import Ipe.Object.Use;

import java.util.*;

public class BentleyOttmann {
    public ArrayList<Layer> layers = new ArrayList<>();
    public ArrayList<LineSegment> lineSegments = new ArrayList<>();
    public TreeMap<Double, Deque<Endpoint>> eventPoints = new TreeMap<>();
    public TreeMap<Double, ArrayList<Integer>> activeLines = new TreeMap<>();
    public double sweepLineMaxY;
    public double sweepLineMinY;

    public BentleyOttmann(ArrayList<Path> paths) {
        setLineSegments(paths);
        sortLineSegments();
        generateLayers();
    }

    public ArrayList<Path> setLineSegmentsIpe() {
        ArrayList<Path> paths = new ArrayList<>();

        for (LineSegment lineSegment : lineSegments) {
            ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();
            HashMap<String, String> attributes = new HashMap<>();

            strPoints.add(new Ipe.Object.Point(String.valueOf(lineSegment.p1.x), String.valueOf(lineSegment.p1.y), "m"));
            strPoints.add(new Ipe.Object.Point(String.valueOf(lineSegment.p2.x), String.valueOf(lineSegment.p2.y), "l"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "black");
            attributes.put("pen", "fat (1.2)");
            paths.add(new Path(strPoints, attributes));
        }
        return paths;
    }

    public ArrayList<Use> setPointsIpe() {
        ArrayList<Use> uses = new ArrayList<>();

        for (LineSegment lineSegment : lineSegments) {
            HashMap<String, String> attributes = new HashMap<>();

            double startX = lineSegment.p1.x;
            double startY = lineSegment.p1.y;
            double endX = lineSegment.p2.x;
            double endY = lineSegment.p2.y;

            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("name", "mark/disk(sx)");
            attributes.put("pos", startX + " " + startY);
            attributes.put("size", "large (5.0)");
            attributes.put("stroke", "black");
            uses.add(new Use(new Ipe.Object.Point(String.valueOf(startX), String.valueOf(startY)), attributes));

            attributes = new HashMap<>();
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("name", "mark/disk(sx)");
            attributes.put("pos", endX + " " + endY);
            attributes.put("size", "large (5.0)");
            attributes.put("stroke", "black");
            uses.add(new Use(new Ipe.Object.Point(String.valueOf(endX), String.valueOf(endY)), attributes));
        }

        return uses;
    }

    public ArrayList<Text> setLabelsIpe() {
        ArrayList<Text> texts = new ArrayList<>();

        for (int i = 0; i < lineSegments.size(); i++) {
            HashMap<String, String> attributes = new HashMap<>();

            double startX = lineSegments.get(i).p1.x;
            double startY = lineSegments.get(i).p1.y;
            double endX = lineSegments.get(i).p2.x;
            double endY = lineSegments.get(i).p2.y;

            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("transformations", "translations");
            attributes.put("pos", (startX - 16) + " " + (startY + 8));
            attributes.put("stroke", "black");
            attributes.put("type", "label");
            attributes.put("width", "14.6575");
            attributes.put("height", "18.59");
            attributes.put("depth", "0.0825");
            attributes.put("valign", "baseline");
            texts.add(new Text("$p_{" + i + "}$", attributes));

            attributes = new HashMap<>();
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("transformations", "translations");
            attributes.put("pos", (endX + 8) + " " + (endY + 8));
            attributes.put("stroke", "black");
            attributes.put("type", "label");
            attributes.put("width", "14.6575");
            attributes.put("height", "18.59");
            attributes.put("depth", "0.0825");
            attributes.put("valign", "baseline");
            texts.add(new Text("$q_{" + i + "}$", attributes));
        }

        return texts;
    }

    public ArrayList<Path> setSweepLineIpe() {
        ArrayList<Path> paths = new ArrayList<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();
        HashMap<String, String> attributes = new HashMap<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(eventPoints.firstEntry().getValue().getFirst().x), String.valueOf(sweepLineMaxY), "m"));
        strPoints.add(new Ipe.Object.Point(String.valueOf(eventPoints.firstEntry().getValue().getFirst().x), String.valueOf(sweepLineMinY), "l"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "red");
        attributes.put("pen", "ultrafat (2.0)");
        paths.add(new Path(strPoints, attributes));

        return  paths;
    }

    public ArrayList<Path> setDataFramesIpe() {
        ArrayList<Path> paths = new ArrayList<>();
        return  paths;
    }

    public ArrayList<Text> setDataTextIpe() {
        ArrayList<Text> texts = new ArrayList<>();
        return texts;
    }

    public ArrayList<Text> setOutputTextIpe() {
        ArrayList<Text> texts = new ArrayList<>();
        return texts;
    }

    public void generateLayers() {
        sweepLineMaxY = getMaxY() + 16;
        sweepLineMinY = getMinY() - 16;
        eventPoints = getEventPoints();

        ArrayList<Path> paths = new ArrayList<>();
        ArrayList<Use> uses = new ArrayList<>();
        ArrayList<Text> texts = new ArrayList<>();

        // Layer 0
        paths = new ArrayList<>(setLineSegmentsIpe());
        uses = new ArrayList<>(setPointsIpe());
        texts = new ArrayList<>(setLabelsIpe());
        layers.add(new Layer(paths, uses, texts));

        int n = eventPoints.size();
        for (int i = 0; i < n; i++) {
            if (eventPoints.firstEntry() == null) {
                break;
            }
            int m = eventPoints.firstEntry().getValue().size();
            for (int j = 0; j < m; j++) {
                Endpoint endpoint = eventPoints.firstEntry().getValue().getFirst();
                int leftNeighbourLineSegmentIndex = -1;
                int rightNeighbourLineSegmentIndex = -1;
                // Start point
                if (endpoint.status == 0) {
                    // Add line segment to activeLines
                    if (activeLines.containsKey(endpoint.y)) {
                        leftNeighbourLineSegmentIndex = activeLines.get(endpoint.y).get(activeLines.get(endpoint.y).size() - 1);
                    }
                    // Add line segment to activeLines
                    else {
                        // If left neighbour exist
                        if (activeLines.lowerKey(endpoint.y) != null) {
                            leftNeighbourLineSegmentIndex = activeLines.get(activeLines.lowerKey(endpoint.y)).get(activeLines.get(activeLines.lowerKey(endpoint.y)).size() - 1);
                        }
                    }
                    // If right neighbour exist
                    if (activeLines.higherKey(endpoint.y) != null) {
                        rightNeighbourLineSegmentIndex = activeLines.get(activeLines.higherKey(endpoint.y)).get(0);
                    }

                    // Check left neighbour if exist
                    if (leftNeighbourLineSegmentIndex > -1) {
                        // Check the intersection with left neighbour
                        if (lineSegments.get(endpoint.segmentIndex).isIntersect(lineSegments.get(leftNeighbourLineSegmentIndex))) {
                            Point intersectionPoint = lineSegments.get(endpoint.segmentIndex).getIntersectPoint(lineSegments.get(leftNeighbourLineSegmentIndex));
                            Deque<Endpoint> endpoints = new ArrayDeque<>();
                            if (eventPoints.containsKey(intersectionPoint.x)) {
                                endpoints = eventPoints.get(intersectionPoint.x);
                            }
                            endpoints.add(new Endpoint(intersectionPoint.x, intersectionPoint.y, -1, endpoint.segmentIndex, leftNeighbourLineSegmentIndex));
                            eventPoints.put(intersectionPoint.x, endpoints);
                            n++;
                        }
                    }
                    // Check right neighbour if exist
                    if (rightNeighbourLineSegmentIndex > -1) {
                        // Check the intersection with right neighbour
                        if (lineSegments.get(endpoint.segmentIndex).isIntersect(lineSegments.get(rightNeighbourLineSegmentIndex))) {
                            Point intersectionPoint = lineSegments.get(endpoint.segmentIndex).getIntersectPoint(lineSegments.get(rightNeighbourLineSegmentIndex));
                            Deque<Endpoint> endpoints = new ArrayDeque<>();
                            if (eventPoints.containsKey(intersectionPoint.x)) {
                                endpoints = eventPoints.get(intersectionPoint.x);
                            }
                            endpoints.add(new Endpoint(intersectionPoint.x, intersectionPoint.y, -1, endpoint.segmentIndex, rightNeighbourLineSegmentIndex));
                            eventPoints.put(intersectionPoint.x, endpoints);
                            n++;
                        }
                    }

                    ArrayList<Integer> lineSegmentList = new ArrayList<>();
                    if (activeLines.containsKey(endpoint.y)) {
                        lineSegmentList = activeLines.get(endpoint.y);
                    }
                    lineSegmentList.add(endpoint.segmentIndex);
                    activeLines.put(endpoint.y, lineSegmentList);

                }
                // End point
                else if (endpoint.status == 1) {
                    //
                }
                // Intersection point
                else if (endpoint.status == -1) {
                    // Swap
                    int lineSegmentIndex1 = eventPoints.firstEntry().getValue().getFirst().segmentIndex;
                    int lineSegmentIndex2 = eventPoints.firstEntry().getValue().getFirst().intersectSegmentIndex;

                    int leftNeighbourLineSegmentIndex1 = -1;
                    int rightNeighbourLineSegmentIndex1 = -1;
                    int leftNeighbourLineSegmentIndex2 = -1;
                    int rightNeighbourLineSegmentIndex2 = -1;

                    ArrayList<Integer> lineSegmentList1 = activeLines.get(lineSegments.get(lineSegmentIndex1).p1.y);
                    ArrayList<Integer> lineSegmentList2 = activeLines.get(lineSegments.get(lineSegmentIndex1).p1.y);

                    // cara swap untuk menghindari jika nilai y sama dengan menambahkan (-) sementara
                    for (int k = 0; k < lineSegmentList1.size(); k++) {
                        if (lineSegmentList1.get(k) == lineSegmentIndex1) {
                            lineSegmentList1.set(k, -lineSegmentIndex2);
                        }
                    }
                    for (int k = 0; k < lineSegmentList2.size(); k++) {
                        if (lineSegmentList2.get(k) == lineSegmentIndex2) {
                            lineSegmentList2.set(k, -lineSegmentIndex1);
                        }
                    }

                    // kembalikan nilai index menjadi (+) setelah selesai melakukan swap
                    for (int k = 0; k < lineSegmentList1.size(); k++) {
                        if (lineSegmentList1.get(k) == -lineSegmentIndex2) {
                            lineSegmentList1.set(k, lineSegmentIndex2);

                            // Set neighbour
                            if (k == 0) {
                                if (activeLines.lowerKey(lineSegments.get(lineSegmentIndex1).p1.y) != null) {
                                    leftNeighbourLineSegmentIndex1 = activeLines.get(activeLines.lowerKey(lineSegments.get(lineSegmentIndex1).p1.y)).get(activeLines.get(activeLines.lowerKey(lineSegments.get(lineSegmentIndex1).p1.y)).size()-1);
                                }
                                if (lineSegmentList1.size() > 1) {
                                    rightNeighbourLineSegmentIndex1 = lineSegmentList1.get(k+1);
                                }
                                else if (activeLines.higherKey(lineSegments.get(lineSegmentIndex1).p1.y) != null) {
                                    rightNeighbourLineSegmentIndex1 = activeLines.get(activeLines.higherKey(lineSegments.get(lineSegmentIndex1).p1.y)).get(0);
                                }
                            }
                            else if (k < lineSegmentList1.size() - 1) {
                                leftNeighbourLineSegmentIndex1 = lineSegmentList1.get(k-1);
                                rightNeighbourLineSegmentIndex1 = lineSegmentList1.get(k+1);
                            }
                            else if (k == lineSegmentList1.size() - 1) {
                                leftNeighbourLineSegmentIndex1 = lineSegmentList1.get(k-1);
                                if (activeLines.higherKey(lineSegments.get(lineSegmentIndex1).p1.y) != null) {
                                    rightNeighbourLineSegmentIndex1 = activeLines.get(activeLines.higherKey(lineSegments.get(lineSegmentIndex1).p1.y)).get(0);
                                }
                            }
                        }
                    }
                    for (int k = 0; k < lineSegmentList2.size(); k++) {
                        if (lineSegmentList2.get(k) == -lineSegmentIndex1) {
                            lineSegmentList2.set(k, lineSegmentIndex1);

                            // Set neighbour
                            if (k == 0) {
                                if (activeLines.lowerKey(lineSegments.get(lineSegmentIndex2).p1.y) != null) {
                                    leftNeighbourLineSegmentIndex2 = activeLines.get(activeLines.lowerKey(lineSegments.get(lineSegmentIndex2).p1.y)).get(activeLines.get(activeLines.lowerKey(lineSegments.get(lineSegmentIndex2).p1.y)).size()-1);
                                }
                                if (lineSegmentList2.size() > 1) {
                                    rightNeighbourLineSegmentIndex2 = lineSegmentList2.get(k+1);
                                }
                                else if (activeLines.higherKey(lineSegments.get(lineSegmentIndex2).p1.y) != null) {
                                    rightNeighbourLineSegmentIndex2 = activeLines.get(activeLines.higherKey(lineSegments.get(lineSegmentIndex2).p1.y)).get(0);
                                }
                            }
                            else if (k < lineSegmentList2.size() - 1) {
                                leftNeighbourLineSegmentIndex2 = lineSegmentList2.get(k-1);
                                rightNeighbourLineSegmentIndex2 = lineSegmentList2.get(k+1);
                            }
                            else if (k == lineSegmentList2.size() - 1) {
                                leftNeighbourLineSegmentIndex2 = lineSegmentList2.get(k-1);
                                if (activeLines.higherKey(lineSegments.get(lineSegmentIndex2).p1.y) != null) {
                                    rightNeighbourLineSegmentIndex2 = activeLines.get(activeLines.higherKey(lineSegments.get(lineSegmentIndex2).p1.y)).get(0);
                                }
                            }
                        }
                    }

                    if (leftNeighbourLineSegmentIndex1 > -1) {
                        if (lineSegments.get(lineSegmentIndex2).isIntersect(lineSegments.get(leftNeighbourLineSegmentIndex1))) {
                            Point intersectionPoint = lineSegments.get(lineSegmentIndex2).getIntersectPoint(lineSegments.get(leftNeighbourLineSegmentIndex1));
                            Deque<Endpoint> endpoints = new ArrayDeque<>();
                            if (eventPoints.containsKey(intersectionPoint.x)) {
                                endpoints = eventPoints.get(intersectionPoint.x);
                            }
                            endpoints.add(new Endpoint(intersectionPoint.x, intersectionPoint.y, -1, lineSegmentIndex2, leftNeighbourLineSegmentIndex1));
                            eventPoints.put(intersectionPoint.x, endpoints);
                            n++;
                        }
                    }
                    if (rightNeighbourLineSegmentIndex1 > -1 && rightNeighbourLineSegmentIndex1 != lineSegmentIndex1) {
                        if (lineSegments.get(lineSegmentIndex2).isIntersect(lineSegments.get(rightNeighbourLineSegmentIndex1))) {
                            Point intersectionPoint = lineSegments.get(lineSegmentIndex2).getIntersectPoint(lineSegments.get(rightNeighbourLineSegmentIndex1));
                            Deque<Endpoint> endpoints = new ArrayDeque<>();
                            if (eventPoints.containsKey(intersectionPoint.x)) {
                                endpoints = eventPoints.get(intersectionPoint.x);
                            }
                            endpoints.add(new Endpoint(intersectionPoint.x, intersectionPoint.y, -1, lineSegmentIndex2, rightNeighbourLineSegmentIndex1));
                            eventPoints.put(intersectionPoint.x, endpoints);
                            n++;
                        }
                    }
                    if (leftNeighbourLineSegmentIndex2 > -1 && leftNeighbourLineSegmentIndex2 != lineSegmentIndex2) {
                        if (lineSegments.get(lineSegmentIndex1).isIntersect(lineSegments.get(leftNeighbourLineSegmentIndex2))) {
                            Point intersectionPoint = lineSegments.get(lineSegmentIndex1).getIntersectPoint(lineSegments.get(leftNeighbourLineSegmentIndex2));
                            Deque<Endpoint> endpoints = new ArrayDeque<>();
                            if (eventPoints.containsKey(intersectionPoint.x)) {
                                endpoints = eventPoints.get(intersectionPoint.x);
                            }
                            endpoints.add(new Endpoint(intersectionPoint.x, intersectionPoint.y, -1, lineSegmentIndex1, leftNeighbourLineSegmentIndex2));
                            eventPoints.put(intersectionPoint.x, endpoints);
                            n++;
                        }
                    }
                    if (rightNeighbourLineSegmentIndex2 > -1) {
                        if (lineSegments.get(lineSegmentIndex1).isIntersect(lineSegments.get(rightNeighbourLineSegmentIndex2))) {
                            Point intersectionPoint = lineSegments.get(lineSegmentIndex1).getIntersectPoint(lineSegments.get(rightNeighbourLineSegmentIndex2));
                            Deque<Endpoint> endpoints = new ArrayDeque<>();
                            if (eventPoints.containsKey(intersectionPoint.x)) {
                                endpoints = eventPoints.get(intersectionPoint.x);
                            }
                            endpoints.add(new Endpoint(intersectionPoint.x, intersectionPoint.y, -1, lineSegmentIndex1, rightNeighbourLineSegmentIndex2));
                            eventPoints.put(intersectionPoint.x, endpoints);
                            n++;
                        }
                    }
                }

                // Layer
                paths = new ArrayList<>(setLineSegmentsIpe());
                paths.addAll(setSweepLineIpe());
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setLabelsIpe());
                layers.add(new Layer(paths, uses, texts));

                if (eventPoints.firstEntry() != null) {
                    eventPoints.firstEntry().getValue().removeFirst();
                }
            }

            if (eventPoints.firstEntry() != null) {
                eventPoints.pollFirstEntry();
            }
        }

        // Layer
        paths = new ArrayList<>(setLineSegmentsIpe());
        uses = new ArrayList<>(setPointsIpe());
        texts = new ArrayList<>(setLabelsIpe());
        layers.add(new Layer(paths, uses, texts));

    }

    public TreeMap<Double, Deque<Endpoint>> getEventPoints() {
        TreeMap<Double, Deque<Endpoint>> eventPoints = new TreeMap<>();

        for (int i = 0; i < lineSegments.size(); i++) {
            double startX = lineSegments.get(i).p1.x;
            double startY = lineSegments.get(i).p1.y;
            double endX = lineSegments.get(i).p2.x;
            double endY = lineSegments.get(i).p2.y;
            Endpoint startPoint = new Endpoint(startX, startY, 0, i);
            Endpoint endPoint = new Endpoint(endX, endY, 1, i);

            // If index already exist (start point)
            Deque<Endpoint> deque = new ArrayDeque<>();
            if (eventPoints.containsKey(startX)) {
                deque = eventPoints.get(startX);
            }
            deque.add(startPoint);
            eventPoints.put(startX, deque);

            // If index already exist (end point)
            deque = new ArrayDeque<>();
            if (eventPoints.containsKey(endX)) {
                deque = eventPoints.get(endX);
            }
            deque.add(endPoint);
            eventPoints.put(endX, deque);
        }

        return eventPoints;
    }

    public double getMaxY() {
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
