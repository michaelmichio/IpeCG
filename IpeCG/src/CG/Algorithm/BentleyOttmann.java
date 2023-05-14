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
    public ArrayList<String> intersectionPointsOutput = new ArrayList<>();
    public ArrayList<Endpoint> intersectionPoints = new ArrayList<>();
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
        HashMap<String, String> attributes;

        for (LineSegment lineSegment : lineSegments) {
            attributes = new HashMap<>();

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

        for (Endpoint endpoint : intersectionPoints) {
            attributes = new HashMap<>();
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("name", "mark/disk(sx)");
            attributes.put("pos", endpoint.x + " " + endpoint.y);
            attributes.put("size", "larger (8.0)");
            attributes.put("stroke", "red");
            uses.add(new Use(new Ipe.Object.Point(String.valueOf(endpoint.x), String.valueOf(endpoint.x)), attributes));
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

            attributes = new HashMap<>();
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("transformations", "translations");
            attributes.put("pos", ((startX + endX) / 2) + " " + ((startY + endY) / 2 + 8));
            attributes.put("stroke", "black");
            attributes.put("type", "label");
            attributes.put("width", "14.6575");
            attributes.put("height", "18.59");
            attributes.put("depth", "0.0825");
            attributes.put("valign", "baseline");
            texts.add(new Text("$\\ell_{" + i + "}$", attributes));

            for (Endpoint endpoint : intersectionPoints) {
                attributes = new HashMap<>();
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("transformations", "translations");
                attributes.put("pos", endpoint.x + " " + (endpoint.y - 24));
                attributes.put("stroke", "black");
                attributes.put("type", "label");
                attributes.put("width", "14.6575");
                attributes.put("height", "18.59");
                attributes.put("depth", "0.0825");
                attributes.put("valign", "baseline");
                texts.add(new Text(endpoint.toString(), attributes));
            }
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

        strPoints = new ArrayList<>();
        attributes = new HashMap<>();
        strPoints.add(new Ipe.Object.Point(String.valueOf((eventPoints.firstEntry().getValue().getFirst().x - 16)), String.valueOf((sweepLineMaxY + 16)), "m"));
        strPoints.add(new Ipe.Object.Point(String.valueOf((eventPoints.firstEntry().getValue().getFirst().x + 16)), String.valueOf((sweepLineMaxY + 16)), "l"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "red");
        attributes.put("pen", "ultrafat (2.0)");
        attributes.put("arrow", "normal/normal");
        paths.add(new Path(strPoints, attributes));

        strPoints = new ArrayList<>();
        attributes = new HashMap<>();
        strPoints.add(new Ipe.Object.Point(String.valueOf((eventPoints.firstEntry().getValue().getFirst().x - 16)), String.valueOf((sweepLineMinY - 16)), "m"));
        strPoints.add(new Ipe.Object.Point(String.valueOf((eventPoints.firstEntry().getValue().getFirst().x + 16)), String.valueOf((sweepLineMinY - 16)), "l"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "red");
        attributes.put("pen", "ultrafat (2.0)");
        attributes.put("arrow", "normal/normal");
        paths.add(new Path(strPoints, attributes));

        strPoints = new ArrayList<>();
        attributes = new HashMap<>();
        strPoints.add(new Ipe.Object.Point(String.valueOf(eventPoints.firstEntry().getValue().getFirst().x), String.valueOf(eventPoints.firstEntry().getValue().getFirst().y), "e"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "red");
        attributes.put("pen", "fat (1.2)");
        paths.add(new Path(strPoints, attributes));

        return  paths;
    }

    public ArrayList<Path> setDataFramesIpe() {
        ArrayList<Path> paths = new ArrayList<>();

        for (int j = 0; j < 16; j++) {
            HashMap<String, String> attributes = new HashMap<>();
            ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point(String.valueOf((j * 48) + 80), "544", "m"));
            strPoints.add(new Ipe.Object.Point(String.valueOf(((j + 1) * 48) + 80), "544", "l"));
            strPoints.add(new Ipe.Object.Point(String.valueOf(((j + 1) * 48) + 80), "592", "l"));
            strPoints.add(new Ipe.Object.Point(String.valueOf((j * 48) + 80), "592", "l"));
            strPoints.add(new Ipe.Object.Point("h"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "black");
            attributes.put("pen", "fat (1.2)");
            paths.add(new Path(strPoints, attributes));
        }

        for (int j = 0; j < 9; j++) {
            HashMap<String, String> attributes = new HashMap<>();
            ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point("0", String.valueOf((j * 48) + 80), "m"));
            strPoints.add(new Ipe.Object.Point("0", String.valueOf(((j + 1) * 48) + 80), "l"));
            strPoints.add(new Ipe.Object.Point("48", String.valueOf(((j + 1) * 48) + 80), "l"));
            strPoints.add(new Ipe.Object.Point("48", String.valueOf((j * 48) + 80), "l"));
            strPoints.add(new Ipe.Object.Point("h"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "black");
            attributes.put("pen", "fat (1.2)");
            paths.add(new Path(strPoints, attributes));
        }

        return  paths;
    }

    public ArrayList<Text> setDataTextIpe() {
        ArrayList<Text> texts = new ArrayList<>();
        HashMap<String, String> attributes = new HashMap<>();
        int n;

        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("transformations", "translations");
        attributes.put("pos", 48 + " " + 560);
        attributes.put("stroke", "black");
        attributes.put("type", "label");
        attributes.put("width", "14.6575");
        attributes.put("height", "18.59");
        attributes.put("depth", "0.0825");
        attributes.put("valign", "baseline");
        texts.add(new Text("{\\bf E}", attributes));

        n = 0;
        for (Map.Entry<Double, Deque<Endpoint>> entry : eventPoints.entrySet()) {
            ArrayList<Endpoint> eventPointList = new ArrayList<>(entry.getValue());
            for (Endpoint endpoint : eventPointList) {
                attributes = new HashMap<>();

                if (endpoint.status == -1) {
                    attributes.put("pos", (48 * (n + 1)) + 36 + " " + 560);
                }
                else {
                    attributes.put("pos", (48 * (n + 1)) + 44 + " " + 560);
                }

                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("transformations", "translations");
                attributes.put("stroke", "black");
                attributes.put("type", "label");
                attributes.put("width", "14.6575");
                attributes.put("height", "18.59");
                attributes.put("depth", "0.0825");
                attributes.put("valign", "baseline");
                texts.add(new Text(endpoint.toString(), attributes));

                n++;
            }
        }

        attributes = new HashMap<>();
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("transformations", "translations");
        attributes.put("pos", 16 + " " + 528);
        attributes.put("stroke", "black");
        attributes.put("type", "label");
        attributes.put("width", "14.6575");
        attributes.put("height", "18.59");
        attributes.put("depth", "0.0825");
        attributes.put("valign", "baseline");
        texts.add(new Text("{\\bf L}", attributes));

        n = 0;
        TreeMap<Double, ArrayList<Integer>> activeLinesReversed = new TreeMap<>(Collections.reverseOrder());
        activeLinesReversed.putAll(activeLines);
        for (Map.Entry<Double, ArrayList<Integer>> entry : activeLinesReversed.entrySet()) {
            for (int i = entry.getValue().size() - 1; i >= 0; i--) {
                attributes = new HashMap<>();

                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("transformations", "translations");
                attributes.put("pos", 12 + " " + (480 - (n * 48)));
                attributes.put("stroke", "black");
                attributes.put("type", "label");
                attributes.put("width", "14.6575");
                attributes.put("height", "18.59");
                attributes.put("depth", "0.0825");
                attributes.put("valign", "baseline");
                texts.add(new Text("$\\ell_{" + entry.getValue().get(i) + "}$", attributes));

                n++;
            }
        }

        attributes = new HashMap<>();
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("transformations", "translations");
        attributes.put("pos", 80 + " " + 16);
        attributes.put("stroke", "black");
        attributes.put("type", "label");
        attributes.put("width", "14.6575");
        attributes.put("height", "18.59");
        attributes.put("depth", "0.0825");
        attributes.put("valign", "baseline");
        texts.add(new Text("{\\bf Output: }" + intersectionPointsOutput, attributes));

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
        paths.addAll(setDataFramesIpe());
        uses = new ArrayList<>(setPointsIpe());
        texts = new ArrayList<>(setLabelsIpe());
        texts.addAll(setDataTextIpe());
        layers.add(new Layer(paths, uses, texts));

        while (eventPoints.size() > 0) {
            while (eventPoints.firstEntry().getValue().size() > 0) {
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
                            Endpoint ep = new Endpoint(intersectionPoint.x, intersectionPoint.y, -1, endpoint.segmentIndex, leftNeighbourLineSegmentIndex);
                            if (!intersectionPointsOutput.contains(ep.toString())) {
                                Deque<Endpoint> endpoints = new ArrayDeque<>();
                                endpoints.add(ep);
                                if (eventPoints.containsKey(intersectionPoint.x)) {
                                    endpoints = eventPoints.get(intersectionPoint.x);
                                }
                                intersectionPointsOutput.add(ep.toString());
                                intersectionPoints.add(ep);
                                eventPoints.put(intersectionPoint.x, endpoints);
                            }
                        }
                    }
                    // Check right neighbour if exist
                    if (rightNeighbourLineSegmentIndex > -1) {
                        // Check the intersection with right neighbour
                        if (lineSegments.get(endpoint.segmentIndex).isIntersect(lineSegments.get(rightNeighbourLineSegmentIndex))) {
                            Point intersectionPoint = lineSegments.get(endpoint.segmentIndex).getIntersectPoint(lineSegments.get(rightNeighbourLineSegmentIndex));
                            Endpoint ep = new Endpoint(intersectionPoint.x, intersectionPoint.y, -1, endpoint.segmentIndex, rightNeighbourLineSegmentIndex);
                            if (!intersectionPointsOutput.contains(ep.toString())) {
                                Deque<Endpoint> endpoints = new ArrayDeque<>();
                                endpoints.add(ep);
                                if (eventPoints.containsKey(intersectionPoint.x)) {
                                    endpoints = eventPoints.get(intersectionPoint.x);
                                }
                                intersectionPointsOutput.add(ep.toString());
                                intersectionPoints.add(ep);
                                eventPoints.put(intersectionPoint.x, endpoints);
                            }
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

                    if (activeLines.lowerKey(endpoint.y) != null) {
                        leftNeighbourLineSegmentIndex = activeLines.get(activeLines.lowerKey(endpoint.y)).get(activeLines.get(activeLines.lowerKey(endpoint.y)).size()-1);
                    }
                    if (activeLines.higherKey(endpoint.y) != null) {
                        rightNeighbourLineSegmentIndex = activeLines.get(activeLines.higherKey(endpoint.y)).get(0);
                    }

                    if (leftNeighbourLineSegmentIndex > -1 && rightNeighbourLineSegmentIndex > -1) {
                        if (lineSegments.get(leftNeighbourLineSegmentIndex).isIntersect(lineSegments.get(rightNeighbourLineSegmentIndex))) {
                            Point intersectionPoint = lineSegments.get(leftNeighbourLineSegmentIndex).getIntersectPoint(lineSegments.get(rightNeighbourLineSegmentIndex));
                            Endpoint ep = new Endpoint(intersectionPoint.x, intersectionPoint.y, -1, leftNeighbourLineSegmentIndex, rightNeighbourLineSegmentIndex);
                            if (!intersectionPointsOutput.contains(ep.toString())) {
                                Deque<Endpoint> endpoints = new ArrayDeque<>();
                                endpoints.add(ep);
                                if (eventPoints.containsKey(intersectionPoint.x)) {
                                    endpoints = eventPoints.get(intersectionPoint.x);
                                }
                                intersectionPointsOutput.add(ep.toString());
                                intersectionPoints.add(ep);
                                eventPoints.put(intersectionPoint.x, endpoints);
                            }
                        }
                    }

                    // Remove line segment from activeLines
                    int endLineSegmentIndex = endpoint.segmentIndex;
                    ArrayList<Integer> endLineSegmentList = new ArrayList<>();
                    double removeKey = -1;
                    for (Map.Entry<Double, ArrayList<Integer>> entry : activeLines.entrySet()) {
                        if (entry.getValue().contains(endLineSegmentIndex)) {
                            endLineSegmentList.addAll(entry.getValue());
                            removeKey = entry.getKey();
                            break;
                        }
                    }
                    endLineSegmentList.remove(endLineSegmentList.indexOf(endLineSegmentIndex));
                    if (endLineSegmentList.size() > 0) {
                        activeLines.put(removeKey, endLineSegmentList);
                    }
                    else {
                        activeLines.remove(removeKey);
                    }
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

                    ArrayList<Integer> lineSegmentList1 = new ArrayList<>();
                    for (Map.Entry<Double, ArrayList<Integer>> entry : activeLines.entrySet()) {
                        if (entry.getValue().contains(lineSegmentIndex1)) {
                            lineSegmentList1 = entry.getValue();
                        }
                    }
                    ArrayList<Integer> lineSegmentList2 = new ArrayList<>();
                    for (Map.Entry<Double, ArrayList<Integer>> entry : activeLines.entrySet()) {
                        if (entry.getValue().contains(lineSegmentIndex2)) {
                            lineSegmentList2 = entry.getValue();
                        }
                    }

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

                            // Set neighbour for lineSegmentIndex2
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

                            // Set neighbour for lineSegmentIndex1
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

                    if (leftNeighbourLineSegmentIndex1 > -1 && leftNeighbourLineSegmentIndex1 != lineSegmentIndex2) {
                        if (lineSegments.get(lineSegmentIndex2).isIntersect(lineSegments.get(leftNeighbourLineSegmentIndex1))) {
                            Point intersectionPoint = lineSegments.get(lineSegmentIndex2).getIntersectPoint(lineSegments.get(leftNeighbourLineSegmentIndex1));
                            Endpoint ep = new Endpoint(intersectionPoint.x, intersectionPoint.y, -1, lineSegmentIndex1, leftNeighbourLineSegmentIndex1);
                            if (!intersectionPointsOutput.contains(ep.toString())) {
                                Deque<Endpoint> endpoints = new ArrayDeque<>();
                                endpoints.add(ep);
                                if (eventPoints.containsKey(intersectionPoint.x)) {
                                    endpoints = eventPoints.get(intersectionPoint.x);
                                }
                                intersectionPointsOutput.add(ep.toString());
                                intersectionPoints.add(ep);
                                eventPoints.put(intersectionPoint.x, endpoints);
                            }
                        }
                    }
                    if (rightNeighbourLineSegmentIndex1 > -1 && rightNeighbourLineSegmentIndex1 != lineSegmentIndex2) {
                        if (lineSegments.get(lineSegmentIndex2).isIntersect(lineSegments.get(rightNeighbourLineSegmentIndex1))) {
                            Point intersectionPoint = lineSegments.get(lineSegmentIndex2).getIntersectPoint(lineSegments.get(rightNeighbourLineSegmentIndex1));
                            Endpoint ep = new Endpoint(intersectionPoint.x, intersectionPoint.y, -1, lineSegmentIndex2, rightNeighbourLineSegmentIndex1);
                            if (!intersectionPointsOutput.contains(ep.toString())) {
                                Deque<Endpoint> endpoints = new ArrayDeque<>();
                                endpoints.add(ep);
                                if (eventPoints.containsKey(intersectionPoint.x)) {
                                    endpoints = eventPoints.get(intersectionPoint.x);
                                }
                                intersectionPointsOutput.add(ep.toString());
                                intersectionPoints.add(ep);
                                eventPoints.put(intersectionPoint.x, endpoints);
                            }
                        }
                    }
                    if (leftNeighbourLineSegmentIndex2 > -1 && leftNeighbourLineSegmentIndex2 != lineSegmentIndex1) {
                        if (lineSegments.get(lineSegmentIndex1).isIntersect(lineSegments.get(leftNeighbourLineSegmentIndex2))) {
                            Point intersectionPoint = lineSegments.get(lineSegmentIndex1).getIntersectPoint(lineSegments.get(leftNeighbourLineSegmentIndex2));
                            Endpoint ep = new Endpoint(intersectionPoint.x, intersectionPoint.y, -1, lineSegmentIndex1, leftNeighbourLineSegmentIndex2);
                            if (!intersectionPointsOutput.contains(ep.toString())) {
                                Deque<Endpoint> endpoints = new ArrayDeque<>();
                                endpoints.add(ep);
                                if (eventPoints.containsKey(intersectionPoint.x)) {
                                    endpoints = eventPoints.get(intersectionPoint.x);
                                }
                                intersectionPointsOutput.add(ep.toString());
                                intersectionPoints.add(ep);
                                eventPoints.put(intersectionPoint.x, endpoints);
                            }
                        }
                    }
                    if (rightNeighbourLineSegmentIndex2 > -1 && rightNeighbourLineSegmentIndex2 != lineSegmentIndex1) {
                        if (lineSegments.get(lineSegmentIndex1).isIntersect(lineSegments.get(rightNeighbourLineSegmentIndex2))) {
                            Point intersectionPoint = lineSegments.get(lineSegmentIndex1).getIntersectPoint(lineSegments.get(rightNeighbourLineSegmentIndex2));
                            Endpoint ep = new Endpoint(intersectionPoint.x, intersectionPoint.y, -1, lineSegmentIndex1, rightNeighbourLineSegmentIndex2);
                            if (!intersectionPointsOutput.contains(ep.toString())) {
                                Deque<Endpoint> endpoints = new ArrayDeque<>();
                                endpoints.add(ep);
                                if (eventPoints.containsKey(intersectionPoint.x)) {
                                    endpoints = eventPoints.get(intersectionPoint.x);
                                }
                                intersectionPointsOutput.add(ep.toString());
                                intersectionPoints.add(ep);
                                eventPoints.put(intersectionPoint.x, endpoints);
                            }
                        }
                    }
                }

                // Layer
                paths = new ArrayList<>(setLineSegmentsIpe());
                paths.addAll(setSweepLineIpe());
                paths.addAll(setDataFramesIpe());
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));

                eventPoints.firstEntry().getValue().removeFirst();
            }

            eventPoints.pollFirstEntry();
        }

        // Layer
        paths = new ArrayList<>(setLineSegmentsIpe());
        paths.addAll(setDataFramesIpe());
        uses = new ArrayList<>(setPointsIpe());
        texts = new ArrayList<>(setLabelsIpe());
        texts.addAll(setDataTextIpe());
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
